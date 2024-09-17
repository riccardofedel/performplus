package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum TipologiaObiettiviOperativi {
	IMPATTO("Obiettivo di impatto"), MANTENIMENTO("Obiettivo di mantenimento"),
	SVILUPPO("Obiettivo di sviluppo");

	public final String label;

	private TipologiaObiettiviOperativi() {
		this.label = ModelHelper.normalize(this.name());
	}

	private TipologiaObiettiviOperativi(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
