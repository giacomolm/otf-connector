package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import it.univaq.disim.connectorOTF.core.Port;

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
	private int size = 1;
	Object permutation = new DefaultOrderLogic();
	String perm_method_name = "order"; 

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
		size = receivers_port.size();
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
					//è questa prossima istruzione che fa girare sempre il programma
					resequence(header("seqnum")).size(size).
					process(new Processor() {
						@Override
						public void process(Exchange arg0) throws Exception {
							// TODO Auto-generated method stub
							//il get di 0 permette di assumere che abbiamo un singolo receiver
							producer.send(receivers_port.get(0).getUri(), arg0);
							out.append("Sendend message "+arg0+"\n");
							out.flush();
							/*sended++;
							for(int i=0; i<receivers_port.size(); i++){
								sequence[i]=false;
							}*/
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
			/* potrei fare così.. prendo n messaggi in ingresso e poi su questa
			 * ci applico una funzione di permutazione
			 */
			if(temp.getUri().equals(uri)/*&&!sequence[k]*/){
				//sequence[k]=true;
				for(int i=0; i<temp.getId().size(); i++){
					trovato = true;
					al[k]=e;
					
					//Recalling permutation function
					Integer sequence_num = new Integer(0);
					try {
						Method myMethod = null;
						Method[] m = permutation.getClass().getDeclaredMethods();
						for(int v=0; v<m.length; v++){
							if(m[v].getName().equals(perm_method_name)){
								myMethod = m[v];
							}
						}
						sequence_num =  (Integer) myMethod.invoke(permutation,e.getIn().getBody());
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.getIn().setHeader("seqnum", sequence_num);
					producer.send(internal+""+temp.getId().get(i), e);
				}
			}
			k++;
		}
	}

	public void setSequenceSize(int s){
		this.size = s;
	}
	
	public void setPermutation(Object o, String funct_name){
		permutation = o;
		perm_method_name = funct_name;
	}
}
