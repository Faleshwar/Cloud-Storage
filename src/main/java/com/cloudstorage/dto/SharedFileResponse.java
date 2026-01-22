package com.cloudstorage.dto;

import java.time.LocalDateTime;

public class SharedFileResponse {
	
	private String fileId;
	
	private String sharedBy;
	
	private LocalDateTime expiresAt;
	
	private String accessType;

	public SharedFileResponse(String fileId, String sharedBy, LocalDateTime expiresAt, String accessType) {
		this.fileId = fileId;
		this.sharedBy = sharedBy;
		this.expiresAt = expiresAt;
		this.accessType = accessType;
	}

	public SharedFileResponse() {
		
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getSharedBy() {
		return sharedBy;
	}

	public void setSharedBy(String sharedBy) {
		this.sharedBy = sharedBy;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	
	
}
