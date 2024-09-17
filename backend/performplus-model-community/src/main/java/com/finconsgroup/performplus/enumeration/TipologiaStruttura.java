package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum TipologiaStruttura implements EnumWithLabel{
    DIPARTIMENTO, STRUTT_COMP("Struttura Complessa"), UOSVD("Unità Operativa semplice a valenza dipartimentale"), UOS("Unità Operativa semplice");
    
	public final String label;

	private TipologiaStruttura() {
		this.label = ModelHelper.normalize(this.name());
	}
	private TipologiaStruttura(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
