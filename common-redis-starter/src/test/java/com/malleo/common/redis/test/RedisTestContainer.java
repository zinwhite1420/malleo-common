package com.malleo.common.redis.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public final class RedisTestContainer {

	private static final DockerImageName REDIS_IMAGE =
		DockerImageName.parse("redis:7.4-alpine");

	private static final GenericContainer<?> REDIS =
		new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379);

	private RedisTestContainer() {
	}

	public static void startIfNeeded() {
		if (!REDIS.isRunning()) {
			REDIS.start();
		}
	}

	public static String host() {
		return REDIS.getHost();
	}

	public static int port() {
		return REDIS.getMappedPort(6379);
	}
}
