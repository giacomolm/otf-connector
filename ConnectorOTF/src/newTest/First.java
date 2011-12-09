package newTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import core.Port;
import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.Prod;
import core.compoundterm.primitiveterm.Split;

public class First {

	public static void main(String[] args) {
		CompoundTerm comp = new Plug(new Plug(new Prod("vm://start", String.class, "Ciao"), new Cons("vm:stop",String.class)),new Cons("vm:start",String.class));
		//System.out.println(comp+" "+comp.getSources_uri());
		/*Iterator<Port> i = comp.getSources_uri().iterator();
		if(i.hasNext()){
			Port p = i.next();
			System.out.println(p.getTerm()+" "+p.getTerm().getSources_uri());
		}*/
		comp.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
