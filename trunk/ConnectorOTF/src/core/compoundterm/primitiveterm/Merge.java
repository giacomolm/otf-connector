package core.compoundterm.primitiveterm;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Merge extends PrimitiveTerm{

	private int id;
	private String[] sources_uri;
	private String receiver;

	public Merge(final String sourcesuri, Class in_type,String receiveruri,Class out_type) {
		// TODO Auto-generated constructor stub
		sources_uri = sourcesuri.split(",");
		for(int i=0; i<sources_uri.length; i++){
			Port port = new Port(sources_uri[i],out_type,order);
			port.setTerm(this);
			addSource(port);
		}
		addReceiver(new Port(receiveruri, out_type, order));
		receiver =receiveruri;
		System.out.println("Component "+this+" added, source: ("+internal+""+order+") to: "+sourcesuri);
		id=order;
		order+=2;
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(internal+""+id).
					aggregate(header("id"),new MyAggregationStrategy()).
					completionSize(sources_uri.length).
					to(receiver);
					
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Component "+this+" started, source: ("+internal+""+id+")");
		super.start();
	}

	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		
	}
}
