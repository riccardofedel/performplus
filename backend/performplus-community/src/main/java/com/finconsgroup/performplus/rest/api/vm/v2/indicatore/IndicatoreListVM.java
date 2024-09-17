package com.finconsgroup.performplus.rest.api.vm.v2.indicatore;

import com.finconsgroup.performplus.enumeration.CalcoloConsuntivazione;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class IndicatoreListVM  extends EntityVM {

	private String denominazione;
	private String formula;
	private TipoFormula tipoFormula;
	private Boolean percentuale;
	private Boolean decrescente;
	private String raggruppamento;
	private String calcoloConsuntivazione;
	private CalcoloConsuntivazione tipoCalcoloConsuntivazione;
	
}
