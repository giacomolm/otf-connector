package core.compoundterm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Altern extends CompoundTerm{

	CompoundTerm c1,c2;
	Collection<Port> sources = new ArrayList<Port>(),receivers = new ArrayList<Port>();
	
	public Altern(CompoundTerm c1, CompoundTerm c2) {
		addSources_uri(c1.getSources_uri());
		addReceivers_uri(c1.getReceivers_uri());
		addSources_uri(c2.getSources_uri());
		addReceivers_uri(c2.getReceivers_uri());
		c1.setComposed();
		c2.setComposed();
		/*c1.getSources_uri().clear();
		c2.getSources_uri().clear();
		c1.getReceivers_uri().clear();
		c2.getReceivers_uri().clear();*/
		this.c1 = c1;
		this.c2 = c2;
		//ci teniamo le sorgenti in quanto ci serviranno per discriminare
		sources.addAll(sources_uri);
		receivers.addAll(receivers_uri);
	}
	
	@Override
	public void start() {
		if(!isComposed()){
			// Verifica qual'è il tipo in ingresso e avvia la componente desiderata
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
									int k = 0;
									System.out.println(sources_uri);
									for(Iterator<Class> i = temp.getType().iterator(); i.hasNext();){
										Class c = i.next();
										Object message = exchange.getIn().getBody(c);
										if(message!=null){
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
			Iterator<Port> p = c1.getSources_uri().iterator();
			while(p.hasNext()){
				final Port temp = p.next();
				//System.out.println("term "+temp.getTerms());
				for(Iterator<Class> i = temp.getType().iterator(); i.hasNext();){
					Class c = i.next();
					Object message = e.getIn().getBody(c);
					if(message!=null){
						c1.start();
						c1.setMessage(temp.getUri(), e);
						//producer.sendBody(internal+""+temp.getId().get(k), message);	
					}
				}
			}
			p = c2.getSources_uri().iterator();
			while(p.hasNext()){
				final Port temp = p.next();
				//System.out.println("term "+temp.getTerms());
				int k=0;
				for(Iterator<Class> i = temp.getType().iterator(); i.hasNext();){
					Class c = i.next();
					Object message = e.getIn().getBody(c);
					if(message!=null){
						c2.start();
						c2.setMessage(temp.getUri(), e);
						//producer.sendBody(internal+""+temp.getId().get(k), message);	
					}
				}
			}
	}

}
