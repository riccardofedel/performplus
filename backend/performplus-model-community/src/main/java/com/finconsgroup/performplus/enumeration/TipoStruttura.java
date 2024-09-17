package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum TipoStruttura implements EnumWithLabel{
    OSPEDALIERA, TERRITORIALE;
    
	public final String label;

	private TipoStruttura() {
		this.label = ModelHelper.normalize(this.name());
	}
	private TipoStruttura(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
