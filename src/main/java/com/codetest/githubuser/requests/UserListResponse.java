package com.codetest.githubuser.requests;

import java.util.ArrayList;
import java.util.List;

public class UserListResponse {
	
	int total_count = 0;
	
	List<GitHubUser> items = new ArrayList<>();
	
	public List<GitHubUser> getItems() {
		return items;
	}

	public void setItems(List<GitHubUser> items) {
		this.items = items;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	
	
	
}
