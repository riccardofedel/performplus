package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class ForzaturaNodoVM implements Serializable{
	@NotNull
	private Long idNodoPiano;
	private Double forzaturaRisorse;
	private Double forzaturaResponsabili;

}
