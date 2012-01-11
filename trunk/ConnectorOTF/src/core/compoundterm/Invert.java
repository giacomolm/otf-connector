package core.compoundterm;

import org.apache.camel.Exchange;

/**
 * Invert class implement the concept of Inversion operator introduced in the 
 * connector algebra theory. This is an unary operator: it is applied to one 
 * single term. This operator acts like the inverse of its operand by interchanging 
 * inputs and outputs. The result term obtained by the application of this 
 * operator is a new term (same previous term) with the inputs and outputs ports 
 * interchanged.
 * 
 * @author giacomolm
 *
 */
public class Invert extends CompoundTerm{

	CompoundTerm term;
	
	/**
	 * Build new term starting from the definition of CompoundTerm c passed 
	 * as parameter. The result term is a new term with input and output port 
	 * inverted.
	 * @param c Compoundterm to invert
	 */
	public Invert(CompoundTerm c) {
		// TODO Auto-generated constructor stub
		//Simple interchanging! Source(Receiver) - Receiver(Source)
		addSources(c.getReceivers());
		addReceivers(c.getSources());
		c.isComposed();
		term = c;
	}
	
	/**
	 * Simply starts the original term passed for the building of
	 * the inverted term.
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		term.start();
	}
	
	/**
	 * Set the received exchange as input of the contained term. Naturally
	 * this term consume from the receiver port of the contained term. 
	 */
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		term.setMessage(uri, e);
	}

}
