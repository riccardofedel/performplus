package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import com.finconsgroup.performplus.domain.AllegatoIndicatorePiano;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class AllegatoVM extends AllegatoListVM{
	private String base64;
	public AllegatoVM() {}
	public AllegatoVM(AllegatoIndicatorePiano a) {
		super(a);
	}

}
