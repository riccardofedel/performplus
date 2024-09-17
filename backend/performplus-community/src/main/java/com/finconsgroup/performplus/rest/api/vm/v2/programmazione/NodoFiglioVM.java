package com.finconsgroup.performplus.rest.api.vm.v2.programmazione;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class NodoFiglioVM extends EntityVM {
	private Integer anno;
	private String denominazione;
	private String struttura;
	private String tipoNodo;
	private String codice;
	private String codiceCompleto;
	private String responsabile;
	private Long idStruttura;
	
}
