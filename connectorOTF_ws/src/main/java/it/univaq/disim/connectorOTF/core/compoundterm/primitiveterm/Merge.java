package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import it.univaq.disim.connectorOTF.core.Port;
import it.univaq.disim.connectorOTF.core.exceptions.DefaultAggregationLogicException;

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
 * By default, merge primitive uses a default aggregation strategy: once all expected
 * messages are received, merge sends aggregation (an ArrayList<Object>) result to 
 * receiver(s) uri. 
 *
 * @author giacomolm
 *
 */

public class Merge extends PrimitiveTerm{

	
	private Port receiver;
	private AggregationStrategy agg_strategy = new DefaultAggregationLogic();
	private static boolean sequence[];
	private int completition_size = 1;

	public Merge(final String sourcesuri, Class in_type,String receiveruri,Class out_type) {
		// TODO Auto-generated constructor stub
		String[] sources_uri = sourcesuri.split(",");
                System.out.println("Sources "+sourcesuri);
		for(int i=0; i<sources_uri.length; i++){
			Port port = new Port(sources_uri[i],in_type,getId());
			port.setTerm(this);
			addSource(port);
		}
		completition_size = getSources().size();
		receiver = new Port(receiveruri, out_type, getId());
		addReceiver(receiver);
		sequence = new boolean[sources_uri.length];
		try {
			agg_strategy = new DefaultAggregationLogic(out_type);
		} catch (DefaultAggregationLogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.append("Component "+this+" added, source: ("+internal+""+order+") to: "+receiveruri+"\n");
		out.flush();
	}
	
	public Merge(final String sourcesuri, Class in_type,String receiveruri,Class out_type, AggregationStrategy a) {
		// TODO Auto-generated constructor stub
		String[] sources_uri = sourcesuri.split(",");
		for(int i=0; i<sources_uri.length; i++){
			Port port = new Port(sources_uri[i],in_type,getId());
			port.setTerm(this);
			getSources().add(port);
			addSource(port);
		}
		receiver = new Port(receiveruri, out_type, getId());
		addReceiver(receiver);
		agg_strategy = a;
		sequence = new boolean[sources_uri.length];
		out.append("Component "+this+" added, source: ("+internal+""+order+") to: "+sourcesuri+"\n");
		out.flush();
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
					completionSize(completition_size).
					process(new Processor() {
						@Override
						public void process(Exchange arg0) throws Exception {
							// TODO Auto-generated method stub
                                                        System.out.println("Aggregation result "+arg0);
							for(int i=0; i<sequence.length; i++){
								//dobbiamo azzerare il fatto che abbiamo ricevuto un messaggio di quel tipo
								sequence[i]=false;
							}
                                                        setCurrentState(getStart());
						}
					}).
					to(receiver.getUri());
					
				}
			});
			out.append("Component "+this+" started, source: ("+internal+""+getId()+") completion size:"+completition_size+"\n");                       
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void setAggregationStrategy(AggregationStrategy a){
		this.agg_strategy = a;
	}
	
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		int k = 0;
		//System.out.println("set "+e);
		for(Iterator<Port> i = getSources().iterator(); i.hasNext();){
			Port source  = i.next();
			//controllo se esiste qualche porta associata al merge che sia mappata con quell'uri
			if(source.getUri().equals(uri)){
				
				//verifico se ho già nel 'buffer' un messaggio di quel tipo
				//if(!sequence[k]){
				//	sequence[k]=true;
					
					producer.send(internal+""+source.getId().get(0), e);
				//}
			}
			k++;
		}
	}
	
	public void setCompletitionSize(int size){
		this.completition_size = size;
	}
}
