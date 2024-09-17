package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum ModalitaAttuative {
    INTERNE("Interne"), 
    ESTERNE("Esterne"),
    MISTE("Miste"), 
    ESTERNE_AZIENDE ("Esterne tramite aziende di servizi pubblici locali"),
    ESTERNE_SOGGETTI ("Esterne tramite soggetti esterni"),
    COLLABORAZIONE_PRIVATI("Collaborazione con soggetti privati"),
    COLLABORAZIONE_NO_PROFIT("Collaborazione con soggetti non profit"),
    COLLABORAZIONE_PUBBLICI("Collaborazione con soggetti pubblici");

	public final String label;

	private ModalitaAttuative() {
		this.label = ModelHelper.normalize(this.name());
	}
	private ModalitaAttuative(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
