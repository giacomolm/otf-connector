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
import it.univaq.disim.connectorOTF.core.compoundterm.processors.LegalityProcessor;

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
	
	/**
	 * Starts the context of primitive term container. Each primitive term
	 * has a container, defined by this class, 
	 */
	public void start(){
		try {
			if(!composed){ // c'era order
					Iterator<Port> p = sources_uri.iterator();
					while(p.hasNext()){
						final Port temp = p.next();
                                    //declaring the legal state processor checking the state consistency
                                    final Processor legalState = new LegalityProcessor(this);

						context.addRoutes(new RouteBuilder() {
							@Override
							public void configure() throws Exception {
								// TODO Auto-generated method stub
								from(context.getEndpoint(temp.getUri())).
                                                    process(legalState).
								process(new Processor() {
									@Override
									public void process(Exchange e) throws Exception {
                                                                    //setting message for the right uri
                                                                    out.println("Consumed from "+temp.getUri()+" exchange"+e);
										setMessage(temp.getUri(), e);
									}
								});
								//to(internal+""+temp.getId().get(0));
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
}
