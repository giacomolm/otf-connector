package core.compoundterm;

import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Alternative extends CompoundTerm{

	public Alternative(Collection<String> sourcesuri,
			Collection<String> receiversuri) {
		//super(sourcesuri, receiversuri);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void start() {
		// Verifica qual'è il tipo in ingresso e avvia la componente desiderata
		for(int i=0; i<order; i++){
			Iterator<Port> p = sources_uri.iterator();
			while(p.hasNext()){
				final Port temp = p.next();
				try {
					context.addRoutes(new RouteBuilder() {
						@Override
						public void configure() throws Exception {
							// TODO Auto-generated method stub
							from(temp.getUri()).
							process(new Processor() {
								@Override
								public void process(Exchange exchange) throws Exception {
									// TODO Auto-generated method stub
									Object message = exchange.getIn().getBody(temp.getType());
									if(message!=null){
										temp.getTerm().start();
										producer.sendBody(temp.getUri(), message);	
									}
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
	}

}
