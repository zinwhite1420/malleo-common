package com.malleo.common.redis.support;

import java.util.StringJoiner;

public class RedisKeyGenerator {

	private final String prefix;

	public RedisKeyGenerator(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 예: prefix=malleo, key("auth", "login_fail", "user@example.com")
	 * -> malleo:auth:login_fail:user@example.com
	 */
	public String key(String... parts) {
		StringJoiner sj = new StringJoiner(":");
		sj.add(prefix);
		if (parts != null) {
			for (String p : parts) {
				if (p != null && !p.isBlank()) {
					sj.add(p);
				}
			}
		}
		return sj.toString();
	}
}
