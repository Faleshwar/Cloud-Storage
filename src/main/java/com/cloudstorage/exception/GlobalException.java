package com.cloudstorage.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cloudstorage.response.ErrorResponse;
import com.cloudstorage.response.ValidationErrorResponse;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleAuthException(BadCredentialsException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessException(AccessDeniedException ex){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(ex.getMessage()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex){
		List<String> errors =ex.getBindingResult().getFieldErrors().stream().map(error->error.getField()+": "+error.getDefaultMessage()).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationErrorResponse(false, "Validation failed", errors));
	}
	
	@ExceptionHandler(InvalidTimeException.class)
	public ResponseEntity<ErrorResponse> handleInvalidTime(InvalidTimeException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
	}
	
}
