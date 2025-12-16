package com.malleo.common.core.api;

import java.util.Map;

import com.malleo.common.core.error.ErrorCode;

public record ErrorResponse(
	String code,
	String message,
	Map<String, Object> details
) {
	public static ErrorResponse of(String code, String message) {
		return new ErrorResponse(code, message, null);
	}

	public static ErrorResponse of(String code, String message, Map<String, Object> details) {
		return new ErrorResponse(code, message, details);
	}

	public static ErrorResponse from(ErrorCode ec) {
		return new ErrorResponse(ec.code(), ec.message(), null);
	}

	public static ErrorResponse from(ErrorCode ec, Map<String, Object> details) {
		return new ErrorResponse(ec.code(), ec.message(), details);
	}
}
