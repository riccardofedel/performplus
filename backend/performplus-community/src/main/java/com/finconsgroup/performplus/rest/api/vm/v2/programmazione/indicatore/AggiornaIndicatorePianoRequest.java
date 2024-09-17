package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class AggiornaIndicatorePianoRequest implements Serializable{
	private String specifica;
	private String specificaNumeratore;
	private String specificaDenominatore;
	private String note;
	private Boolean specificaPercentuale;
	@NotNull
	private Boolean decrescente=false;
	private Boolean strategico=false;
	private Boolean sviluppoSostenibile=false;
	@NotNull
	private Long idIndicatore;

	private String tipoIndicatore;
	private LocalDate scadenzaIndicatore;
	
	private String dimensione;
	private String fonte;
	private String baseline;

	private String prospettiva;
	
	private String unitaMisura;
	private String descrizione;
	
	private Long organizzazione;
	private Integer specificaDecimali;
	private Integer specificaDecimaliA;
	private Integer specificaDecimaliB;
	

}
