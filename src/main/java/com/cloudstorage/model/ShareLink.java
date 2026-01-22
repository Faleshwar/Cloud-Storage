package com.cloudstorage.model;


import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "share_links")
public class ShareLink {


	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	@Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
	private UUID id;
	
	@Column(unique = true)
	private String token;
	
	private LocalDateTime expiredAt;
	
	private String passwordHash;
	
	private String accessLevel;
	
	@ManyToOne
	@JoinColumn(name = "file_id")
	private DriveFile file;

	public ShareLink(String token, LocalDateTime expireAt, String passwordHash, String accessLevel, DriveFile file) {
		this.token = token;
		this.expiredAt = expireAt;
		this.passwordHash = passwordHash;
		this.accessLevel = accessLevel;
		this.file = file;
	}

	public ShareLink() {
		
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(LocalDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public DriveFile getFile() {
		return file;
	}

	public void setFile(DriveFile file) {
		this.file = file;
	}

	@Override
	public int hashCode() {
		return Objects.hash(token);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShareLink other = (ShareLink) obj;
		return Objects.equals(token, other.token);
	}
	
}