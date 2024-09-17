package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum TipoNodo {
	PIANO("PIANO"), 
	AREA("Area strategica"),
	OBIETTIVO("Obiettivo operativo"),
	AZIONE("Azione"),
	FASE("Sub-Azione");
	

	public final String label;

	private TipoNodo() {
		this.label = ModelHelper.normalize(this.name());
	}
	private TipoNodo(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
