package com.malleo.common.querydsl.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;

class QuerydslPredicatesTest {

	static class User { }

	@Test
	void and_returnsNull_whenAllNull() {
		BooleanExpression result = QuerydslPredicates.and(null, null);
		assertNull(result);
	}

	@Test
	void or_returnsNull_whenAllNull() {
		BooleanExpression result = QuerydslPredicates.or(null, null);
		assertNull(result);
	}

	@Test
	void and_ignoresNull_andCombines() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		StringPath email = user.getString("email");
		StringPath name = user.getString("name");

		BooleanExpression exp1 = email.eq("a@malleo.com");
		BooleanExpression exp2 = name.containsIgnoreCase("jin");

		BooleanExpression result = QuerydslPredicates.and(exp1, null, exp2);

		assertNotNull(result);
		String rendered = result.toString();

		// toString은 구현에 따라 표현이 조금 다를 수 있어 핵심 토큰만 확인
		assertTrue(rendered.contains("user.email = a@malleo.com"));
		assertTrue(rendered.contains("&&") || rendered.contains("and"));
	}

	@Test
	void or_ignoresNull_andCombines() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		StringPath email = user.getString("email");
		StringPath name = user.getString("name");

		BooleanExpression exp1 = email.eq("a@malleo.com");
		BooleanExpression exp2 = name.eq("baek");

		BooleanExpression result = QuerydslPredicates.or(exp1, null, exp2);

		assertNotNull(result);
		String rendered = result.toString();

		assertTrue(rendered.contains("user.email = a@malleo.com"));
		assertTrue(rendered.contains("||") || rendered.contains("or"));
		assertTrue(rendered.contains("user.name = baek"));
	}

	@Test
	void anyOf_isAliasOfOr() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		BooleanExpression exp1 = user.getString("email").isNotNull();
		BooleanExpression exp2 = user.getString("name").isNotNull();

		BooleanExpression result = QuerydslPredicates.anyOf(exp1, exp2);

		assertNotNull(result);
	}

	@Test
	void allOf_isAliasOfAnd() {
		PathBuilder<User> user = new PathBuilder<>(User.class, "user");
		BooleanExpression exp1 = user.getString("email").isNotNull();
		BooleanExpression exp2 = user.getString("name").isNotNull();

		BooleanExpression result = QuerydslPredicates.allOf(exp1, exp2);

		assertNotNull(result);
	}
}
