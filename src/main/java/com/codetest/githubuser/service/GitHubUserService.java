package com.codetest.githubuser.service;

import java.util.List;

import com.codetest.githubuser.requests.GitHubUser;

public interface GitHubUserService {
	
	public void asyncDownLoadUserByLocation(String location);
	
	public List<GitHubUser> getUsersFromCache(String location, int n);
	
	public List<GitHubUser> searchUserByLocation(final String locaion); 

	
}
