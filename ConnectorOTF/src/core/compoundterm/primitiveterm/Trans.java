package core.compoundterm.primitiveterm;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Trans extends PrimitiveTerm{

	Class methodclass;
	String methodname;
	String receiver;
	int id;
	
	public Trans(final String sourceUri, Class in_type, final String receiverUri, Class out_type){
		super(new Port(sourceUri,in_type,order), new Port(receiverUri,out_type,order));
		setReceiver(receiverUri);
		System.out.println("Component "+this+" added, source: ("+internal+""+order+") to: "+receiverUri);
		id = order;
		order++;
	}
	
	public Trans(final String sourceUri, Class in_type,final String receiverUri, Class out_type,Class method_class, String method_name){
		super(new Port(sourceUri,in_type,order), new Port(receiverUri,out_type,order));
		setTransformLogic(method_class, method_name);
		setReceiver(receiverUri);
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
					to(receiver);
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
}
