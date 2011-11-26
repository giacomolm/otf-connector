package core;

import org.apache.camel.builder.RouteBuilder;

public class Transformer extends Primitive{

	Class methodclass;
	String methodname;
	
	public Transformer(String sourceUri, String receiverUri){
		super(sourceUri, receiverUri);
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				from(source_uri).
				transform().method(methodclass, methodname).
				to(getReceiver_uri());
			}
		};
	}

	public Transformer(final Primitive p, String receiverUri){
		super(p.getReceiver_uri(), receiverUri);
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				includeRoutes(p.getRoute());
				// TODO Auto-generated method stub
				from(source_uri).
				transform().method(methodclass, methodname).
				to(getReceiver_uri());
			}
		};
	}
	
	public Transformer(final Primitive p, String receiverUri, final Class method_class, final String method_name){
		super(p.getReceiver_uri(), receiverUri);
		setTransformLogic(method_class, method_name);
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				
				includeRoutes(p.getRoute());
				from(source_uri).
				transform().method(methodclass, methodname).
				to(getReceiver_uri());
			}
		};
	}
	
	public void setTransformLogic(Class method_class, String method_name){
		methodclass = method_class;
		methodname = method_name;
	}
}
