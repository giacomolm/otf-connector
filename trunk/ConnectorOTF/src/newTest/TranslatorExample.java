package newTest;


/**
 * In questo esempio facciamo vedere come possiamo utilizzare l'operatore Trans(lator).
 * Facciomo riferimento al seguente scenario. Abbiamo un producer che invia una stringa
 * al translator. Una volta ricevuto il messaggio, il translator modifica la stringa
 * in relazione alla propria logica di traduzione e inoltra il messaggio all'effettivo
 * destinatario. Lo scopo dell'esempio è quello di fare
 */

import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.DefaultTransformLogic;
import core.compoundterm.primitiveterm.Prod;
import core.compoundterm.primitiveterm.Trans;

public class TranslatorExample {
	public static void main(String[] args) {
		CompoundTerm comp = new Plug(new Plug(new Trans("vm:start",String.class,"vm:end", String.class,TranslatorExample.class, "setContent"),new Prod("vm:start", String.class, "Hello world!")),new Cons("vm:end",String.class));
		//CompoundTerm comp = new Plug(new Plug(new Trans("vm:start",String.class,"vm:end", String.class),new Prod("vm:start", String.class, "Ciao")),new Cons("vm:end",String.class));
		comp.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String setContent(String body){
		return body+" Cameled";
	}
}
