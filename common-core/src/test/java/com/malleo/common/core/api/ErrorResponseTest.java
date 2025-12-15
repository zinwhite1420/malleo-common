package com.malleo.common.core.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {

	@Test
	@DisplayName("of(code, message)는 details=null 생성")
	void of_withoutDetails_shouldSetDetailsNull() {
		// when
		ErrorResponse err = ErrorResponse.of("C-404", "Not found");

		// then
		assertEquals("C-404", err.code());
		assertEquals("Not found", err.message());
		assertNull(err.details());
	}

	@Test
	@DisplayName("of(code, message, details)는 details 그대로 보존")
	void of_withDetails_shouldKeepDetails() {
		// given
		Map<String, Object> details = Map.of(
			"field", "name",
			"reason", "blank"
		);

		// when
		ErrorResponse err = ErrorResponse.of("C-400", "Invalid request", details);

		// then
		assertEquals("C-400", err.code());
		assertEquals("Invalid request", err.message());
		assertNotNull(err.details());
		assertEquals("name", err.details().get("field"));
		assertEquals("blank", err.details().get("reason"));
	}
}
