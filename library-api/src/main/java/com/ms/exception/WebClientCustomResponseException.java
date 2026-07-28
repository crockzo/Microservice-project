package com.ms.exception;

public class WebClientCustomResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WebClientCustomResponseException(String message) {
		super(message);
	}

}
