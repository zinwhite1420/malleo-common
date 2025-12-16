package com.malleo.common.core.exception;

import java.util.Map;

import com.malleo.common.core.error.ErrorCode;

public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;
	private final Map<String, Object> details;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.message());
		this.errorCode = errorCode;
		this.details = null;
	}

	public BusinessException(ErrorCode errorCode, Map<String, Object> details) {
		super(errorCode.message());
		this.errorCode = errorCode;
		this.details = details;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public Map<String, Object> getDetails() {
		return details;
	}
}
