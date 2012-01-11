package core.compoundterm.primitiveterm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import core.Port;

/**
 * Merge class implements merge message mismatch primitive. Similar to the split
 * case, in this case some components expect to receive a single message a in 
 * place of a fragmented version of a. If messages a1 , . . . , an can be 
 * composed into a,then the mismatch may be resolved with a primitive Merge, 
 * which accepts messages as input in specified order, and generates unique 
 * message as output. 
 * Merge primitive is semantically equivalent to Aggregator pattern introduced 
 * with EIP. Apache Camel framework directly implements this pattern: we can 
 * simply use the aggregator() method into route definition. This method require
 * the definition of an aggregation strategy: it means that we must define a new
 * class that extends AggregationStrategy class defined in apache camel framework
 * which allows how creates the new (composed message). You can find more
 * information visiting this link <a href="http://camel.apache.org/aggregator.html">
 * http://camel.apache.org/aggregator.html</a>
 *
 * @author giacomolm
 *
 */

public class Merge extends PrimitiveTerm{

	private ArrayList<Port> sources = new ArrayList<Port>();
	private Port receiver;
	private AggregationStrategy agg_strategy = new MyAggregationStrategy();

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
	
	public Merge(final String sourcesuri, Class in_type,String receiveruri,Class out_type, AggregationStrategy a) {
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
		agg_strategy = a;
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
					aggregate(constant(true),agg_strategy).
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

	public void setAggregationStrategy(AggregationStrategy a){
		this.agg_strategy = a;
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
