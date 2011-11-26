package core;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;

public class Producer extends Primitive{

	public Producer(String receiverUri, final Object o) {
		setReceiver_uri(receiverUri);
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				outBody().in(o);
				sendTo(getReceiver_uri());
			}
		};
	}
	
	public Producer(final Primitive p, String receiverUri, final Object o) {
		super("",receiverUri);
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				includeRoutes(p.getRoute());
				body().in(o);
				sendTo(getReceiver_uri());
			}
		};
	}


}
