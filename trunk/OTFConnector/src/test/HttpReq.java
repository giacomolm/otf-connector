package test;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;


public class HttpReq {
	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();

		context.addRoutes(new RouteBuilder() { 
			public void configure() throws Exception { 
				from("http://www.google.it").process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						System.out.println("Questo è il corpo del messaggio "+exchange.getIn().getBody(String.class));
					}
				});
				//to("vm:test");
			}
		});

		//Come posso definire una route che include una composizione di RouteDefinition
		context.start();
		Thread.sleep(10000);
		context.stop();
	}
}
