package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum Dimensione implements EnumWithLabel{
	UTENZA("Utenza"),
	ECONOMIA("Economico-finanziaria"),
	INNOVAZIONE("Innovazione e sostenibilità");
	public final String label;

	private Dimensione() {
		this.label = ModelHelper.normalize(this.name());
	}
	private Dimensione(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
