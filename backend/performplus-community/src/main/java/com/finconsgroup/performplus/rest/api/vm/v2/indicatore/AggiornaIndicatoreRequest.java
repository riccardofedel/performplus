package com.finconsgroup.performplus.rest.api.vm.v2.indicatore;

import java.io.Serializable;

import com.finconsgroup.performplus.enumeration.CalcoloConsuntivazione;
import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.enumeration.TipoFormula;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class AggiornaIndicatoreRequest implements Serializable{
	
	@NotBlank
	private String denominazione;
	private String descrizione;
	private String nomeValoreA;
	private String nomeValoreB;
	@NotNull
	private TipoFormula formula;
	private Boolean percentuale=false;
	private RaggruppamentoIndicatori raggruppamento=RaggruppamentoIndicatori.SPECIFICO;
	private CalcoloConsuntivazione calcoloConsuntivazione=CalcoloConsuntivazione.DEFAULT;
	private Integer decimali;
	private Integer decimaliA;
	private Integer decimaliB;

	private Long idEnte=0l;


}
