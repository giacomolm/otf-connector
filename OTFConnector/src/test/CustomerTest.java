package test;

import core_old.Connector_old;
import test.Customer;
import test.Department;

public class CustomerTest {

	public static void main(String[] args) {
		Connector_old c = new Connector_old();
		Customer customer = new Customer(1,"Giacomo");
		Department d1 =  new Department(1, "informatica");
		Department d2 =  new Department(2, "matematica");
		customer.addDepartments(d1);
		customer.addDepartments(d2);
		c.addSource("direct:start");
		//c.addTransformer(Customer.class, Handler.class, "addLastName");
		c.addSplitter(CustomerService.class, "splitDepartments");
		c.addReceiver("vm:server");
		c.consume("vm:server");
		c.produce("direct:start", customer);
		c.start(10000);
	}
}
