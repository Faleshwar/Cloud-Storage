package com.cloudstorage.dto;

import java.util.List;
import java.util.UUID;

public class FileResponse {

	private UUID id;
	
	private String name;
	
	private Long size;
	
	private String mimeType;
	
	private UUID parentId;
	
	private String ownerName;
	
	private List<BreadCrumbs> breadCrumbs;

	public FileResponse() {
		
	}

	public FileResponse(UUID id, String name, Long size, String mimeType, UUID parentId,
			String ownerName) {
		this.id = id;
		this.name = name;
		this.size = size;
		this.mimeType = mimeType;
		this.parentId = parentId;
		this.ownerName = ownerName;
	}
	
	public FileResponse(UUID id, String name, Long size, String mimeType, UUID parentId,
			String ownerName, List<BreadCrumbs> breadCrumbs) {
		this.id = id;
		this.name = name;
		this.size = size;
		this.mimeType = mimeType;
		this.parentId = parentId;
		this.ownerName = ownerName;
		this.breadCrumbs = breadCrumbs;
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

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public UUID getParentId() {
		return parentId;
	}

	public void setParentId(UUID parentId) {
		this.parentId = parentId;
	}

	

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public List<BreadCrumbs> getBreadCrumbs() {
		return breadCrumbs;
	}

	public void setBreadCrumbs(List<BreadCrumbs> breadCrumbs) {
		this.breadCrumbs = breadCrumbs;
	}
	
	
}
