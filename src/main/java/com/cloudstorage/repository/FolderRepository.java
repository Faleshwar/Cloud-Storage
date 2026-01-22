package com.cloudstorage.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloudstorage.model.Folder;
import java.util.Set;
import java.util.Optional;




public interface FolderRepository extends JpaRepository<Folder, UUID>{
	
	@Query("SELECT f FROM Folder f WHERE f.id = :folderId AND f.isDeleted = FALSE")
	Optional<Folder> findById(@Param("folderId") UUID folderId);

	@Query("SELECT f from Folder f WHERE f.isDeleted = FALSE AND f.owner.email = :email AND f.parentFolder IS NULL")
	Set<Folder> findRootFoldersByEmail(@Param("email") String email);
	
	@Query("SELECT f FROM Folder f WHERE f.isDeleted = false AND f.parentFolder.id = :folderId")
	Set<Folder> findAllChildrens(@Param("folderId") UUID folderId);
	
	@Query("SELECT f FROM Folder f WHERE f.isDeleted = true AND f.parentFolder.id = :folderId")
	Set<Folder> findAllDeletedChildrens(@Param("folderId") UUID folderId);
	
	
	@Query("SELECT f FROM Folder f WHERE f.isDeleted = TRUE AND f.id = :folderId")
	Optional<Folder> findDeletedFolder(@Param("folderId") UUID folderId);
	
	@Query("SELECT f FROM Folder f LEFT JOIN f.parentFolder p WHERE f.owner.id = :ownerId AND f.isDeleted is true AND (p IS NULL OR p.isDeleted IS false)")
	Set<Folder> findTrashed(@Param("ownerId") UUID ownerId);
}
