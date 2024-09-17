package com.finconsgroup.performplus.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class RisorsaStrumentaleOrganizzazioneDTO extends EntityDTO
		implements Comparable<RisorsaStrumentaleOrganizzazioneDTO> {

	private Integer disponibilita;
	private OrganizzazioneDTO organizzazione;
	private RisorsaStrumentaleDTO risorsaStrumentale;



	@Override
	public int compareTo(RisorsaStrumentaleOrganizzazioneDTO o) {
		
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
