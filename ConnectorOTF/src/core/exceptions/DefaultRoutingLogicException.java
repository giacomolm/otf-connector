package core.exceptions;

public class DefaultRoutingLogicException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DefaultRoutingLogicException(String msg) {
		// TODO Auto-generated constructor stub
		super(msg);
		System.err.println(msg);
	}
	
	public  DefaultRoutingLogicException(Throwable cause) {
		super(cause);
	}
}
