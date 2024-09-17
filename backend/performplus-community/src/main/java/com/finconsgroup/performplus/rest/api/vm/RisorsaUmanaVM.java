package com.finconsgroup.performplus.rest.api.vm;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.TipoFunzione;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class RisorsaUmanaVM  extends EntityVM {

	private String nome;
	private String cognome;

	private LocalDate dataNascita;

	private Disponibile disponibile;
	private Boolean esterna;
	private Integer mesi;

	private Integer ordine;
	private Integer orePartTime;

	private Boolean partTime;
	private Boolean politico;
	private TipoFunzione funzione;
	private String delega;
	private String codiceFiscale;
	private String codiceInterno;
	private DecodificaVM categoria;
	private DecodificaVM contratto;
	private DecodificaVM profilo;

	
	private Boolean po=false;
	private DecodificaVM incarico;
	private String email;
	private LocalDate inizioValidita;
	private LocalDate fineValidita;
	private Integer anno;
	private Long idEnte;

	

}
