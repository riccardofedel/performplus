package com.finconsgroup.performplus.rest.api.vm.v2;

import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class IndicatoreTemplateVM extends EntityVM{

	private String denominazione;
	private TipoFormula formula;
	public IndicatoreTemplateVM() {}
	public IndicatoreTemplateVM(Long id,String denominazione, TipoFormula formula) {
		super(id);
		this.denominazione = denominazione;
		this.formula = formula;
	}
	
}
