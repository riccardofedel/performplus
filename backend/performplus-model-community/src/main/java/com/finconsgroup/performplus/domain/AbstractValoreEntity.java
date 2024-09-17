package com.finconsgroup.performplus.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode
public abstract class AbstractValoreEntity extends AbstractAuditingEntity {
	private static final long serialVersionUID = 5663719667143793881L;
 
	@Column(name = "VALORE_NUMERICO_A")
	private BigDecimal valoreNumericoA;
	@Column(name = "VALORE_NUMERICO_B")
	private BigDecimal valoreNumericoB;
	@Column(name = "VALORE_NUMERICO")
	private BigDecimal valoreNumerico;
	@Column(name = "VALORE_BOOLEANO")
	private Boolean valoreBooleano;
	@Column(name = "VALORE_TEMPORALE")
	private LocalDate valoreTemporale;
	@Column(name = "INIZIO")
	private LocalDate inizio;
	@Column(name = "SCADENZA")
	private LocalDate scadenza;
	
	@Column(name = "ANNO")
	private Integer anno;
	@Column(name = "PERIODO")
	private Integer periodo;
	
}
