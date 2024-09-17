package com.finconsgroup.performplus.service.dto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class RisorsaUmanaOrganizzazioneDTO extends EntityDTO implements Comparable<RisorsaUmanaOrganizzazioneDTO>{
	private Integer disponibilita;
	private OrganizzazioneDTO organizzazione;
	private RisorsaUmanaDTO risorsaUmana;
	private Date inizioValidita;
	private Date fineValidita;
	private String organizzazioneSettore;
	private String organizzazioneServizio;
	private String organizzazioneUfficio;


	
	
	@Override
	public int compareTo(RisorsaUmanaOrganizzazioneDTO o) {
		
		if (o == this)
			return 0;
		if (o == null)
			return -1;

		int risultato = 0;
		
		if(getOrganizzazione()!=null)
			risultato = o.getOrganizzazione().getLivello().compareTo(getOrganizzazione().getLivello());	
		
		return risultato;
	}


}
