package com.cloudstorage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SupabaseConfig {

	@Bean
	public WebClient supabaseClient(@Value("${supabase.url}") String url, @Value("${supabase.service.key}")String serviceKey) {
		
		return WebClient.builder()
				.baseUrl(url)
				.defaultHeader("apiKey", serviceKey)
				.defaultHeader("Authorization", "Bearer "+serviceKey)
				.build();
	}
}
