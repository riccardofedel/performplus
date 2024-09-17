package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum Livello implements EnumWithLabel{
    ENTE("Community"), SUPERIORE("Direzione"), MEDIO("Dipartimento"), INFERIORE("Unità"), CDC;
    
	public final String label;

	private Livello() {
		this.label = ModelHelper.normalize(this.name());
	}
	private Livello(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
