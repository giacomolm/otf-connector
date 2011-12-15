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
	int id;
	
	public Trans(final String sourceUri, Class in_type, final String receiverUri, Class out_type){
		source_port = new Port(sourceUri,in_type,order);
		addSource(source_port);
		receiver_port = new Port(receiverUri,out_type,order);
		addReceiver(receiver_port);
		//setReceiver(receiverUri);
		System.out.println("Component "+this+" added, source: ("+internal+""+order+") to: "+receiverUri);
		id = order;
		order++;
	}
	
	public Trans(final String sourceUri, Class in_type,final String receiverUri, Class out_type,Class method_class, String method_name){
		source_port = new Port(sourceUri,in_type,order);
		addSource(source_port);
		receiver_port = new Port(receiverUri,out_type,order);
		addReceiver(receiver_port);
		setTransformLogic(method_class, method_name);
		//setReceiver(receiverUri);
		System.out.println("Component "+this+" added, source: ("+internal+""+order+") to: "+receiverUri);
		id=order++;
	}
	
	public void setTransformLogic(Class method_class, String method_name){
		methodclass = method_class;
		methodname = method_name;
	}
	
	@Override
	public void start() {
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(internal+""+id).
					transform().method(methodclass, methodname).
					to(receiver_port.getUri());
				}
			});
			System.out.println("Component "+this+" started source"+internal+""+id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.start();
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
