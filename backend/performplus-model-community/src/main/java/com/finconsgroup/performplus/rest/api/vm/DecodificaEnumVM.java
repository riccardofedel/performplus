package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;

import com.finconsgroup.performplus.enumeration.EnumWithLabel;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;

import lombok.Data;

@SuppressWarnings("serial")
@Data

public class  DecodificaEnumVM<T extends Enum<?>> implements Serializable{
	private T codice;
	private String descrizione;
	public DecodificaEnumVM() {}

	public DecodificaEnumVM(T codice, String descrizione) {
		super();
		this.codice = codice;
		this.descrizione = descrizione;
	}

	public DecodificaEnumVM(T t) {
		this.codice=t;
		if(t instanceof EnumWithLabel ele) {
			this.descrizione=ele.getLabel();
		}
		if(this.descrizione==null)
			this.descrizione=ModelHelper.normalize(t);
	}
	
	
}
