package com.cloudstorage.model;


import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "file_shared",
uniqueConstraints = {@UniqueConstraint(columnNames = {"file_id", "shared_with_id"})})
public class FileShare {

	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	@Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
	private UUID id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_id")
	private DriveFile file;
	
	@Enumerated(EnumType.STRING)
	private AccessType accessType = AccessType.PUBLIC_USER;
	
	private LocalDateTime expiryTime;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
	@JoinColumn(name = "shared_with_id")
	private User sharedWith;
	
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
	@JoinColumn(name = "shared_by_id")
	private User sharedBy;


	@Override
	public int hashCode() {
		return Objects.hash(file.getId(), sharedWith.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileShare other = (FileShare) obj;
		return Objects.equals(sharedWith.getId(), other.sharedWith.getId()) && Objects.equals(file.getId(), other.file.getId());
	}

	public FileShare() {
		
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public DriveFile getFile() {
		return file;
	}

	public void setFile(DriveFile file) {
		this.file = file;
	}

	public AccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}

	public LocalDateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(LocalDateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

	public User getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(User sharedWith) {
		this.sharedWith = sharedWith;
	}

	public User getSharedBy() {
		return sharedBy;
	}

	public void setSharedBy(User sharedBy) {
		this.sharedBy = sharedBy;
	}

	public FileShare(DriveFile file, AccessType accessType, LocalDateTime expiryTime, User sharedWith, User sharedBy) {
		this.file = file;
		this.accessType = accessType;
		this.expiryTime = expiryTime;
		this.sharedWith = sharedWith;
		this.sharedBy = sharedBy;
	}

	
	
	
}
