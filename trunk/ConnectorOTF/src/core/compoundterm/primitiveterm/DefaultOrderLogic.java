package core.compoundterm.primitiveterm;

public class DefaultOrderLogic {

	static int k = 0;
	
	public Integer order(Object o){
		return new Integer(k++);
	}
}
