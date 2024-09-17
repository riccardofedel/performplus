package com.finconsgroup.performplus.rest.api.vm.v2.view;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;

import org.springframework.data.annotation.Immutable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString

public class ViewProcessi implements Serializable{
	private String codice;
	private String tipo;
	private String obiettivo;
	private String descrizione;
	private String progressivo;
	private LocalDate inizio_validita;
	private LocalDate fine_validita;
	private String direzione;
	private String normativa;
	private String numero;
	private String tipo_norm;
	private String area;

}
