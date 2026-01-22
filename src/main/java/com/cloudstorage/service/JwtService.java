package com.cloudstorage.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET_KEY= "7527555555733537752535285825771527528525727525829527572252858238883";
	
	public String generateToken(String email) {
		return Jwts
				.builder()
				.signWith(getKey())
				.subject(email)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
				.compact();
	}
	
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public boolean validateToken(String token, String email) {
		return extractEmail(token).equals(email) && !isTokenExpired(token);
	}
	
	private SecretKey getKey() {
		byte[] bytes= Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(bytes);
	}
	

	
	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> resolver) {
		final Claims claims = extractAllClaims(token);
		return resolver.apply(claims);
	}
	
	
	private Claims extractAllClaims(String token) {
		return Jwts
				.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
