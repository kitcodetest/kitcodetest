package com.codetest.githubuser.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CacheUpdateScheduler {
	
	private CacheManager cacheManager;
	
	@Autowired
	public CacheUpdateScheduler(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	@Scheduled(fixedDelay = 1800000)
	public void scheduleFixedDelayTask() {
		for(String name:cacheManager.getCacheNames()){
			cacheManager.getCache(name).clear();     // clear cache by name
		}
	}
}
