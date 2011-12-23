package core.compoundterm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Altern extends CompoundTerm{

	//CompoundTerm c1,c2;
	Collection<Port> sources = new ArrayList<Port>(),receivers = new ArrayList<Port>();
	
	public Altern(CompoundTerm c1, CompoundTerm c2) {
		c1.setComposed();
		c2.setComposed();
		addComponent(c1);
		addComponent(c2);
		//ci teniamo le sorgenti in quanto ci serviranno per discriminare
		sources.addAll(sources_uri);
		receivers.addAll(receivers_uri);
	}
	
	@Override
	public void start() {
		if(!isComposed()){
			// Verifica qual'è il tipo in ingresso e avvia la componente desiderata
			Iterator<Port> p = sources_uri.iterator();
			//System.out.println("Port "+sources_uri);
			while(p.hasNext()){
				final Port temp = p.next();
				System.out.println(temp.getUri());
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
									int k = 0;
									System.out.println(sources_uri);
									for(Iterator<Class> i = temp.getType().iterator(); i.hasNext();){
										Class c = i.next();
										Object message = exchange.getIn().getBody(c);
										if(message!=null){
											System.out.println("start");
											temp.getTerms().get(k).start();											
											producer.sendBody(internal+""+temp.getId().get(k), message);	
										}
										k++;
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
		try {
			if(!context.getRouteDefinitions().isEmpty())
				System.out.println(context.getRouteDefinitions());
			context.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
			Iterator<Port> p = component.get(0).getSources_uri().iterator();
			while(p.hasNext()){
				final Port temp = p.next();
				//System.out.println("term "+temp.getTerms());
				for(Iterator<Class> i = temp.getType().iterator(); i.hasNext();){
					Class c = i.next();
					Object message = e.getIn().getBody(c);
					if(message!=null){
						component.get(0).start();
						component.get(0).setMessage(temp.getUri(), e);
						//producer.sendBody(internal+""+temp.getId().get(k), message);	
					}
				}
			}
			//Stessa cosa per il secondo componente
			p = component.get(1).getSources_uri().iterator();
			while(p.hasNext()){
				final Port temp = p.next();
				//System.out.println("term "+temp.getTerms());
				int k=0;
				for(Iterator<Class> i = temp.getType().iterator(); i.hasNext();){
					Class c = i.next();
					Object message = e.getIn().getBody(c);
					if(message!=null){
						component.get(1).start();
						component.get(1).setMessage(temp.getUri(), e);
						//producer.sendBody(internal+""+temp.getId().get(k), message);	
					}
				}
			}
	}

}
