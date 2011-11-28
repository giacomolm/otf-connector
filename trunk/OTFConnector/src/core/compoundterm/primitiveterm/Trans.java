package core.compoundterm.primitiveterm;

import org.apache.camel.builder.RouteBuilder;

public class Trans extends PrimitiveTerm{

	Class methodclass;
	String methodname;
	
	public Trans(final String sourceUri, final String receiverUri){
		super(sourceUri, receiverUri);
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(sourceUri).
					transform().method(methodclass, methodname).
					to(receiverUri);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Trans(final String sourceUri, final String receiverUri,Class method_class, String method_name){
		super(sourceUri, receiverUri);
		setTransformLogic(method_class, method_name);
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(sourceUri).
					transform().method(methodclass, methodname).
					to(receiverUri);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setTransformLogic(Class method_class, String method_name){
		methodclass = method_class;
		methodname = method_name;
	}
}
