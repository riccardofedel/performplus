package com.finconsgroup.performplus.rest.api.vm.v2;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class OrganizzazioneVM  extends EntityVM {

	private OrganizzazioneVM padre;
	private int ordine;
	private String codice;
	private String codiceInterno;
	private String codiceCompleto;
	private String descrizione;
	private String intestazione;
	private PersonaVM responsabile;
	private Livello livello;
	private LocalDate inizioValidita;
	private LocalDate fineValidita;
	private Boolean interim;
	private Integer anno;
	private Long idEnte=0l;


}