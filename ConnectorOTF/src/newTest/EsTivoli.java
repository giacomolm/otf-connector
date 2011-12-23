package newTest;

import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Trans;

public class EsTivoli {

	public static void main(String[] args) {
		Trans t1 = new Trans("vm:endpoint1", String.class, "vm:endpoint2", String.class);
		t1.setTransformLogic(EsTivoli.class, "setContent");
		Trans t2 = new Trans("vm:endpoint2", String.class, "vm:endpoint3", String.class);
		t2.setTransformLogic(EsTivoli.class, "setContent");
		CompoundTerm c = new Plug(t1,t2);
		c.start();
		System.out.println(c.getSources_uri()+" "+c.getReceivers_uri());
	}
	
	public String setContent(String body){
		return "Giacomo";
	}
	
}
