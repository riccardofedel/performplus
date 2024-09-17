package com.finconsgroup.performplus.rest.api.vm.v2.attivitagestionali;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class AggiornaAttivitaGestionaleRequest extends CreaAttivitaGestionaleRequest{
	
	@NotNull
	private Long id;
}
