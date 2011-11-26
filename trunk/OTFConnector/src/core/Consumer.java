package core;

import org.apache.camel.builder.RouteBuilder;

public class Consumer extends Primitive{

	public Consumer(String sourceUri) {
		super(sourceUri);
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				from(source_uri).clearOutput();
			}
		};
	}

	public Consumer(final Primitive p){
		super(p.getReceiver_uri());
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				includeRoutes(p.getRoute());
				from(source_uri).clearOutput();
			}
		};
	}
}
