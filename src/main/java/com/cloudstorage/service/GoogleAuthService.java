package com.cloudstorage.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class GoogleAuthService {

	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;
	
	public GoogleIdToken.Payload verifyToken(String idToken) throws GeneralSecurityException, IOException{
	
		if(idToken == null || idToken.isBlank()) {
			throw new RuntimeException("Idtoken cannot be null or empty");
		}
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
				.setAudience(Collections.singleton(clientId))
				.setAcceptableTimeSkewSeconds(30)
				.setIssuers(List.of(
                        "https://accounts.google.com",
                        "accounts.google.com"
                ))
				.build();
		GoogleIdToken token = verifier.verify(idToken);
		
		if(token == null) {
			throw new RuntimeException("Invalid token");
		}
		return token.getPayload();
	}
}
