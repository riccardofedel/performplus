package com.finconsgroup.performplus.rest.api.vm.v2;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BilancioListVM implements Serializable{

	private Integer anno;
	private String capitolo;
	private String descrizione;
	private String missione;
	private String descMissione;
	private String programma;
	private String descProgramma;
	private Double stanziato=0d;
	private Double impegnato=0d;
	private Double liquidato=0d;
	
	
}
