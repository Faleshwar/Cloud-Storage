package com.cloudstorage.dto;

import java.util.List;

public class DriveResponse {

	private FileResponse file;
	private FolderResponse folder;
	private List<BreadCrumbs> breadCrumbs;
	
	
	public DriveResponse(FileResponse file,FolderResponse folder, List<BreadCrumbs> breadCrumbs) {
		this.file = file;
		this.folder = folder;
		this.breadCrumbs = breadCrumbs;
	}
	
	public DriveResponse(FileResponse file,FolderResponse folder) {
		this.file = file;
		this.folder = folder;
	}
	public FileResponse getFile() {
		return file;
	}
	public void setFile(FileResponse file) {
		this.file = file;
	}
	public FolderResponse getFolder() {
		return folder;
	}
	public void setFolder(FolderResponse folder) {
		this.folder = folder;
	}
	public List<BreadCrumbs> getBreadCrumbs() {
		return breadCrumbs;
	}
	public void setBreadCrumbs(List<BreadCrumbs> breadCrumbs) {
		this.breadCrumbs = breadCrumbs;
	}
	public DriveResponse() {
		
	}
	
	
	
	
}
