package com.finconsgroup.performplus.rest.api.vm;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class QuadraturaTestoRequest extends QuadraturaRequest {
	@NotBlank
	private String testo;
}
