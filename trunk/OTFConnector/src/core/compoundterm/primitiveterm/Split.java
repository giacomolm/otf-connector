package core.compoundterm.primitiveterm;

import java.util.ArrayList;
import java.util.Collection;

import newTest.Department;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class Split extends PrimitiveTerm{

	Class methodclass;
	String methodname;
	Router router;
	
	public Split(final String sourceUri, Collection<String> receivers_uri) {
		// TODO Auto-generated constructor stub
		super(sourceUri, receivers_uri);
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(getSource()).
					split().method(methodclass, methodname).
					dynamicRouter(bean(router, "route"));
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Split(final String sourceUri, Collection<String> receivers_uri,Class method_class, String method_name) {
		// TODO Auto-generated constructor stub
		super(sourceUri, receivers_uri);
		setSplittingLogic(method_class, method_name);
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(getSource()).
					split().method(methodclass, methodname).
					dynamicRouter(bean(router, "route"));
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Split(final String sourceUri, Collection<String> receivers_uri,Class method_class, String method_name,Class routeclass, String routeMethod) {
		// TODO Auto-generated constructor stub
		super(sourceUri, receivers_uri);
		setSplittingLogic(method_class, method_name);
		setRoutingLogic(routeclass, routeMethod);
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(getSource()).
					split().method(methodclass, methodname).
					dynamicRouter(bean(router, "route"));
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void setSplittingLogic(Class method_class, String method_name){
			// TODO Auto-generated constructor stub
			methodclass = method_class;
			methodname = method_name;
	}
	
	public void setRoutingLogic(Class routeclass, String routeMethod){
		router = new Router(routeclass,routeMethod);
	}
}
