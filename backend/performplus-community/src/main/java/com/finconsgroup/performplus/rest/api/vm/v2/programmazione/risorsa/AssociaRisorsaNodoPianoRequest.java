package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.risorsa;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class AssociaRisorsaNodoPianoRequest implements Serializable{
	@NotNull
	private Long idRisorsa;
	@NotNull
	private Long idNodoPiano;
	private Integer disponibilita;
}
