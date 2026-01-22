package com.cloudstorage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

@Service
public class SupabaseStorageService implements StorageService{
	
	private final WebClient supabaseClient;
	
	@Value("${supabase.url}")
	private String bucketPath;
	
	public SupabaseStorageService(WebClient supabaseClient) {
		this.supabaseClient = supabaseClient;
	}

	@Override
	public void upload(String path, byte[] data, String contentType) {
		supabaseClient.post()
		.uri("/storage/v1/object/media-store/files/{path}", path)
		.header("Content-Type", contentType)
		.bodyValue(data)
		.retrieve()
		.bodyToMono(String.class)
		.block();
	}

	@Override
	public String generateSignedUrl(String path) {
		
		String json = """
				{
					"expiresIn":120
				}
				""";
		String userId = path.substring(0, path.indexOf("/"));
		 path = path.substring(path.indexOf("/")+1);
		JsonNode respose = supabaseClient.post()
				.uri("/storage/v1/object/sign/media-store/files/{userId}/{path}", userId, path)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(json)
				.retrieve()
				.bodyToMono(JsonNode.class)
				.block();
	
		return bucketPath+"/storage/v1"+respose.get("signedURL").asString();
	}

	@Override
	public void delete(String path) {
		supabaseClient.delete()
		.uri("/storage/v1/object/media-store/files/{path}", path)
		.retrieve()
		.bodyToMono(Void.class)
		.block();
	}

}
