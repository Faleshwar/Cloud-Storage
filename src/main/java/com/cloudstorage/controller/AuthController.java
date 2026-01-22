package com.cloudstorage.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloudstorage.request.GoogleLoginRequest;
import com.cloudstorage.request.LoginRequest;
import com.cloudstorage.request.RegisterRequest;
import com.cloudstorage.response.AuthResponse;
import com.cloudstorage.service.AuthService;
import com.cloudstorage.service.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import jakarta.validation.Valid;

@RestController
public class AuthController {

	@Autowired
	private GoogleAuthService googleAuthService;
	
	@Autowired
	private AuthService authService;

	
	@PostMapping("/api/auth/google")
	public ResponseEntity<AuthResponse> googleLogin(@RequestBody GoogleLoginRequest request) throws GeneralSecurityException, IOException{
		GoogleIdToken.Payload  payload = googleAuthService.verifyToken(request.getIdToken());
		String jwt = authService.loginWithGoogle(payload);
		return ResponseEntity.ok(new AuthResponse("success", jwt));
	}
	
	@PostMapping("/api/auth/login")
	public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request){
		String jwt = authService.login(request);
		return new ResponseEntity<>(new AuthResponse("Login successfully completed", jwt), HttpStatus.CREATED);
	}
	
	@PostMapping("/api/auth/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request){
		AuthResponse response= authService.register(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
