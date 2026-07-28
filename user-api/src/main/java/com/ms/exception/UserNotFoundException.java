package com.ms.exception;

public class UserNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 56377836;

	public UserNotFoundException(String message) {
		super(message);
	}

	
}
