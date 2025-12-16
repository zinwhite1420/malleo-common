package com.malleo.common.core.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

	@Test
	@DisplayName("ok(data, traceId) 호출 시 success=true, data 세팅, error=null, traceId 세팅")
	void ok_withData_shouldCreateSuccessResponse() {
		// given
		String traceId = "t-1";

		// when
		ApiResponse<String> res = ApiResponse.ok("hello", traceId);

		// then
		assertTrue(res.success());
		assertEquals("hello", res.data());
		assertNull(res.error());
		assertEquals(traceId, res.traceId());
	}

	@Test
	@DisplayName("ok(traceId) 호출 시 success=true, data=null, error=null, traceId 세팅")
	void ok_withoutData_shouldCreateSuccessResponse() {
		// given
		String traceId = "t-2";

		// when
		ApiResponse<Void> res = ApiResponse.ok(traceId);

		// then
		assertTrue(res.success());
		assertNull(res.data());
		assertNull(res.error());
		assertEquals(traceId, res.traceId());
	}

	@Test
	@DisplayName("fail(error, traceId) 호출 시 success=false, data=null, error 세팅, traceId 세팅")
	void fail_shouldCreateFailureResponse() {
		// given
		ErrorResponse err = ErrorResponse.of("C-400", "Invalid request");
		String traceId = "t-3";

		// when
		ApiResponse<Void> res = ApiResponse.fail(err, traceId);

		// then
		assertFalse(res.success());
		assertNull(res.data());
		assertNotNull(res.error());
		assertEquals("C-400", res.error().code());
		assertEquals("Invalid request", res.error().message());
		assertNull(res.error().details());
		assertEquals(traceId, res.traceId());
	}
}
