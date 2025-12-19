package com.malleo.common.jpa.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class BaseSoftDeleteEntity extends BaseEntity {

	@Getter
	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	@SuppressWarnings("unused")
	protected void markDeleted() {
		this.deleted = true;
		this.deletedAt = Instant.now();
	}

}

/*
서비스 사용 예시
@Entity
@SQLDelete(sql = "UPDATE users SET deleted = true, deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted = false")
public class User extends SoftDeleteEntity {
    // ...
}
 */
