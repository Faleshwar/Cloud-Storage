package com.cloudstorage.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ShareUserRequest {
	
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email address")
	private String email;
	
	@NotBlank(message = "Expire time is required")
	private String expiresAt;
	
	@NotBlank(message = "Access type is required")
	private String accessType;

	public ShareUserRequest() {
		
	}


	public ShareUserRequest(
			@NotBlank(message = "Email is required") @Email(message = "Invalid email address") String email,
			@NotBlank(message = "Expire time is required") String expiresAt,
			@NotBlank(message = "Access type is required") String accessType) {
		super();
		this.email = email;
		this.expiresAt = expiresAt;
		this.accessType = accessType;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}



	public String getExpiresAt() {
		return expiresAt;
	}



	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
