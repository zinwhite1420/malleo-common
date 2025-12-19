package com.malleo.common.security.config;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malleo.common.security.filter.JwtAuthenticationFilter;
import com.malleo.common.security.handler.RestAccessDeniedHandler;
import com.malleo.common.security.handler.RestAuthenticationEntryPoint;
import com.malleo.common.security.jwt.AccessTokenResolver;
import com.malleo.common.security.jwt.CookieAccessTokenResolver;
import com.malleo.common.security.jwt.DefaultJwtVerifier;
import com.malleo.common.security.jwt.JwtVerifier;

@AutoConfiguration
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({SecurityFilterChain.class, HttpSecurity.class})
@EnableConfigurationProperties(SecurityProperties.class)
public class CommonSecurityAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(AccessTokenResolver.class)
	public AccessTokenResolver accessTokenResolver(SecurityProperties properties) {
		return new CookieAccessTokenResolver(properties.getAccessTokenCookieName());
	}

	@Bean
	@ConditionalOnMissingBean(RestAuthenticationEntryPoint.class)
	public RestAuthenticationEntryPoint authenticationEntryPoint(
		ObjectProvider<ObjectMapper> objectMapperProvider
	) {
		return new RestAuthenticationEntryPoint(
			objectMapperProvider.getIfAvailable(ObjectMapper::new)
		);
	}

	@Bean
	@ConditionalOnMissingBean(RestAccessDeniedHandler.class)
	public RestAccessDeniedHandler accessDeniedHandler(
		ObjectProvider<ObjectMapper> objectMapperProvider
	) {
		return new RestAccessDeniedHandler(
			objectMapperProvider.getIfAvailable(ObjectMapper::new)
		);
	}

	@Bean
	@ConditionalOnMissingBean(JwtVerifier.class)
	public JwtVerifier jwtVerifier() {
		return new DefaultJwtVerifier();
	}

	@Bean
	@ConditionalOnMissingBean(JwtAuthenticationFilter.class)
	public JwtAuthenticationFilter jwtAuthenticationFilter(
		AccessTokenResolver accessTokenResolver,
		JwtVerifier jwtVerifier
	) {
		return new JwtAuthenticationFilter(accessTokenResolver, jwtVerifier);
	}

	@Bean
	@ConditionalOnMissingBean(CorsConfigurationSource.class)
	public CorsConfigurationSource corsConfigurationSource(SecurityProperties properties) {
		SecurityProperties.Cors corsProps = properties.getCors();

		CorsConfiguration config = new CorsConfiguration();

		// allowCredentials=true 인 경우 allowedOrigins에 "*"는 불가하니, 값이 비어있으면 CORS를 사실상 열지 않습니다.
		if (corsProps.getAllowedOrigins() != null && !corsProps.getAllowedOrigins().isEmpty()) {
			config.setAllowedOrigins(corsProps.getAllowedOrigins());
		}

		config.setAllowedMethods(corsProps.getAllowedMethods());
		config.setAllowedHeaders(corsProps.getAllowedHeaders());
		config.setExposedHeaders(corsProps.getExposedHeaders());
		config.setAllowCredentials(corsProps.isAllowCredentials());
		config.setMaxAge(corsProps.getMaxAge());

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Bean
	@ConditionalOnMissingBean(SecurityFilterChain.class)
	public SecurityFilterChain securityFilterChain(
		HttpSecurity http,
		SecurityProperties properties,
		JwtAuthenticationFilter jwtAuthenticationFilter,
		RestAuthenticationEntryPoint authenticationEntryPoint,
		RestAccessDeniedHandler accessDeniedHandler
	) throws Exception {

		List<String> permitAll = properties.getPermitAll();

		http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler)
			)
			.authorizeHttpRequests(auth -> {
				if (permitAll != null && !permitAll.isEmpty()) {
					for (String pattern : permitAll) {
						auth.requestMatchers(pattern).permitAll();
					}
				}
				auth.anyRequest().authenticated();
			})
			.cors(Customizer.withDefaults())
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
