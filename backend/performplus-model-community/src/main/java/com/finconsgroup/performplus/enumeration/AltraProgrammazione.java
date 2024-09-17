package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum AltraProgrammazione implements EnumWithLabel{
	STRATEGIA_REGIONALE("Strategia regionale di sviluppo sostenibile"), 
	PROGRAMMAZIONE_EUROPEA("Programmazione europea"), 
	PNRR("PNRR");
	public final String label;

	private AltraProgrammazione() {
		this.label = ModelHelper.normalize(this.name());
	}
	private AltraProgrammazione(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
