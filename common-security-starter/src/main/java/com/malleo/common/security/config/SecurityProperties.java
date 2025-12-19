package com.malleo.common.security.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "malleo.security")
public class SecurityProperties {

	/**
	 * AccessToken을 담고 있는 HttpOnly Cookie 이름
	 */
	private String accessTokenCookieName = "accessToken";

	/**
	 * 인증 없이 허용할 경로들
	 * 예) /actuator/health, /swagger-ui/**, /v3/api-docs/**
	 */
	private List<String> permitAll = new ArrayList<>();

	/**
	 * CORS 설정
	 */
	private final Cors cors = new Cors();

	public String getAccessTokenCookieName() {
		return accessTokenCookieName;
	}

	public void setAccessTokenCookieName(String accessTokenCookieName) {
		this.accessTokenCookieName = accessTokenCookieName;
	}

	public List<String> getPermitAll() {
		return permitAll;
	}

	public void setPermitAll(List<String> permitAll) {
		this.permitAll = permitAll;
	}

	public Cors getCors() {
		return cors;
	}

	public static class Cors {

		private List<String> allowedOrigins = new ArrayList<>();
		private List<String> allowedMethods = new ArrayList<>(List.of("GET", "POST", "PUT",
			"PATCH", "DELETE", "OPTIONS"));
		private List<String> allowedHeaders = new ArrayList<>(List.of("*"));
		private List<String> exposedHeaders = new ArrayList<>(List.of("X-Trace-Id"));
		private boolean allowCredentials = true;
		private Long maxAge = 3600L;

		public List<String> getAllowedOrigins() {
			return allowedOrigins;
		}

		public void setAllowedOrigins(List<String> allowedOrigins) {
			this.allowedOrigins = allowedOrigins;
		}

		public List<String> getAllowedMethods() {
			return allowedMethods;
		}

		public void setAllowedMethods(List<String> allowedMethods) {
			this.allowedMethods = allowedMethods;
		}

		public List<String> getAllowedHeaders() {
			return allowedHeaders;
		}

		public void setAllowedHeaders(List<String> allowedHeaders) {
			this.allowedHeaders = allowedHeaders;
		}

		public List<String> getExposedHeaders() {
			return exposedHeaders;
		}

		public void setExposedHeaders(List<String> exposedHeaders) {
			this.exposedHeaders = exposedHeaders;
		}

		public boolean isAllowCredentials() {
			return allowCredentials;
		}

		public void setAllowCredentials(boolean allowCredentials) {
			this.allowCredentials = allowCredentials;
		}

		public Long getMaxAge() {
			return maxAge;
		}

		public void setMaxAge(Long maxAge) {
			this.maxAge = maxAge;
		}
	}
}
