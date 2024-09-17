package com.finconsgroup.performplus.service.dto;

import com.finconsgroup.performplus.enumeration.TipoNodo;

@SuppressWarnings("serial")

public class ObiettivoStrategicoDTO extends NodoPianoDTO {
	
	public ObiettivoStrategicoDTO() {
		super();
		setTipoNodo(TipoNodo.OBIETTIVO);
	}
	public ObiettivoStrategicoDTO(Long idEnte, Integer anno) {
		super(idEnte,anno,TipoNodo.OBIETTIVO);
    }

	
}