package com.finconsgroup.performplus.rest.api.vm.v2;

import java.time.LocalDate;
import java.util.List;

import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Enabling;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoSmartVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class ObiettivoVM extends EntityVM implements EnablingInterface{

	private TipoNodo tipoNodo;
	private Boolean bloccato=false;
	private int ordine;
	private Integer anno;
	private Integer annoInizio;
	private Integer annoFine;

	private String codice;
	private String codiceRidotto;
	private String denominazione;
	private String note;
	private String descrizione;
	private Long idEnte = 0l;
	private String codiceCompleto;
	private String codiceInterno;
	private NodoPianoSmartVM padre;

	private Integer peso;
	private Integer  percentualeFigli;
	
	private String responsabile;
    private LocalDate inizio;
    private LocalDate scadenza;
    
    private LocalDate inizioEffettivo;
    private LocalDate scadenzaEffettiva;
    private String noteConsuntivo;
    

	private List<StrutturaVM> organizzazioni;
	private StrutturaVM organizzazione;
	
	private Boolean strategico;

	
    private Long idResponsabile;
    
 
	private Enabling enabling;
	
	private String tipo;
	private Boolean obiettivoImpatto;
	private String modalita;

	private Double penalizzazione;
	
	private String politica;
	
	private String dimensione;
	private String contributors;
	private String stakeholders;
	
	private Boolean focusSemplificazione;
	private Boolean focusDigitalizzazione;
	private Boolean focusAccessibilita;
	private Boolean focusPariOpportunita;
	private Boolean focusRisparmioEnergetico;
	
	private String prospettiva;
	private String innovazione;
	private String annualita;

	private Boolean flagPnrr;
	
	private Boolean flagOIV;
	private String noteOIV;
	
	public ObiettivoVM enabling(Enabling enabling) {
		this.enabling=enabling;
		return this;
	}
}

