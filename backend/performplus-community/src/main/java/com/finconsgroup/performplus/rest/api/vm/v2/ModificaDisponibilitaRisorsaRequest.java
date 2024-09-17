package com.finconsgroup.performplus.rest.api.vm.v2;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class ModificaDisponibilitaRisorsaRequest extends EntityVM{
	private Integer disponibilita;

}
