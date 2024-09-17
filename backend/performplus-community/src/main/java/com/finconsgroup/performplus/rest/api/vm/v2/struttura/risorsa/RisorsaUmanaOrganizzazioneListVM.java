package com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa;

import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaListVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class RisorsaUmanaOrganizzazioneListVM extends RisorsaUmanaListVM{
	private long idRisorsa;
	private Long idOrganizzazione;
	private Integer disponibilita;

}
