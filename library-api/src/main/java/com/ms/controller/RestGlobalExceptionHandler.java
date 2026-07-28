package com.ms.controller;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.ms.dto.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import reactor.netty.http.server.HttpServerRequest;

@RestControllerAdvice
public class RestGlobalExceptionHandler {

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<ExceptionResponse> handle(MethodArgumentNotValidException exception, WebRequest request,
			HttpServerRequest req) {

		ExceptionResponse response = new ExceptionResponse();

		String errorMessage = exception.getFieldErrors().stream().map(error -> error.getDefaultMessage()).reduce("",
				(a, b) -> a + ", " + b);

		response.setError(errorMessage);
		response.setPath(request.getDescription(false));
		response.setStatus(HttpStatus.BAD_REQUEST.toString());
		response.setTimeStamp(LocalDate.now().toString());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<ExceptionResponse> handle(Exception exception, WebRequest request) {

		ExceptionResponse response = new ExceptionResponse();
		
		response.setError(exception.getMessage());
		response.setPath(request.getDescription(false));
		response.setStatus(HttpStatus.BAD_REQUEST.toString());
		response.setTimeStamp(LocalDate.now().toString());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = { FeignException.class })
	public ResponseEntity<ExceptionResponse> handle(FeignException exception, WebRequest request)
			throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();

		String errorMessage = "";
		errorMessage = exception.getMessage();

		int index = errorMessage.indexOf('{');
		errorMessage = exception.getMessage().substring(index, errorMessage.length() - 1);
		
		ExceptionResponse response = mapper.readValue(errorMessage, ExceptionResponse.class);

		response.setPath(request.getDescription(false));

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

}
