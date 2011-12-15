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
	//protected Collection<Endpoint> internal_endpoint = new ArrayList<Endpoint>();
	protected CamelContext context= new DefaultCamelContext();
	protected ProducerTemplate producer = context.createProducerTemplate();
	protected String internal = "vm:internal";
	protected static int order=0;
	protected boolean composed = false;
	
	public CompoundTerm(){
	}
	
	public CompoundTerm(Port sourceuri){
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
		receivers_uri.addAll(receiversuri);
	}

	public Collection<Port> getSources_uri() {
		return sources_uri;
	}
	
	public void addSource(Port source){
		source.setTerm(this);
		if(!existingPort(sources_uri, source))
			sources_uri.add(source);
	}
	
	public void addSources_uri(Collection<Port> sourcesuri){
		for(Iterator<Port> i = sourcesuri.iterator(); i.hasNext();){
			Port p = new Port(i.next());
			p.setTerm(this);
			if(!existingPort(sources_uri, p))
				sources_uri.add(p);
		}
	}

	public void addReceiver(Port receiver){
		receivers_uri.add(receiver);
	}
	
	public Collection<Port> getReceivers_uri() {
		return receivers_uri;
	}
	
	public void addReceivers_uri(Collection<Port> receiversUri){
		receivers_uri.addAll(receiversUri);
	}
	
	
	protected Port getSource(){
		return sources_uri.iterator().next();
	}
	
	protected Port getReceiver(){
		return receivers_uri.iterator().next();
	}
	
	private boolean existingPort(Collection<Port> list, Port p){
		for(Iterator<Port> i = list.iterator(); i.hasNext();){
			Port temp = i.next();
			if(temp.getUri()!=null && temp.getUri().equals(p.getUri())){
				//per ogni porta esistente aggiungo i termini che ne fanno uso
				//temp.getTerms().addAll(p.getTerms());
				return true;
			}
		}
		return false;
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
	
	public int getOrder(){
		return order;
	}
	
	public boolean isComposed(){
		return composed;
	}
	
	public void setComposed(){
		composed = true;
	}
	
	public abstract void setMessage(String uri,Exchange e);
	
}
