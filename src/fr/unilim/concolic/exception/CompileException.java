package fr.unilim.concolic.exception;

public class CompileException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CompileException() {
		super();
	}

	public CompileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CompileException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompileException(String message) {
		super(message);
	}

	public CompileException(Throwable cause) {
		super(cause);
	}

	
}
