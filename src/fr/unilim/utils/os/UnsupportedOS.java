package fr.unilim.utils.os;

public class UnsupportedOS extends Exception {

	public UnsupportedOS(String message) {
		super(message);
	}

	public UnsupportedOS(Throwable cause) {
		super(cause);
	}

	public UnsupportedOS(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedOS(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
