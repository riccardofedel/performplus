package com.finconsgroup.performplus.rest.api.vm;
import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.TipoFunzione;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class RisorsaUmanaSmartVM  extends EntityVM {

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
	private DecodificaEasyVM categoria;
	private DecodificaEasyVM contratto;
	private DecodificaEasyVM profilo;

	
	private Boolean po=false;
	private DecodificaEasyVM incarico;
	private String email;
	private LocalDate inizioValidita;
	private LocalDate fineValidita;
	private Integer anno;
	private Long idEnte;

}