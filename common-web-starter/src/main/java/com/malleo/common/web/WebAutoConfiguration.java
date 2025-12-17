package com.malleo.common.web;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import com.malleo.common.web.exception.GlobalExceptionHandler;
import com.malleo.common.web.trace.TraceIdFilter;

@AutoConfiguration
@ConditionalOnWebApplication
public class WebAutoConfiguration {

	@Bean
	public FilterRegistrationBean<@NonNull TraceIdFilter> traceIdFilter() {
		var bean = new FilterRegistrationBean<>(new TraceIdFilter());
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	// @RestControllerAdvice도 "빈으로 등록"되면 정상 동작
	@Bean
	public GlobalExceptionHandler globalExceptionHandler() {
		return new GlobalExceptionHandler();
	}
}
