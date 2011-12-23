package core.compoundterm;

import java.util.Collection;
import java.util.Iterator;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Plug extends CompoundTerm{

	//CompoundTerm c1,c2;
	
	public Plug(CompoundTerm c1, CompoundTerm c2) {
		c1.setComposed();
		c2.setComposed();
		addComponent(c1);
		addComponent(c2);
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		if(!isComposed()){
			final Iterator<Port> p = sources_uri.iterator();
			while(p.hasNext()){
				try {
					context.addRoutes(new RouteBuilder() {
							@Override
							public void configure() throws Exception {
								// TODO Auto-generated method stub
								final Port port = p.next();
								if(port.getUri()!=null){
	
									from(port.getUri()).
									process(new Processor() {
										@Override
										public void process(Exchange m) throws Exception {
											// TODO Auto-generated method stub
											Collection<CompoundTerm> terms = port.getTerms();
											for(Iterator<CompoundTerm> i = terms.iterator(); i.hasNext();){
												CompoundTerm term = i.next();
												term.setMessage(port.getUri(),m);
											}
										}
									});
								}
							}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		super.start();
		getComponents().get(0).start();
		getComponents().get(1).start();
	}
	
	public void setMessage(String uri, Exchange e){
		getComponents().get(0).setMessage(uri,e);
		getComponents().get(1).setMessage(uri,e);
	}

}
