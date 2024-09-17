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

@Entity
@Table(name = "RISORSA_UMANA_ORG")
@Data
@EqualsAndHashCode

public class RisorsaUmanaOrganizzazione extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "RISORSA_UMANA_ORG_STORE")
	@SequenceGenerator(name = "RISORSA_UMANA_ORG_STORE", sequenceName = "RISORSA_UMANA_ORG_SEQ"
			, allocationSize = 1)

    protected Long id;

    @Column(nullable = true, name = "DISPONIBILITA")
    private Integer disponibilita;
    @ManyToOne(optional = false)
    @JoinColumn(name = "ORGANIZZAZIONE_ID")
    private Organizzazione organizzazione;
    @ManyToOne(optional = false)
    @JoinColumn(name = "RISORSA_UMANA_ID")
    private RisorsaUmana risorsaUmana;
    @Column(name = "INIZIO_VALIDITA")
    private LocalDate inizioValidita;
    @Column(name = "FINE_VALIDITA")
    private LocalDate fineValidita;
   
  
}