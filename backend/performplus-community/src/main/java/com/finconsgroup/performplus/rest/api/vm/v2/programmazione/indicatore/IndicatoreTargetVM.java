package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.finconsgroup.performplus.enumeration.Periodicita;
import com.finconsgroup.performplus.enumeration.Periodo;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.rest.api.vm.v2.ValoreDetailVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class IndicatoreTargetVM implements Serializable {
	@NotNull
	private Long idIndicatorePiano;
	@NotBlank
	private String denominazione;
	private String nomeNumeratore;
	private String nomeDenominatore;
	private Boolean percentuale;
	private Boolean decrescente;
	@NotNull
	private TipoFormula formula;
	private List<ValoreDetailVM> storici;
	private List<ValoreDetailVM> targets;
	
	private ValoreDetailVM storico;

	private Double peso;
	
	private Integer annoInizio;
	private Integer annoFine;
	private Integer anno;
	
	private Integer annoInizioTarget;
	private Integer annoFineTarget;
	private Periodicita periodicitaTarget;
	
	private String unitaMisura;
	

}
