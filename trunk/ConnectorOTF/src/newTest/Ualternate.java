package newTest;

import core.compoundterm.Altern;
import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.Prod;

public class Ualternate {

	public static void main(String[] args) {
		CompoundTerm comp = new Plug(
								new Plug(new Prod("vm://start", String.class, "Ciao"), 
										new Altern(new Cons("vm:start",String.class),
												   new Cons("vm:start",Integer.class))),
								new Cons("vm:start",String.class));
		comp.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
