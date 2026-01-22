package com.cloudstorage.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloudstorage.model.Starred;

public interface StarredRepository extends JpaRepository<Starred, UUID>{

	@Query("SELECT s FROM Starred s WHERE s.user.id = :userId AND s.file.isDeleted = false")
	Set<Starred> findStarredFiles(@Param("userId") UUID userId);
	
	@Query("SELECT s FROM Starred s WHERE s.file.id = :fileId AND s.file.isDeleted = false")
	Optional<Starred> findByFileId(@Param("fileId") UUID fileId);
	
}
