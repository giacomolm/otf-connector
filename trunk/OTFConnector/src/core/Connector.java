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

import core.compoundterm.primitiveterm.PrimitiveTerm;



public class Connector {

	private CamelContext context;
	private Collection<PrimitiveTerm> primitivecollection;
	boolean started = false;
	
	public Connector() {
		// TODO Auto-generated constructor stub
		context = new DefaultCamelContext();
		primitivecollection = new ArrayList<PrimitiveTerm>();
	}
	
	public void start(){
		try {
			for(Iterator<PrimitiveTerm> i = primitivecollection.iterator(); i.hasNext();){
				PrimitiveTerm p = i.next();
				context.addRoutes(p.getRoute());
			}
			started=true;
			context.start();
			/*Customer c = new Customer(1,"Giacomo");
			Department d1 =  new Department(1, "informatica");
			Department d2 =  new Department(2, "matematica");
			c.addDepartments(d1);
			c.addDepartments(d2);
			ProducerTemplate template = context.createProducerTemplate();
			template.sendBody("direct:start", "contento");
			
			/*PollingConsumer consumer = context.getEndpoint("vm:stop2").createPollingConsumer();
			consumer.start();
			Exchange e = consumer.receive();
			/*Customer customer = e.getIn().getBody(Customer.class);
			System.out.println(customer.getDepartments().get(0).name);*/
			/*Department d = e.getIn().getBody(Department.class);
			System.out.println(d.name);
			/*PollingConsumer consumer = context.getEndpoint("log:foo").createPollingConsumer();
			consumer.start();
			Exchange e = consumer.receive();
			System.out.println(e);*/
			
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
	
	public void add(PrimitiveTerm p){
		primitivecollection.add(p);
	}

	public CamelContext getContext() {
		return context;
	}

	public void setContext(CamelContext context) {
		this.context = context;
	}
	
	public boolean isStarted(){
		return started;
	}
}
