package com.malleo.common.security.handler;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final String TRACE_ID_HEADER = "X-Trace-Id";

	private final ObjectMapper objectMapper;

	public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException
	) throws IOException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", OffsetDateTime.now().toString());
		body.put("status", 401);
		body.put("error", "UNAUTHORIZED");
		body.put("message", "Authentication is required.");
		body.put("path", request.getRequestURI());
		body.put("traceId", request.getHeader(TRACE_ID_HEADER));

		objectMapper.writeValue(response.getWriter(), body);
	}
}
