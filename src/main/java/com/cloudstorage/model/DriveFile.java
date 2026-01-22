package com.cloudstorage.model;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "drive_files",
uniqueConstraints = {@UniqueConstraint(columnNames = {"owner_id", "folder_id", "name"})})
public class DriveFile {

	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	@Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
	private UUID id;
	
	private String name;
	
	private Long size;
	
	private String mimeType;
	
	private String storagePath;
	
	private Boolean isDeleted = false;
	
	@ManyToOne
	@JoinColumn(name = "folder_id")
	private Folder folder;
	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;
	
	@OneToMany(mappedBy = "file")
	private Set<ShareLink> shareLinks = new HashSet<>();
	
	@OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Starred> starredBy = new HashSet<>();
	
	@OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<FileShare> fileShares = new HashSet<>();

	public DriveFile(String name, Long size, String mimeType, String storagePath,
			Folder folder, User owner) {
		this.name = name;
		this.size = size;
		this.mimeType = mimeType;
		this.storagePath = storagePath;
		this.folder = folder;
		this.owner = owner;
	}

	public DriveFile() {
		
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Override
	public int hashCode() {
		return Objects.hash(folder, name, owner.getId());
	}
	
	

	public Set<ShareLink> getShareLinks() {
		return shareLinks;
	}

	public void setShareLinks(Set<ShareLink> shareLinks) {
		this.shareLinks = shareLinks;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DriveFile other = (DriveFile) obj;
		return Objects.equals(folder, other.folder) && Objects.equals(name, other.name)
				&& Objects.equals(owner.getId(), other.owner.getId());
	}

	public Set<FileShare> getFileShares() {
		return fileShares;
	}

	public void setFileShares(Set<FileShare> fileShares) {
		this.fileShares = fileShares;
	}

}