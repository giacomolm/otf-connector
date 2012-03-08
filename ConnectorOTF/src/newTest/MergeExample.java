/**
 * In questo esempio vogliamo vedere come possiamo applicare. Dobbiamo
 * implementare una strategia di aggregazione
 */

package newTest;

import java.util.ArrayList;
import java.util.Collection;


import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.Merge;
import core.compoundterm.primitiveterm.Prod;

public class MergeExample {

	public static void main(String[] args) {
		
		Merge m = new Merge("vm:start1,vm:start2,vm:start3", String.class, "vm:end", ArrayList.class);
		m.setCompletitionSize(3);
		
		CompoundTerm ct = new Plug(new Prod("vm:start1", String.class, "Ciao "),
									new Plug(new Prod("vm:start2", String.class, "come "),
											new Plug(new Prod("vm:start3", String.class, "stai ?"),
													new Plug(m,
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
