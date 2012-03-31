package core.compoundterm.primitiveterm;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;


import core.Port;

/**
 * Cons class model concept of extra send primitive. This first mismatch 
 * considers a component that generates a redundant message a. 
 * Such a mismatch may be resolved by introducing a consumer that swallows
 * the superfluous message. We model this by a parameterised primitive Cons.
 * Apache camel provides ConsumerTemplate class that deals with consuming
 * of Exchanges from an Endpoint: however we decided to define a dedicated
 * route which consumes from provided endpoint, obtaining tha same behavior. 
 * @author giacomolm
 *
 */
public class Cons extends PrimitiveTerm{
	Port source_port;
	Object consumed_message;
	/**
	 * Build new extra send primitive that consumes message comes from sourceUri
	 * parameter. in_type parameter gives information about type managed by this
	 * term.
	 * @param sourceUri term consume message from this sourceUri
	 * @param in_type term is able to manage this type of input-message
	 */
	public Cons(final String sourceUri, Class in_type) {
		source_port = new Port(sourceUri,in_type,getId()); 
		addSource(source_port);
		out.append("Component "+this+" added, source:  ("+internal+""+getId()+")");
		out.flush();
	}
	
	/**
	 * Build new extra send primitive that consumes message comes from sourceUri
	 * parameter. in_type parameter gives information about type managed by this
	 * term.
	 * @param sourceUri term consume message from this sourceUri
	 * @param in_type term is able to manage this type of input-message
	 * @param o contains consumed message content
	 */
	public Cons(final String sourceUri, Class in_type,Object o) {
		source_port = new Port(sourceUri,in_type,getId()); 
		addSource(source_port);
		o = consumed_message;
		out.append("Component "+this+" added, source:  ("+internal+""+getId()+")");
		out.flush();
	}
	
	/**
	 * Starts context of this cons term. When term is started, add to
	 * context a new route consuming from his internal endpoint; source uri
	 * of this term is linked with this internal endpoint. 
	 * Every time this term consumes message, an output message is shown.
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(internal+""+getId()).
					process(new Processor() {
						@Override
						public void process(Exchange e) throws Exception {
							consumed_message = e.getIn().getBody();
							System.out.append("consumed "+e+" by "+this+"\n");
							out.flush();
						}
					});
				}
			});
			out.append("Component "+this+" started\n");
			out.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * If the uri passed ad parameter is equal to source uri of the term,
	 * the exchange e is sended to terms internal endpoint  
	 */
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		if(source_port.getUri().equals(uri)){
			for(int i=0; i<source_port.getId().size(); i++)
				producer.send(internal+""+source_port.getId().get(i), e);
		}
	}
	
	/**
	 * @return last consumed message
	 */
	public Object getConsumedMessage(){
		return consumed_message;
	}
	
}
