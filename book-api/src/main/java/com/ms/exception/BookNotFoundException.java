package com.ms.exception;

public class BookNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 456453;

	public BookNotFoundException(String message) {
		super(message);
	}

}
