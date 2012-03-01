package core.compoundterm.primitiveterm;

import java.util.Collection;

import core.exceptions.DefaultSplitLogicException;

public class DefaultSplitLogic {
	
	public Object[] split(Object o) throws DefaultSplitLogicException{
		if(o instanceof Object[]){
			Object[] a = (Object[]) o;
			return a;
		}
		else {
			throw new DefaultSplitLogicException("Default Splitting Logic assume that incoming messages can be only array of object (Object[]). Other type of message cannot be splitted. In this case message received contains this:"+o);	
		}
	}
}
