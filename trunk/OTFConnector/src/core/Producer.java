package core;

import org.apache.camel.builder.RouteBuilder;

public class Producer extends Primitive{
	
	org.apache.camel.ProducerTemplate producer;//= ( new DefaultCamelContext()).createProducerTemplate();
	Primitive p = null;
	
	
	private Object message;
	public Producer(final Connector c,String receiverUri, final Object o) {
		setReceiver_uri(receiverUri);
		setMessage(o);
		producer = c.getContext().createProducerTemplate();
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				producer.sendBody(getReceiver_uri(), o);		
			}
		};
	}
	
	public Producer(Connector c, final Primitive p, String receiverUri, final Object o) {
		setReceiver_uri(receiverUri);
		setMessage(o);
		this.p = p;
		producer = c.getContext().createProducerTemplate();
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				includeRoutes(p.getRoute());
				producer.sendBody(getReceiver_uri(), o);
			}
		};
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
