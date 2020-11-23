package com.codetest.githubuser.util;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.codetest.githubuser.requests.GitHubUser;

@Component
public class ConcurrentWorker {

	private RestTemplate restTemplate;
	
	Logger logger = LoggerFactory.getLogger(ConcurrentWorker.class);
	
	@Autowired
	public ConcurrentWorker(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	
	
	@Async("asynchThreadPoolTaskExecutor")
	public  CompletableFuture<GitHubUser>  asynchJob(final String user)  {
		logger.info(" Job started " + new Date() + "thread id: " + Thread.currentThread().getName());
	

		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("https://api.github.com/users/");
		urlBuilder.append(user);

		GitHubUser aGitHubUser = restTemplate.getForObject(urlBuilder.toString(), GitHubUser.class);
		//aUser.setPublic_repos(aGitHubUser.getPublic_repos());
		
		logger.info(" Job finished " + new Date() + "thread id: " +  urlBuilder.toString() + " # of repo fetched " + aGitHubUser.getPublic_repos());

		return new AsyncResult<>(aGitHubUser).completable(); 
	}
	
}


