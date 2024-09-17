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

@SuppressWarnings("serial")
@Entity
@Table(name = "RANGE_INDICATORE")
@Data
@EqualsAndHashCode

public class RangeIndicatore extends AbstractAuditingEntity {
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

	@Column(name = "MINIMO", nullable = false)
	private Double minimo;
	@Column(name = "MASSIMO", nullable = false)
	private Double massimo;
	@Column(name = "VALORE", nullable = false)
	private Double valore;
	@Column(name = "PROPORZIONALE")
	private Boolean proporzionale;
	@ManyToOne(optional = false)
	@JoinColumn(name = "INDICATORE_PIANO_ID")
	private IndicatorePiano indicatorePiano;



}
