package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class StatoAvanzamentoRequest {
	@NotNull
	Integer periodo;
	@NotNull
	Long idNodoPiano;
	@NotBlank
	String statoAvanzamento;
}
