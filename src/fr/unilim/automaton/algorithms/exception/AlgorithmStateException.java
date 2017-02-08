package fr.unilim.automaton.algorithms.exception;

public class AlgorithmStateException extends Exception {

	private static final long serialVersionUID = 1L;

	public AlgorithmStateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AlgorithmStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlgorithmStateException(String message) {
		super(message);
	}

	public AlgorithmStateException(Throwable cause) {
		super(cause);
	}

	
}
