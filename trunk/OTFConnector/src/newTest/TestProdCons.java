package newTest;

import core.Connector;
import core.Consumer;
import core.Producer;

public class TestProdCons {
	
	public static void main(String[] args) {
		Connector c = new Connector();
		String message = "content";
		/*Producer p = new Producer(c,"vm:start", message);
		Consumer cons = new Consumer(p);
		Producer p2 = new Producer(c, cons, "vm:stop", "content2");
		Consumer cons2 = new Consumer(p2);*/
		Consumer cons2 = new Consumer(new Producer(c, new Consumer(new Producer(c,"vm:start", message)), "vm:stop", "content2"));
		c.add(cons2);
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
