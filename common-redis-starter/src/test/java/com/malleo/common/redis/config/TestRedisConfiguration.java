package com.malleo.common.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import com.malleo.common.redis.test.RedisTestContainer;

@Configuration
class TestRedisConfiguration {

	@Bean
	RedisConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory factory =
			new LettuceConnectionFactory(RedisTestContainer.host(), RedisTestContainer.port());
		factory.afterPropertiesSet();
		return factory;
	}
}
