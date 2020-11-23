package com.codetest.githubuser.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.codetest.githubuser.requests.GitHubUser;
import com.codetest.githubuser.requests.UserListResponse;
import com.codetest.githubuser.service.GitHubUserService;
import com.codetest.githubuser.util.CacheService;
import com.codetest.githubuser.util.ConcurrentWorker;

@Service
public class GitHubUserServiceImpl implements GitHubUserService {

	private final CacheService cache;
	private final RestTemplate restTemplate;
	private final ConcurrentWorker concurrentWorker;
	private final ConcurrentHashMap<String,String> requestMap;
	
	Logger logger = LoggerFactory.getLogger(GitHubUserServiceImpl.class);
	
	@Autowired
	public GitHubUserServiceImpl(CacheService cache, RestTemplate restTemplate, ConcurrentWorker concurrentWorker) {
		this.cache = cache;
		this.restTemplate = restTemplate;
		this.concurrentWorker = concurrentWorker;
		this.requestMap = new ConcurrentHashMap<>();
	}
	
	@Async("asynchThreadPoolTaskExecutor")
	public void asyncDownLoadUserByLocation(final String location) {
		
		if (requestMap.containsKey(location)) return;
		  else requestMap.put(location, Thread.currentThread().getName());
				
		 List<GitHubUser> searchUserResponse = searchUserByLocation(location);
			
		 List<CompletableFuture<GitHubUser>> allFutures = searchUserResponse
				 												.stream()
				 												.map(user -> concurrentWorker.asynchJob(user.getLogin()))
				 												.collect(Collectors.toList());				
		
		 CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[allFutures.size()])).join();
		 
		 
		List<GitHubUser> topUsers = allFutures.stream()
									.map(future -> future.join())
									.collect(Collectors.toList());
		
		Collections.sort(topUsers, Collections.reverseOrder());
		
		
		cache.cacheUserByLocation(location,topUsers);
		
		requestMap.remove(location);		
	}

	
	public List<GitHubUser> searchUserByLocation(final String locaion) {

		UserListResponse searchUserResponse = null;
		
		String searchURL= "https://api.github.com/search/users?q=repos:>{0}+location:{1}&page={2}&per_page=100";

		List<GitHubUser> allUsersByLocation = new ArrayList<>();
		
		boolean isGoodRepoSize = false;
		int initRepoSize = 15;
		int increment = 5;
		int retryCount = 0;
		int max_page = 100;
		
		// This logic below is to get around GITHUB abuse-rate-limits by reducing concurrent requests and narrowing down large resultset of users to a range of hundreds of users instead of thousands by repository count using repo filter condition repos:>{0} 		
		while (!isGoodRepoSize) { 
			
			if (retryCount > 10) break;
			
			
			String newURl = MessageFormat.format(searchURL, initRepoSize,locaion,1);

			searchUserResponse	= restTemplate.getForObject(newURl,UserListResponse.class);
								
			retryCount++;
			
			if (searchUserResponse.getTotal_count() > 5000) {
				initRepoSize += increment * 30;
				continue;
			} else if (searchUserResponse.getTotal_count() > 3000) {
				initRepoSize += increment * 10;
				continue;
			} else if (searchUserResponse.getTotal_count() > 1000) {
					initRepoSize += increment * 6;
					continue;
			} else if (searchUserResponse.getTotal_count() > 350) {
				initRepoSize += increment * 4 + retryCount;
				continue;
			} else if (searchUserResponse.getTotal_count() > 200) {
				initRepoSize += (increment / 2) +retryCount;
				continue;
			} else {
				isGoodRepoSize = true;
			}
			
		}
		
		allUsersByLocation.addAll(searchUserResponse.getItems());
		
		
		for (int i = 2; i < 100; i++ ) {
			
			String nextUrl = MessageFormat.format(searchURL, initRepoSize,locaion,i);

			searchUserResponse	= restTemplate.getForObject(nextUrl,UserListResponse.class);
			
			allUsersByLocation.addAll(searchUserResponse.getItems());
			
			if (searchUserResponse.getItems().size() < max_page ) break; // no more next page						
		}
		
		
		return allUsersByLocation;
	}
	
	
	@Override
	public List<GitHubUser> getUsersFromCache(String city,int n) {
		
		List<GitHubUser> myList = cache.getUserByLocation(city);
		
		if (myList != null && myList.size() > n)
			return cache.getUserByLocation(city).subList(0, n);
		
		return myList;
	}
}
