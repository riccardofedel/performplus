package com.finconsgroup.performplus.service.dto;

import com.finconsgroup.performplus.enumeration.TipoNodo;


public class AzioneDTO extends NodoPianoDTO {


	private static final long serialVersionUID = 8289653425269562027L;
	public AzioneDTO() {
	    	setTipoNodo(TipoNodo.AZIONE);
	     }
	public AzioneDTO(Long idEnte, Integer anno) {
		super(idEnte, anno, TipoNodo.AZIONE);
	}

}
