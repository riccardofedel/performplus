package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class RendicontazioneRequest {
	private Long idIndicatore;
	private String note;

}
