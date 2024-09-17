package com.finconsgroup.performplus.domain;

import java.time.LocalDate;

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

@SuppressWarnings("serial")
@Entity
@Table(name = "RESPONSABILE_NODO")
@Data
@EqualsAndHashCode
public class ResponsabileNodo extends AbstractAuditingEntity {

    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "RESPONSABILE_NODO_STORE")
	@SequenceGenerator(name = "RESPONSABILE_NODO_STORE", sequenceName = "RESPONSABILE_NODO_SEQ"
			, allocationSize = 1)

    protected Long id;

    @ManyToOne
    @JoinColumn(name="NODO_PIANO_ID")
    private NodoPiano nodoPiano;
    @ManyToOne
    @JoinColumn(name="RESPONSABILE_ID")
    private RisorsaUmana responsabile;
        
    @Column(name = "INIZIO_VALIDITA")
    private LocalDate inizioValidita;
    @Column(name = "FINE_VALIDITA")
    private LocalDate fineValidita;
 
    
}