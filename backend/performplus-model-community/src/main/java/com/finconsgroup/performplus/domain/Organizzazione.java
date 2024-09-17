package com.finconsgroup.performplus.domain;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.enumeration.TipoStruttura;
import com.finconsgroup.performplus.enumeration.TipologiaStruttura;
import com.finconsgroup.performplus.service.dto.AnnoInterface;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "ORGANIZZAZIONE",uniqueConstraints = { @UniqueConstraint(columnNames = {
		"ID_ENTE","ANNO", "CODICE_COMPLETO" })})
@Data
@EqualsAndHashCode
public class Organizzazione extends AbstractAuditingEntity implements EnteInterface, AnnoInterface{

    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ORGANIZZAZIONE_STORE")
	@SequenceGenerator(name = "ORGANIZZAZIONE_STORE", sequenceName = "ORGANIZZAZIONE_SEQ"
	, allocationSize = 1)

    protected Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PADRE_ID")
    private Organizzazione padre;
    @Column(name="ORDINE")
    private int ordine=0;
    @Column(nullable = false,name="CODICE")
    private String codice;
    @Column(name="DESCRIZIONE", length = 1000)
    private String descrizione;
    @Column(name="INTESTAZIONE",nullable = false, length = 500)
    private String intestazione;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="RESPONSABILE_ID")
    private RisorsaUmana responsabile;
    @Column(nullable = false,name="LIVELLO")
    @Enumerated(EnumType.STRING)
    private Livello livello;
    @Column(name="CODICE_COMPLETO",nullable=false)
    private String codiceCompleto;
    @Column(nullable = false, name = "ID_ENTE")
    private Long idEnte=0l;
    @Column(name = "ANNO")
    private Integer anno;
    
    @Column(nullable = true, name = "CODICE_INTERNO",length=100)
    private String codiceInterno;
    
    @Column(name = "INIZIO_VALIDITA")
    private LocalDate inizioValidita;
    @Column(name = "FINE_VALIDITA")
    private LocalDate fineValidita;
    
    @Column(name = "INTERIM")
    private Boolean interim;

    @Column(nullable = true,name="TIPO_STRUTTURA")
    @Enumerated(EnumType.STRING)
    private TipoStruttura tipoStruttura;

    @Column(nullable = true,name="TIPOLOGIA_STRUTTURA")
    @Enumerated(EnumType.STRING)
    private TipologiaStruttura tipologiaStruttura;

    @Transient
    public String getPosizione() {
	if (getPadre() == null)
	    return "0";
	return getPadre().getPosizione() + "." + ordine;
    }
    
   
    
}