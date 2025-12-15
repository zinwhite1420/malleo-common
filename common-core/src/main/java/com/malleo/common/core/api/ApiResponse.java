package com.malleo.common.core.api;

public record ApiResponse<T>(
	boolean isSuccess,
	T data,
	ErrorResponse error,
	String traceId
) {
	public static <T> ApiResponse<T> ok(T data, String traceId) {
		return new ApiResponse<>(true, data, null, traceId);
	}

	public static <T> ApiResponse<T> ok(String traceId) {
		return new ApiResponse<>(true, null, null, traceId);
	}

	public static <T> ApiResponse<T> fail(ErrorResponse error, String traceId) {
		return new ApiResponse<>(false, null, error, traceId);
	}
}
