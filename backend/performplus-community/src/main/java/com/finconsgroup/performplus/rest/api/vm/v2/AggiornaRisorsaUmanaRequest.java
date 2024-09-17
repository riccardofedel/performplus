package com.finconsgroup.performplus.rest.api.vm.v2;

import java.io.Serializable;
import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.Disponibile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class AggiornaRisorsaUmanaRequest implements Serializable{
	
	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;

	@NotNull
	private Disponibile disponibile=Disponibile.DISPONIBILE;
	@NotNull
	private Boolean interno=true;
	
	private Long idCategoria;
	private Long idContratto;
	private Long idProfilo;	
	private Long idIncarico;	
	
	private LocalDate dataNascita;
	private Integer mesi=12;
	private Integer orePartTime;
	private Boolean partTime=false;
	
	private String codiceFiscale;
	private String codiceInterno;
	private String email;
	
	private Integer anno;
	private Long idEnte=0l;
	
	private LocalDate inizioValidita;
	private LocalDate fineValidita;




}
