package com.malleo.common.core.error;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CommonErrorCodeTest {

	@Test
	void commonErrorCode_shouldExposeContractFields() {
		CommonErrorCode ec = CommonErrorCode.INVALID_REQUEST;

		assertEquals("C-400", ec.code());
		assertEquals("Invalid request", ec.message());
		assertEquals(400, ec.httpStatus());
	}
}
