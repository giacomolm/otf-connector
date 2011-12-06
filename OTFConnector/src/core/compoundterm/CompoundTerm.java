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

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

public abstract class CompoundTerm {

	protected Collection<Port> sources_uri = new ArrayList<Port>();;
	protected Collection<Port> receivers_uri = new ArrayList<Port>();;
	//protected Collection<Endpoint> internal_endpoint = new ArrayList<Endpoint>();
	protected CamelContext context= new DefaultCamelContext();
	protected ProducerTemplate producer = context.createProducerTemplate();
	protected String internal = "vm:internal";
	protected static int order=0;
	
	public CompoundTerm(){
		
	}
	
	public CompoundTerm(Port sourceuri, Port receiveruri) {
		sourceuri.setTerm(this);
		sources_uri.add(sourceuri);
		receivers_uri.add(receiveruri);
		//addroute da source del compound a source del primitivo
	}
	
	public CompoundTerm(Port sourceuri, Collection<Port> receiversuri) {
		receivers_uri.addAll(receiversuri);
		sources_uri.add(sourceuri);
	}
	
	public CompoundTerm(Collection<Port> sourcesuri, Collection<Port> receiversuri) {
		if(sourcesuri!=null)
			sources_uri.addAll(sourcesuri);
		if(receiversuri!=null)
			receivers_uri.addAll(receiversuri);
	}

	public Collection<Port> getSources_uri() {
		return sources_uri;
	}
	
	public void addSources_uri(Collection<Port> sourcesUri){
		sources_uri.addAll(sourcesUri);
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
	
	public void start(){
		try {
			final Iterator<Port> p = sources_uri.iterator();
				
			while(p.hasNext()){
				//System.out.println(getSources_uri());
				try {
					context.addRoutes(new RouteBuilder() {
							@Override
							public void configure() throws Exception {
								// TODO Auto-generated method stub
								Port temp = p.next();
								if(temp.getUri()!=null)
									from(temp.getUri()).to(internal+""+temp.getId());
							}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//System.out.println(context.getRouteDefinitions());
			context.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getOrder(){
		return order;
	}
	
}
