package core.compoundterm.primitiveterm;

import java.io.IOException;
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
import core.exceptions.DefaultRoutingLogicException;
import core.exceptions.DefaultSplitLogicException;

/**
 * Split class define behavior of split message mismatch primitive.
 * A component may expect to receive a message a as a sequence of fragments 
 * of a. If message a can be decomposed into a1 , . . . , an , then the mismatch
 * may be resolved with a primitive Split which accepts message a as input 
 * and offers a1 , . . . , an as output in that order.
 * Split message mismatch is semantically identically to Splitter pattern
 * included in EIP. Apache Camel provide a direct implementation of this 
 * pattern: we can simply use split() method into route definition.
 * This type of primitive includes two types of logic: splitting logic and 
 * routing logic. 
 * With splitting logic user must define new method having as
 * parameter the input message; body of the method processes the input message
 * splitting it into sub-messages of the same type: this method must returns
 * the set of sub-message under predefined type (such as a Collection).
 * User must also set routing logic: must be defined new method deals with
 * output message receivers; for each splitted message must be decided the 
 * receiver. This method take as parameter the input message, and returns 
 * a string collection of receiverUri. 
 * The routing logic is based on <a href="http://camel.apache.org/dynamic-router.html">
 * dynamic router</a> pattern, also defined in Camel: it allows you to route messages 
 * while avoiding the dependency of the router on all possible destinations while 
 * maintaining its efficiency.
 * Information about logic and routing splitting must be included with the 
 * split definition. If these information are not defined, splitter use default
 * behaviour for splitting and routing. Default splitter consides each input message
 * as an array of object (Object[]). The default router send each splitted message
 * to all destinations uris. 
 * 
 * @author giacomolm
 *
 */
public class Split extends PrimitiveTerm{

	Class methodclass = DefaultSplitLogic.class;
	String methodname = "split";
	public Port source_port;
	ArrayList<Port> receivers_port = new ArrayList<Port>();
	String[] receivers;
	
	Object router_class;
	String router_name;
	
	/**
	 * Build new split term with base information. This term consumes messages 
	 * from sourceUri and can send the result of split to receivers uri
	 * @param sourceUri Term consumes messages from this sourceUri
	 * @param in_type Type of input message
	 * @param receiversuri Term can send splitted message to this set of receivers
	 * @param out_type Type of splitted messages
	 */
	public Split(final String sourceUri, Class in_type,String receiversuri,Class out_type) {
		// TODO Auto-generated constructor stub
		source_port = new Port(sourceUri,in_type,getId());
		addSource(source_port);
		receivers = receiversuri.split(",");
		for(int i=0; i<receivers.length; i++){
			Port port = new Port(receivers[i],out_type,getId());
			port.setTerm(this);
			addReceiver(port);
		}
		receivers_port.addAll(receivers_uri);
		try {
			router_class = new DefaultRoutingLogic(receiversuri);
		} catch (DefaultRoutingLogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		router_name = "route";
		
		out.append("Component "+this+" added, source: ("+internal+""+order+") to: "+receiversuri+"\n");
		out.flush();
	}

	/**
	 * Build new split term, such the previous constructor, but now adding
	 * information about splitting and routing logics 
	 * @param sourceUri Term consumes messages from this sourceUri
	 * @param in_type Type of input message
	 * @param receiversuri Term can send splitted message to this set of receivers
	 * @param out_type Type of splitted messages
	 * @param method_class Class of the splitting method
	 * @param method_name Name of splitting method
	 * @param routeclass Class containing routing method
	 * @param routemethod Name of routing method
	 */
	public Split(final String sourceUri, Class in_type, String receiversuri, Class out_type, Class method_class, String method_name,final Class routeclass, final String routemethod) {
		// TODO Auto-generated constructor stub
		source_port = new Port(sourceUri,in_type,getId());
		addSource(source_port);
		receivers = receiversuri.split(",");
		for(int i=0; i<receivers.length; i++){
			Port port = new Port(receivers[i],out_type,getId());
			port.setTerm(this);
			addReceiver(port);
		}
		setSplittingLogic(method_class, method_name);
		setRoutingLogic(routeclass,routemethod);
		out.append("Component "+this+" added, source: ("+internal+""+getId()+") to: "+receiversuri+"\n");
		out.flush();
	}	
	
	/**
	 * Set information about splitting logic
	 * @param method_class Class containing splitting method 
	 * @param method_name Name of method deals with splitting
	 */
	public void setSplittingLogic(Class method_class, String method_name){
			// TODO Auto-generated constructor stub
			methodclass = method_class;
			methodname = method_name;
	}
	
	/**
	 * Set information about routing logic
	 * @param routeobj Object containing routing method 
	 * @param routeMethod Name (String) of the routing method
	 */
	public void setRoutingLogic(Object routeobj, String routeMethod){
		//router = new ReflectionExample(routeclass,routeMethod);
		router_class = routeobj;
		router_name = routeMethod;
	}
	
	/**
	 * Starts context associated the spit term. It simply add new route: consumes
	 * message from internal endpoint, split message based on splitting logic
	 * and sends splitted messages to receivers uri based on routing logic. 
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub

					
					from(internal+""+getId()).
					split().method(methodclass, methodname).
					dynamicRouter(bean(router_class, router_name));
				}
			});
			out.append("Component "+this+" started, source: ("+internal+""+getId()+")\n");
			out.flush();
		}catch (DefaultSplitLogicException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Set the Exchange e as input message of this split term. This term
	 * check the equality between received uri and source uri: if uris are
	 * identical, the exchange is forwarded to internal endpoint
	 */
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		if(source_port.getUri().equals(uri)){
			for(int i=0; i<source_port.getId().size(); i++)
				producer.send(internal+""+source_port.getId().get(i), e);
		}
	}
}
