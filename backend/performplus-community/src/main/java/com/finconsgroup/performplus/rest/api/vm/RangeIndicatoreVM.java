package com.finconsgroup.performplus.rest.api.vm;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

@ToString
public class RangeIndicatoreVM extends EntityVM{

	private Double minimo;
	private Double massimo;
	private Double valore;
	private Boolean proporzionale;
	private EntityVM indicatorePiano;

}
