package com.cloudstorage.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudstorage.dto.BreadCrumbs;
import com.cloudstorage.dto.FileResponse;
import com.cloudstorage.dto.FolderResponse;
import com.cloudstorage.request.FolderRequest;
import com.cloudstorage.response.ApiResponse;
import com.cloudstorage.service.FolderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class FolderController {
	
	private final FolderService folderService;
	
	

	public FolderController(FolderService folderService) {
		this.folderService = folderService;
	}



	// Creates new folder
	@PostMapping("/folders")
	public ResponseEntity<ApiResponse<FolderResponse>> create(@RequestBody @Valid FolderRequest request){
		FolderResponse response = folderService.createNewFolder(request);
		return new ResponseEntity<>(new ApiResponse<>(true, "Folder created successfully", response), HttpStatus.CREATED);
	}
	
	// Rename existing folder
	@PatchMapping("/folders/{folderId}")
	public ResponseEntity<ApiResponse<?>> rename(@RequestBody @Valid FolderRequest request, @PathVariable UUID folderId){
		folderService.renameFolder(folderId, request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Folder rename success", null));
	}
	
	
	
	@PutMapping("/folders/{folderId}")
	public ResponseEntity<ApiResponse<?>> move(@PathVariable UUID folderId, @RequestParam(required = true) UUID parentId){
		folderService.moveFolder(folderId, parentId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Folder moved successfully", null));
	}
	
	
	
	@DeleteMapping("/folders/{folderId}")
	public ResponseEntity<ApiResponse<?>> delete(@PathVariable UUID folderId){
		folderService.deleteFolder(folderId);
		return ResponseEntity.ok(new ApiResponse<>(true,"Folder deleted successfully completed", null));
	}
	
	
	@GetMapping("/folders/root")
	public ResponseEntity<ApiResponse<?>> rootFolders(){
		List<FolderResponse> folderResponses = folderService.getRootFolders();
		List<FileResponse> fileResponses = folderService.getAllRootFiles();
		return ResponseEntity.ok(new ApiResponse<>(true, "Fetched success", Map.of("folders", folderResponses, "files", fileResponses)));
	}
	
	
	
	@GetMapping("/folders/{folderId}/items")
	public ResponseEntity<ApiResponse<Map<String, ?>>> getFoldersAndFiles(@PathVariable UUID folderId){
		List<FolderResponse> folderResponses = folderService.getAllChildrenFolder(folderId);
		List<FileResponse> fileResponses = folderService.getAllChildrenFiles(folderId);
		List<BreadCrumbs> breadCrumbs = folderService.getBreadCrumbs(folderId);
		Map<String, ?> res = Map.of("folders", folderResponses, "files", fileResponses, "breadcrumbs", breadCrumbs);
		return ResponseEntity.ok(new ApiResponse<>(true, "Fetched files and folders successfully", res));
	}
	
	@GetMapping("/folders/{folderId}")
	public ResponseEntity<ApiResponse<?>> getChildFolders(@PathVariable UUID folderId){
		List<FolderResponse> folderResponses = folderService.getAllChildrenFolder(folderId);
		//List<BreadCrumbs> breadCrumbs = folderService.getBreadCrumbs(folderId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Fetch success", folderResponses));
	}
	
	
	
	@PutMapping("/folders/{folderId}/restore")
	public ResponseEntity<ApiResponse<?>> restore(@PathVariable UUID folderId){
		folderService.restoreFolder(folderId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Folder restore completed successfully", null));
	}
}
