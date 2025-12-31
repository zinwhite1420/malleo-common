package com.malleo.common.redis.support;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.Test;

class RedisTtlSupportTest {

	@Test
	void defaultTtl_isReturned() {
		RedisTtlSupport ttl = new RedisTtlSupport(Duration.ofMinutes(30));
		assertThat(ttl.defaultTtl()).isEqualTo(Duration.ofMinutes(30));
	}

	@Test
	void ttl_helpers_work() {
		RedisTtlSupport ttl = new RedisTtlSupport(Duration.ofMinutes(30));
		assertThat(ttl.ofSeconds(10)).isEqualTo(Duration.ofSeconds(10));
		assertThat(ttl.ofMinutes(5)).isEqualTo(Duration.ofMinutes(5));
		assertThat(ttl.ofHours(2)).isEqualTo(Duration.ofHours(2));
	}
}
