package com.malleo.common.core.api;

import java.util.List;

public record PageResponse<T>(
	List<T> items,
	int page,
	int size,
	long totalElements,
	int totalPages
) {
}
