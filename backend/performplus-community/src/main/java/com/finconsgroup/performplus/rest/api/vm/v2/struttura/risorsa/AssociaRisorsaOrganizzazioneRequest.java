package com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class AssociaRisorsaOrganizzazioneRequest implements Serializable{
	@NotNull
	private Long idRisorsa;
	@NotNull
	private Long idOrganizzazione;
	private Integer disponibilita;

}
