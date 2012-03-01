package core.compoundterm.primitiveterm;

import core.exceptions.DefaultTransformLogicException;

public class DefaultTransformLogic {
	
	static Class out_type;
	
	public DefaultTransformLogic() {
		// TODO Auto-generated constructor stub
		this.out_type = String.class;
	}
	
	public DefaultTransformLogic(Class out_type) {
		// TODO Auto-generated constructor stub
		this.out_type = out_type;
	}
	
	public static Object transform(Object o) throws DefaultTransformLogicException{
		if(o.getClass().equals(out_type))
			return o;
		else throw new DefaultTransformLogicException("DefaultTransformLogic cannot transform incoming message. Type expected by the receiver is "+out_type+" and is different respect "+o);
	}

}
