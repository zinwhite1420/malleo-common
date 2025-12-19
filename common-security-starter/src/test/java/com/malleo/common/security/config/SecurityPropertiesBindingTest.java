package com.malleo.common.security.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

class SecurityPropertiesBindingTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withUserConfiguration(PropertiesTestConfig.class)
		.withPropertyValues(
			"malleo.security.access-token-cookie-name=accessToken",
			"malleo.security.permit-all[0]=/public",
			"malleo.security.cors.allowed-origins[0]=http://localhost:5173"
		);

	@Test
	@DisplayName("SecurityProperties가 malleo.security.* 설정으로 바인딩된다")
	void binds() {
		contextRunner.run(context -> {
			assertThat(context).hasSingleBean(SecurityProperties.class);

			SecurityProperties props = context.getBean(SecurityProperties.class);
			assertThat(props.getAccessTokenCookieName()).isEqualTo("accessToken");
			assertThat(props.getPermitAll()).contains("/public");
			assertThat(props.getCors().getAllowedOrigins()).contains("http://localhost:5173");
		});
	}

	@Configuration
	@EnableConfigurationProperties(SecurityProperties.class)
	static class PropertiesTestConfig {
	}
}
