package com.finconsgroup.performplus.rest.api.vm.v2;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class PersonaVM extends EntityVM{
	private String codiceInterno;
	private String cognome;
	private String nome;
	private String codiceFiscale;


}
