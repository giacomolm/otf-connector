package it.univaq.disim.connectorOTF.core.compoundterm;

import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import it.univaq.disim.connectorOTF.core.Port;

/**
 * Plug class define the plugging operator introduced in the connector algebra
 * theory. This is a binary operator: result of application is new term composed
 * by two components terms. The operator connects two terms of the algebra on 
 * common message ports; during composition, if the two terms share same message
 * port, more specifically one term consumes from an uri and the other  
 * term send exchange to the same uri, we connects directly the two term.This is
 * equivalent to plugging the corresponding IA into each other, or potentially 
 * synchronising them. 
 * @author giacomolm
 *
 */
public class Plug extends CompoundTerm{

	//CompoundTerm c1,c2;
	
	/**
	 * Build new Plug term starting from c1, c2 compound terms. The two terms
	 * are simply added to build this compound terms. 
	 */
	public Plug(CompoundTerm c1, CompoundTerm c2) {
		c1.setComposed();
		c2.setComposed();
		addComponent(c1);
		addComponent(c2);
	}
	
	/**
	 * This method simply start component terms of this compound term. If this
	 * term is the outer (it is not a component of another term), we must publish 
	 * associated port. 
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		//if this term is the outer term, we must publish associated port
		if(!isComposed()){
			final Iterator<Port> p = sources_uri.iterator();
			while(p.hasNext()){
				try {
					context.addRoutes(new RouteBuilder() {
							@Override
							public void configure() throws Exception {
								// TODO Auto-generated method stub
								final Port port = p.next();
								if(port.getUri()!=null){
	
									from(port.getUri()).
									process(new Processor() {
										@Override
										public void process(Exchange m) throws Exception {
											// TODO Auto-generated method stub
											Collection<CompoundTerm> terms = port.getTerms();
											for(Iterator<CompoundTerm> i = terms.iterator(); i.hasNext();){
												CompoundTerm term = i.next();
												term.setMessage(port.getUri(),m);
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
		}
		super.start();
		getComponents().get(0).start();
		getComponents().get(1).start();
	}
	
	/**
	 * Set the received exchange e received by this main term as input of
	 * the two contained terms.
	 */
	public void setMessage(String uri, Exchange e){
		getComponents().get(0).setMessage(uri,e);
		getComponents().get(1).setMessage(uri,e);
	}

}
