package com.malleo.common.redis.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.malleo.common.redis.support.RedisKeyGenerator;
import com.malleo.common.redis.support.RedisTtlSupport;

@AutoConfiguration
@ConditionalOnClass(RedisTemplate.class)
@EnableConfigurationProperties(RedisProperties.class)
public class CommonRedisAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public RedisKeyGenerator redisKeyGenerator(RedisProperties props) {
		return new RedisKeyGenerator(props.keyPrefix());
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisTtlSupport redisTtlSupport(RedisProperties props) {
		return new RedisTtlSupport(props.defaultTtl());
	}

	@Bean
	@ConditionalOnMissingBean
	public StringRedisTemplate stringRedisTemplate(
		RedisConnectionFactory connectionFactory
	) {
		return new StringRedisTemplate(connectionFactory);
	}

	@Bean
	@ConditionalOnMissingBean(name = "byteArrayRedisTemplate")
	public RedisTemplate<String, byte[]> byteArrayRedisTemplate(
		RedisConnectionFactory connectionFactory
	) {
		RedisTemplate<String, byte[]> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(RedisSerializer.byteArray());

		template.afterPropertiesSet();
		return template;
	}
}
