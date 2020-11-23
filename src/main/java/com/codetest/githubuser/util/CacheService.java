package com.codetest.githubuser.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.codetest.githubuser.requests.GitHubUser;

@Component
public class CacheService {

	private CacheManager cacheManager;	

	@Autowired
	public CacheService(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	@Cacheable(value = "location", key = "#city",  sync = true)
	public List<GitHubUser> cacheUserByLocation(String city, List<GitHubUser> aList) {

		return aList;
	}

	public List<GitHubUser> getUserByLocation(String city) {

		Cache aCache = cacheManager.getCache("location");
		
		if (aCache.get(city) !=null ) {			
			Object cacheStore = aCache.get(city).get();
			return (List<GitHubUser>) cacheStore ;
		}
				
		return null;
	}

}