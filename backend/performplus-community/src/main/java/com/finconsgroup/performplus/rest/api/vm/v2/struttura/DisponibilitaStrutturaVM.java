package com.finconsgroup.performplus.rest.api.vm.v2.struttura;

import java.io.Serializable;

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.enumeration.Over;
import com.finconsgroup.performplus.service.business.utils.OrganizzazioneHelper;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class DisponibilitaStrutturaVM implements Serializable{
	private Float contributo;
	private Float contributoTemporale;
	private Float contributoEffettivo;
	private String codiceStruttura;
	private String denominazioneStruttura;
	private Over utilizzo;
	public DisponibilitaStrutturaVM(Organizzazione org) {
		codiceStruttura=OrganizzazioneHelper.ridotto(org.getCodiceCompleto());
		denominazioneStruttura=org.getIntestazione();
		utilizzo=Over.UTILIZZO_PARZIALE;
		contributoEffettivo=0f;
		contributoTemporale=0f;
		contributo=0f;
	}
	public DisponibilitaStrutturaVM() {}

}
