package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class PesaturaNodoListVM extends EntityVM{
	private String codiceRidotto;
	private String denominazione;
	private TipoNodo tipoNodo;
	private Double pesoRelativo;
	private LocalDate inizio;
	private LocalDate scadenza;
	private Double peso;
	public PesaturaNodoListVM() {}
	public PesaturaNodoListVM(Long id,String codiceRidotto, String denominazione, TipoNodo tipoNodo, LocalDate inizio, LocalDate scadenza,
			Double peso) {
		super(id);
		this.codiceRidotto = codiceRidotto;
		this.denominazione = denominazione;
		this.tipoNodo = tipoNodo;
		this.inizio = inizio;
		this.scadenza = scadenza;
		this.peso = peso;
	}
	
}
