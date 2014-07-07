package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import it.univaq.disim.connectorOTF.core.Port;
import it.univaq.disim.connectorOTF.core.compoundterm.CompoundTerm;

/**
 * Primitive Term supply an initial description of primitives introduced
 * in the connector algebra theory. Each primitive term, Extra send,
 * Missing send, Signature mismatch, Split message mismatch, Merge message 
 * mismatch, Ordering mismatch are well defined with separated class which
 * extends this class. If this primitive term is the outer term, the role 
 * of this class is define a primitive term container which allows link 
 * creation between this outer term and others external applications 
 * @author giacomolm
 *
 */

public abstract class PrimitiveTerm extends CompoundTerm {

	protected RouteBuilder primitive_route;
	
	/*public PrimitiveTerm() {
		// TODO Auto-generated constructor stub
	}
	
	public PrimitiveTerm(String source_uri){
		this.source_uri = source_uri;
	}*/
	/*public PrimitiveTerm(Port source_uri){
		super(source_uri);
	}
	
	public PrimitiveTerm(Port source_uri, Port receiver_uri){
		super(source_uri,receiver_uri);
	}
	
	/*public PrimitiveTerm(Port source_uri, Collection<Port> receivers_uri){
		super(source_uri,receivers_uri);
	}
	
	PrimitiveTerm(Collection<Port> sources_uri, Collection<Port> receivers_uri){
		super(sources_uri,receivers_uri);
	}*/

	/*public RouteBuilder getRoute() {
		return primitive_route;
	}

	public void setRoute(RouteBuilder primitiveroute) {
		this.primitive_route = primitiveroute;
	}*/
	
	
	/**
	 * Starts the context of primitive term container. Each primitive term
	 * has a container, defined by this class, 
	 */
	public void start(){
		try {
			if(!composed){ // c'era order
				for(int i=0; i<sources_uri.size(); i++){
					Iterator<Port> p = sources_uri.iterator();
					while(p.hasNext()){
						final Port temp = p.next();
						System.out.println(order);
						context.addRoutes(new RouteBuilder() {
							@Override
							public void configure() throws Exception {
								// TODO Auto-generated method stub
								from(context.getEndpoint(temp.getUri())).
								process(new Processor() {
									@Override
									public void process(Exchange e) throws Exception {
										// TODO Auto-generated method stub
										setMessage(temp.getUri(), e);
									}
								});
								//to(internal+""+temp.getId().get(0));
							}
						});
					}
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
	
	/*void setReceivers_uri(ArrayList<Port> receiversUri) {
		receivers_uri = receiversUri;
	}*/

	
}
