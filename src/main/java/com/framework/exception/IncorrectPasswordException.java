package com.framework.exception;

public class IncorrectPasswordException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public IncorrectPasswordException() {
		super();
	}

	public IncorrectPasswordException(String message) {
		super(message);
	}

	public IncorrectPasswordException(Throwable cause) {
		super(cause);
	}

	public IncorrectPasswordException(String message, Throwable cause) {
		super(message, cause);
	}
}
