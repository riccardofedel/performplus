package com.finconsgroup.performplus.rest.api.vm.v2.programmazione;

import java.util.List;

import com.finconsgroup.performplus.enumeration.TipoNodo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class CreaNodoPianoRequest extends AggiornaNodoPianoRequest {
	@NotNull
	private Long idPadre;
	
	private TipoNodo tipoNodo;
	
	public String codiceRidottoPadre;
	
	public CreaNodoPianoRequest fields(List<String> fields) {
		setFields(fields); 
		return this;
	}
}
