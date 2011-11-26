package test;

import core.*;
import core_old.Connector_old;

public class SimpleTransformer {
	
	public static void main(String[] args) throws Exception {
		Connector_old c = new Connector_old();
		c.addSource("file://test");
		
		c.addTransformer(String.class, Handler.class, "setContent");
		
		c.addReceiver("file://receiver");
		c.start(5000);		
	}
}