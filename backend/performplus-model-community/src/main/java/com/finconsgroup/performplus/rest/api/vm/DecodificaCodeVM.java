package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class DecodificaCodeVM implements Serializable {
	private String codice;
	private String descrizione;
	public DecodificaCodeVM() {}
	public DecodificaCodeVM(String codice, String descrizione) {
		this.codice=codice;
		this.descrizione=descrizione;
	}
	public DecodificaCodeVM(Enum<?> e) {
		this(e.name(),ModelHelper.normalize(e.name()));
	}

}
