package core.compoundterm.primitiveterm;

import java.util.ArrayList;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;


import core.Port;

public class Cons extends PrimitiveTerm{
	Port source_port;
	
	public Cons(final String sourceUri, Class in_type) {
		source_port = new Port(sourceUri,in_type,order); 
		addSource(source_port);
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(internal+""+order).
					process(new Processor() {
						@Override
						public void process(Exchange e) throws Exception {
							System.out.println("consumed "+e+" by "+this);
						}
					});
				}
			});
			System.out.println("Component "+this+" added, source:  ("+internal+""+order+")");
			order++;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		System.out.println("Component "+this+" started");
		super.start();
	}
	
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		if(source_port.getUri().equals(uri)){
			System.out.println(this);
			for(int i=0; i<source_port.getId().size(); i++)
				producer.send(internal+""+source_port.getId().get(i), e);
		}
	}
	
}
