package com.cloudstorage.security;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtil {
	
	public static Optional<String> getCurrentUserDetails(){
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		
		if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
			return Optional.empty();
		}
		
		
		/*
		 * Object principal = authentication.getPrincipal();
		if(principal instanceof UserDetails) {
			return Optional.of((UserDetails)principal);
		}
		*/
		return Optional.of(authentication.getName());
	}
	/*
	public static Optional<String> getCurrentEmail(){
		return getCurrentUserDetails().map(UserDetails::getUsername);
	}
	
	*/
	
	
}
