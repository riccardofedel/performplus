package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum TipoObiettivo {
    AZIONE_STRATEGICA("Azione strategica")
    , AZIONE_MIGLIORAMENTO("Azione di miglioramento")
    , OBIETTIVO_AZIENDALE("Obiettivo aziendale")
    , OBIETTIVO_TRASVERSALE("Obiettivo trasversale")
    , ATTIVITA_ORDINARIA("Attività ordinaria");
 
	public final String label;

	private TipoObiettivo() {
		this.label = ModelHelper.normalize(this.name());
	}
	private TipoObiettivo(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
