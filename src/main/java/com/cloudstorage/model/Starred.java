package com.cloudstorage.model;


import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "starred",
uniqueConstraints = @UniqueConstraint(columnNames = {"file_id", "user_id"}))
public class Starred {
	
	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	@Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
	private UUID id;
	
	@CreationTimestamp
	private LocalDateTime starredAt;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "file_id")
	private DriveFile file;

	public Starred() {
	
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDateTime getStarredAt() {
		return starredAt;
	}

	public void setStarredAt(LocalDateTime starredAt) {
		this.starredAt = starredAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DriveFile getFile() {
		return file;
	}

	public void setFile(DriveFile file) {
		this.file = file;
	}

	public Starred(LocalDateTime starredAt, User user, DriveFile file) {
		this.starredAt = starredAt;
		this.user = user;
		this.file = file;
	}

	@Override
	public int hashCode() {
		return Objects.hash(file.getId(), user.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Starred other = (Starred) obj;
		return Objects.equals(file.getId(), other.file.getId()) && Objects.equals(user.getId(), other.user.getId());
	}

	
	
}
