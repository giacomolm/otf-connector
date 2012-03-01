/**
 * Questo esempio fa riferimento all'email scambiata con il professor Tivoli del 19/12, 
 * In questo esempio facciamo vedere come applicando l'operatore di plug, le effetive 
 * porte che vengono riesposte all'esterno sono quelle che non sono state collegate
 * tramite un canale interno.
 */

package newTest;

import core.compoundterm.CompoundTerm;
import core.compoundterm.Invert;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Trans;

public class TivoliExample {

	public static void main(String[] args) {
		Trans t1 = new Trans("http://www.google.it", String.class, "vm:endpoint2", String.class);
		t1.setTransformLogic(TivoliExample.class, "setContent");
		try {
			Trans t2 = new Trans("vm:endpoint2", String.class, "vm:endpoint3", String.class);
			t2.setTransformLogic(TivoliExample.class, "setContent");
			CompoundTerm c = new Invert(new Plug(t1,t2));
			c.start();
			//Stampiamo le porte.
			System.out.println(c.getSources()+" "+c.getReceivers());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String setContent(String body){
		return "Giacomo";
	}
	
}
