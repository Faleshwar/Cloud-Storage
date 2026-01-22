package com.cloudstorage.response;

import java.time.LocalDateTime;

public class ErrorResponse {

	private boolean success;
	private String message;
	private LocalDateTime timestamp;
	
	
	public ErrorResponse(String message) {
		this.success = false;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}


	public ErrorResponse(boolean success, String message, LocalDateTime timestamp) {
		super();
		this.success = success;
		this.message = message;
		this.timestamp = timestamp;
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


	public LocalDateTime getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
}
