package com.malleo.common.querydsl.util;

import com.querydsl.core.types.dsl.BooleanExpression;

public final class QuerydslPredicates {

	private QuerydslPredicates() {
	}

	public static BooleanExpression and(BooleanExpression... expressions) {
		BooleanExpression result = null;

		for (BooleanExpression exp : expressions) {
			if (exp == null) {
				continue;
			}

			if (result == null) {
				result = exp;
			} else {
				result = result.and(exp);
			}
		}
		return result;
	}

	public static BooleanExpression or(BooleanExpression... expressions) {
		BooleanExpression result = null;

		for (BooleanExpression exp : expressions) {
			if (exp == null) {
				continue;
			}

			if (result == null) {
				result = exp;
			} else {
				result = result.or(exp);
			}
		}
		return result;
	}

	public static BooleanExpression anyOf(BooleanExpression... expressions) {
		return or(expressions);
	}

	public static BooleanExpression allOf(BooleanExpression... expressions) {
		return and(expressions);
	}
}