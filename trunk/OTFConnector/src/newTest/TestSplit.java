package newTest;

import java.util.ArrayList;

import core.Connector;
import core.Splitter;

public class TestSplit {
	
	public static void main(String[] args) {
		Splitter splitter = new Splitter("direct:start");
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
	
	public ArrayList<String> routing(String body,ArrayList<String> receivers){
		ArrayList<String> al = new ArrayList<String>();
		al.add("vm:stop");
		return al;
	}
}
