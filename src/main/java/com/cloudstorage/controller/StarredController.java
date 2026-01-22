package com.cloudstorage.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudstorage.dto.FileResponse;
import com.cloudstorage.response.ApiResponse;
import com.cloudstorage.service.FileService;
import com.cloudstorage.service.StarredService;

@RestController
@RequestMapping("/api/v1/files")
public class StarredController {
	
	private final StarredService starredService;
	private final FileService fileService;
	
	
	public StarredController(StarredService starredService, FileService fileService) {
		this.starredService = starredService;
		this.fileService = fileService;
	}

	@PostMapping("/starred/{fileId}/mark")
	public ResponseEntity<ApiResponse<?>> mark(@PathVariable UUID fileId){
		starredService.markStarred(fileId);
		return new ResponseEntity<>(new ApiResponse<>(true, "File marked starred success", null), HttpStatus.CREATED);
	}
	
	@PatchMapping("/starred/{fileId}/unmark")
	public ResponseEntity<ApiResponse<?>> unmark(@PathVariable UUID fileId){
		starredService.unmarkStarred(fileId);
		return ResponseEntity.ok(new ApiResponse<>(true, "File unmarked starred success", null));
	}
	
	@GetMapping("/starred")
	public ResponseEntity<ApiResponse<?>> starredFiles(){
		List<FileResponse> responses = fileService.myStarredFiles();
		return ResponseEntity.ok(new ApiResponse<>(true, "Fetched success", responses));
	}
}
