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
@Table(name = "RESPONSABILE_ORG")
@Data
@EqualsAndHashCode
public class ResponsabileOrganizzazione extends AbstractAuditingEntity {

    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "RESPONSABILE_ORG_STORE")
	@SequenceGenerator(name = "RESPONSABILE_ORG_STORE", sequenceName = "RESPONSABILE_ORG_SEQ"
			, allocationSize = 1)

    protected Long id;

    @ManyToOne
    @JoinColumn(name="ORGANIZZAZIONE_ID")
    private Organizzazione organizzazione;
    @ManyToOne
    @JoinColumn(name="RESPONSABILE_ID")
    private RisorsaUmana responsabile;
        
    @Column(name = "INIZIO_VALIDITA")
    private LocalDate inizioValidita;
    @Column(name = "FINE_VALIDITA")
    private LocalDate fineValidita;
 
    @Column(name = "INTERIM")
    private Boolean interim;
    
}