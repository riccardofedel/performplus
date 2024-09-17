package com.finconsgroup.performplus.rest.api.vm.v2;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class RisorsaUmanaDetailVM extends EntityVM{
	
	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;

	@NotNull
	private Disponibile disponibile=Disponibile.DISPONIBILE;
	@NotBlank
	private boolean interno=true;
	
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
	
	private LocalDate inizioValidita;
	private LocalDate fineValidita;


}
