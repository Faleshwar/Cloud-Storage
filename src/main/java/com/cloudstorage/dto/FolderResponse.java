package com.cloudstorage.dto;

import java.util.UUID;

public class FolderResponse {
	private UUID id;
	
	private String name;
	
	private String ownerName;
	
	private UUID parentFolderId;

	

	public FolderResponse(UUID id, String name, String ownerName, UUID parentFolderId) {
		super();
		this.id = id;
		this.name = name;
		this.ownerName = ownerName;
		this.parentFolderId = parentFolderId;
	}
	
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}



	public UUID getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(UUID parentFolderId) {
		this.parentFolderId = parentFolderId;
	}
}
