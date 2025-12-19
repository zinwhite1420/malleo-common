package com.malleo.common.security.handler;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RestAccessDeniedHandler implements AccessDeniedHandler {

	private static final String TRACE_ID_HEADER = "X-Trace-Id";

	private final ObjectMapper objectMapper;

	public RestAccessDeniedHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void handle(
		HttpServletRequest request,
		HttpServletResponse response,
		AccessDeniedException accessDeniedException
	) throws IOException {

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", OffsetDateTime.now().toString());
		body.put("status", 403);
		body.put("error", "FORBIDDEN");
		body.put("message", "Access is denied.");
		body.put("path", request.getRequestURI());
		body.put("traceId", request.getHeader(TRACE_ID_HEADER));

		objectMapper.writeValue(response.getWriter(), body);
	}
}
