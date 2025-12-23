package com.malleo.common.openapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "malleo.openapi")
public record OpenApiProperties(
	String title,
	String description,
	String version,
	Boolean securityEnabled
) {
	public OpenApiProperties {
		if (title == null || title.isBlank()) title = "Malleo API";
		if (description == null) description = "";
		if (version == null || version.isBlank()) version = "v1";
		if (securityEnabled == null) securityEnabled = Boolean.TRUE; // ✅ 기본값 true
	}
}
