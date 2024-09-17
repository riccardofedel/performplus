package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class IndicatoreRangeVM extends EntityVM{
	private Double minimo;
	private Double massimo;
	private Double valore;
	private Boolean proporzionale;

}
