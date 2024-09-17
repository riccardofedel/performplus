package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class ParametriRicercaRegistrazione implements Serializable{
	@NotNull
	Long idEnte;
	@NotNull
	Integer anno;
	String cognome;
	String cognomeValutato;
	String cognomeValutatore;
	Long idValutatore;
	Long idValutato;
	Long idStruttura;
	Long idQuestionario;
	Long idRegolamento;
	Boolean responsabile;
	Boolean inattiva;
	Boolean interim;
	Boolean po;
}
