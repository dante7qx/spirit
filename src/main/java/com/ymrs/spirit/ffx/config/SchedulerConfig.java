package com.ymrs.spirit.ffx.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.ymrs.spirit.ffx.scheduler.factory.SpiritJobFactory;
import com.ymrs.spirit.ffx.scheduler.listener.SpiritJobListener;

@Configuration
public class SchedulerConfig {
	
	@Autowired
	@Qualifier("spiritJobFactory")
    private SpiritJobFactory spiritJobFactory;
	
	@Bean(name="spiritSchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(quartzProperties());
        factory.setOverwriteExistingJobs(true);
        factory.setJobFactory(spiritJobFactory);
        return factory;
    }
	
	@Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
        propertiesFactory.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactory.afterPropertiesSet();
        return propertiesFactory.getObject();
    }
	
	@Bean
	public SpiritJobListener spiritJobListener() {
		return new SpiritJobListener();
	}
	
}
