package com.ymrs.spirit.ffx.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ExecutorConfig {

	private int corePoolSize = 10;
	private int maxPoolSize = 200;
	private int queueCapacity = 20;

	@Bean("syslogAsync")
	public Executor syslogAsync() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("SPIRIT_SYS_LOG_Executor-");
		executor.initialize();
		return executor;
	}


}
