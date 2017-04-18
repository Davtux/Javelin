package fr.unilim.concolic.exception;

public class ConcolicExecutionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConcolicExecutionException(String msg) {
		super(msg);
	}
	
	public ConcolicExecutionException(String msg, Throwable th) {
		super(msg, th);
	}
}
