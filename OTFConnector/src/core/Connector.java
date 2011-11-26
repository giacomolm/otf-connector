package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import newTest.Customer;
import newTest.Department;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Route;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;



public class Connector {

	private CamelContext context;
	private Collection<Primitive> primitivecollection;
 
	public Connector() {
		// TODO Auto-generated constructor stub
		context = new DefaultCamelContext();
		primitivecollection = new ArrayList<Primitive>();
	}
	
	public void start(){
		try {
			for(Iterator<Primitive> i = primitivecollection.iterator(); i.hasNext();){
				Primitive p = i.next();
				context.addRoutes(p.getRoute());
			}
			context.start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop(){
		try {
			context.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Route> getRoute(){
		return context.getRoutes();
	}
	
	public void add(Primitive p){
		primitivecollection.add(p);
	}
}
