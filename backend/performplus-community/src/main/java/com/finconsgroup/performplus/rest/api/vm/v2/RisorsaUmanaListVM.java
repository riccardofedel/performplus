package com.finconsgroup.performplus.rest.api.vm.v2;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class RisorsaUmanaListVM  extends EntityVM {

	private String nome;
	private String cognome;
	private boolean interno=true;
	private Disponibile disponibile=Disponibile.DISPONIBILE;
	private String codiceInterno;
	private String categoria;
	private String contratto;
	private String profilo;
	
	private Boolean partTime;
	private Integer mesi;
	private Integer orePartTime;

	private LocalDate dataNascita;
	private String email;
	private String codiceFiscale;
	
	private LocalDate inizioValidita;
	private LocalDate fineValidita;
	
	private String incarico;


}
