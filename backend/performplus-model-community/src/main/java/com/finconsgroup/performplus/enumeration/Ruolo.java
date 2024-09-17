	package com.finconsgroup.performplus.enumeration;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public enum Ruolo implements EnumWithLabel{
	AMMINISTRATORE ("Amministratore di Sistema"),
	SUPPORTO_SISTEMA("Supporto di Sistema"),		 	//tutto
	DIRETTORE_GENERALE ("Direttore Generale"), 		//solo lettura tutto	
	POSIZIONE_ORGANIZZATIVA ("Posizione Organizzativa"), 	//visibilità su tutto, modifica solo da Azione in giù e per struttura	
	REFERENTE ("Referente"), 					//visibilità su tutto, modifica solo da Azione in giù e per struttura	
	OIV ("OIV"),						// accesso a performance individuale responsabili, solo lettura progr	
	RISORSA ("Risorsa") 					// risorsa associata a obiettivo e performane individuale
	;

	public final String  label;
	
	private Ruolo() {
		this.label=ModelHelper.normalize(this.name());
	}
	private Ruolo(String label) {
		this.label=label;
	}
	@Override
	public String getLabel() {
		return this.label;
	}
}
