package core;

import java.util.ArrayList;

import newTest.Department;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class Splitter extends Primitive{

	Class methodclass;
	String methodname;
	Router router;
	
	public Splitter(String sourceUri) {
		// TODO Auto-generated constructor stub
		super(sourceUri);
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				from(source_uri).
				split().method(methodclass, methodname).
				dynamicRouter(bean(router, "route"));
			}
		};
	}
	
	public Splitter(String sourceUri,Class method_class, String method_name) {
		// TODO Auto-generated constructor stub
		super(sourceUri);
		setSplittingLogic(method_class, method_name);
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				from(source_uri).
				split().method(methodclass, methodname).
				dynamicRouter(bean(router, "route"));
			}
		};
	}
	
	
	public Splitter(final Primitive p){
		super(p.getReceiver_uri());
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				
				includeRoutes(p.getRoute());
				from(source_uri).
				split().method(methodclass, methodname).
				dynamicRouter(bean(router, "route"));
			}
		};
	}
	
	public Splitter(final Primitive p,Class method_class, String method_name,Class routeclass, String routeMethod){
		super(p.getReceiver_uri());
		setSplittingLogic(method_class, method_name);
		setRoutingLogic(routeclass,routeMethod);
		myroute = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				includeRoutes(p.getRoute());
				
				from(source_uri).
				split().method(methodclass, methodname).
				dynamicRouter(bean(router, "route"));
			}
		};
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
