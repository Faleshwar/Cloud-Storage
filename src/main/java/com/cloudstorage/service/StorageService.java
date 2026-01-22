package com.cloudstorage.service;

public interface StorageService {
	
	void upload(String path, byte[] data, String contentType);
	
	String generateSignedUrl(String path);
	
	void delete(String path);
}
