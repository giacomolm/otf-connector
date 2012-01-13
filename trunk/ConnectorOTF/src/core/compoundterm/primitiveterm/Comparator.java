package core.compoundterm.primitiveterm;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

import core.Port;

public class Comparator implements Expression{

	ArrayList<Port> receivers= new ArrayList<Port>();
	int received = 0;
	
	public Comparator(ArrayList<Port> receivers) {
		// TODO Auto-generated constructor stub
		this.receivers = receivers;
	}
	
	public void evaluate(Exchange e){
		Class c = e.getIn().getBody().getClass();
		Iterator<Port> i = receivers.iterator();
		while(i.hasNext()){
			Port p = i.next();
			if(p.getClass().equals(c)){
				
			}
		}
	}
	
	@Override
	public <T> T evaluate(Exchange e, Class<T> arg1) {
		// TODO Auto-generated method stub
		evaluate(e);
		if(received==receivers.size()){
			
		}
		return null;
	}

}
