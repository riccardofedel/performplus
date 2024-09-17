package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;

import com.finconsgroup.performplus.enumeration.TipoVariazione;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@SuppressWarnings("serial")
@Data
public class VariazioneAttributoRequest implements Serializable{
	@NotNull
	 private Long idEnte=0l;
	@NotNull
	private  Long idPiano;
	@NotNull
	private  Serializable dto;
	@NotNull
	private Serializable old;
	@NotNull
	private TipoVariazione tipoVariazione;
}
