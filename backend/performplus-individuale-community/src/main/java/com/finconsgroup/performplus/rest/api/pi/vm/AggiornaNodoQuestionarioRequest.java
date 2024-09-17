package com.finconsgroup.performplus.rest.api.pi.vm;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class AggiornaNodoQuestionarioRequest extends AggiornaQuestionarioRequest {
	@NotBlank
	private String codice;
	private Boolean foglia = false;
	private BigDecimal peso;
	private BigDecimal pesoMancataAssegnazione;
	private BigDecimal pesoMancatoColloquio;
	private Boolean flagSoloAdmin=false;


}
