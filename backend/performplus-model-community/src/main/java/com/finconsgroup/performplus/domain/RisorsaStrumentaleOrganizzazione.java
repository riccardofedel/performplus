package com.finconsgroup.performplus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "RISORSA_STRUMENTALE_ORG")
@Data
@EqualsAndHashCode

public class RisorsaStrumentaleOrganizzazione extends AbstractAuditingEntity {
	
	private static final long serialVersionUID = 1L;
	
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

	@Column(nullable = true, name = "DISPONIBILITA")
	private Integer disponibilita;
	@ManyToOne(optional = false)
	@JoinColumn(name = "ORGANIZZAZIONE_ID")
	private Organizzazione organizzazione;
	@ManyToOne(optional = false)
	@JoinColumn(name = "RISORSA_STRUMENTALE_ID")
	private RisorsaStrumentale risorsaStrumentale;

}