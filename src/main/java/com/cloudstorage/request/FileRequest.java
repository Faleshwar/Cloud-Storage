package com.cloudstorage.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public class FileRequest {

	@NotBlank(message = "File name is required")
	private String name;
	
	private UUID parentId;

	public FileRequest() {
		
	}

	public FileRequest(@NotBlank(message = "File name is required") String name, UUID parentId) {
		this.name = name;
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getParentId() {
		return parentId;
	}

	public void setParentId(UUID parentId) {
		this.parentId = parentId;
	}
	
	
}
