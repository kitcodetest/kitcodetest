package com.codetest.githubuser.requests;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitHubUser implements Comparable<GitHubUser> {

	private String login = null;
	private String repos_url = null;
	private int public_repos;

	
	public int getPublic_repos() {
		return public_repos;
	}
	public void setPublic_repos(int public_repos) {
		this.public_repos = public_repos;
	}
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	
	public String getRepos_url() {
		return repos_url;
	}
	public void setRepos_url(String repos_url) {
		this.repos_url = repos_url;
	}

	@Override
	public int compareTo(GitHubUser o) {
		// TODO Auto-generated method stub
		return this.public_repos > o.public_repos ? 1 : this.public_repos < o.public_repos ? -1 : 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GitHubUser)) return false;
		GitHubUser that = (GitHubUser) o;
		return getPublic_repos() == that.getPublic_repos() &&
				Objects.equals(getLogin(), that.getLogin()) &&
				Objects.equals(getRepos_url(), that.getRepos_url());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getLogin(), getRepos_url(), getPublic_repos());
	}


}
