package com.cloudstorage.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudstorage.model.ShareLink;
import java.util.Optional;
import java.time.LocalDateTime;



public interface ShareLinkRepository extends JpaRepository<ShareLink, UUID>{

	Optional<ShareLink> findByToken(String token);
	
	void deleteByExpiredAtBefore(LocalDateTime now);
}
