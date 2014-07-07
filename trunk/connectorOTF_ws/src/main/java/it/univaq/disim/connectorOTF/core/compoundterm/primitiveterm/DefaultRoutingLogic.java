package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import java.util.ArrayList;

import it.univaq.disim.connectorOTF.core.Port;
import it.univaq.disim.connectorOTF.core.exceptions.DefaultRoutingLogicException;

public class DefaultRoutingLogic {
	String receivers;
	static Object prev;
	
	public DefaultRoutingLogic(String receivers) throws DefaultRoutingLogicException{
		// TODO Auto-generated constructor stub
		if(receivers.equals("") || receivers.split(",").length==0) throw new DefaultRoutingLogicException("Cannot use default routing logic: user must specify at least one receiver endpoint");
		this.receivers = receivers;
	}
	
	public String route(Object o){
		//System.out.println("object "+o);
		if(!o.equals(prev)){
			prev = o;
			return receivers;
		}
		else return null;
	}

}
