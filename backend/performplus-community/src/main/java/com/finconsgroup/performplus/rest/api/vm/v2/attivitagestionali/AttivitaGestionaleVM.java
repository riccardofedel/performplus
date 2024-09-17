package com.finconsgroup.performplus.rest.api.vm.v2.attivitagestionali;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class AttivitaGestionaleVM extends CreaAttivitaGestionaleRequest{
	
	@NotNull
	private Long id;

	public AttivitaGestionaleVM id(Long id) {
		this.id=id;
		return this;
	}
	public AttivitaGestionaleVM denominazione(String denominazione) {
		setDenominazione(denominazione);
		return this;
	}
	public AttivitaGestionaleVM codice(String codice) {
		setCodice(codice);
		return this;
	}
	public AttivitaGestionaleVM descrizione(String descrizione) {
		setDescrizione(descrizione);
		return this;
	}
	public AttivitaGestionaleVM inizioValidita(LocalDate inizioValidita) {
		setInizioValidita(inizioValidita);
		return this;
	}
	public AttivitaGestionaleVM fineValidita(LocalDate fineValidita) {
		setFineValidita(fineValidita);
		return this;
	}
}
