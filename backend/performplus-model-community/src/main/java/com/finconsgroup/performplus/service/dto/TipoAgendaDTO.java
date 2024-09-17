package com.finconsgroup.performplus.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class TipoAgendaDTO extends EntityDTO {
	private String codice;
	private String descrizione;

	public String getCodiceEDescrizione() {
		return codice + " " + descrizione;
	}
}
