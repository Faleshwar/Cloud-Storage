package com.cloudstorage.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public class FolderRequest {

	@NotBlank(message = "Folder name is required")
	private String name;
	
	// if null server assumes as 'Root' directory
	private UUID parentId;

	public FolderRequest(@NotBlank(message = "Folder name is required") String name, UUID parentId) {
		this.name = name;
		this.parentId = parentId;
	}

	public FolderRequest() {
		
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
