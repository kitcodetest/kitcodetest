package com.codetest.githubuser;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.client.RestTemplate;

import com.codetest.githubuser.requests.GitHubUser;
import com.codetest.githubuser.service.impl.GitHubUserServiceImpl;
import com.codetest.githubuser.util.CacheService;
import com.codetest.githubuser.util.ConcurrentWorker;
public class GitHubUserServiceTest {


	@Mock
	CacheService cacheMock;
	
	@Mock
	RestTemplate restTemplateMock;
	
	@Mock
	ConcurrentWorker concurrentWorkerMock;
	
    @Spy
	@InjectMocks
	GitHubUserServiceImpl gitHubUserServiceMock;
	
	ArrayList<GitHubUser> userList = new ArrayList<>();
	
	@BeforeEach
	public void setup() {
		
		MockitoAnnotations.initMocks(this);		
		
	}
	
	@Test
	public void when_asyncSearch_invoked_then_search_result_is_cached() {

		CompletableFuture<GitHubUser> futureMock = mock(CompletableFuture.class);	
				
		when(concurrentWorkerMock.asynchJob(anyString())).thenReturn(futureMock);
		
		Mockito.doReturn(userList).when(gitHubUserServiceMock).searchUserByLocation("NY");
		
		gitHubUserServiceMock.asyncDownLoadUserByLocation("NY");
		
		verify(cacheMock, times(1)).cacheUserByLocation("NY",userList);
	}


}

