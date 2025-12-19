package com.malleo.common.querydsl.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;

class QuerydslConditionsTest {

	static class User { }

	@Test
	void eq_returnsNull_whenValueNull() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		StringPath email = user.getString("email");

		BooleanExpression exp = QuerydslConditions.eq(email, null);
		assertNull(exp);
	}

	@Test
	void eq_returnsExpression_whenValueNotNull() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		StringPath email = user.getString("email");

		BooleanExpression exp = QuerydslConditions.eq(email, "a@malleo.com");

		assertNotNull(exp);
		assertTrue(exp.toString().contains("user.email = a@malleo.com"));
	}

	@Test
	void containsIgnoreCase_returnsNull_whenBlank() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		StringPath name = user.getString("name");

		assertNull(QuerydslConditions.containsIgnoreCase(name, null));
		assertNull(QuerydslConditions.containsIgnoreCase(name, ""));
		assertNull(QuerydslConditions.containsIgnoreCase(name, "   "));
	}

	@Test
	void containsIgnoreCase_returnsExpression_whenKeywordProvided() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		StringPath name = user.getString("name");

		BooleanExpression exp = QuerydslConditions.containsIgnoreCase(name, "jin");

		assertNotNull(exp);

		String rendered = exp.toString().toLowerCase();

		// 핵심만 검증 (구현 세부에는 의존하지 않음)
		assertTrue(rendered.contains("user.name"));
		assertTrue(rendered.contains("jin"));
	}


	@Test
	void between_returnsNull_whenBothNull() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		DateTimePath<Instant> createdAt = user.getDateTime("createdAt", Instant.class);

		assertNull(QuerydslConditions.between(createdAt, null, null));
	}

	@Test
	void between_returnsGoe_whenOnlyFromProvided() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		DateTimePath<Instant> createdAt = user.getDateTime("createdAt", Instant.class);

		Instant from = Instant.parse("2025-01-01T00:00:00Z");
		BooleanExpression exp = QuerydslConditions.between(createdAt, from, null);

		assertNotNull(exp);
		String rendered = exp.toString();
		assertTrue(rendered.contains("user.createdAt") && (rendered.contains(">= ") || rendered.contains("goe")));
	}

	@Test
	void between_returnsLoe_whenOnlyToProvided() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		DateTimePath<Instant> createdAt = user.getDateTime("createdAt", Instant.class);

		Instant to = Instant.parse("2025-12-31T00:00:00Z");
		BooleanExpression exp = QuerydslConditions.between(createdAt, null, to);

		assertNotNull(exp);
		String rendered = exp.toString();
		assertTrue(rendered.contains("user.createdAt") && (rendered.contains("<= ") || rendered.contains("loe")));
	}

	@Test
	void between_returnsBetween_whenBothProvided() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		DateTimePath<Instant> createdAt = user.getDateTime("createdAt", Instant.class);

		Instant from = Instant.parse("2025-01-01T00:00:00Z");
		Instant to = Instant.parse("2025-12-31T00:00:00Z");

		BooleanExpression exp = QuerydslConditions.between(createdAt, from, to);

		assertNotNull(exp);
		assertTrue(exp.toString().toLowerCase().contains("between"));
	}
}
