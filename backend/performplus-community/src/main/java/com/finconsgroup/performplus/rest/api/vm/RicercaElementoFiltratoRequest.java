package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class RicercaElementoFiltratoRequest implements  Serializable{

	private Long idEnte=0l;
	@NotNull
	private Integer anno;
	@NotEmpty
	private  List<Long> filtro;
}
