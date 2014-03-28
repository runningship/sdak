package org.bc.sdak;

public class GException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5725246199424201107L;

	private ExceptionType type;
	
	public GException(ExceptionType type,String msg){
		super(msg);
		this.type = type;
	}
	
	public GException(ExceptionType type,String msg, Throwable ex){
		super(msg ,ex);
		this.type = type;
	}

	public ExceptionType getType() {
		return type;
	}
}
