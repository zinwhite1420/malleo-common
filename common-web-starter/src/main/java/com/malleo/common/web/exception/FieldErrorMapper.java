package com.malleo.common.web.exception;

import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.malleo.common.core.error.FieldError;

public final class FieldErrorMapper {
	private FieldErrorMapper() {
	}

	public static List<FieldError> from(Exception ex) {
		if (ex instanceof MethodArgumentNotValidException manv) {
			return manv.getBindingResult().getFieldErrors().stream()
				.map(fe -> new FieldError(fe.getField(), fe.getDefaultMessage()))
				.toList();
		}
		if (ex instanceof BindException bind) {
			return bind.getBindingResult().getFieldErrors().stream()
				.map(fe -> new FieldError(fe.getField(), fe.getDefaultMessage()))
				.toList();
		}
		return List.of();
	}
}