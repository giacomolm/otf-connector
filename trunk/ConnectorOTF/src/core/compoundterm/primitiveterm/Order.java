package core.compoundterm.primitiveterm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

/**
 * Order class implements Ordering mismatch primitive introduced in Connector 
 * Algebra Theory. A component can expect to receive messages in an order 
 * different from the order used by the sending component. The mismatch can be
 * resolved by introducing an ordering primitive Order, that acts like 
 * permutation function.
 * This primitive is semantically identical to Resequencer pattern from EIP.
 * This pattern allows you to reorganise messages based on some comparator. This
 * comparator must be based on Expression interface: this package include 
 * Comparator class.
 * The order is directly implemented in Apache Camel Framework: we use resequence()
 * method into route definition.
 * How users can define permutation between messages? User simply define expected
 * order while create order term: this is defined by the order of source uri 
 * passed as parameter. Every time that a message arrive, this term assign it a
 * sequence number depending by source uri position into source uri list.
 * Messages are resequenced by this id and finally are sended to receivers 
 * endpoint.
 * @author giacomolm
 *
 */
public class Order extends PrimitiveTerm{
	
	ArrayList<Port> sources_port = new ArrayList<Port>();
	ArrayList<Port> receivers_port = new ArrayList<Port>();
	Exchange al[];
	int sended = 0;
	private static boolean sequence[]; 

	/**
	 * Build new order term. Rember that the order of uri listed as first 
	 * parameter define also the expected order of messages. If messages
	 * don't arrive in this order, this term deals with resequencing.
	 * @param sourcesuri source uri separated by a comma 
	 * @param in_types list of types accepted by this term
	 * @param receiversuri receivers uri separated by a comma 
	 * @param out_type list of types outputted by this term
	 */
	public Order(String sourcesuri, ArrayList<Class> in_types,String receiversuri,ArrayList<Class> out_types) {
		// TODO Auto-generated method stub
		String[] sources = sourcesuri.split(",");
		for(int i=0; i<sources.length; i++){
			Port port = new Port(sources[i],in_types,getId());
			port.setTerm(this);
			sources_port.add(port);
			addSource(port);
		}
		al = new Exchange[sources.length];
		
		String[] receivers = receiversuri.split(",");
		for(int i=0; i<receivers.length; i++){
			Port port = new Port(receivers[i],out_types,getId());
			port.setTerm(this);
			//Lo aggiungo alla componente interna
			receivers_port.add(port);
			addReceiver(port);
		}
		sequence = new boolean[sources.length];
		out.append("Component "+this+" added\n");
		out.flush();
	}
	
	/**
	 * Add route associated to this term. Resequence message depending by 
	 * message id. Id is included in message header. Once messages are 
	 * requensenced, are sended to receiver uri indicated.
	 */
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
					resequence(header("seqnum")).size(receivers_port.size()).
					process(new Processor() {
						@Override
						public void process(Exchange arg0) throws Exception {
							// TODO Auto-generated method stub
								producer.send(receivers_port.get(sended).getUri(), arg0);
								out.append("Sendend "+arg0+" to "+receivers_port.get(sended).getUri()+"\n");
								out.flush();
								sended++;
								for(int i=0; i<receivers_port.size(); i++){
									sequence[i]=false;
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
	
	@Override
	/**
	 * Set exchange e as input message of the Order term. Every time that a message
	 * arrive, this term assign it a sequence number depending by uri position in
	 * source_uri list, hence by the definition of the permutation. Messages are then
	 * forwarded to internal endpoint. 
	 * @param uri source uri of message received
	 * @param e exchange containing message received
	 */
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		boolean trovato = false;
		int k=0;
		for(Iterator<Port> it = sources_port.iterator(); it.hasNext()&&!trovato;){
			Port temp = it.next();
			if(temp.getUri().equals(uri)&&!sequence[k]){
				sequence[k]=true;
				for(int i=0; i<temp.getId().size(); i++){
					trovato = true;
					al[k]=e;
					e.getIn().setHeader("seqnum", new Integer(k));
					producer.send(internal+""+temp.getId().get(i), e);
				}
			}
			k++;
		}
	}

}
