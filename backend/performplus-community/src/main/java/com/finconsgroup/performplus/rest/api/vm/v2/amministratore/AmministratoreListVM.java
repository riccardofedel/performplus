package com.finconsgroup.performplus.rest.api.vm.v2.amministratore;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class AmministratoreListVM  extends EntityVM {

	private String nome;
	private String cognome;
	private String codiceInterno;
	private String funzione;
	
}
