package newTest;

import java.util.Collection;

import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.Merge;
import core.compoundterm.primitiveterm.Prod;

public class Merger {

	public static void main(String[] args) {
		CompoundTerm ct = new Plug(new Prod("vm:start1", String.class, "Ciao "),
									new Plug(new Prod("vm:start2", String.class, "come "),
											new Plug(new Prod("vm:start3", String.class, "stai ?"),
													new Plug(new Merge("vm:start1,vm:start2,vm:start3", String.class, "vm:end", Collection.class),
															new Cons("vm:end", Collection.class)
															)
													)
									)
		);
		ct.start();
		try{
			Thread.sleep(5000);
		}
		catch(Exception e){
			
		}
	}
}
