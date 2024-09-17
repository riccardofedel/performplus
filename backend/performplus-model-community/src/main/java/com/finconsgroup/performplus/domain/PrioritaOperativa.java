package com.finconsgroup.performplus.domain;

import java.math.BigDecimal;

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
@Table(name = "PRIORITA_OPERATIVA")
@Data
@EqualsAndHashCode

public class PrioritaOperativa extends AbstractAuditingEntity {
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "NODO_PIANO_ID")
    private NodoPiano nodoPiano;
    @ManyToOne(optional = false)
    @JoinColumn(name = "ORGANIZZAZIONE_ID")
    private Organizzazione organizzazione;
    @Column(name = "PRIORITA", nullable = false)
    private BigDecimal priorita;

}