package com.malleo.common.redis.support;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RedisKeyGeneratorTest {

	@Test
	void key_isPrefixedAndJoined() {
		RedisKeyGenerator gen = new RedisKeyGenerator("malleo");
		assertThat(gen.key("auth", "login_fail", "user"))
			.isEqualTo("malleo:auth:login_fail:user");
	}

	@Test
	void blanks_areSkipped() {
		RedisKeyGenerator gen = new RedisKeyGenerator("malleo");
		assertThat(gen.key("auth", "", "user"))
			.isEqualTo("malleo:auth:user");
	}
}
