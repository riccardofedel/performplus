package com.finconsgroup.performplus.rest.api.vm.v2.programmazione;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class InvalidaRequest extends ElencoAzioni{
	String note;
}
