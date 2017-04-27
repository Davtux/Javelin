package fr.unilim.filter.exception;

/**
 * Exception throw while filter error.
 */
public class FilterException extends Exception {

	private static final long serialVersionUID = -4131693719051038812L;

	public FilterException(String message) {
		super(message);
	}

	public FilterException(Throwable cause) {
		super(cause);
	}

	public FilterException(String message, Throwable cause) {
		super(message, cause);
	}

	public FilterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
