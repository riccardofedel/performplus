package com.finconsgroup.performplus.rest.api.pi.vm;

import java.time.LocalDate;
import java.util.Date;

import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.rest.api.vm.RisorsaUmanaSmartVM;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class RegistrazioneVM extends EntityVM{
	private RisorsaUmanaSmartVM valutatore;
	private RisorsaUmanaSmartVM valutato;
	private LocalDate dataValutazione;
	private DecodificaVM questionario;
	private DecodificaVM regolamento;
	private DecodificaVM organizzazione;
	private Long idEnte=0l;
	private Integer anno;
	private String note;
	
	private LocalDate dataPubblicazioneScheda;
	private String notePubblicazioneScheda;
	private LocalDate dataPresaVisioneScheda;
	private String notePresaVisioneScheda;

	private String noteApprovazioneSchedaOiv;
	private LocalDate dataApprovazioneSchedaOiv;
	private String noteApprovazioneValutazioneOiv;
	private LocalDate dataApprovazioneValutazioneOiv;
	
	private LocalDate dataPubblicazioneValutazione;
	private String notePubblicazioneValutazione;

	private LocalDate dataPresaVisioneValutazione;
	private String notePresaVisioneValutazione;
	
	private LocalDate inizioValidita;
	
	private LocalDate fineValidita;

	private Boolean po=false;
	private Boolean responsabile=false;
	private Boolean interim=false;
	
	private Boolean registrazioneSeparata=false; 
	private Boolean nonValutare=false; 
	
	private Boolean forzaSchedaSeparata=false;
	private Boolean inattiva=false;

	private Boolean forzaValutatore=false;

	private Boolean mancataAssegnazione=false;
	
	private Boolean mancatoColloquio=false;


}
