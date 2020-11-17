package com.andrii.demo.exception;

public class TheEmailAlreadyExistsException extends RuntimeException{
	public TheEmailAlreadyExistsException() {
	}

	public TheEmailAlreadyExistsException(String message) {
		super(message);
	}

	public TheEmailAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public TheEmailAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
