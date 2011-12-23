package core.compoundterm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import core.Port;

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
	
	public CompoundTerm(){
		this.id = order;
		order++;
	}
	
	/*public CompoundTerm(Port sourceuri){
		sourceuri.setTerm(this);
		if(!existingPort(sources_uri, sourceuri))
			sources_uri.add(sourceuri);
	}
	
	public CompoundTerm(Port sourceuri, Port receiveruri) {
		sourceuri.setTerm(this);
		receiveruri.setTerm(this);
		if(!existingPort(sources_uri, sourceuri))
			sources_uri.add(sourceuri);
		if(!existingPort(receivers_uri, receiveruri))
			receivers_uri.add(receiveruri);
		//addroute da source del compound a source del primitivo
	}
	
	public CompoundTerm(Port sourceuri, Collection<Port> receiversuri) {
		receivers_uri.addAll(receiversuri);
		sourceuri.setTerm(this);
		sources_uri.add(sourceuri);
	}
	
	public CompoundTerm(Collection<Port> sourcesuri, Collection<Port> receiversuri) {
		//sources_uri.addAll(sourcesuri);
		for(Iterator<Port> i = sourcesuri.iterator(); i.hasNext();){
			Port p = i.next();
			p.setTerm(this);
			if(!existingPort(sources_uri, p))
				sources_uri.add(p);
		}
		for(Iterator<Port> i = receiversuri.iterator(); i.hasNext();){
			Port p = i.next();
			p.setTerm(this);
			if(!existingPort(receivers_uri, p))
				receivers_uri.add(p);
		}
	}*/

	public Collection<Port> getSources_uri() {
		return sources_uri;
	}
	
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
	
	public void addSources_uri(Collection<Port> sourcesuri){
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
	
	public Collection<Port> getReceivers_uri() {
		return receivers_uri;
	}
	
	public void addReceivers_uri(Collection<Port> receiversUri){
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
	
	
	/*protected Port getSource(){
		return sources_uri.iterator().next();
	}
	
	protected Port getReceiver(){
		return receivers_uri.iterator().next();
	}*/	
	
	private Port includePort(Port p, Collection<Port> list){
		for(Iterator<Port> i = list.iterator(); i.hasNext();){
			Port temp = i.next();
			if(temp.getUri().equals(p.getUri())){
				return temp;
			}
		}
		return null;
	}
	
	public void addInternalEndpoint(final Port source){
		try {
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
	
	public void removePort(Collection<Port> list, Port p){
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
	
	public void start(){
		try {
			/*final Iterator<Port> p = sources_uri.iterator();
				
			while(p.hasNext()){
				//System.out.println(getSources_uri());
				try {
					context.addRoutes(new RouteBuilder() {
							@Override
							public void configure() throws Exception {
								// TODO Auto-generated method stub
								Port temp = p.next();
								String rec = "";
								if(temp.getUri()!=null){
									if(temp.getId().size()>0) rec+= internal+""+temp.getId().get(0);
									for(int k = 1; k<temp.getId().size(); k++){
										rec+=","+internal+""+temp.getId().get(k);
									}
									from(context.getEndpoint(temp.getUri())).to(rec);
								}
							}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			if(!context.getRouteDefinitions().isEmpty())
				System.out.println("Route defined: "+context.getRouteDefinitions());
			context.start();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isComposed(){
		return composed;
	}
	
	public void setComposed(){
		composed = true;
	}
	
	public abstract void setMessage(String uri,Exchange e);
	
	public void addComponent(CompoundTerm c){
		component.add(c);
		addSources_uri(c.getSources_uri());
		addReceivers_uri(c.getReceivers_uri());
	}
	
	public ArrayList<CompoundTerm> getComponents(){
		return component;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
}
