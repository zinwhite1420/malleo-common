package com.malleo.common.querydsl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Sort;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;

/**
 * Spring Data Sort -> QueryDSL OrderSpecifier 변환 유틸.
 * 핵심 원칙:
 * - 요청된 sort key(클라이언트 입력)를 엔티티 경로로 직접 사용하지 않는다.
 * - 반드시 allowlist(key -> propertyPath)로 매핑한 뒤에만 정렬에 반영한다.
 */
public final class QuerydslSorts {

	private QuerydslSorts() {
	}
	/**
	 * allowlist 매핑용 키 인터페이스.
	 * 서비스(도메인)에서 enum 구현해서 사용
	 * 예:
	 *  CREATED_AT("createdAt", "createdAt")
	 *  CATEGORY_NAME("categoryName", "category.name")
	 */
	public interface SortKey {
		String key();

		String propertyPath();
	}

	/**
	 * 유효하지 않은 정렬 키/경로 처리 정책.
	 */
	public enum InvalidSortPolicy {
		/** 허용되지 않은 sort 무시하고 계속 진행 */
		IGNORE,
		/** 허용되지 않은 sort 하나라도 있으면 예외 발생 */
		THROW
	}

	/**
	 * SortKey enum(values()) 기반 allowlist resolver 생성기.
	 */
	public static Function<String, Optional<String>> allowlistResolver(SortKey[] keys) {
		Map<String, String> map = new HashMap<>();
		for (SortKey key : keys) {
			map.put(key.key(), key.propertyPath());
		}
		return requestedKey -> Optional.ofNullable(map.get(requestedKey));
	}

	/**
	 * Sort를 OrderSpecifier 배열로 변환 (유효하지 않은 sort 기본적으로 무시).
	 *
	 * @param sort Spring Data Sort
	 * @param rootEntityType 엔티티 타입 (예: User.class)
	 * @param rootAlias QueryDSL root alias (예: "user", 보통 Q타입 변수명과 동일)
	 * @param keyResolver allowlist resolver: requestedKey -> propertyPath
	 */
	public static OrderSpecifier<?>[] toOrderSpecifiers(
		Sort sort,
		Class<?> rootEntityType,
		String rootAlias,
		Function<String, Optional<String>> keyResolver
	) {
		return toOrderSpecifiers(sort, rootEntityType, rootAlias, keyResolver, InvalidSortPolicy.IGNORE, null);
	}

	/**
	 * Sort를 OrderSpecifier 배열로 변환 (정책/기본정렬 포함).
	 *
	 * @param sort Spring Data Sort
	 * @param rootEntityType 엔티티 타입
	 * @param rootAlias root alias
	 * @param keyResolver allowlist resolver
	 * @param invalidSortPolicy 잘못된 sort 처리 정책(무시/예외)
	 * @param defaultSort Sort 비어있거나 모두 무시된 경우 적용할 기본 정렬(없으면 null)
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static OrderSpecifier<?>[] toOrderSpecifiers(
		Sort sort,
		Class<?> rootEntityType,
		String rootAlias,
		Function<String, Optional<String>> keyResolver,
		InvalidSortPolicy invalidSortPolicy,
		Sort defaultSort
	) {
		Objects.requireNonNull(rootEntityType, "rootEntityType must not be null");
		Objects.requireNonNull(rootAlias, "rootAlias must not be null");
		Objects.requireNonNull(keyResolver, "keyResolver must not be null");
		if (invalidSortPolicy == null) {
			invalidSortPolicy = InvalidSortPolicy.IGNORE;
		}

		Sort effectiveSort = (sort == null || sort.isUnsorted()) ? defaultSort : sort;
		if (effectiveSort == null || effectiveSort.isUnsorted()) {
			return new OrderSpecifier<?>[0];
		}

		PathBuilder<?> root = new PathBuilder<>(rootEntityType, rootAlias);

		List<OrderSpecifier<?>> specs = new ArrayList<>();

		for (Sort.Order sOrder : effectiveSort) {
			String requestedKey = sOrder.getProperty();
			Optional<String> mapped = keyResolver.apply(requestedKey);

			if (mapped.isEmpty() || mapped.get().isBlank()) {
				if (invalidSortPolicy == InvalidSortPolicy.THROW) {
					throw new IllegalArgumentException("Invalid sort key: " + requestedKey);
				}
				continue;
			}

			String propertyPath = mapped.get().trim();

			Expression<?> expression;
			try {
				expression = resolvePath(root, propertyPath);
			} catch (RuntimeException e) {
				// allowlist는 통과했지만 실제 경로가 잘못된 경우(오타 등)
				if (invalidSortPolicy == InvalidSortPolicy.THROW) {
					throw new IllegalArgumentException("Invalid sort propertyPath: "
						+ propertyPath + " (key=" + requestedKey + ")", e);
				}
				continue;
			}

			Order direction = sOrder.isAscending() ? Order.ASC : Order.DESC;

			// 제네릭 추론이 꼬이는 케이스가 있어 raw ctor 사용(가장 안정적)
			specs.add(new OrderSpecifier(direction, expression));
		}

		// 요청 sort 있었지만 전부 무시된 경우에도 defaultSort를 재적용하고 싶다면 여기서 한 번 더 처리 가능
		// (현재 구현은 effectiveSort defaultSort를 반영했으므로 specs 0이면 그대로 0 반환)
		return specs.toArray(new OrderSpecifier<?>[0]);
	}

	/**
	 * dot-path("category.name") 지원 경로 해석기.
	 * PathBuilder#get(String)을 단계적으로 호출해 Expression 만든다.
	 */
	private static Expression<?> resolvePath(PathBuilder<?> root, String propertyPath) {
		PathBuilder<?> current = root;

		String[] parts = propertyPath.split("\\.");
		for (String part : parts) {
			if (part.isBlank()) {
				throw new IllegalArgumentException("Invalid propertyPath segment: '" + propertyPath + "'");
			}
			current = current.get(part);
		}

		return current;
	}
}
