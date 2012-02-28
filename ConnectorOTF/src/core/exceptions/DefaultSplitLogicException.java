package core.exceptions;

public class DefaultSplitLogicException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DefaultSplitLogicException(String msg) {
		// TODO Auto-generated constructor stub
		super(msg);
		System.err.println(msg);
	}
	
	public  DefaultSplitLogicException(Throwable cause) {
		super(cause);
	}

	
}
