package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum TipoValutazione implements EnumWithLabel{
     CONSUNTIVO("Rendicontazione"),PREVENTIVO("Target"),STORICO("Storico"),BENCHMARK("Benchmark");
	public final String label;

	private TipoValutazione() {
		this.label = ModelHelper.normalize(this.name());
	}
	private TipoValutazione(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
