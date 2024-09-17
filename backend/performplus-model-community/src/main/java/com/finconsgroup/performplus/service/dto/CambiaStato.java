package com.finconsgroup.performplus.service.dto;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finconsgroup.performplus.enumeration.TipoPiano;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class CambiaStato implements Serializable {
   
	private EntityVM piano;
	private AzionePiano azione;

	@JsonIgnore
	public TipoPiano getTipoPiano() {
	    switch(azione){
	    case APPROVATO:return null;
	    case VARIAZIONE: return TipoPiano.VARIAZIONE;
	    case RELAZIONE:return TipoPiano.RELAZIONE;
	    case CAMBIO_ANNO: return TipoPiano.PIANO;
	    case COMPLETATO: return null;
	    default:
		return null;
	    }
	}



}
