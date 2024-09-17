package com.finconsgroup.performplus.rest.api.vm;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finconsgroup.performplus.enumeration.StatoPiano;
import com.finconsgroup.performplus.enumeration.TipoPiano;

import lombok.Data;
import lombok.EqualsAndHashCode;


@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class PianoVM extends NodoPianoVM{

	private Integer annoInizio;
	private Integer annoFine;
	private LocalDate approvazione;
	private TipoPiano tipoPiano;
	private StatoPiano stato;


	@JsonIgnore
	@Override
	public String getCodiceCompleto() {
		return getCodice();
	}
}

