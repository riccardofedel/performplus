package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class ForzatureRequest implements Serializable {
	@NotNull
	private Long idIndicatorePiano;
	private Double richiestaForzatura;
	private String noteRichiestaForzatura;

}

