package com.finconsgroup.performplus.rest.api.vm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class DecodificaVM extends EntityVM {
	private String codice;
	private String descrizione;
	public DecodificaVM() {}
	public DecodificaVM(Long id, String codice, String descrizione) {
		setId(id);
		this.codice=codice;
		this.descrizione=descrizione;
	}


}
