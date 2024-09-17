package com.finconsgroup.performplus.service.dto;


import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class RisorsaUmanaNodoPianoDTO extends EntityDTO 
implements Comparable<RisorsaUmanaNodoPianoDTO> 
{
	
	private NodoPianoDTO nodoPiano;
	private RisorsaUmanaDTO risorsaUmana;
	private BigDecimal disponibilita;
	private List<OrganizzazioneEasyDTO> organizzazioniAppartenenza;
	private PrioritaPesata prioritaPesata;

	
	
	@Override
	public int compareTo(RisorsaUmanaNodoPianoDTO r) {
		int ret=getRisorsaUmana().getCognomeNome().compareTo(r.getRisorsaUmana().getCognomeNome());
		if(ret==0)
			ret=getNodoPiano().getCodiceCompleto().compareTo(r.getNodoPiano().getCodiceCompleto());
		return ret;
	}
    
	public String getCognomeNome(){
		return getRisorsaUmana().getCognomeNome();
	}
	public String getObiettivo(){
		return getNodoPiano().getCodiceRidotto()+" "+getNodoPiano().getDenominazione();
	}

	
}
