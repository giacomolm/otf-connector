package test;

import core.Connector;
import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.Prod;

public class TestProdCons {
	
	public static void main(String[] args) {
		Connector c = new Connector();
		/*Producer p = new Producer(c,"vm:start", "content");
		Consumer cons = new Consumer(p);
		Producer p2 = new Producer(c, cons, "vm:stop", "content2");
		Consumer cons2 = new Consumer(p2);*/
		Cons cons2 = new Cons(new Prod(c, new Cons(new Prod(c,"vm:start", "content")), "vm:stop", "content2"));
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
