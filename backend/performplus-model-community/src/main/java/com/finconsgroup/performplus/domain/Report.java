package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.service.dto.AnnoAndEnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "REPORT")
@Data
@EqualsAndHashCode
public class Report extends AbstractAuditingEntity 
implements AnnoAndEnteInterface
{

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id=0l;

	@Column(name = "ID_ENTE",nullable = false)
	private Long idEnte = 0l;
	
	@Column(name = "ANNO",nullable = false)
	private Integer anno;

	@Column(name = "NOME",nullable = false)
	private String nome;

	@Column(name = "TIPO",nullable = false)
	private String tipo;

	@Column(name = "DESCRIZIONE", length = 1000)
	private String descrizione;

	@Column(name = "CONTENUTO",nullable = false, length = 10000)
	private String contenuto;
	
}