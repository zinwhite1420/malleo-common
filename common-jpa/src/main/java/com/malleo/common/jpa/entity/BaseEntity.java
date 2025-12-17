package com.malleo.common.jpa.entity;

import org.hibernate.Hibernate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (Hibernate.getClass(this) != Hibernate.getClass(obj)) {
			return false;
		}

		BaseEntity that = (BaseEntity) obj;

		return id != null && id.equals(that.id);
	}

	@Override
	public final int hashCode() {
		return getClass().hashCode();
	}
}
