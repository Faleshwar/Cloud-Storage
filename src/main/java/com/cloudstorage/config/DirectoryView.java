package com.cloudstorage.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.cloudstorage.dto.BreadCrumbs;
import com.cloudstorage.model.DriveFile;
import com.cloudstorage.model.Folder;
import com.cloudstorage.repository.DFileRepository;
import com.cloudstorage.repository.FolderRepository;

@Component
public class DirectoryView {

	
	private DFileRepository dFileRepository;
	
	private FolderRepository folderRepository;

	public DirectoryView(
			DFileRepository dFileRepository, FolderRepository folderRepository) {
		this.dFileRepository = dFileRepository;
		this.folderRepository = folderRepository;
	}
	
	public List<BreadCrumbs> getFolderDirectoryView(UUID folderId){
		List<BreadCrumbs> breadcrumbs = new ArrayList<>();
		Folder current = folderRepository.findById(folderId).orElseGet(null);
		while(current != null) {
			breadcrumbs.add(new BreadCrumbs(current.getName(), current.getId()));
			current = current.getParentFolder();
		}
		Collections.reverse(breadcrumbs);
		return breadcrumbs;
	}
	
	public List<BreadCrumbs> getFileDirectoryView(UUID fileId){
		List<BreadCrumbs> breadcrumbs = new ArrayList<>();
		DriveFile driveFile = dFileRepository.findById(fileId).orElse(null);
		Folder current = folderRepository.findById(driveFile.getFolder().getId()).orElseGet(null);
		while(current != null) {
			breadcrumbs.add(new BreadCrumbs(current.getName(), current.getId()));
			current = current.getParentFolder();
		}
		Collections.reverse(breadcrumbs);
		return breadcrumbs;
	}
	
}
