package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "TEMPLATE_DATA")
@Data
@EqualsAndHashCode
public class TemplateData extends AbstractAuditingEntity 
implements EnteInterface 
{

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id=0l;

	@Column(name = "ID_ENTE",nullable = false)
	private Long idEnte = 0l;

	@Column(name = "CONTAINER",nullable = false)
	private String container;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "FORMLY_FIELD_CONFIG",nullable = false, length = 10000)
	private String formlyFieldConfig;
	
}