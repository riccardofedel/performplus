package com.finconsgroup.performplus.rest.api.vm.v2.attivitagestionali;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class CreaAttivitaGestionaleRequest implements Serializable{
	
	@NotNull
	private Long idEnte=0l;

	@NotBlank
	private String codice;
	@NotBlank
	private String descrizione;
	private LocalDate inizioValidita;
	private LocalDate fineValidita;
	@NotBlank
	private String denominazione;

}
