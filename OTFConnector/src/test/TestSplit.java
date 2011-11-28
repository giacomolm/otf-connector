package test;

import java.util.ArrayList;

import newTest.CustomerService;

import core.Connector;
import core.compoundterm.primitiveterm.Split;

public class TestSplit {
	
	public static void main(String[] args) {
		Split splitter = new Split("direct:start");
		splitter.setSplittingLogic(CustomerService.class, "splitDepartments");
		splitter.setRoutingLogic(TestSplit.class, "routing");
		Connector c = new Connector();
		c.add(splitter);
		c.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.stop();
	}
	static int i=1;
	//Questo metodo viene chiamato per ogni oggetto splittato
	public ArrayList<String> routing(Object body){
		//System.out.println(((Department)body));
		ArrayList<String> al = new ArrayList<String>();	
		al.add("vm:stop"+i++);
		return al;
	}
}
