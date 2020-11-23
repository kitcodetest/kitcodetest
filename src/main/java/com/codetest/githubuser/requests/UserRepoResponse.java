package com.codetest.githubuser.requests;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserRepoResponse {

	String status = "";
	String message = "";
	List<GitHubUser> users = Collections.EMPTY_LIST;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<GitHubUser> getUsers() {
		return users;
	}
	public void setUsers(List<GitHubUser> users) {
		this.users = users;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRepoResponse)) return false;
        UserRepoResponse that = (UserRepoResponse) o;
        return Objects.equals(getStatus(), that.getStatus()) &&
                Objects.equals(getMessage(), that.getMessage()) &&
                Objects.equals(getUsers(), that.getUsers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getMessage(), getUsers());
    }
}
