package com.ms.restcontroller;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.ms.exception.UserNameAlreadyExistException;
import com.ms.exception.UserNotFoundException;
import com.ms.dto.ExceptionResponse;

@RestControllerAdvice
public class RestGlobalExceptionHandler {


	@ExceptionHandler(value = {UserNotFoundException.class, UserNameAlreadyExistException.class})
	public ResponseEntity<ExceptionResponse> handle(Exception exception, WebRequest request){
		
		ExceptionResponse response = new ExceptionResponse();
		response.setError(exception.getMessage());
		response.setPath(request.getDescription(false));
		response.setStatus(HttpStatus.BAD_REQUEST.toString());
		response.setTimeStamp(LocalDate.now().toString());
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		
		
	}
	
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public ResponseEntity<ExceptionResponse> handle(MethodArgumentNotValidException exception, WebRequest request){
		
		ExceptionResponse response = new ExceptionResponse();
		
		
		String errorMessage = exception.getFieldErrors().stream().map(error->error.getDefaultMessage()).reduce("", (a,b) -> a + ", " +b);
		
		response.setError(errorMessage);
		response.setPath(request.getDescription(false));
		response.setStatus(HttpStatus.BAD_REQUEST.toString());
		response.setTimeStamp(LocalDate.now().toString());
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		
		
	}
}
