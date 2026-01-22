package com.cloudstorage.service;

import java.util.ArrayList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cloudstorage.model.User;
import com.cloudstorage.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username).orElseThrow();
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), new ArrayList<>());
	}

}
