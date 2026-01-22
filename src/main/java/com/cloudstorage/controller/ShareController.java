package com.cloudstorage.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudstorage.dto.FileResponse;
import com.cloudstorage.request.ShareLinkRequest;
import com.cloudstorage.request.ShareUserRequest;
import com.cloudstorage.response.ApiResponse;
import com.cloudstorage.service.ShareService;
import com.cloudstorage.service.SignedUrlService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/files")
public class ShareController {

    private final SignedUrlService signedUrlService;

	
	private final ShareService shareService;
	
	
	
	public ShareController(ShareService shareService, SignedUrlService signedUrlService) {
		this.shareService = shareService;
		this.signedUrlService = signedUrlService;
	}

	@PostMapping("/share/{fileId}/user")
	public ResponseEntity<ApiResponse<?>> sharedWithUser(@RequestBody @Valid ShareUserRequest request, @PathVariable UUID fileId){
		shareService.share(request, fileId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Sharing with user completed", null));
	}
	
	@GetMapping("/shared-with-me")
	public ResponseEntity<ApiResponse<List<FileResponse>>> sharedFilesWithMe(){
		List<FileResponse> list = shareService.sharedWithMeFiles();
		return ResponseEntity.ok(new ApiResponse<>(true, "Fetched success", list));
	}
	
	@DeleteMapping("/share/{fileId}/revoke")
	public ResponseEntity<ApiResponse<?>> revokeWithSharedUser(@PathVariable UUID fileId, @RequestParam(required = true) UUID userId){
		shareService.revokeShare(fileId, userId);
		return ResponseEntity.ok(new ApiResponse<>(true, "File share revoked success", null));
	}
	
	@PostMapping("/share/{fileId}/link")
	public ResponseEntity<?> shareLink(@PathVariable UUID fileId, @RequestBody @Valid ShareLinkRequest request){
		String token = signedUrlService.createSharedLink(fileId, request.getAccessLevel(), request.getExpiredAt());
		return new ResponseEntity<>(Map.of("status", "Share link token generated successfully", "link", "http://localhost:8080/api/v1/files/storage/"+token), HttpStatus.CREATED);
	}
	
	@GetMapping("/storage/{token}")
	public void accessFile(@PathVariable String token, HttpServletResponse response) throws IOException {
		String link = signedUrlService.validateSharedLink(token).get("link");
		response.sendRedirect(link);
	}
}
