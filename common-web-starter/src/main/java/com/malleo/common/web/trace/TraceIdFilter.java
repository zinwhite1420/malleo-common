package com.malleo.common.web.trace;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TraceIdFilter extends OncePerRequestFilter {

	public static final String TRACE_ID = "traceId";
	public static final String HEADER = "X-Trace-Id";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws ServletException, IOException {

		String incoming = request.getHeader(HEADER);
		String traceId = (incoming != null && !incoming.isBlank())
			? incoming
			: UUID.randomUUID().toString();

		MDC.put(TRACE_ID, traceId);
		response.setHeader(HEADER, traceId);

		try {
			chain.doFilter(request, response);
		} finally {
			MDC.remove(TRACE_ID);
		}
	}
}
