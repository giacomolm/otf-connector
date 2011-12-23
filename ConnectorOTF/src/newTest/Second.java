package newTest;

import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.Prod;
import core.compoundterm.primitiveterm.Trans;

public class Second {
	public static void main(String[] args) {
		CompoundTerm comp = new Plug(new Plug(new Trans("vm:start",String.class,"vm:end", String.class,Second.class, "setContent"),new Prod("vm:start", String.class, "Ciao")),new Cons("vm:end",String.class));
		comp.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String setContent(String body){
		return "Giacomo";
	}
}
