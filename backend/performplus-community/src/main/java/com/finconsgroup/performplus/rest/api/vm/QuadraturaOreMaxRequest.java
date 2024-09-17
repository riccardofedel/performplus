package com.finconsgroup.performplus.rest.api.vm;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class QuadraturaOreMaxRequest extends QuadraturaRequest {
	@NotNull
	private Integer oreSettimanaliMax;
}
