package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class AggiornaRegistrazioneRequest implements Serializable{
	@NotNull
	private Long idValutatore;
	@NotNull
	private Long idValutato;
	@NotNull
	private Long idQuestionario;
	@NotNull
	private Long idRegolamento;
	@NotNull
	private Long idOrganizzazione;
	private String note;
	@NotNull
	private LocalDate inizioValidita;
	@NotNull
	private LocalDate fineValidita;
	private Boolean po=false;
	private Boolean responsabile=false;
	private Boolean interim=false;
	
	//forzature admin
	
	private Boolean forzaSchedaSeparata=false; 
	private Boolean inattiva=false; 
	private Boolean forzaValutatore=false;
	private Boolean mancataAssegnazione=false;
	private Boolean mancatoColloquio=false;

	
}
