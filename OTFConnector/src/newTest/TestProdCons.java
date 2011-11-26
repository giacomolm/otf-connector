package newTest;

import core.Connector;
import core.Consumer;
import core.Producer;

public class TestProdCons {
	
	public static void main(String[] args) {
		String message = "content";
		Producer p = new Producer("direct:start", message);
		Consumer cons = new Consumer(p);
		Connector c = new Connector();
		c.add(p);
		c.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.stop();
	}
}
