package fr.unilim.tests.exception;

/**
 * Exception throw when {@link fr.unilim.tests.APDUValuesReader}
 * read invalid concolic values files.
 *
 */
public class APDUValuesReaderException extends Exception {

	private static final long serialVersionUID = -3055775286546317998L;

	public APDUValuesReaderException() {
		super();
	}

	public APDUValuesReaderException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public APDUValuesReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public APDUValuesReaderException(String message) {
		super(message);
	}

	public APDUValuesReaderException(Throwable cause) {
		super(cause);
	}

	
}
