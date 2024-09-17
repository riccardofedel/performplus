package com.finconsgroup.performplus.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @CreatedDate
    @Column(name = "date_insert", updatable = false)
    private LocalDateTime dateInsert=LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "date_modify")
    private LocalDateTime dateModify;

    @CreatedBy
    @Column(name = "user_insert", length = 255, updatable = false)
    private String userInsert="system";

    @LastModifiedBy
    @Column(name = "user_modify", length = 255)
    private String userModify="system";


    public abstract Long getId();
    public abstract void setId(Long id);
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractAuditingEntity other = (AbstractAuditingEntity) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

    public AbstractAuditingEntity id(Long id) {
    	this.setId(id);
    	return this;
    }
}
