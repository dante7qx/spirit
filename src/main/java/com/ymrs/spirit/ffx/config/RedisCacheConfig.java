package com.ymrs.spirit.ffx.config;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {
	
	@Autowired
	private RedisConnectionFactory connectionFactory;

	/**
	 * 缓存管理器
	 */
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//		RedisCacheManager cacheManager = new RedisCacheManager(cacheRedisTemplate());
//		return cacheManager;
		return RedisCacheManager.create(connectionFactory);
	}
	
	@Bean(name = "redisTemplate")
	RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
		return redisTemplate;
	}

	@Bean(name = "cacheRedisTemplate")
	public RedisTemplate<String, String> cacheRedisTemplate() {
		RedisTemplate<String, String> template = new RedisTemplate<String, String>();
		// 设置redis连接Factory
		template.setConnectionFactory(connectionFactory);
		// Redis value 序列化
		Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);  
        ObjectMapper om = new ObjectMapper();  
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);  
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);  
        jackson2JsonRedisSerializer.setObjectMapper(om);  
        template.setValueSerializer(jackson2JsonRedisSerializer); 
        // Redis key 序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();  
		return template;
	}

	/**
	 * 缓存 Key 的生成策略，该参数与key是互斥的
	 * 
	 */
	@Override
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
	           @Override
	           public Object generate(Object o, Method method, Object... objects) {
	              StringBuilder sb = new StringBuilder();
	              sb.append(o.getClass().getName());
	              sb.append(method.getName());
	              for (Object obj : objects) {
	                  sb.append(obj.toString());
	              }
	              return sb.toString();
	           }
	       };
	}

	/**
	 * 缓存解析器
	 */
	@Override
	public CacheResolver cacheResolver() {
		return super.cacheResolver();
	}

	@Override
	public CacheErrorHandler errorHandler() {
		return super.errorHandler();
	}

}
