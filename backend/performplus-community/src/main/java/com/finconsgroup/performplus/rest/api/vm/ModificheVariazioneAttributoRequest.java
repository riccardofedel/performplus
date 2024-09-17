package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class ModificheVariazioneAttributoRequest implements Serializable{
	@NotNull
	private Long idEnte=0l;
	@NotNull
	private Long idPiano;
	@NotNull
	private Object dto;
}
