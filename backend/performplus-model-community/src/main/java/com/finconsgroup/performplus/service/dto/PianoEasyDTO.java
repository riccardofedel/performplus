package com.finconsgroup.performplus.service.dto;

import java.util.Date;

import com.finconsgroup.performplus.enumeration.StatoPiano;
import com.finconsgroup.performplus.enumeration.TipoPiano;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode

public class PianoEasyDTO extends NodoPianoEasyDTO {

	private static final long serialVersionUID = -4833956628278775898L;

	private TipoPiano tipoPiano;
	private StatoPiano stato;
	private Integer annoInizio;
	private Integer annoFine;
	private Date approvazione;


}
