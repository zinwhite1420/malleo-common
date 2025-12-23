package com.malleo.common.openapi.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class CommonOpenApiAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(CommonOpenApiAutoConfiguration.class))
		.withPropertyValues(
			"malleo.openapi.title=Test API",
			"malleo.openapi.description=desc",
			"malleo.openapi.version=v1",
			"malleo.openapi.security-enabled=false"
		);
	@Test
	void openApiBean_isCreated_withDefaultProperties() {
		contextRunner.run(context -> {
			assertThat(context).hasSingleBean(io.swagger.v3.oas.models.OpenAPI.class);
			var openApi = context.getBean(io.swagger.v3.oas.models.OpenAPI.class);
			assertThat(openApi).isNotNull();
			assertThat(openApi.getInfo().getTitle()).isEqualTo("Test API");
		});
	}

	@Test
	void openApiBean_hasSecurity_whenEnabled() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(CommonOpenApiAutoConfiguration.class))
			.withPropertyValues(
				"malleo.openapi.title=Test API",
				"malleo.openapi.description=desc",
				"malleo.openapi.version=v1",
				"malleo.openapi.security-enabled=true"
			)
			.run(context -> {
				var openApi = context.getBean(io.swagger.v3.oas.models.OpenAPI.class);
				assertThat(openApi.getComponents().getSecuritySchemes()).containsKey("bearerAuth");
			});
	}
}
