package com.ymrs.spirit.ffx.config;

import java.util.List;

import javax.servlet.http.HttpSessionListener;

import org.springframework.context.annotation.Bean;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;

import com.google.common.collect.Lists;
import com.ymrs.spirit.ffx.constant.SpiritSessionConfigConsts;

@EnableRedisHttpSession(redisNamespace = SpiritSessionConfigConsts.REDIS_NAMESPACE, maxInactiveIntervalInSeconds = SpiritSessionConfigConsts.MAX_INACTIVE_INTERVAL_IN_SECONDS, redisFlushMode = RedisFlushMode.IMMEDIATE)
public class HttpSessionConfig {

	/**
	 * Spring-session 监听
	 * 
	 * @return
	 */
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new RedisHttpSessionEventPublisher();
	}

	@Bean
	public SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapter() {
		List<HttpSessionListener> listeners = Lists.newArrayList(httpSessionEventPublisher());
		return new SessionEventHttpSessionListenerAdapter(listeners);
	}

}
