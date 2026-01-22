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
@Table(name = "folders",
uniqueConstraints = {@UniqueConstraint(columnNames = {"owner_id", "parent_folder_id", "name"})})
public class Folder {

	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	@Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
	private UUID id;
	
	private String name;
	
	private Boolean isDeleted = false;
	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_folder_id",nullable = true)
	private Folder parentFolder;
	
	@OneToMany(mappedBy = "parentFolder", orphanRemoval = true)
	private Set<Folder> subFolders = new HashSet<>();
	
	@OneToMany(mappedBy = "folder", orphanRemoval = true)
	private Set<DriveFile> files;


	public Folder() {
		
	}

	public Folder( String name, User owner, Folder parentFolder) {
		this.name = name;
		this.owner = owner;
		this.parentFolder = parentFolder;
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

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Folder getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(Folder parentFolder) {
		this.parentFolder = parentFolder;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, owner.getId(), parentFolder);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Folder other = (Folder) obj;
		return Objects.equals(name, other.name)
				&& Objects.equals(owner.getId(), other.owner.getId()) && Objects.equals(parentFolder, other.parentFolder);
	}

	public Set<DriveFile> getFiles() {
		return files;
	}

	public void setFiles(Set<DriveFile> files) {
		this.files = files;
	}

	public Set<Folder> getSubFolders() {
		return subFolders;
	}

	public void setSubFolders(Set<Folder> subFolders) {
		this.subFolders = subFolders;
	}
	
	
}
