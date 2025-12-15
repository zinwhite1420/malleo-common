package com.malleo.common.core.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageResponseTest {

	@Test
	@DisplayName("PageResponse는 전달된 값들을 그대로 보존")
	void pageResponse_shouldKeepValues() {
		// given
		List<String> items = List.of("a", "b", "c");

		// when
		PageResponse<String> page = new PageResponse<>(items, 1, 10, 35L, 4);

		// then
		assertEquals(items, page.items());
		assertEquals(1, page.page());
		assertEquals(10, page.size());
		assertEquals(35L, page.totalElements());
		assertEquals(4, page.totalPages());
	}
}
