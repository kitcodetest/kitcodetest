package com.codetest.githubuser.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.codetest.githubuser.requests.GitHubUser;
import com.codetest.githubuser.requests.UserRepoResponse;
import com.codetest.githubuser.service.GitHubUserService;



@RestController
public class GitHubRestController {
	
	private GitHubUserService gitHubUserService; 	
	
	@Autowired
	public GitHubRestController(GitHubUserService gitHubUserService) {
		this.gitHubUserService = gitHubUserService;
		
	}
	
	@GetMapping(value = "users/{location}/{topN}",			
			produces = {MediaType.APPLICATION_JSON_VALUE, "application/pretty+json"})
	public ResponseEntity<UserRepoResponse> getTopUsers(@PathVariable("location") String location, @PathVariable("topN") int top) {
	
		UserRepoResponse aResponse = new UserRepoResponse();
		List<GitHubUser> topUsersByRepoCount = gitHubUserService.getUsersFromCache(location,top);
		
		
		if (!Arrays.asList(50,100,150).contains(top))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		if (topUsersByRepoCount != null) { // Return data from cache
			aResponse.setStatus("SUCCESS");
			aResponse.setUsers(topUsersByRepoCount);
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("Top ").append(top).append(" user by repo count in ").append(location);
			
			aResponse.setMessage(strBuilder.toString());
		
		} else { // download users from GitHUB
			
			gitHubUserService.asyncDownLoadUserByLocation(location);
			aResponse.setStatus("RUNNING");
			aResponse.setMessage("Keep polling or freshing the same request for latest status. Result will be ready shortly  ");
									
		}
		
		return ResponseEntity.ok(aResponse);
		
	}
	
}
