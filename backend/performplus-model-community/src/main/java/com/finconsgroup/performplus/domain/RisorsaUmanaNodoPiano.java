package com.finconsgroup.performplus.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "RISORSA_UMANA_NODO_PIANO")
@Data
@EqualsAndHashCode

public class RisorsaUmanaNodoPiano extends AbstractAuditingEntity {
	private static final long serialVersionUID = 1L;
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "RISORSA_UMANA_NODO_PIANO_STORE")
	@SequenceGenerator(name = "RISORSA_UMANA_NODO_PIANO_STORE", sequenceName = "RISORSA_UMANA_NODO_PIANO_SEQ"
			, allocationSize = 1)

    protected Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "NODO_PIANO_ID")
	private NodoPiano nodoPiano;
	@ManyToOne(optional = false)
	@JoinColumn(name = "RISORSA_UMANA_ID")
	private RisorsaUmana risorsaUmana;
	@Column(nullable = true, name = "DISPONIBILITA")
	private BigDecimal disponibilita;
	

	@Column(nullable = true, name = "PESO")
	private BigDecimal peso;


}