package core.compoundterm.primitiveterm;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class Cons extends PrimitiveTerm{

	public Cons(final String sourceUri) {
		super(sourceUri,new ArrayList<String>());
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(sourceUri).
					process(new Processor() {
						@Override
						public void process(Exchange e) throws Exception {
							System.out.println("consumed "+e);
						}
					});
					
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
