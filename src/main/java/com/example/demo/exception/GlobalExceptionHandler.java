package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
@RestControllerAdvice
public class GlobalExceptionHandler{
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<ErrorResponse> accountNotFound(AccountNotFoundException ex){
		  ErrorResponse error = new ErrorResponse(
	                LocalDateTime.now(),
	                HttpStatus.NOT_FOUND.value(),
	                HttpStatus.NOT_FOUND.getReasonPhrase(),
	                ex.getMessage()
	        );

		return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
	}
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> illgalArgu(IllegalArgumentException ex){
		ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );
		return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
	}
	
	
	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<ErrorResponse> inefficientAmount(InsufficientBalanceException ex){
		ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );
		return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthentication(
	        AuthenticationException ex) {

	    ErrorResponse error = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.UNAUTHORIZED.value(),
	            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
	            "Invalid email or password"
	    );

	    return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(error);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(
	        AccessDeniedException ex) {

	    ErrorResponse error = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.FORBIDDEN.value(),
	            HttpStatus.FORBIDDEN.getReasonPhrase(),
	            "You do not have permission to access this resource"
	    );

	    return ResponseEntity
	            .status(HttpStatus.FORBIDDEN)
	            .body(error);
	}
}