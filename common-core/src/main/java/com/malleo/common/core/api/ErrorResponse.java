package com.malleo.common.core.api;

import java.util.Map;

public record ErrorResponse(
	String code,
	String message,
	Map<String, Object> details
) {
	public ErrorResponse of(String code, String message) {
		return new ErrorResponse(code, message, null);
	}

	public ErrorResponse of(String code, String message, Map<String, Object> details) {
		return new ErrorResponse(code, message, details);
	}
}
