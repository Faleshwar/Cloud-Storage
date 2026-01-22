package com.cloudstorage.response;

import java.util.List;

public class ValidationErrorResponse {
	private boolean success;
	private String message;
	private List<String> errors;
	public ValidationErrorResponse(boolean success, String message, List<String> errors) {
		super();
		this.success = success;
		this.message = message;
		this.errors = errors;
	}
	public ValidationErrorResponse() {
		super();
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	
}
