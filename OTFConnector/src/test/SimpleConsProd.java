package test;

import core_old.Connector_old;

public class SimpleConsProd {
	public static void main(String[] args) {
		Connector_old c = new Connector_old();
		Customer customer = new Customer(1,"Giacomo");
		c.addSource("direct:start");
		c.addConsumer(Customer.class, SimpleConsProd.class, "isHim");
		c.addProducer(Customer.class, SimpleConsProd.class,  "generate");
		c.addReceiver("direct:stop");
		c.produce("direct:start", customer);
		c.consume("direct:stop");
		c.start(10000);
	}
	
	public Boolean isHim(Customer c){
		if(c.getName().equals("Giacomo"))
			return true;
		else return false;
	}
	
	public Customer generate(Customer c){
		if(c.getName().equals("Giacomo"))
			return new Customer(2, "Filippo");
		else return null;
	}
}
