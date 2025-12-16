package com.malleo.common.core.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.malleo.common.core.error.CommonErrorCode;
import com.malleo.common.core.error.ErrorCode;

class BusinessExceptionTest {

	@Test
	@DisplayName("ErrorCode만으로 생성하면 message는 ErrorCode.message(), details는 null이다")
	void constructor_withErrorCode_shouldSetMessageAndNullDetails() {
		// given
		ErrorCode errorCode = CommonErrorCode.INVALID_REQUEST;

		// when
		BusinessException ex = new BusinessException(errorCode);

		// then
		assertSame(errorCode, ex.getErrorCode());
		assertEquals(errorCode.message(), ex.getMessage());
		assertNull(ex.getDetails());
	}

	@Test
	@DisplayName("ErrorCode + details로 생성하면 details를 그대로 보관한다")
	void constructor_withErrorCodeAndDetails_shouldKeepDetails() {
		// given
		ErrorCode errorCode = CommonErrorCode.VALIDATION_FAILED;
		Map<String, Object> details = Map.of(
			"field", "email",
			"reason", "invalid format"
		);

		// when
		BusinessException ex = new BusinessException(errorCode, details);

		// then
		assertSame(errorCode, ex.getErrorCode());
		assertEquals(errorCode.message(), ex.getMessage());
		assertNotNull(ex.getDetails());
		assertEquals("email", ex.getDetails().get("field"));
		assertEquals("invalid format", ex.getDetails().get("reason"));
	}

	@Test
	@DisplayName("ErrorCode가 null이면 예외를 던진다(권장)")
	void constructor_withNullErrorCode_shouldThrow() {
		// when & then
		assertThrows(NullPointerException.class, () -> new BusinessException(null));
	}


}