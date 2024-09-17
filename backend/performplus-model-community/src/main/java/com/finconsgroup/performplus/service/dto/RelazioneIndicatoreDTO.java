package com.finconsgroup.performplus.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class RelazioneIndicatoreDTO extends IndicatorePianoDTO {

	public RelazioneIndicatoreDTO() {}
	public RelazioneIndicatoreDTO(IndicatorePianoDTO ip, ConfigurazioneDTO configurazione) {
		super(configurazione);
		setIndicatorePiano(ip);
		setConfigurazione(configurazione);
	}

	public RelazioneIndicatoreDTO(ConfigurazioneDTO configurazione) {
		super(configurazione);
	}

	public void setIndicatorePiano(IndicatorePianoDTO ip) {
		setId(ip.getId());
		setNodoPiano(ip.getNodoPiano());
		setIndicatore(ip.getIndicatore());
		setSpecifica(ip.getSpecifica());
		setSpecificaNumeratore(ip.getSpecificaNumeratore());
		setSpecificaDenominatore(ip.getSpecificaDenominatore());
		setNote(ip.getNote());
		setPeso(ip.getPeso());
		setSpecificaPercentuale(ip.getSpecificaPercentuale());
		setDecrescente(ip.getDecrescente());
		setDecrescenteInt(ip.getDecrescenteInt());

		setConsuntivi(ip.getConsuntivi());
		setPreventivi(ip.getPreventivi());
		setStorici(ip.getStorici());

		setNroValutazioni(ip.getNroValutazioni());
		
		setRaggiungimentoForzato(ip.getRaggiungimentoForzato());
		setNoteRaggiungimentoForzato(ip.getNoteRaggiungimentoForzato());
		setNonValutabile(ip.getNonValutabile());
		setRichiestaForzatura(ip.getRichiestaForzatura());
		setNoteRichiestaForzatura(ip.getNoteRichiestaForzatura());
		
		setStrategico(Boolean.TRUE.equals(ip.getStrategico()));
		setSviluppoSostenibile(ip.getSviluppoSostenibile());
		setInizio(ip.getInizio());
		setNroValutazioni(ip.getNroValutazioni());
		setPesoRelativo(ip.getPesoRelativo());
		setScadenza(ip.getScadenza());
		setSpecificaPercentuale(ip.getSpecificaPercentuale());
		setRaggruppamento(ip.getRaggruppamento());
		
		getRanges().addAll(ip.getRanges());
		
	}
}