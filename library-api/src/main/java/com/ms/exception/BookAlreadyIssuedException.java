package com.ms.exception;

public class BookAlreadyIssuedException extends RuntimeException{

	private static final long serialVersionUID = 57672836;

	public BookAlreadyIssuedException(String message) {
		super(message);
	}
	
	

}
