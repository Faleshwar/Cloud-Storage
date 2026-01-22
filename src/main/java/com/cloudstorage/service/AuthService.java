package com.cloudstorage.service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudstorage.model.User;
import com.cloudstorage.repository.UserRepository;
import com.cloudstorage.request.LoginRequest;
import com.cloudstorage.request.RegisterRequest;
import com.cloudstorage.response.AuthResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

@Service
public class AuthService {

   

    
	private final UserRepository userRepository;
	
	private final JwtService jwtService;

	private final PasswordEncoder passwordEncoder;
	
	
	private final AuthenticationManager authenticationManager;
	
	
	public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		
	}
	
	@Transactional
	public AuthResponse register(RegisterRequest request) {
		if(userRepository.existsByEmail(request.getEmail())) {
			throw new BadCredentialsException("User is already registered");
		}
		User newUser = new User(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
		userRepository.save(newUser);
		
		return new AuthResponse("Register success", jwtService.generateToken(newUser.getEmail()));
	}
	
	
	public String loginWithGoogle(GoogleIdToken.Payload payload) {
		String email = payload.getEmail();
		if(!userRepository.existsByEmail(email)) {
			String name = (String)payload.get("name");
			User user = new User(name, email, null);
			userRepository.save(user);
		}
		return jwtService.generateToken(email);
	}
	
	
	public String login(LoginRequest request) {
		try {
			Authentication authentication= authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			UserDetails userDetails = (UserDetails)authentication.getPrincipal();
			return jwtService.generateToken(userDetails.getUsername());
		}catch(AuthenticationException exception) {
			throw exception;
		}
	}
	
}
