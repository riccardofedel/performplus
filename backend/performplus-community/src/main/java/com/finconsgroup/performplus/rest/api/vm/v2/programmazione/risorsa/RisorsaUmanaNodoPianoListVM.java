package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.risorsa;

import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaListVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class RisorsaUmanaNodoPianoListVM extends RisorsaUmanaListVM{
	private long idRisorsa;
	private Long idNodoPiano;
	private Integer disponibilita;

}
