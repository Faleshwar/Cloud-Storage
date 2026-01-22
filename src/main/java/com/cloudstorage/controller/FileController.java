package com.cloudstorage.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.cloudstorage.service.SignedUrlService;

import jakarta.validation.Valid;

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
import org.springframework.web.multipart.MultipartFile;

import com.cloudstorage.dto.FileResponse;
import com.cloudstorage.dto.FolderResponse;
import com.cloudstorage.request.FileRequest;
import com.cloudstorage.response.ApiResponse;
import com.cloudstorage.service.FileService;
import com.cloudstorage.service.FolderService;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;
    private final FolderService folderService;
	
	public FileController(FileService fileService, SignedUrlService signedUrlService, FolderService folderService) {
		this.fileService = fileService;
		this.folderService = folderService;
	}

	@GetMapping("/files/{fileId}")
	public ResponseEntity<ApiResponse<FileResponse>> details(@PathVariable UUID fileId){
		FileResponse response = fileService.retriveFileDetails(fileId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Fetched details success", response));
	}
	
	@PatchMapping("/files/{fileId}")
	public ResponseEntity<ApiResponse<?>> rename(@PathVariable UUID fileId, @RequestBody @Valid FileRequest request){
		fileService.rename(fileId, request);
		return ResponseEntity.ok(new ApiResponse<>(true, "File renamed success", null));
	}
	
	
	@PutMapping("/files/{fileId}/move")
	public ResponseEntity<ApiResponse<?>> move(@PathVariable UUID fileId, @RequestParam UUID targetFolderId){
		fileService.moveFile(fileId, targetFolderId);
		return ResponseEntity.ok(new ApiResponse<>(true, "File moved success", null));
	}
	
	@DeleteMapping("/files/{fileId}")
	public ResponseEntity<ApiResponse<?>> delete(@PathVariable UUID fileId){
		fileService.delete(fileId);
		return ResponseEntity.ok(new ApiResponse<>(true, "File deleted success", null));
	}
	
	@PatchMapping("/files/{fileId}/restore")
	public ResponseEntity<ApiResponse<?>> restore(@PathVariable UUID fileId){
		fileService.restoreFile(fileId);
		return ResponseEntity.ok(new ApiResponse<>(true, "File restored success", null));
	}
	
	@PostMapping("/files/{folderId}/upload")
	public ResponseEntity<ApiResponse<?>> upload(@RequestParam("file") MultipartFile file, @PathVariable UUID folderId) throws IOException{
		fileService.upload(file, folderId);
		return ResponseEntity.ok(new ApiResponse<>(true, "File uploaded successfully completed", null));
	}
	
	@PostMapping("/files/upload")
	public ResponseEntity<ApiResponse<?>> upload(@RequestParam("file") MultipartFile file) throws IOException{
		fileService.upload(file, null);
		return ResponseEntity.ok(new ApiResponse<>(true, "File uploaded successfully completed", null));
	}
	
	@GetMapping("/files/{token}/download")
	public ResponseEntity<?> generateDownloadLink(@PathVariable String token){
		String url = fileService.download(token);
		return ResponseEntity.ok(Map.of("status", "Download link generated success","downloadUrl", url));
	}
	
	@GetMapping("/items/trashed")
	public ResponseEntity<?> myTrash(){
		List<FileResponse> fileResponses = fileService.trashFiles();
		List<FolderResponse> folderResponses = folderService.trashedFolders();
		return ResponseEntity.ok(Map.of("folders", folderResponses, "files", fileResponses));
	}
	
}
