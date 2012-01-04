package newTest;

import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Order;
import core.compoundterm.primitiveterm.Prod;

public class Ordering {

	public static void main(String[] args) {
		
		Prod p1 = new Prod("vm:endpoint1", String.class, " stai ?");
		Prod p2 = new Prod("vm:endpoint2", String.class, " come ");
		Prod p3 = new Prod("vm:endpoint3", String.class, " giacomo ");
		Order o = new Order("vm:endpoint3,vm:endpoint2,vm:endpoint1", String.class, "vm:endpoint4,vm:endpoint5,vm:endpoint6", String.class);
		CompoundTerm cp = new Plug(p1, new Plug(p2, new Plug(p3, o)));
		cp.start();
		try {
			//System.out.println(cp.getSources_uri()+" "+cp.getReceivers_uri());
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
