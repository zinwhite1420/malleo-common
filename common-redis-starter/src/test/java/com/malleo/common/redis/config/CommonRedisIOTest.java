package com.malleo.common.redis.config;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.malleo.common.redis.support.RedisKeyGenerator;
import com.malleo.common.redis.test.RedisTestContainer;

class CommonRedisIOTest {

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
	void stringRedisTemplate_canReadWrite_withTtl() {
		contextRunner.run(ctx -> {
			StringRedisTemplate redis = ctx.getBean(StringRedisTemplate.class);
			RedisKeyGenerator keys = ctx.getBean(RedisKeyGenerator.class);

			String key = keys.key("test", "string");
			redis.opsForValue().set(key, "hello", Duration.ofSeconds(5));

			String v = redis.opsForValue().get(key);
			assertThat(v).isEqualTo("hello");

			Long ttl = redis.getExpire(key);
			assertThat(ttl).isNotNull();
			assertThat(ttl).isGreaterThan(0);
		});
	}

	@SuppressWarnings("unchecked")
	@Test
	void byteArrayRedisTemplate_canReadWrite() {
		contextRunner.run(ctx -> {
			RedisTemplate<String, byte[]> bytesTemplate =
				(RedisTemplate<String, byte[]>) ctx.getBean("byteArrayRedisTemplate");

			RedisKeyGenerator keys = ctx.getBean(RedisKeyGenerator.class);

			String key = keys.key("test", "bytes");
			byte[] payload = "hello-bytes".getBytes(StandardCharsets.UTF_8);

			bytesTemplate.opsForValue().set(key, payload, Duration.ofSeconds(5));

			byte[] read = bytesTemplate.opsForValue().get(key);
			assertThat(read).isNotNull();
			assertThat(new String(read, StandardCharsets.UTF_8)).isEqualTo("hello-bytes");
		});
	}
}
