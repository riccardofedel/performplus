package com.finconsgroup.performplus.rest.api.vm.v2.programmazione;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.finconsgroup.performplus.enumeration.ModalitaAttuative;
import com.finconsgroup.performplus.enumeration.TipoObiettivo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class AggiornaNodoPianoRequest implements Serializable {
	@NotBlank
	private String codice;
	@NotNull
	private String denominazione;
	private String descrizione;
	private List<Long> organizzazioni;
	private Long organizzazione;

	private String note;
	private BigDecimal peso;


	@NotNull
    private LocalDate inizio;
    private LocalDate scadenza;

	private String nomeResponsabile;
	
	private Integer  percentualeFigli;

	private Boolean strategico;
	private Boolean enabledStrategico;
    
	private TipoObiettivo tipo;
	private Boolean obiettivoImpatto;
	private ModalitaAttuative modalita;

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
    private List<String> fields;

    private Boolean bloccato=false;
    
    private String noteOIV;
    private Boolean flagOIV;
    
	public AggiornaNodoPianoRequest fields(List<String> fields) {
		setFields(fields); 
		return this;
	}
}
