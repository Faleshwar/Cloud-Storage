package com.cloudstorage.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.tika.Tika;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudstorage.config.DirectoryView;
import com.cloudstorage.dto.BreadCrumbs;
import com.cloudstorage.dto.FileResponse;
import com.cloudstorage.exception.ResourceNotFoundException;
import com.cloudstorage.model.DriveFile;
import com.cloudstorage.model.Folder;
import com.cloudstorage.model.User;
import com.cloudstorage.repository.DFileRepository;
import com.cloudstorage.repository.FolderRepository;
import com.cloudstorage.repository.UserRepository;
import com.cloudstorage.request.FileRequest;
import com.cloudstorage.security.SecurityUtil;

@Service
public class FileService {

	
	private final DFileRepository dFileRepository;
	private final StorageService storageService;
	private final UserRepository userRepository;
	private final FolderRepository folderRepository;
	private final SignedUrlService signedUrlService;
	private final DirectoryView directoryView;
	private final Tika tika = new Tika();

	public FileService(DFileRepository dFileRepository, StorageService storageService, UserRepository userRepository, FolderRepository folderRepository, SignedUrlService signedUrlService, DirectoryView directoryView) {
		this.dFileRepository = dFileRepository;
		this.storageService = storageService;
		this.userRepository = userRepository;
		this.folderRepository = folderRepository;
		this.signedUrlService = signedUrlService;
		this.directoryView = directoryView;
	}
	
	@Transactional
	public void upload(MultipartFile file, UUID folderId) throws IOException {
		User currentUser = getUser();
		Folder folder = null;
		if(folderId != null) {
			folder = folderRepository.findById(folderId).orElseThrow(()->new ResourceNotFoundException("Folder not found"));
			validateUser(currentUser, folder);
		}
		String fileId = UUID.randomUUID().toString();
		String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
		String extension = originalFileName.substring(originalFileName.indexOf("."));
		//riginalFileName = UriUtils.encode(originalFileName, StandardCharsets.UTF_8);
		String path = currentUser.getId() +"/"+fileId+"."+extension;
	
		storageService.upload(path, file.getBytes(), file.getContentType());
		String actualContentType = tika.detect(file.getInputStream());
		DriveFile driveFile = new DriveFile(originalFileName, file.getSize(), actualContentType, path, folder, currentUser);
		dFileRepository.save(driveFile);
	}
	
	public FileResponse retriveFileDetails(UUID fileId) {
		DriveFile driveFile = getDriveFile(fileId);
		validateUser(getUser(), driveFile);
		List<BreadCrumbs> breadCrumbs= directoryView.getFileDirectoryView(fileId);
		return convertToResponse(driveFile, breadCrumbs);
	}
	
	@Transactional
	public void moveFile(UUID fileId, UUID targetFolderId) {
		DriveFile driveFile = getDriveFile(fileId);
		Folder targetFolder = folderRepository.findById(targetFolderId).orElseThrow(()->new ResourceNotFoundException("Folder not found"));
		validateUser(getUser(), driveFile, targetFolder);
		driveFile.setFolder(targetFolder);
	}
	
	@Transactional
	public void restoreFile(UUID fileId) {
		DriveFile driveFile = getDeletedFile(fileId);
		validateUser(getUser(), driveFile);
		driveFile.setIsDeleted(Boolean.FALSE);
		dFileRepository.save(driveFile);
	}
	
	@Transactional
	public void delete(UUID fileId) {
		DriveFile driveFile = getDriveFile(fileId);
		validateUser(getUser(), driveFile);
		driveFile.setIsDeleted(Boolean.TRUE);
		dFileRepository.save(driveFile);
	}
	
	@Transactional
	public void rename(UUID fileId, FileRequest request) {
		DriveFile driveFile = getDriveFile(fileId);
		validateUser(getUser(), driveFile);
		driveFile.setName(request.getName());
	}
	
	
	public List<FileResponse> myStarredFiles(){
		User user = getUser();
		Set<DriveFile> starreDriveFiles = dFileRepository.findMyStarredFiles(user.getId());
		return starreDriveFiles.stream().map(this::convertToResponse).collect(Collectors.toList());
	}
	
	public String download(String token) {
		Map<String, String> linkMap = signedUrlService.validateSharedLink(token);
		return linkMap.get("link")+"&download="+linkMap.get("path");
	}
	
	public List<FileResponse> trashFiles(){
		User user = getUser();
		Set<DriveFile> trashFiles = dFileRepository.findTrashed(user.getId());
		return trashFiles.stream().map(this::convertToResponse).collect(Collectors.toList());
	}
	
	
	private FileResponse convertToResponse(DriveFile file) {
		return new FileResponse(file.getId(), file.getName(), file.getSize(), file.getMimeType(), null, file.getOwner().getName());
	}
	
	private FileResponse convertToResponse(DriveFile file, List<BreadCrumbs> breadCrumbs) {
		return new FileResponse(file.getId(), file.getName(), file.getSize(), file.getMimeType(), null, file.getOwner().getName(), breadCrumbs);
	}
	
	
	private DriveFile getDriveFile(UUID fileId) {
		return dFileRepository.findDriveFile(fileId).orElseThrow(()->new ResourceNotFoundException("File is not found"));
	}
	
	private DriveFile getDeletedFile(UUID fileId) {
		return dFileRepository.findDeleted(fileId).orElseThrow(()->new ResourceNotFoundException("File is not found"));
	}
	
	private void validateUser(User user, Folder folder) {
		if(!folder.getOwner().getId().equals(user.getId())) {
			throw new AccessDeniedException("Access denied");
		}
	}
	
	private void validateUser(User user, DriveFile driveFile) {
		if(!driveFile.getOwner().getId().equals(user.getId())) {
			throw new AccessDeniedException("Access denied");
		}
	}
	
	private void validateUser(User user, DriveFile driveFile, Folder folder) {
		if(!driveFile.getOwner().getId().equals(user.getId()) || !folder.getOwner().getId().equals(user.getId())) {
			throw new AccessDeniedException("Access denied");
		}
	}
	
	private User getUser() {
		String email = SecurityUtil.getCurrentUserDetails().orElseThrow(()->new BadCredentialsException("Invalid authentication token"));
		return userRepository.findByEmail(email).orElseThrow(()->new BadCredentialsException("Invalid authentication token"));
	}
	
}
