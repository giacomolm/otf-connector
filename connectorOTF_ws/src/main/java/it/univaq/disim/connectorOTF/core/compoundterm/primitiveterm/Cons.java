package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;


import it.univaq.disim.connectorOTF.core.Port;
import it.univaq.disim.ips.data.action.Action;
import it.univaq.disim.ips.data.state.State;
import it.univaq.disim.ips.data.transition.Transition;

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
        String source_uri;
	Object consumed_message;
        
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
                source_uri = internal+""+getId();
                //setting start state
                setStart(new State("s0"));
                //adding transition
                addTransition(new Transition(new State("s0"), new Action((String)o),new State("s1")));
                
		out.append("Component "+this+" added, source: "+source_uri);
		out.flush();
	}
        
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
                source_uri = internal+""+getId();
		out.append("Component "+this+" added, source:  "+source_uri);
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
					from(source_uri).
					process(new Processor() {
						@Override
						public void process(Exchange e) throws Exception {
							consumed_message = e.getIn().getBody();
							out.append("consumed "+e+" by "+this+"\n");
							out.flush();
						}
					});
				}
			});
			out.append("Component "+this+" started and consuming from "+source_uri+"\n");
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

    @Override
    public void updateUri(String old_uri, String new_sourceuri, String new_receiveruri) {
        super.updateUri(old_uri, new_sourceuri, new_receiveruri);
        source_uri = new_sourceuri;
    }
        
        
	
}
