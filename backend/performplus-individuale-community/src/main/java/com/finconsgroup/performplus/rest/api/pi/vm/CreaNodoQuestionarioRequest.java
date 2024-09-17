package com.finconsgroup.performplus.rest.api.pi.vm;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class CreaNodoQuestionarioRequest extends AggiornaNodoQuestionarioRequest{
	@NotNull
	TipoNodoQuestionario tipo;
	TipoNodoQuestionario tipoPadre;
	private Long idPadre;
	
}
