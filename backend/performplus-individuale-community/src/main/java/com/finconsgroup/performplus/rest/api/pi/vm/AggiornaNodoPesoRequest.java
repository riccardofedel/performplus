package com.finconsgroup.performplus.rest.api.pi.vm;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class AggiornaNodoPesoRequest {
	@NotNull
	private Float peso;
}
