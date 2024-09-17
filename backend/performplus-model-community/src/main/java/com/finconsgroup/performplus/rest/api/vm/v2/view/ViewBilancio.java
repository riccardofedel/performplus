package com.finconsgroup.performplus.rest.api.vm.v2.view;

import java.io.Serializable;

import jakarta.persistence.Entity;

import org.springframework.data.annotation.Immutable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString

public class ViewBilancio implements Serializable{
	private Integer anno;
	private String capitolo;
	private String tipo;
	private String codice;
	private String padre;
	private String programma;
	private String missione;
	private Double stanziato;
	private Double impegnato;
	private Double liquidato;
	private String area;
}
