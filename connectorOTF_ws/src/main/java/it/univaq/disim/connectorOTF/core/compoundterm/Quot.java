package it.univaq.disim.connectorOTF.core.compoundterm;

import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import it.univaq.disim.connectorOTF.core.Port;

/**
 * Quot class implements concept of quotienting operator mentioned in the 
 * algebra connector theory. It is a binary operator.   
 * Based on P. Bhaduri and S. Ramesh. Interface synthesis and protocol 
 * conversion. Form. Asp. Comput., 20(2):205–224, 2008, it follows that 
 * quotienting is a derived operator: if the two component terms are t and u, 
 * the result of application of the quotienting operator is a new term s, where
 * s = Invert(Plug(t,Invert(u)));
 * @author giacomolm
 *
 */
public class Quot extends CompoundTerm{

	CompoundTerm d1, d2;
	
	/**
	 * Build new Quot term, based on definition of the component terms
	 * passed as parameter. 
	 * @param c1
	 * @param c2
	 */
	public Quot(CompoundTerm c1, CompoundTerm c2) {
		c1.setComposed();
		c2.setComposed();
		d1 = new Invert(c1);
		d2 = c2;
		CompoundTerm d = new Invert(new Plug(d1,d2));
		addComponent(d);
	}
	
	/**
	 * As the quotientig operator is mainly a plugging operator, the behavior
	 * of this method is identical to the behavior of the start() method in the
	 * class Quot. Simply starts the component associated to this terms, and if 
	 * it is the outer term, publish its ports.
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		if(!this.isComposed()){
			//Considero le porte dopo aver fatto la invert
			final Iterator<Port> p = getSources().iterator();
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
											// Recupero tutti i termini associati a quella porta
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
				d1.start();
				d2.start();
			}
		}
	}
	
	/**
	 * Set external message as input of components terms. Message sending
	 * is preceded by simple scanning: if the sub-term considered can receive
	 * message from indicated uri, message is simply, forwarded; if the term
	 * is not linked with indicated uri, this quot term not forward nothing.
	 */
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		for(Iterator<Port> it = getSources().iterator(); it.hasNext();){
			Port port = it.next();
			for(Iterator<Port> itd1 = d1.getSources().iterator(); itd1.hasNext();){
				Port port_d1 = itd1.next();
				//scan all port of 
				if(port.getUri().equals(port_d1.getUri())){
					d1.setMessage(uri, e);
				}
			}
			for(Iterator<Port> itd2 = d2.getSources().iterator(); itd2.hasNext();){
				Port port_d2 = itd2.next();
				if(port.getUri().equals(port_d2.getUri())){
					d2.setMessage(uri, e);
				}
			}
		}
		
	}

}
