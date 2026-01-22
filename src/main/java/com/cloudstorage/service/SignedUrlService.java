package com.cloudstorage.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.cloudstorage.exception.ResourceNotFoundException;
import com.cloudstorage.model.DriveFile;
import com.cloudstorage.model.ShareLink;
import com.cloudstorage.model.User;
import com.cloudstorage.repository.DFileRepository;
import com.cloudstorage.repository.ShareLinkRepository;
import com.cloudstorage.repository.UserRepository;
import com.cloudstorage.security.SecurityUtil;

@Service
public class SignedUrlService {
	
	private final ShareLinkRepository shareLinkRepository;
	private final DFileRepository dFileRepository;
	private final StorageService storageService;
	private final UserRepository userRepository;

	public SignedUrlService(ShareLinkRepository shareLinkRepository, DFileRepository dFileRepository, StorageService storageService, UserRepository userRepository) {
		this.shareLinkRepository = shareLinkRepository;
		this.dFileRepository = dFileRepository;
		this.storageService = storageService;
		this.userRepository = userRepository;
	}
	
	public String createSharedLink(UUID fileID, String accessLevel, String expiredAt) {
		User owner = getCurrentUser();
		String token = UUID.randomUUID().toString().replace("-", "");
		DriveFile driveFile = dFileRepository.findById(fileID).orElseThrow(()->new ResourceNotFoundException("File not found with id "+fileID));
		if(!driveFile.getOwner().getId().equals(owner.getId())) {
			throw new AccessDeniedException("User has not permission to generate share link");
		}
		ShareLink shareLink = new ShareLink(token, LocalDateTime.parse(expiredAt), null, accessLevel, driveFile);
		ShareLink savedLink= shareLinkRepository.save(shareLink);
		return savedLink.getToken();
	}
	
	public Map<String, String> validateSharedLink(String token) {
		ShareLink shareLink = shareLinkRepository.findByToken(token).orElseThrow();
		if(shareLink.getExpiredAt() != null && shareLink.getExpiredAt().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Token is expired");
		}
		String storagePath = shareLink.getFile().getStoragePath();
		
		return Map.of("path", storagePath.substring(storagePath.indexOf("/")+1), "link", storageService.generateSignedUrl(storagePath));
	}
	
	private User getCurrentUser() {
		String email = SecurityUtil.getCurrentUserDetails().orElseThrow(()->new BadCredentialsException("Invalid authentication token"));
		return userRepository.findByEmail(email).orElseThrow(()->new BadCredentialsException("Invalid authentication token"));
	}
}
