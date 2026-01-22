package com.cloudstorage.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudstorage.config.DirectoryView;
import com.cloudstorage.dto.BreadCrumbs;
import com.cloudstorage.dto.FileResponse;
import com.cloudstorage.dto.FolderResponse;
import com.cloudstorage.exception.ResourceNotFoundException;
import com.cloudstorage.model.DriveFile;
import com.cloudstorage.model.Folder;
import com.cloudstorage.model.User;
import com.cloudstorage.repository.DFileRepository;
import com.cloudstorage.repository.FolderRepository;
import com.cloudstorage.repository.UserRepository;
import com.cloudstorage.request.FolderRequest;
import com.cloudstorage.security.SecurityUtil;

@Service
public class FolderService {

    

	private final FolderRepository folderRepository;
	private final UserRepository userRepository;
	private final DFileRepository dFileRepository;

	private final DirectoryView directoryView;
	
	public FolderService(FolderRepository folderRepository, UserRepository userRepository, DFileRepository dFileRepository, DirectoryView directoryView) {
		this.folderRepository = folderRepository;
		this.userRepository = userRepository;
		this.dFileRepository = dFileRepository;
		this.directoryView = directoryView;
		
	}
	
	@Transactional
	public FolderResponse createNewFolder(FolderRequest request) {
		Folder parent = null;
		if(request.getParentId() != null) {
			parent = folderRepository.findById(request.getParentId()).orElseThrow();
		}
		User owner = getCurrentUser();
		Folder folder = new Folder(request.getName(), owner, parent);
		Folder savedFolder= folderRepository.save(folder);
		return folderToResponse(savedFolder);
	}
	
	@Transactional
	public void renameFolder(UUID folderId, FolderRequest request) {
		Folder folder = getFolder(folderId);
		User currentUser = getCurrentUser();
		validateUser(currentUser, folder);
		folder.setName(request.getName());
		folderRepository.save(folder);
	}
	@Transactional
	public void moveFolder(UUID folderId, UUID parentFolderId) {
		Folder folder = getFolder(folderId);
		Folder newParentFolder = getFolder(parentFolderId);
		validateUser(getCurrentUser(), folder, newParentFolder);
		folder.setParentFolder(newParentFolder);
		folderRepository.save(folder);
	}
	
	@Transactional
	public void deleteFolder(UUID folderId) {
		Folder folder = getFolder(folderId);
		validateUser(getCurrentUser(), folder);
		Queue<Folder> queue = new LinkedList<>();
		queue.offer(folder);
		
		while(!queue.isEmpty()) {
			Folder currentFolder = queue.poll();
			
			
			for(DriveFile file: currentFolder.getFiles()) {
				file.setIsDeleted(Boolean.TRUE);
				dFileRepository.save(file);
			}
			
			currentFolder.setIsDeleted(Boolean.TRUE);
			folderRepository.save(currentFolder);
			
			Set<Folder> chilFolders = folderRepository.findAllChildrens(currentFolder.getId());
			queue.addAll(chilFolders);
		}
	}
	
	@Transactional
	public void restoreFolder(UUID folderId){
		Folder folder = getDeletedFolder(folderId);
		validateUser(getCurrentUser(), folder);
		Queue<Folder> queue = new LinkedList<>();
		queue.add(folder);
		
		while(!queue.isEmpty()) {
			Folder currentFolder = queue.poll();
			
			
			for(DriveFile driveFile: currentFolder.getFiles()) {
				driveFile.setIsDeleted(Boolean.FALSE);
				dFileRepository.save(driveFile);
			}
			currentFolder.setIsDeleted(Boolean.FALSE);
			folderRepository.save(currentFolder);
			
			Set<Folder> childFolders = folderRepository.findAllDeletedChildrens(currentFolder.getId());
			queue.addAll(childFolders);
		}
	}
	
	public FolderResponse getFolderDetails(UUID folderId) {
		Folder folder = getFolder(folderId);
		validateUser(getCurrentUser(), folder);
		//List<BreadCrumbs> breadCrumbs= directoryView.getFolderDirectoryView(folderId);
		return folderToResponse(folder);
	}
	
	public List<FolderResponse> getRootFolders(){
		String email = SecurityUtil.getCurrentUserDetails().orElseThrow(()->new BadCredentialsException("Invalid authentication token"));
		Set<Folder> folders = folderRepository.findRootFoldersByEmail(email);
		return folders.stream().map(this::folderToResponse).collect(Collectors.toList());
	}
	
	public List<FolderResponse> getAllChildrenFolder(UUID parentFolderId){
		Set<Folder> folders = folderRepository.findAllChildrens(parentFolderId);
		return folders.stream().map(this::folderToResponse).collect(Collectors.toList());
	}
	
	public List<FileResponse> getAllChildrenFiles(UUID parentFolderId){
		Set<DriveFile> files = dFileRepository.findAllFiles(parentFolderId);
		return files.stream().map(this::fileToDto).collect(Collectors.toList());
	}
	
	
	public List<FileResponse> getAllRootFiles(){
		User currentUser = getCurrentUser();
		Set<DriveFile> files = dFileRepository.findAllRootFiles(currentUser.getId());
		return files.stream().map(this::fileToDto).collect(Collectors.toList());
	}
	
	public List<FolderResponse> trashedFolders(){
		User currentUser = getCurrentUser();
		Set<Folder> trashedFolders = folderRepository.findTrashed(currentUser.getId());
		return trashedFolders.stream().map(this::folderToResponse).collect(Collectors.toList());
	}
	
	public List<BreadCrumbs> getBreadCrumbs(UUID folderId){
		return directoryView.getFolderDirectoryView(folderId);
	}
	
	public List<BreadCrumbs> getBreadCrumbsFile(UUID fileId){
		return directoryView.getFileDirectoryView(fileId);
	}
	
	
	// User and folder validations
	private void validateUser(User currentUser, Folder folder, Folder parentFolder) {
		if(!folder.getOwner().getId().equals(currentUser.getId()) || !parentFolder.getOwner().getId().equals(currentUser.getId())) {
			throw new AccessDeniedException("Access denied");
		}
	}
	
	private void validateUser(User currentUser, Folder folder) {
		if(!folder.getOwner().getId().equals(currentUser.getId())){
			throw new AccessDeniedException("Access denied");
		}
	}
	
	private Folder getFolder(UUID folderId) {
		return folderRepository.findById(folderId).orElseThrow(()->new ResourceNotFoundException("Folder not found with id "+folderId));
	}
	private Folder getDeletedFolder(UUID folderId) {
		return folderRepository.findDeletedFolder(folderId).orElseThrow(()->new ResourceNotFoundException("Folder not found with id "+folderId));
	}
	private FolderResponse folderToResponse(Folder folder) {
		return new FolderResponse(folder.getId(), folder.getName(), folder.getOwner().getName(), folder.getParentFolder()==null?null:folder.getParentFolder().getId());
	}
	
	private FileResponse fileToDto(DriveFile driveFile) {
		return new FileResponse(driveFile.getId(), driveFile.getName(), driveFile.getSize(), driveFile.getMimeType(), driveFile.getFolder()!=null?driveFile.getFolder().getId():null, driveFile.getOwner().getName());
	}
	
	private User getCurrentUser() {
		String email = SecurityUtil.getCurrentUserDetails().orElseThrow(()->new BadCredentialsException("Invalid authentication token"));
		return userRepository.findByEmail(email).orElseThrow(()->new BadCredentialsException("Invalid authentication token"));
	}

}
