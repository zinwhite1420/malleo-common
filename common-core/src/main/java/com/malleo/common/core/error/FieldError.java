package com.malleo.common.core.error;

public record FieldError(
	String field,
	String message
) {
}
