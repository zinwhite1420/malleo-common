package com.malleo.common.redis.support;

import java.time.Duration;

public class RedisTtlSupport {

	private final Duration defaultTtl;

	public RedisTtlSupport(Duration defaultTtl) {
		this.defaultTtl = defaultTtl;
	}

	public Duration defaultTtl() {
		return defaultTtl;
	}

	public Duration ofSeconds(long seconds) {
		return Duration.ofSeconds(seconds);
	}

	public Duration ofMinutes(long minutes) {
		return Duration.ofMinutes(minutes);
	}

	public Duration ofHours(long hours) {
		return Duration.ofHours(hours);
	}
}
