package core.compoundterm.primitiveterm;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Order extends PrimitiveTerm{
	
	ArrayList<Port> sources_port = new ArrayList<Port>();
	ArrayList<Port> receivers_port = new ArrayList<Port>();
	Exchange al[];
	int received = 0, sended = 0;

	public Order(String sourcesuri, Class in_type,String receiversuri,Class out_type) {
		// TODO Auto-generated method stub
		String[] sources = sourcesuri.split(",");
		for(int i=0; i<sources.length; i++){
			Port port = new Port(sources[i],in_type,getId());
			port.setTerm(this);
			sources_port.add(port);
			addSource(port);
		}
		al = new Exchange[sources.length];
		
		String[] receivers = receiversuri.split(",");
		for(int i=0; i<receivers.length; i++){
			Port port = new Port(receivers[i],out_type,getId());
			port.setTerm(this);
			//Lo aggiungo alla componente interna
			receivers_port.add(port);
			addReceiver(port);
		}
		System.out.println("Component "+this+" added");
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
					//resequence(body()).size(sources_port.size()).
					process(new Processor() {
						@Override
						public void process(Exchange arg0) throws Exception {
							// TODO Auto-generated method stub
							boolean block = false;
							for(int i=sended; i<received&&!block; i++){
								if(al[i]!=null){
									producer.send(receivers_port.get(i).getUri(), al[i]);
									System.out.println("Sendend "+al[i]+" to "+receivers_port.get(i).getUri());
									sended++;
								}
								else block = true;
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
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		boolean trovato = false;
		for(Iterator<Port> it = sources_port.iterator(); it.hasNext()&&!trovato;){
			Port temp = it.next();
			if(temp.getUri().equals(uri)){
				for(int i=0; i<temp.getId().size(); i++){
					trovato = true;
					producer.send(internal+""+temp.getId().get(i), e);
				}
				received++;
				al[sources_port.indexOf(temp)] = e;
			}
		}
	}

}
