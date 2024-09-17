package com.finconsgroup.performplus.rest.api.vm.v2.attivitagestionali;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.BilancioListVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class AttivitaGestionaleDetailVM extends AttivitaGestionaleVM{
	

	
	private List<BilancioListVM> bilancio;
	
	private List<DecodificaVM> attiFormali;


	public AttivitaGestionaleDetailVM id(Long id) {
		setId(id);
		return this;
	}
	public AttivitaGestionaleDetailVM denominazione(String denominazione) {
		setDenominazione(denominazione);
		return this;
	}
	public AttivitaGestionaleDetailVM codice(String codice) {
		setCodice(codice);
		return this;
	}
	public AttivitaGestionaleDetailVM descrizione(String descrizione) {
		setDescrizione(descrizione);
		return this;
	}
	public AttivitaGestionaleDetailVM inizioValidita(LocalDate inizioValidita) {
		setInizioValidita(inizioValidita);
		return this;
	}
	public AttivitaGestionaleDetailVM fineValidita(LocalDate fineValidita) {
		setFineValidita(fineValidita);
		return this;
	}
}
