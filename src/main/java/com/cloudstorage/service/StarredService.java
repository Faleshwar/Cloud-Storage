package com.cloudstorage.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudstorage.dto.FileResponse;
import com.cloudstorage.dto.StarredResponse;
import com.cloudstorage.model.DriveFile;
import com.cloudstorage.model.FileShare;
import com.cloudstorage.model.Starred;
import com.cloudstorage.model.User;
import com.cloudstorage.repository.DFileRepository;
import com.cloudstorage.repository.FileShareRepository;
import com.cloudstorage.repository.StarredRepository;
import com.cloudstorage.repository.UserRepository;
import com.cloudstorage.security.SecurityUtil;

@Service
public class StarredService {
	private final StarredRepository starredRepository;
	private final UserRepository userRepository;
	private final DFileRepository dFileRepository;
	private final FileShareRepository fileShareRepository;
	
	
	public StarredService(StarredRepository starredRepository, UserRepository userRepository,
			DFileRepository dFileRepository, FileShareRepository fileShareRepository) {
	
		this.starredRepository = starredRepository;
		this.userRepository = userRepository;
		this.dFileRepository = dFileRepository;
		this.fileShareRepository = fileShareRepository;
	}
	
	@Transactional
	public void markStarred(UUID fileId) {
		User currentUser = getCurrentUser();
		FileShare fileShare = fileShareRepository.findByFileSharedWithUser(fileId, currentUser.getId()).orElse(null);
		DriveFile driveFile = dFileRepository.findDriveFile(fileId).orElse(null);
		
		
		if(fileShare == null && !driveFile.getOwner().getId().equals(currentUser.getId())) {
			throw new AccessDeniedException("User has not permission to starred this file");
		}
		
		if(fileShare == null && driveFile.getOwner().getId().equals(currentUser.getId())) {
			Starred starred = new Starred(LocalDateTime.now(), currentUser, driveFile);
			starredRepository.save(starred);
			return;
		}
		
		if(fileShare != null && !driveFile.getOwner().getId().equals(currentUser.getId())) {
			Starred starred = new Starred(LocalDateTime.now(), currentUser, fileShare.getFile());
			starredRepository.save(starred);
			return;
		}
		
	}
	
	@Transactional
	public void unmarkStarred(UUID fileId) {
		User currentUser = getCurrentUser();
		FileShare fileShare = fileShareRepository.findByFileSharedWithUser(fileId, currentUser.getId()).orElse(null);
		DriveFile driveFile = dFileRepository.findById(fileId).orElse(null);
		
		if(fileShare == null && driveFile == null) {
			throw new AccessDeniedException("User has not permission to unstarred this file");
		}
		Starred starred = starredRepository.findByFileId(fileId).orElseThrow(()->new RuntimeException("Starred file not found"));
		starredRepository.delete(starred);
	}

	
	public List<StarredResponse> starredFiles(){
		User currentUser = getCurrentUser();
		Set<Starred> starreds = starredRepository.findStarredFiles(currentUser.getId());
		return starreds.stream().map(this::convertToFileResponse).collect(Collectors.toList());
	}
	
	
	private StarredResponse convertToFileResponse(Starred starred) {
		
		DriveFile driveFile = starred.getFile();
		FileResponse response= new FileResponse(driveFile.getId(), driveFile.getName(), driveFile.getSize(), driveFile.getMimeType(), driveFile.getFolder()==null?null:driveFile.getFolder().getId(), driveFile.getOwner().getName());
		return new StarredResponse(starred.getId(), starred.getStarredAt(), starred.getUser().getName(), response);
	}
	
	private User getCurrentUser() {
		String email = SecurityUtil.getCurrentUserDetails().orElseThrow(()->new BadCredentialsException("Invalid authentication token"));
		User user = userRepository.findByEmail(email).orElseThrow(()->new BadCredentialsException("Invalid authentication token"));
		return user;
	}

}
