package com.cloudstorage.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class StarredResponse {
	private UUID starredId;
	
	private LocalDateTime starredAt;
	
	private String username;
	
	private FileResponse fileResponse;

	public StarredResponse(UUID starredId, LocalDateTime starredAt, String username, FileResponse fileResponse) {
		this.starredId = starredId;
		this.starredAt = starredAt;
		this.username = username;
		this.fileResponse = fileResponse;
	}

	public StarredResponse() {
		
	}

	public UUID getStarredId() {
		return starredId;
	}

	public void setStarredId(UUID starredId) {
		this.starredId = starredId;
	}

	public LocalDateTime getStarredAt() {
		return starredAt;
	}

	public void setStarredAt(LocalDateTime starredAt) {
		this.starredAt = starredAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public FileResponse getFileResponse() {
		return fileResponse;
	}

	public void setFileResponse(FileResponse fileResponse) {
		this.fileResponse = fileResponse;
	}
	
	
}
