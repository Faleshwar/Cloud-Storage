package com.cloudstorage.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", indexes = {@Index(name="idx_email", columnList = "email")})
public class User {

	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	@Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
	private UUID id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = true)
	private String password;

	@OneToMany(mappedBy = "owner", orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Folder> folders = new HashSet<>();
	
	@OneToMany(mappedBy = "owner", orphanRemoval = true)
	private Set<DriveFile> files = new HashSet<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Starred> starredFiles = new HashSet<>();
	
	@OneToMany(mappedBy = "sharedBy", orphanRemoval = true)
	private Set<FileShare> grantedShares = new HashSet<>();
	
	@OneToMany(mappedBy = "sharedWith", orphanRemoval = true)
	private Set<FileShare> receivedShares = new HashSet<>();

	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public User() {
		
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Folder> getFolders() {
		return folders;
	}

	public void setFolders(Set<Folder> folders) {
		this.folders = folders;
	}

	public Set<DriveFile> getFiles() {
		return files;
	}

	public void setFiles(Set<DriveFile> files) {
		this.files = files;
	}

	public Set<Starred> getStarredFiles() {
		return starredFiles;
	}

	public void setStarredFiles(Set<Starred> starredFiles) {
		this.starredFiles = starredFiles;
	}

	public Set<FileShare> getGrantedShares() {
		return grantedShares;
	}

	public void setGrantedShares(Set<FileShare> grantedShares) {
		this.grantedShares = grantedShares;
	}

	public Set<FileShare> getReceivedShares() {
		return receivedShares;
	}

	public void setReceivedShares(Set<FileShare> receivedShares) {
		this.receivedShares = receivedShares;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email);
	}
	
}
