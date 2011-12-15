package core.compoundterm.primitiveterm;

import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Prod extends PrimitiveTerm{
	
	org.apache.camel.ProducerTemplate prod;
	PrimitiveTerm p = null;
	String receiver;
	Port receiver_port;
	
	private Object message;
	public Prod(final String receiverUri, Class type,final Object o) {
		receiver_port = new Port(receiverUri, type, order);
		addReceiver(receiver_port);
		prod = context.createProducerTemplate();
		message = o;
		System.out.println("Component "+this+" added, to: "+getReceiver().getUri());
		order++;
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		System.out.println("Sending message "+message+" to "+context.getEndpoint(receiver_port.getUri()));
		producer.sendBody(context.getEndpoint(receiver_port.getUri()), message);
	}


	public org.apache.camel.ProducerTemplate getProducer() {
		return producer;
	}

	public void setProducer(org.apache.camel.ProducerTemplate producer) {
		this.producer = producer;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public Object getMessage() {
		return message;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		
	}

}
