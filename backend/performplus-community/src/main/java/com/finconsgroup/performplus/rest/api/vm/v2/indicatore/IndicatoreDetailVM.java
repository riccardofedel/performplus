package com.finconsgroup.performplus.rest.api.vm.v2.indicatore;

import com.finconsgroup.performplus.enumeration.CalcoloConsuntivazione;
import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class IndicatoreDetailVM extends EntityVM{
	
	private String denominazione;
	private String descrizione;
	private String nomeValoreA;
	private String nomeValoreB;
	private TipoFormula formula;
	private Boolean percentuale=false;
	private Boolean decrescente=false;
	private RaggruppamentoIndicatori raggruppamento=RaggruppamentoIndicatori.SPECIFICO;
	private Integer decimali;
	private Integer decimaliA;
	private Integer decimaliB;
	private CalcoloConsuntivazione calcoloConsuntivazione=CalcoloConsuntivazione.DEFAULT;
	

}
