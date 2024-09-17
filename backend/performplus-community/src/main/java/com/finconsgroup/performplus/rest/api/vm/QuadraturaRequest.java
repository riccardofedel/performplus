package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class QuadraturaRequest implements Serializable {
	@NotNull
	private Long id;
	private List<Long> filtro;
}
