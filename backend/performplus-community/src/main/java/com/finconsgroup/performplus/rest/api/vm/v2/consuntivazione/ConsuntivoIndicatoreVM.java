package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.Periodicita;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatorePianoListVM;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class ConsuntivoIndicatoreVM extends IndicatorePianoListVM{

	private ValutazioneConsuntivoVM consuntivo;
	private ValutazioneConsuntivoVM consuntivo1;
	private ValutazioneConsuntivoVM consuntivo2;
	private ValutazioneConsuntivoVM consuntivo3;
	private ValutazioneConsuntivoVM consuntivo4;
	
	private ValutazioneConsuntivoVM preventivo;
	private ValutazioneConsuntivoVM preventivo1;
	private ValutazioneConsuntivoVM preventivo2;
	private ValutazioneConsuntivoVM preventivo3;
	private ValutazioneConsuntivoVM preventivo4;

	private ValutazioneConsuntivoVM storico;


	private Double raggiungimentoPerc;

	private Double raggiungimentoPesato;
	
	private Double raggiungimentoForzato;
	
	private Boolean nonValutabile;
	
	private Periodicita periodicitaRend;
	
	private String tipoIndicatore;
	private LocalDate scadenzaIndicatore;
	
}
