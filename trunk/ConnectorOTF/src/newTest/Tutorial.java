package newTest;
import java.util.ArrayList;
import java.util.Collection;

import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.Prod;
import core.compoundterm.primitiveterm.Split;


public class Tutorial {

	public static void main(String[] args) {
		Split s = new Split("mina:tcp://localhost:6881?textline=true&sync=false", newTest.Cart.class, "mina:udp://localhost:6882?textline=true&sync=false",Item.class);
		s.setSplittingLogic(Tutorial.class, "splitItem");
		s.setRoutingLogic(Tutorial.class, "routing");
		s.start();
	
		Cons c = new Cons("mina:udp://localhost:6882?textline=true&sync=false", Item.class);
		c.start();
		
		/*Cons c2 = new Cons("mina:udp://localhost:6882?textline=true&sync=false", Cart.class);
		c2.start();*/
		
		Item i1 = new Item(000, "Descrizione del primo item");
		Item i2 = new Item(000, "Descrizione del secondo item");
		Cart cart = new Cart(002);
		cart.addItem(i1);
		cart.addItem(i2);
		Prod p = new Prod("mina:tcp://localhost:6881?textline=true&sync=false", newTest.Cart.class, cart);	
		p.start();
		
		
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Item> splitItem(Cart c) {
		System.out.println("converting1 "+c.getClass());
		Cart cart =  (Cart) c;
		System.out.println("converting2 "+cart);
		return cart.getItems();
	}
	
	public Collection<String> routing(Item i){ 
		Collection<String> receivers = new ArrayList<String>();
		System.out.println("routing");
		receivers.add("mina:udp://localhost:6882?textline=true&sync=false");
		System.out.println("receivers");
		return receivers;
	}
}