package com.finconsgroup.performplus.rest.api.vm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode


public class DecodificaEasyVM extends EntityVM {
	public DecodificaEasyVM() {
		super();
	}
	public DecodificaEasyVM(Long id, String codice) {
		super(id);
		this.codice = codice;
	}
	private String codice;
}
