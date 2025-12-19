package com.malleo.common.querydsl.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

class QuerydslSortsTest {

	// 테스트용 엔티티 클래스(실제 필드 없어도 PathBuilder 기반은 동작합니다)
	static class User { }

	enum UserSortKey implements QuerydslSorts.SortKey {
		CREATED_AT("createdAt", "createdAt"),
		EMAIL("email", "email"),
		CATEGORY_NAME("categoryName", "category.name");

		private final String key;
		private final String path;

		UserSortKey(String key, String path) {
			this.key = key;
			this.path = path;
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public String propertyPath() {
			return path;
		}
	}

	@Test
	void toOrderSpecifiers_mapsAllowlistedKeys() {
		Sort sort = Sort.by(
			Sort.Order.desc("createdAt"),
			Sort.Order.asc("email")
		);

		OrderSpecifier<?>[] specs = QuerydslSorts.toOrderSpecifiers(
			sort,
			User.class,
			"user",
			QuerydslSorts.allowlistResolver(UserSortKey.values())
		);

		assertEquals(2, specs.length);

		assertEquals(Order.DESC, specs[0].getOrder());
		assertEquals("user.createdAt", specs[0].getTarget().toString());

		assertEquals(Order.ASC, specs[1].getOrder());
		assertEquals("user.email", specs[1].getTarget().toString());
	}

	@Test
	void toOrderSpecifiers_supportsDotPath() {
		Sort sort = Sort.by(Sort.Order.asc("categoryName"));

		OrderSpecifier<?>[] specs = QuerydslSorts.toOrderSpecifiers(
			sort,
			User.class,
			"user",
			QuerydslSorts.allowlistResolver(UserSortKey.values())
		);

		assertEquals(1, specs.length);
		assertEquals(Order.ASC, specs[0].getOrder());
		assertEquals("user.category.name", specs[0].getTarget().toString());
	}

	@Test
	void toOrderSpecifiers_ignoresInvalidKey_whenPolicyIgnore() {
		Sort sort = Sort.by(
			Sort.Order.asc("createdAt"),
			Sort.Order.desc("unknownKey") // allowList 내 없음
		);

		OrderSpecifier<?>[] specs = QuerydslSorts.toOrderSpecifiers(
			sort,
			User.class,
			"user",
			QuerydslSorts.allowlistResolver(UserSortKey.values()),
			QuerydslSorts.InvalidSortPolicy.IGNORE,
			null
		);

		assertEquals(1, specs.length);
		assertEquals("user.createdAt", specs[0].getTarget().toString());
	}

	@Test
	void toOrderSpecifiers_throwsOnInvalidKey_whenPolicyThrow() {
		Sort sort = Sort.by(Sort.Order.asc("unknownKey"));

		assertThrows(IllegalArgumentException.class, () ->
			QuerydslSorts.toOrderSpecifiers(
				sort,
				User.class,
				"user",
				QuerydslSorts.allowlistResolver(UserSortKey.values()),
				QuerydslSorts.InvalidSortPolicy.THROW,
				null
			)
		);
	}

	@Test
	void toOrderSpecifiers_appliesDefaultSort_whenInputSortIsUnsorted() {
		Sort defaultSort = Sort.by(Sort.Order.desc("createdAt"));

		OrderSpecifier<?>[] specs = QuerydslSorts.toOrderSpecifiers(
			Sort.unsorted(),
			User.class,
			"user",
			QuerydslSorts.allowlistResolver(UserSortKey.values()),
			QuerydslSorts.InvalidSortPolicy.THROW,
			defaultSort
		);

		assertEquals(1, specs.length);
		assertEquals(Order.DESC, specs[0].getOrder());
		assertEquals("user.createdAt", specs[0].getTarget().toString());
	}
}
