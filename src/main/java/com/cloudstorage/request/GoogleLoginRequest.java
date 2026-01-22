package com.cloudstorage.request;

import jakarta.validation.constraints.NotBlank;

public class GoogleLoginRequest {
 
	@NotBlank(message = "GoogleIdToken is required")
	private String idToken;

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}
	
}
