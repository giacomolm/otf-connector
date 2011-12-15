package core.compoundterm.primitiveterm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import newTest.Department;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Split extends PrimitiveTerm{

	Class methodclass = Split.class;
	String methodname = "split";
	Router router = new Router(Split.class, "route");
	static int i = 0;
	int id;
	public Port source_port;
	ArrayList<Port> receivers_port = new ArrayList<Port>();
	String[] receivers;
	
	public Split(final String sourceUri, Class in_type,String receiversuri,Class out_type) {
		// TODO Auto-generated constructor stub
		source_port = new Port(sourceUri,in_type,order);
		addSource(source_port);
		id=order;
		receivers = receiversuri.split(",");
		for(int i=0; i<receivers.length; i++){
			Port port = new Port(receivers[i],out_type,order);
			port.setTerm(this);
			addReceiver(port);
		}
		receivers_port.addAll(receivers_uri);
		System.out.println("Component "+this+" added, source: ("+internal+""+order+") to: "+receiversuri);
		id=order;
		order+=2;
	}

	
	public Split(final String sourceUri, Class in_type, String receiversuri, Class out_type, Class method_class, String method_name,final Class routeclass, final String routemethod) {
		// TODO Auto-generated constructor stub
		super(new Port(sourceUri,in_type,order));
		receivers = receiversuri.split(",");
		for(int i=0; i<receivers.length; i++){
			Port port = new Port(receivers[i],out_type,order);
			port.setTerm(this);
			addReceiver(port);
		}
		setSplittingLogic(method_class, method_name);
		setRoutingLogic(routeclass,routemethod);
		System.out.println("Component "+this+" added, source: ("+internal+""+order+") to: "+receiversuri);
		id=order;
		order+=2;
	}	
	
	public void setSplittingLogic(Class method_class, String method_name){
			// TODO Auto-generated constructor stub
			methodclass = method_class;
			methodname = method_name;
	}
	
	public void setRoutingLogic(Class routeclass, String routeMethod){
		router = new Router(routeclass,routeMethod);
	}
	
	//default split che non splitta
	public Collection<Object> split(Object body){
		ArrayList<Object> al = new ArrayList<Object>();
		al.add(body);
		return al;
	}
	
	//default routing
	public Collection<String> route(String body){
		Collection<String> dest = new ArrayList<String>();
		if(i<receivers.length){
			dest.add(receivers[i++]);
			return dest;
		}
		else{
			i=0;
			dest.add(receivers[i++]);
			return dest;
		}
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(internal+""+id).
					split().method(methodclass, methodname).
					dynamicRouter(bean(router, "route"));
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Component "+this+" started, source: ("+internal+""+id+")");
		super.start();
	}
	
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		if(source_port.getUri().equals(uri)){
			System.out.println(this);
			for(int i=0; i<source_port.getId().size(); i++)
				producer.send(internal+""+source_port.getId().get(i), e);
		}
	}
}
