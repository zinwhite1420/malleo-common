package com.malleo.common.querydsl.util;

import java.time.Instant;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringPath;

public final class QuerydslConditions {

	private QuerydslConditions() {
	}

	public static BooleanExpression eq(StringPath path, String value) {
		return value == null ? null : path.eq(value);
	}

	public static BooleanExpression containsIgnoreCase(StringPath path, String keyword) {
		return (keyword == null || keyword.isBlank()) ? null : path.containsIgnoreCase(keyword);
	}

	public static <T extends Comparable<?>> BooleanExpression goe(ComparableExpression<T> path, T value) {
		return value == null ? null : path.goe(value);
	}

	public static <T extends Comparable<?>> BooleanExpression loe(ComparableExpression<T> path, T value) {
		return value == null ? null : path.loe(value);
	}

	public static BooleanExpression between(DateTimePath<Instant> path, Instant from, Instant to) {
		if (from == null && to == null) {
			return null;
		}
		if (from != null && to != null) {
			return path.between(from, to);
		}
		if (from != null) {
			return path.goe(from);
		} else {
			return path.loe(to);
		}
	}
}
