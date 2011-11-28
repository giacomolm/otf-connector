package test;

import java.util.ArrayList;
import java.util.Iterator;

import newTest.Customer;
import newTest.CustomerService;
import newTest.Department;

import core.Connector;
import core.compoundterm.primitiveterm.Split;
import core.compoundterm.primitiveterm.Trans;

public class TestComp {

	public static void main(String[] args) {
		Trans t = new Trans("direct:start", "vm:stop");
		t.setTransformLogic(TestComp.class, "addLastName");
		Split s = new Split(t);
		s.setSplittingLogic(CustomerService.class, "splitDepartments");
		s.setRoutingLogic(TestComp.class, "routing");
		Connector c = new Connector();
		c.add(s);
		c.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.stop();
	}
	
	public newTest.Customer addLastName(newTest.Customer c){
		for(Iterator<newTest.Department> i = c.getDepartments().iterator(); i.hasNext();){
			Department d = i.next();
			d.name = "Dipartimento di "+d.name;
		}
		return c;
	}
	
	public ArrayList<String> routing(String body,ArrayList<String> receivers){
		ArrayList<String> al = new ArrayList<String>();
		al.add("vm:stop2");
		return al;
	}
}
