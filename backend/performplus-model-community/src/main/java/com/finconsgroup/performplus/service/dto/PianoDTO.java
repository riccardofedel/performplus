package com.finconsgroup.performplus.service.dto;

import com.finconsgroup.performplus.enumeration.TipoNodo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class PianoDTO extends NodoPianoDTO {

	public PianoDTO(Long idEnte, Integer anno, TipoNodo tipoNodo) {
		super(idEnte, anno, TipoNodo.PIANO);
	}
	public PianoDTO() {
		super();
		setTipoNodo(TipoNodo.PIANO);
	}
	public PianoDTO(Long idEnte, Integer anno) {
		super(idEnte,anno,TipoNodo.PIANO);
    }
 
 
}
