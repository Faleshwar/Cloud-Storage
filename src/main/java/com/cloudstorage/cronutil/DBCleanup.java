package com.cloudstorage.cronutil;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cloudstorage.repository.FileShareRepository;
import com.cloudstorage.repository.ShareLinkRepository;

@Component
public class DBCleanup {

	@Autowired
	private FileShareRepository fileShareRepository;
	
	@Autowired
	private ShareLinkRepository shareLinkRepository;
	
	@Scheduled(cron = "0 0 * * * *")
	@Transactional
	public void removeExpiredFileShare() {
		fileShareRepository.deleteByExpiryTimeBefore(LocalDateTime.now());
	}
	
	@Scheduled(cron = "0 0 * * * *")
	@Transactional
	public void removeExpiredShareLink() {
		shareLinkRepository.deleteByExpiredAtBefore(LocalDateTime.now());
		System.out.println("Deleted record expired shareLink");
	}
}
