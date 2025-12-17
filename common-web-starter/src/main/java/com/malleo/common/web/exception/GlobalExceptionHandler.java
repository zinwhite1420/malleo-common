package com.malleo.common.web.exception;

import static com.malleo.common.web.trace.TraceIdFilter.*;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.malleo.common.core.api.ApiResponse;
import com.malleo.common.core.api.ErrorResponse;
import com.malleo.common.core.error.CommonErrorCode;
import com.malleo.common.core.exception.BusinessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
		var code = ex.getErrorCode();
		var error = ErrorResponse.from(code, ex.getDetails());
		return ResponseEntity.status(code.httpStatus())
			.body(ApiResponse.fail(error, traceId()));
	}

	@ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
	public ResponseEntity<ApiResponse<Void>> handleValidation(Exception ex) {
		var fields = FieldErrorMapper.from(ex);

		var code = CommonErrorCode.VALIDATION_FAILED;

		// 핵심: ErrorResponse.details는 Map이므로 "fields" 키로 감싼다
		var error = ErrorResponse.from(code, Map.of("fields", fields));

		return ResponseEntity.status(code.httpStatus())
			.body(ApiResponse.fail(error, traceId()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception ignoredEx) {
		var code = CommonErrorCode.INTERNAL_ERROR;
		var error = ErrorResponse.from(code);
		return ResponseEntity.status(code.httpStatus())
			.body(ApiResponse.fail(error, traceId()));
	}

	private String traceId() {
		var id = MDC.get(TRACE_ID);
		return (id != null && !id.isBlank()) ? id : "unknown";
	}
}
