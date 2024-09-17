package com.finconsgroup.performplus.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode
public abstract class AbstractLogicalDeleteEntity extends AbstractAuditingEntity {
	private static final long serialVersionUID = 5663719667143793882L;

	@Column(name = "date_delete")
	private LocalDateTime dateDelete;

	@Column(name = "user_delete", length = 255)
	private String userDelete;

}
