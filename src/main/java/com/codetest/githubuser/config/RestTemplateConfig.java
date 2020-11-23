package com.codetest.githubuser.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	 
	    return builder
	            .setConnectTimeout(Duration.ofMillis(9000))
	            .setReadTimeout(Duration.ofMillis(9000))
	            .basicAuthentication("kitcodetest", "951ad88681cfcf457688760e28c5918d1525700f")
	            .build();
	}
	
}
