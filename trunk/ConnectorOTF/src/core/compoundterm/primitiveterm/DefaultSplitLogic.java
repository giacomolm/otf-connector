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
			throw new DefaultSplitLogicException("Default Splitting Logic cannot be applied:");	
		}
	}
}
