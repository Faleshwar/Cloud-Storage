package com.cloudstorage.request;

import jakarta.validation.constraints.NotBlank;

public class ShareLinkRequest {
	
	@NotBlank(message = "Access level is required")
	private String accessLevel;
	
	@NotBlank(message = "Expired at is required")
	private String expiredAt;

	public ShareLinkRequest(String accessLevel, String expiredAt) {
		this.accessLevel = accessLevel;
		this.expiredAt = expiredAt;
	}

	public ShareLinkRequest() {
		
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(String expiredAt) {
		this.expiredAt = expiredAt;
	}
	
	
}
