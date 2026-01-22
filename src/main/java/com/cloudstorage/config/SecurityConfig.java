package com.cloudstorage.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cloudstorage.security.JwtFilter;
import com.cloudstorage.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfigurationSource urlConfiguration;
	
	private final CustomUserDetailsService userDetailsService;
	private final JwtFilter jwtFilter;
	
	

	public SecurityConfig(CustomUserDetailsService userDetailsService, JwtFilter jwtFilter, CorsConfigurationSource urlConfiguration) {
		this.userDetailsService = userDetailsService;
		this.jwtFilter = jwtFilter;
		this.urlConfiguration = urlConfiguration;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) {
		return http
				.csrf(csrf->csrf.disable())
				.cors(c->c.configurationSource(urlConfiguration()))
				.authorizeHttpRequests(auth->auth
						.requestMatchers("/api/auth/**", "/google", "/api/v1/files/storage/**", "/api/v1/files/{token}/download").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager manager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(provider);
	}
	
	@Bean
	public CorsConfigurationSource urlConfiguration() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:5173"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "PUT", "POST", "DELETE", "PATCH"));
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		configurationSource.registerCorsConfiguration("/**", configuration);
		return configurationSource;
	}
}
