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
import com.cloudstorage.exception.InvalidTimeException;
import com.cloudstorage.exception.ResourceNotFoundException;
import com.cloudstorage.model.AccessType;
import com.cloudstorage.model.DriveFile;
import com.cloudstorage.model.FileShare;
import com.cloudstorage.model.User;
import com.cloudstorage.repository.DFileRepository;
import com.cloudstorage.repository.FileShareRepository;
import com.cloudstorage.repository.UserRepository;
import com.cloudstorage.request.ShareUserRequest;
import com.cloudstorage.security.SecurityUtil;

@Service
public class ShareService {

 
	private final FileShareRepository fileShareRepository;
	private final DFileRepository dFileRepository;
	private final UserRepository userRepository;

	public ShareService(FileShareRepository fileShareRepository, UserRepository userRepository, DFileRepository dFileRepository, SignedUrlService signedUrlService) {
		this.fileShareRepository = fileShareRepository;
		this.dFileRepository = dFileRepository;
		this.userRepository = userRepository;
	}
	
	@Transactional
	public void share(ShareUserRequest request, UUID fileId) {
		User sharedUser = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new ResourceNotFoundException("User not found with email "+request.getEmail()));
		DriveFile driveFile = getDriveFile(fileId);
		User sharedBy = getCurrentUser();
		LocalDateTime expiresAt = LocalDateTime.parse(request.getExpiresAt());
		validate(driveFile, sharedUser, expiresAt);
		FileShare fileShare = new FileShare(driveFile, AccessType.valueOf(request.getAccessType()), expiresAt, sharedUser, sharedBy);
		fileShareRepository.save(fileShare);
	}
	
	public List<FileResponse> sharedWithMeFiles() {
		User me = getCurrentUser();
		Set<DriveFile> driveFiles = dFileRepository.findSharedWithMeFiles(me.getId());
		return driveFiles.stream().map(this::fileToResponse).collect(Collectors.toList());
	}
	
	public void revokeShare(UUID fileId, UUID userId) {
		FileShare fileShare = fileShareRepository.findBySharedWithAndFileId(userId, fileId).orElseThrow();
		fileShareRepository.delete(fileShare);
	}
	
	private void validate(DriveFile driveFile, User sharedUser, LocalDateTime expiresAt) {
		if(sharedUser.getId().equals(driveFile.getOwner().getId())) {
			throw new AccessDeniedException("Owner cannot share with himself");
		}
		if(expiresAt.isBefore(LocalDateTime.now())) {
			throw new InvalidTimeException("Invalid expire time");
		}
	}
	

	
	private FileResponse fileToResponse(DriveFile driveFile) {
		return new FileResponse(driveFile.getId(), driveFile.getName(), driveFile.getSize(), driveFile.getMimeType(), null, driveFile.getOwner().getName());
	}


	
	private User getCurrentUser() {
		String email = SecurityUtil.getCurrentUserDetails().orElseThrow(()->new BadCredentialsException("Invalid authentication token"));
		return userRepository.findByEmail(email).orElseThrow(()->new BadCredentialsException("Invalid authentication token"));
	}
	
	private DriveFile getDriveFile(UUID fileId) {
		return dFileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("File not found"));
	}
}
