package core.compoundterm.primitiveterm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Merge extends PrimitiveTerm{

	private ArrayList<Port> sources = new ArrayList<Port>();
	private Port receiver;

	public Merge(final String sourcesuri, Class in_type,String receiveruri,Class out_type) {
		// TODO Auto-generated constructor stub
		String[] sources_uri = sourcesuri.split(",");
		for(int i=0; i<sources_uri.length; i++){
			Port port = new Port(sources_uri[i],in_type,getId());
			port.setTerm(this);
			sources.add(port);
			addSource(port);
		}
		receiver = new Port(receiveruri, out_type, getId());
		addReceiver(receiver);
		System.out.println("Component "+this+" added, source: ("+internal+""+order+") to: "+sourcesuri);
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(internal+""+getId()).
					aggregate(constant(true),new MyAggregationStrategy()).
					completionSize(sources.size()).
					to(receiver.getUri());
					
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Component "+this+" started, source: ("+internal+""+getId()+")");
	}

	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		for(Iterator<Port> i = sources.iterator(); i.hasNext();){
			Port source  = i.next();
			if(source.getUri().equals(uri)){
				producer.send(internal+""+source.getId().get(0), e);
			}
		}
	}
}
