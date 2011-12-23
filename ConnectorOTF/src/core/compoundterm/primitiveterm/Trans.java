package core.compoundterm.primitiveterm;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Trans extends PrimitiveTerm{

	Class methodclass;
	String methodname;
	String receiver;
	Port receiver_port,source_port;
	
	public Trans(final String sourceUri, Class in_type, final String receiverUri, Class out_type){
		source_port = new Port(sourceUri,in_type,getId());
		addSource(source_port);
		receiver_port = new Port(receiverUri,out_type,getId());
		addReceiver(receiver_port);
		
		System.out.println("Component "+this+" added, source: "+sourceUri+" to: "+receiverUri);
	}
	
	public Trans(final String sourceUri, Class in_type,final String receiverUri, Class out_type,Class method_class, String method_name){
		source_port = new Port(sourceUri,in_type,getId());
		addSource(source_port);
		receiver_port = new Port(receiverUri,out_type,getId());
		addReceiver(receiver_port);
		setTransformLogic(method_class, method_name);
		System.out.println("Component "+this+" added, source: "+sourceUri+" to: "+receiverUri);
	}
	
	public void setTransformLogic(Class method_class, String method_name){
		methodclass = method_class;
		methodname = method_name;
	}
	
	@Override
	public void start() {
		super.start();
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(internal+""+getId()).
					transform().method(methodclass, methodname).
					to(receiver_port.getUri());
				}
			});
			System.out.println("Component "+this+" started source(s) "+getSources_uri()+" "+getId()+" receiver(s)"+getReceivers_uri());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		if(source_port.getUri().equals(uri)){
			producer.send(internal+""+source_port.getId().get(0), e);
		}
	}
}
