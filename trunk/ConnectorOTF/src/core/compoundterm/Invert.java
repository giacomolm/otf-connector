package core.compoundterm;

import org.apache.camel.Exchange;

public class Invert extends CompoundTerm{

	CompoundTerm term;
	
	public Invert(CompoundTerm c) {
		// TODO Auto-generated constructor stub
		addSources_uri(c.getReceivers_uri());
		addReceivers_uri(c.getSources_uri());
		c.isComposed();
		term = c;
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		term.start();
	}
	
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		term.setMessage(uri, e);
	}

}
