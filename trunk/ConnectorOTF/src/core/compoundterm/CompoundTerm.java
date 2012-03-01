package core.compoundterm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import core.Port;

/**
 * CompoundTerm is the main class of the package connector. Contains methods that permits
 * creation and management of terms,including the main concept of message port; through this
 * concept, this library creates links between component that share same input/output port.
 * Each term, when it is initialised, must specify its input and output message ports: when two 
 * terms are composed, this class deals with decide if there are common message port: if at 
 * least one input port of a term is identically to an output port of other term, we must 
 * create link between these two term.
 * @author giacomolm
 *
 */

public abstract class CompoundTerm {

	protected Collection<Port> sources_uri = new ArrayList<Port>();
	protected Collection<Port> receivers_uri = new ArrayList<Port>();
	protected CamelContext context= new DefaultCamelContext();
	protected ProducerTemplate producer = context.createProducerTemplate();
	protected String internal = "vm:internal";
	protected static int order=0;
	protected boolean composed = false;
	protected ArrayList<CompoundTerm> component = new ArrayList<CompoundTerm>(2);
	private int id;
	static public FileOutputStream fstream;
	static public PrintWriter out;
	
	static{
		try {
			fstream = new FileOutputStream("log.txt");
			fstream = new FileOutputStream("log.txt",true);
			out = new PrintWriter(fstream);
			
			System.out.println("File di log "+System.getProperty("user.dir")+"/log.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * The constructor initialise data structures and assigns an unique 
	 * identifier at the term
	 */
	public CompoundTerm(){
		this.id = order;
		order++;
	}

	/**
	 * The method returns terms sources ports 
	 * @return Collection containing sources ports of the compound term
	 */
	public Collection<Port> getSources() {
		return sources_uri;
	}
	
	/**
	 * Add source port to term. If the port is already been added to term, we must control
	 * if we need to create a link between existing terms.
	 * @param source Message port to include in the term
	 */
	public void addSource(Port source){
		source.setTerm(this);
		Port temp = includePort(source,receivers_uri);
		if(temp!=null){
			//devo aggiungere un canale interno
			addInternalEndpoint(source);
			removePort(receivers_uri, source);
		}
		else {
			temp =includePort(source, sources_uri);
			if(temp!=null){
				temp.add(source);
			}
			else sources_uri.add(source);
		}
	}
	
	/**
	 * This method is semantically identical to addSource, differs only by the parameter type.
	 * Now we have a collection of port that we want to add to term.
	 * @param sourcesuri Collection of ports
	 */
	public void addSources(Collection<Port> sourcesuri){
		for(Iterator<Port> i = sourcesuri.iterator(); i.hasNext();){
			Port p = new Port(i.next());
			p.setTerm(this);
			p.addId(id);
			Port temp = includePort(p,receivers_uri);
			if(temp!=null){
				//devo aggiungere un canale interno
				addInternalEndpoint(p);
				removePort(receivers_uri, p);
			}
			else {
				temp =includePort(p, sources_uri);
				if(temp!=null){
					temp.add(p);
				}
				else sources_uri.add(p);
			}
		}
	}

	/**
	 * Adds output port to term. Simply adds message endpoint that receives
	 * output message from this term.
	 * @param receiver Port the output exchange
	 */
	public void addReceiver(Port receiver){
		receiver.setTerm(this);
		Port temp = includePort(receiver, sources_uri);
		if(temp!=null){
			//devo aggiungere un canale interno
			addInternalEndpoint(temp);
			removePort(sources_uri, temp);
		}
		else {
			temp = includePort(receiver, receivers_uri);
			if(temp!=null){
				temp.add(receiver);
			}
			else receivers_uri.add(receiver);
		}
	}
	
	/**
	 * Returns all receiver ports of this term
	 * @return Collection<Port> associated with this term
	 */
	public Collection<Port> getReceivers() {
		return receivers_uri;
	}
	
	/**
	 * This method is semantically identical to addReceiver, differs only by the parameter type.
	 * Now we have a collection of receiver ports that we want to add to term.
	 * @param receiversUri Collection of port added to term
	 */
	public void addReceivers(Collection<Port> receiversUri){
		for(Iterator<Port> i = receiversUri.iterator(); i.hasNext();){
			Port p = new Port(i.next());
			p.setTerm(this);
			p.addId(id);
			Port temp = includePort(p, sources_uri);
			if(temp!=null){
				//devo aggiungere un canale interno
				addInternalEndpoint(p);
				removePort(sources_uri,p);
			}
			else {
				temp = includePort(p, receivers_uri);
				if(temp!=null){
					temp.add(p);
				}
				else receivers_uri.add(p);
			}
		}
	}
	
	/**
	 * This is an internal method that control if a port exists in a set of port
	 * @param p port under analisys
	 * @param list of port 
	 * @return null if the port p is not present in the list l, else returns the Port that matches with p
	 */
	private Port includePort(Port p, Collection<Port> list){
		for(Iterator<Port> i = list.iterator(); i.hasNext();){
			Port temp = i.next();
			if(temp.getUri().equals(p.getUri())){
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * This internal method add an internal endpoint between terms that share same messagge port.
	 * The source port 
	 * @param source Port 
	 */
	private void addInternalEndpoint(final Port source){
		try {
			//add new route that consumes from the uri of the source port
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					for(int i=0; i<source.getId().size(); i++){
						from(source.getUri()).
						process(new Processor() {
							@Override
							public void process(Exchange m) throws Exception {
								// TODO Auto-generated method stub
								Collection<CompoundTerm> terms = source.getTerms();
								for(Iterator<CompoundTerm> i = terms.iterator(); i.hasNext();){
									CompoundTerm term = i.next();
									term.setMessage(source.getUri(),m);
								}
							}
						});
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes port p from the list passed as parameter. This method controls
	 * if a member of the list is equal to p and remove it.
	 * @param list of ports
	 * @param p port to delete
	 */
	private void removePort(Collection<Port> list, Port p){
		Port port = null;
		boolean trovato = false;
		Port[] ap = (Port[])list.toArray(new Port[list.size()]);
		for(int i = 0; i<ap.length&&!trovato; i++){
			if(ap[i].getUri().equals(p.getUri())){
				trovato = true;
				port = ap[i]; 
			}
		}
		if(trovato) list.remove(port);
	}
	
	/**
	 * Simply recall method start() of the CamelContext class
	 * (from Apache Camel-Core Library)
	 */
	public void start(){
		try {
			if(!context.getRouteDefinitions().isEmpty())
				out.append("Route defined: "+context.getRouteDefinitions()+"\n");
			context.start();
			out.flush();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * We control if the term is a component of compound term
	 * @return true if this term is a component of another compound term
	 */
	public boolean isComposed(){
		return composed;
	}
	
	/**
	 * Set composed this term . Useful when we are building compound term.
	 */
	public void setComposed(){
		composed = true;
	}
	
	/**
	 * Add an input message to term. It allows the messagge passing betwewn
	 * nested term: from the external term to internal(s) term(s)
	 * @param uri source of the exchange
	 * @param e exchange received
	 */
	public abstract void setMessage(String uri,Exchange e);
	
	/**
	 * Add term to this compound term. It is used by unary and binary
	 * operator to add term. 
	 * @param c Term added
	 */
	protected void addComponent(CompoundTerm c){
		component.add(c);
		addSources(c.getSources());
		addReceivers(c.getReceivers());
	}
	
	/**
	 * Get component associated to this compound term, if is composed
	 * @return CompoundTerm components of this term
	 */
	public ArrayList<CompoundTerm> getComponents(){
		return component;
	}
	
	/**
	 * Each term has an unique identifier assigned in the building time
	 * @return id of this term
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Set the terms id
	 * @param id unique identifier for this term
	 */
	public void setId(int id){
		this.id = id;
	}
	
	public void getSourcePort(){
		Iterator<Port> i = sources_uri.iterator();
		System.out.print("Source Uri ");
		while(i.hasNext()){
			Port p = i.next();
			System.out.print(p.getUri()+" ");
		}
		System.out.println();
	}
	
}
