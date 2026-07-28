package com.ms.exception;

public class BookNotAllocatedToUserException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public BookNotAllocatedToUserException(String message) {
		super(message);
	}

	
}
