package com.finconsgroup.performplus.service.dto;

import java.util.ArrayList;
import java.util.List;

import com.finconsgroup.performplus.service.business.utils.OrganizzazioneHelper;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class OrganizzazioneDTO extends OrganizzazioneEasyDTO{

	private String descrizione;
	private List<OrganizzazioneDTO> figli;

	public void add(OrganizzazioneDTO d) {
		if(figli==null) {
			figli=new ArrayList<>();
		}
		figli.add(d);
	}

	public String getNomeCompleto() {
		return OrganizzazioneHelper.ridotto(getCodiceCompleto())+" "+getIntestazione();
	}
	
	public String getOrganizzazioneSettore() {
		return OrganizzazioneHelper.settore(this);
	}
	public String getOrganizzazioneServizio() {
		return OrganizzazioneHelper.servizio(this);
	}
	public String getOrganizzazioneUfficio() {
		return OrganizzazioneHelper.ufficio(this);
	}
}
