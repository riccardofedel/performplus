package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class ForzatureAdminRequest implements Serializable {
	@NotNull
	private Long idIndicatorePiano;
	private Double raggiungimentoForzato;
	private Boolean nonValutabile;
	private String noteRaggiungimentoForzato;

}
