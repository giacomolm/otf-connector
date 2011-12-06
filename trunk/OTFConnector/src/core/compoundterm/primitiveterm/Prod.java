package core.compoundterm.primitiveterm;

import java.util.Iterator;

import org.apache.camel.builder.RouteBuilder;

import core.Connector;
import core.Port;

public class Prod extends PrimitiveTerm{
	
	org.apache.camel.ProducerTemplate producer;//= ( new DefaultCamelContext()).createProducerTemplate();
	PrimitiveTerm p = null;
	
	
	private Object message;
	public Prod(final String receiverUri, Class type,final Object o) {
		super(new Port(),new Port(receiverUri, type, order));
		order++;
		setMessage(o);
		producer = context.createProducerTemplate();
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					producer.sendBody(getReceiver().getUri(), o);

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		try {
			context.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

}
