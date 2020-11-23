package com.codetest.githubuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class GitHubUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitHubUsersApplication.class, args);
	}

}
