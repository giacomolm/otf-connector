package newTest;

import java.util.ArrayList;
import java.util.Collection;

import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.Merge;
import core.compoundterm.primitiveterm.Prod;
import core.compoundterm.primitiveterm.Split;

public class CompoundMessageProcessor {

	public static void main(String[] args) {
		Item i1 = new Item(000, "Descrizione del primo item");
		Item i2 = new Item(000, "Descrizione del secondo item");
		Cart cart = new Cart(002);
		cart.addItem(i1);
		cart.addItem(i2);
		
		Prod p = new Prod("vm:endpoint1", Cart.class, cart);	
		p.start();
		
		Split s = new Split("vm:endpoint1", newTest.Cart.class, "vm:endpoint2",Item.class);
		s.setSplittingLogic(CompoundMessageProcessor.class, "splitItem");
		
		Merge m = new Merge("vm:endpoint2", Item.class, "vm:endpoint3", Collection.class);
		CompoundTerm ct = new Plug(s, m);
		ct.start();
		
		Cons c = new Cons("vm:endpoint3", Collection.class);
		c.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Item> splitItem(Cart c) {
		return c.getItems();
	}
}
