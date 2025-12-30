package com.malleo.common.redis.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "malleo.redis")
public record RedisProperties(
	String keyPrefix,
	Duration defaultTtl
) {
	public RedisProperties {
		if (keyPrefix == null || keyPrefix.isBlank()) {
			keyPrefix = "malleo";
		}
		if (defaultTtl == null) {
			defaultTtl = Duration.ofMinutes(30);
		}
	}
}
