package com.malleo.common.openapi.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@AutoConfiguration
@ConditionalOnClass(OpenAPI.class)
@EnableConfigurationProperties(OpenApiProperties.class)
public class CommonOpenApiAutoConfiguration {

	private static final String BEARER_AUTH = "bearerAuth";

	@Bean
	@ConditionalOnMissingBean(OpenAPI.class)
	public OpenAPI commonOpenApi(OpenApiProperties props) {

		OpenAPI openAPI = new OpenAPI()
			.info(new Info()
				.title(props.title())
				.description(props.description())
				.version(props.version())
			);

		if (Boolean.TRUE.equals(props.securityEnabled())) {
			openAPI
				.addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
				.components(new Components().addSecuritySchemes(
					BEARER_AUTH,
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
				));
		}


		return openAPI;
	}
}
