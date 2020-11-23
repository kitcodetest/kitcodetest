package com.codetest.githubuser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.codetest.githubuser.controller.GitHubRestController;
import com.codetest.githubuser.requests.GitHubUser;
import com.codetest.githubuser.requests.UserRepoResponse;
import com.codetest.githubuser.service.GitHubUserService;

public class GitHuRestControllerTest {

	@Mock
	GitHubUserService gitHubUserServiceMock;

	@InjectMocks
	GitHubRestController gitHubRestController;

	ArrayList<GitHubUser> userList = new ArrayList<>();
	
	@BeforeEach
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		
		for (int i =0; i< 50; i++) {
			userList.add(new GitHubUser());
		}
	}
	
	@Test
	public void when_wrong_topN_then_return_unauthroized() {

				
		ResponseEntity<UserRepoResponse> httpResponse = gitHubRestController.getTopUsers("NY", 10);
				
		
		assertEquals(httpResponse.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	
	@Test
	public void when_correct_topN_then_return_GitHubUsers() {
		
			
		when(gitHubUserServiceMock.getUsersFromCache("NY",50)).thenReturn(userList);

		ResponseEntity<UserRepoResponse> httpResponse = gitHubRestController.getTopUsers("NY", 50);
						
		
		assertEquals(httpResponse.getBody().getUsers().size(), userList.size());
	}
	

}

