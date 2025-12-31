package com.malleo.common.redis.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.malleo.common.redis.support.RedisKeyGenerator;
import com.malleo.common.redis.support.RedisTtlSupport;
import com.malleo.common.redis.test.RedisTestContainer;

class CommonRedisAutoConfigurationTest {

	@BeforeAll
	static void startRedis() {
		RedisTestContainer.startIfNeeded();
	}

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(CommonRedisAutoConfiguration.class))
		.withUserConfiguration(TestRedisConfiguration.class)
		.withPropertyValues(
			"malleo.redis.key-prefix=malleo",
			"malleo.redis.default-ttl=30m"
		);

	@Test
	void beans_areCreated() {
		contextRunner.run(ctx -> {
			assertThat(ctx).hasSingleBean(StringRedisTemplate.class);
			assertThat(ctx).hasSingleBean(RedisKeyGenerator.class);
			assertThat(ctx).hasSingleBean(RedisTtlSupport.class);

			// byteArrayRedisTemplate는 이름으로 검증
			assertThat(ctx).hasBean("byteArrayRedisTemplate");
		});
	}
}
