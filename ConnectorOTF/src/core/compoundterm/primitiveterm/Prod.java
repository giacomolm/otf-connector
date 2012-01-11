package core.compoundterm.primitiveterm;

import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

/**
 * Prod class implements missing send primitive introduced with connector 
 * algebra theory. This mismatch describes the case in which a component
 * expects a message a that is not sent by another component. A mismatch 
 * of this type may be resolved by introducing a producer that generates 
 * the required message. This may be modelled by a parameterised primitive
 * Prod.

 * @author giacomolm
 *
 */
public class Prod extends PrimitiveTerm{
	
	org.apache.camel.ProducerTemplate prod;
	PrimitiveTerm p = null;
	String receiver;
	Port receiver_port;
	
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
		System.out.println("Component "+this+" added, to: "+receiverUri);
	}
	
	/**
	 * Sends 'materially' message to receiver uri. 
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		System.out.println("Sending message "+message+" to "+context.getEndpoint(receiver_port.getUri()));
		producer.sendBody(context.getEndpoint(receiver_port.getUri()), message);
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

	/**
	 * Method inherited by the compound term which allows setting
	 * input message in this term. Due this term not consumes 
	 * anything, methods definition is leaved empty.
	 */
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		
	}

}
