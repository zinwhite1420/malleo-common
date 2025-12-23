package com.malleo.common.openapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "malleo.openapi")
public record OpenApiProperties(
	String title,
	String description,
	String version,
	boolean securityEnabled
) {
	public OpenApiProperties {
		if (title == null || title.isBlank()) title = "Malleo API";
		if (description == null) description = "";
		if (version == null || version.isBlank()) version = "v1";
		// 기본은 true 권장 (JWT 쓰는 구조라면)
		// 단, 서비스마다 공개 API가 있으면 서비스에서 false로 내릴 수 있게
	}
}
