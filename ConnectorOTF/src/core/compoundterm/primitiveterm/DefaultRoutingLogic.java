package core.compoundterm.primitiveterm;

import java.util.ArrayList;

import core.Port;

public class DefaultRoutingLogic {
	String receivers;
	static Object prev;
	
	public DefaultRoutingLogic(String receivers) {
		// TODO Auto-generated constructor stub
		this.receivers = receivers;
	}
	
	public String route(Object o){
		if(!o.equals(prev)){
			prev = o;
			return receivers;
		}
		else return null;
	}

}
