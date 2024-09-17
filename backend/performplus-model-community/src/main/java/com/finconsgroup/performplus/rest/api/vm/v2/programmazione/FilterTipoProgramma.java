package com.finconsgroup.performplus.rest.api.vm.v2.programmazione;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class FilterTipoProgramma implements Serializable {
	@NotNull
	private Long idEnte;
	private String testo;
	private List<Long> tipiMissione;

}