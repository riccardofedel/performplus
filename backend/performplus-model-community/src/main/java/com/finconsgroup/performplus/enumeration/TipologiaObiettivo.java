package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum TipologiaObiettivo {
	AZIONE_LEGISLATIVO, MISURA_SEMPLIFICAZIONE, PROGRAMMA_EUROPEO, AGENDA_DIGITALE;
	
	public final String label;

	private TipologiaObiettivo() {
		this.label = ModelHelper.normalize(this.name());
	}
	private TipologiaObiettivo(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}