package com.cloudstorage.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloudstorage.model.DriveFile;

public interface DFileRepository extends JpaRepository<DriveFile, UUID>{

	@Query("SELECT file FROM DriveFile file WHERE file.isDeleted = false AND file.folder.id = :folderId")
	Set<DriveFile> findAllFiles(@Param("folderId") UUID folderId);
	
	@Query("SELECT file FROM DriveFile file WHERE file.isDeleted = false AND file.owner.id= :ownerId AND file.folder IS NULL")
	Set<DriveFile> findAllRootFiles(@Param("ownerId") UUID ownerId);
	
	
	@Query("SELECT file FROM DriveFile file WHERE file.isDeleted = false AND file.id = :fileId")
	Optional<DriveFile> findDriveFile(@Param("fileId") UUID fileId);
	
	@Query("SELECT file FROM DriveFile file WHERE file.isDeleted = true AND file.id = :fileId")
	Optional<DriveFile> findDeleted(@Param("fileId") UUID fileId);
	
	@Query("SELECT file FROM DriveFile file WHERE file.isDeleted = true AND file.folder IS NULL AND file.owner.id = :ownerId")
	Set<DriveFile> findTrashed(@Param("ownerId") UUID ownerId);
	
	@Query("SELECT file FROM DriveFile file JOIN file.starredBy s JOIN s.user u  WHERE u.id = :userId AND file.isDeleted = false")
	Set<DriveFile> findMyStarredFiles(@Param("userId") UUID userId);
	
	@Query("SELECT file From DriveFile file JOIN file.fileShares s JOIN s.sharedWith u WHERE u.id = :userId AND file.isDeleted = false")
	Set<DriveFile> findSharedWithMeFiles(@Param("userId") UUID userId);
	
}
