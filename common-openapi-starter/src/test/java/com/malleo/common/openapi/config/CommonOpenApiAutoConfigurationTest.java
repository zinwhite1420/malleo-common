package com.malleo.common.openapi.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;

class CommonOpenApiAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(CommonOpenApiAutoConfiguration.class));

	@Test
	void openApiBean_isCreated_withDefaultProperties() {
		contextRunner.run(context -> {
			assertThat(context).hasSingleBean(OpenAPI.class);

			OpenAPI openAPI = context.getBean(OpenAPI.class);
			assertThat(openAPI.getInfo()).isNotNull();
			assertThat(openAPI.getInfo().getTitle()).isEqualTo("Malleo API");
			assertThat(openAPI.getInfo().getVersion()).isEqualTo("v1");

			// securityEnabled 기본값이 true라면 bearerAuth 구성 확인
			assertThat(openAPI.getComponents()).isNotNull();
			assertThat(openAPI.getComponents().getSecuritySchemes()).containsKey("bearerAuth");

			SecurityScheme scheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
			assertThat(scheme.getType()).isEqualTo(SecurityScheme.Type.HTTP);
			assertThat(scheme.getScheme()).isEqualTo("bearer");
			assertThat(scheme.getBearerFormat()).isEqualTo("JWT");
		});
	}

	@Test
	void whenSecurityDisabled_securitySchemeIsNotRegistered() {
		contextRunner
			.withPropertyValues("malleo.openapi.security-enabled=false")
			.run(context -> {
				OpenAPI openAPI = context.getBean(OpenAPI.class);

				// security 비활성 시 components/security가 없어도 정상
				assertThat(openAPI.getComponents()).isNull();
				assertThat(openAPI.getSecurity()).isNullOrEmpty();
			});
	}

}
