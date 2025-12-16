package com.malleo.common.core.error;

public enum CommonErrorCode implements ErrorCode {

	// 400
	INVALID_REQUEST("C-400", "Invalid request", 400),
	VALIDATION_FAILED("C-400-VALID", "Validation failed", 400),

	// 401 / 403
	UNAUTHORIZED("C-401", "Unauthorized", 401),
	FORBIDDEN("C-403", "Forbidden", 403),

	// 404
	NOT_FOUND("C-404", "Resource not found", 404),

	// 409
	CONFLICT("C-409", "Conflict", 409),

	// 500
	INTERNAL_ERROR("C-500", "Internal server error", 500);

	private final String code;
	private final String message;
	private final int httpStatus;

	CommonErrorCode(String code, String message, int httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}

	@Override
	public String code() {
		return code;
	}

	@Override
	public String message() {
		return message;
	}

	@Override
	public int httpStatus() {
		return httpStatus;
	}
}
