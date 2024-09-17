package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;

import com.finconsgroup.performplus.enumeration.Movement;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class SpostaNodoRequest implements Serializable {

	@NotNull
	private Long id;
	@NotNull
	private Long nuovoPadre;
	@NotNull
	private Long organizzazione;
}
