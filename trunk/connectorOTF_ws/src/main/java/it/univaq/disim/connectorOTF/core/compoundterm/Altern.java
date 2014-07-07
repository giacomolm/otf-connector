package it.univaq.disim.connectorOTF.core.compoundterm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import it.univaq.disim.connectorOTF.core.Port;

/**
 * Altern class implements behaviour of alternate binary operator defined by
 * the connector algebra theory. This class define new term composed by
 * two component term. 
 * Briefly, this operator behave like alternation in regular expressions: in 
 * relation to input message, this term select the sub-term able to manage this
 * message: once selected, term start sub term and forward the message.
 *  
 * @author giacomolm
 *
 */

public class Altern extends CompoundTerm{

	//CompoundTerm c1,c2;
	Collection<Port> sources = new ArrayList<Port>(),receivers = new ArrayList<Port>();
	
	/**
	 * Build new compound term which altern two component terms 
	 * @param c1 First1 component term
	 * @param c2 First1 component term
	 */
	public Altern(CompoundTerm c1, CompoundTerm c2) {
		c1.setComposed();
		c2.setComposed();
		addComponent(c1);
		addComponent(c2);
		//ci teniamo le sorgenti in quanto ci serviranno per discriminare
		sources.addAll(sources_uri);
		receivers.addAll(receivers_uri);
	}
	
	/**
	 * If this term is not a component of another compund term, this method publish
	 * port adding route for each uri. For each exchange received this method control
	 * the input type: if it is compatible with the components ports, this term decide
	 * what term will receive the input message. This decision is based even on message
	 * port type. 
	 */
	@Override
	public void start() {
		if(!isComposed()){
			// Verifica qual'è il tipo in ingresso e avvia la componente desiderata
			Iterator<Port> p = getSources().iterator();
			//System.out.println("Port "+sources_uri);
			while(p.hasNext()){
				final Port temp = p.next();
				try {
					out.append(temp.getUri()+"\n");
					context.addRoutes(new RouteBuilder() {
						@Override
						public void configure() throws Exception {
							// TODO Auto-generated method stub
							from(temp.getUri()).
							process(new Processor() {
								@Override
								public void process(Exchange exchange) throws Exception {
									// TODO Auto-generated method stub
									int k = 0;
									out.append(sources_uri+"\n");
									for(Iterator<Class> i = temp.getType().iterator(); i.hasNext();){
										Class c = i.next();
										Object message = exchange.getIn().getBody(c);
										if(message!=null){
											out.append("start\n");
											temp.getTerms().get(k).start();											
											producer.sendBody(internal+""+temp.getId().get(k), message);	
										}
										k++;
									}
								}
							});
						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			if(!context.getRouteDefinitions().isEmpty())
				out.append(context.getRouteDefinitions()+"\n");
			context.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used when another external term put message in this term
	 * (this happens when this term is a component of compound term and the outer
	 * term receive message from uri indicates by this term). This term, like in
	 * start() method, must select the adapt sub-term which receive the input 
	 * message.
	 */
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
			Iterator<Port> p = component.get(0).getSources().iterator();
			while(p.hasNext()){
				final Port temp = p.next();
				//System.out.println("term "+temp.getTerms());
				for(Iterator<Class> i = temp.getType().iterator(); i.hasNext();){
					Class c = i.next();
					Object message = e.getIn().getBody(c);
					if(message!=null){
						component.get(0).start();
						component.get(0).setMessage(temp.getUri(), e);
						//producer.sendBody(internal+""+temp.getId().get(k), message);	
					}
				}
			}
			//Stessa cosa per il secondo componente
			p = component.get(1).getSources().iterator();
			while(p.hasNext()){
				final Port temp = p.next();
				//System.out.println("term "+temp.getTerms());
				int k=0;
				for(Iterator<Class> i = temp.getType().iterator(); i.hasNext();){
					Class c = i.next();
					Object message = e.getIn().getBody(c);
					if(message!=null){
						component.get(1).start();
						component.get(1).setMessage(temp.getUri(), e);
						//producer.sendBody(internal+""+temp.getId().get(k), message);	
					}
				}
			}
	}

}
