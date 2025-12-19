package com.malleo.common.querydsl;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;

@AutoConfiguration
@ConditionalOnClass(JPAQueryFactory.class)
@ConditionalOnBean(EntityManagerFactory.class)
@ConditionalOnProperty(name = "malleo.querydsl.enabled", havingValue = "true", matchIfMissing = true)
public class CommonQuerydslAutoConfiguration {

	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}
}
