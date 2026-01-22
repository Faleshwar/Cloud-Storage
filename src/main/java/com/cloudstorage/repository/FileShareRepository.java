package com.cloudstorage.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloudstorage.model.FileShare;

public interface FileShareRepository extends JpaRepository<FileShare, UUID>{

	@Query("Select fs From FileShare fs Where fs.sharedWith.id = :userId And fs.expiryTime Is Null Or fs.expiryTime > CURRENT_TIMESTAMP")
	Set<FileShare> findSharesWithUser(@Param("userId") UUID userId);
	
	@Query("Select fs From FileShare fs Where fs.sharedWith.id = :userId And fs.file.id = :fileId And fs.expiryTime Is Null Or fs.expiryTime > CURRENT_TIMESTAMP")
	Optional<FileShare> findBySharedWithAndFileId(@Param("userId") UUID userId, @Param("fileId") UUID fileId);
	
	@Query("Select fs From FileShare fs Where fs.file.id = :fileId And fs.sharedWith.id = :userId")
	Optional<FileShare> findByFileSharedWithUser(@Param("fileId") UUID fileId, @Param("userId") UUID userId);
	
	void deleteByExpiryTimeBefore(LocalDateTime now);
}
