package fr.unilim.application.gui.tasks.exception;

public class TaskException extends Exception {

	public TaskException() {
	}

	public TaskException(String message) {
		super(message);
	}

	public TaskException(Throwable cause) {
		super(cause);
	}

	public TaskException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
