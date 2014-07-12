package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import java.io.IOException;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import it.univaq.disim.connectorOTF.core.Port;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Prod class implements missing send primitive introduced with connector 
 * algebra theory. This mismatch describes the case in which a component
 * expects a message a that is not sent by another component. A mismatch 
 * of this type may be resolved by introducing a producer that generates 
 * the required message. This may be modelled by a parameterised primitive Prod.
 * Implementation of missing send primitive was made taking inspiration to 
 * <a href="http://camel.apache.org/producertemplate.html">Producer Template</a>
 * concept: this class "allows you to send message exchanges to endpoints in a
 * variety of different ways to make it easy to work with Camel Endpoint 
 * instances from Java code". 
 * If we are in the case in which a component expects a message a that is 
 * not sent by another component, we can easily send it through this class.
 * 
 * @author giacomolm
 *
 */
public class Prod extends PrimitiveTerm{
	
	org.apache.camel.ProducerTemplate prod;
	PrimitiveTerm p = null;
	String receiver;
	Port receiver_port;
        List<Port> receivers_port = new ArrayList<Port>() ;
	
	private Object message;
	
	/**
	 * Build new prod term which, when it is started, send message o of 
	 * type type to receiverUri endpoint. This constructor simply add new
	 * port defined bt the first two paramenter and the id of this term.
	 * @param receiverUri terms send message to receiverUri 
	 * @param type message sended is of this type
	 * @param o object sended by this term
	 */
	public Prod(final String receiverUri, Class type,final Object o) {
		receiver_port = new Port(receiverUri, type, getId());
		addReceiver(receiver_port);
		prod = context.createProducerTemplate();
		message = o;
		out.append("Component "+this+" added, to: "+receiverUri+"\n");
		out.flush();
	}
	
	/**
	 * Sends 'materially' message to receiver uri. 
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();		
                for(Iterator<Port> i = receivers_port.iterator(); i.hasNext();){
                    try {
                        Port p = i.next();
                        out.append(this+" Sending message "+message+" to "+context.getEndpoint(p.getUri())+"\n");
                        out.flush();                       
                        producer.sendBody(context.getEndpoint(p.getUri()), message);
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Prod.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
	}

	/**
	 * Set the message to send to prefixed receiver
	 * @param message message sendend 
	 */
	public void setMessage(Object message) {
		this.message = message;
	}

	/**
	 * Get the message extra send message sended to receiver uri
	 * @return extra send message
	 */
	public Object getMessage() {
		return message;
	}

	/**
	 * Set the receiver uri of the message
	 * @param receiver Uri of receiver
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

    @Override
    public void updateUri(String old_uri, String new_uri) {
        super.updateUri(old_uri, new_uri); //To change body of generated methods, choose Tools | Templates.
        receivers_port.add(new Port(old_uri, receiver_port.getType(), getId()));
    }

    @Override
    public void setMessage(String uri, Exchange e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addReceiver(Port receiver) {
        super.addReceiver(receiver); //To change body of generated methods, choose Tools | Templates.
        receivers_port.add(receiver);
    }

    

        
}
