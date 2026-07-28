package com.ms.exception;

public class BookIssueLimitPerUserException extends RuntimeException{

	private static final long serialVersionUID = 736478567;

	public BookIssueLimitPerUserException(String message) {
		super(message);
	}

	
}
