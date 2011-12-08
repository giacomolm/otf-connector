package core.compoundterm.primitiveterm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;
import core.compoundterm.CompoundTerm;

public abstract class PrimitiveTerm extends CompoundTerm {

	protected RouteBuilder primitive_route;
	
	/*public PrimitiveTerm(String source_uri){
		this.source_uri = source_uri;
	}*/
	public PrimitiveTerm(Port source_uri){
		super(source_uri);
	}
	
	public PrimitiveTerm(Port source_uri, Port receiver_uri){
		super(source_uri,receiver_uri);
	}
	
	public PrimitiveTerm(Port source_uri, Collection<Port> receivers_uri){
		super(source_uri,receivers_uri);
	}
	
	PrimitiveTerm(Collection<Port> sources_uri, Collection<Port> receivers_uri){
		super(sources_uri,receivers_uri);
	}

	public RouteBuilder getRoute() {
		return primitive_route;
	}

	public void setRoute(RouteBuilder primitiveroute) {
		this.primitive_route = primitiveroute;
	}
	
	public void start(){
		try {
			for(int i=0; i<order; i++){
				Iterator<Port> p = sources_uri.iterator();
				while(p.hasNext()){
					final Port temp = p.next();
					context.addRoutes(new RouteBuilder() {
							@Override
							public void configure() throws Exception {
								// TODO Auto-generated method stub
								from(context.getEndpoint(temp.getUri())).
								to(internal+""+temp.getId());
							}
						});
				}
			}
			
			context.start();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Hanno visibilità di package in quanto l'utente del sistema non deve sapere come manipoliamo i nostri receiver
	
	void setReceivers_uri(ArrayList<Port> receiversUri) {
		receivers_uri = receiversUri;
	}
	
}
