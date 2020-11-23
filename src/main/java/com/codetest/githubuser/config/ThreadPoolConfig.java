package com.codetest.githubuser.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class ThreadPoolConfig  {

	@Bean(name = "asynchThreadPoolTaskExecutor")
	@Qualifier("asynchThreadPoolTaskExecutor")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(100);
		threadPoolTaskExecutor.setMaxPoolSize(200);
		threadPoolTaskExecutor.setThreadNamePrefix("asynchThread-");
		threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}

	
	
}