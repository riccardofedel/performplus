package com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class AssociaRisorseRequest implements Serializable{
	@NotNull
	private Long id;
	@NotEmpty
	private List<Long> selezionati;
}
