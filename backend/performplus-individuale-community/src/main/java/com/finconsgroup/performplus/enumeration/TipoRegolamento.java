package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum TipoRegolamento {
     INDIVIDUALE, STRUTTURA, PERFORMANCE;
	public final String label;

	private TipoRegolamento() {
		this.label = ModelHelper.normalize(this.name());
	}
	private TipoRegolamento(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
