package com.finconsgroup.performplus.service.dto;

import com.finconsgroup.performplus.enumeration.TipoNodo;


public class LineaStrategicaDTO extends NodoPianoDTO {

	private static final long serialVersionUID = -4833956628278775898L;

	public LineaStrategicaDTO() {
		super();
		setTipoNodo(TipoNodo.AREA);
	}
	public LineaStrategicaDTO(Long idEnte, Integer anno) {
		super(idEnte,anno,TipoNodo.AREA);
    }
 
 
}
