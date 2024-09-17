package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class PrioritaRisorseDTO implements Serializable,
		Comparable<PrioritaRisorseDTO> {
	private NodoPianoDTO nodoPiano;
	private List<RisorsaUmanaNodoPianoDTO> risorse=new ArrayList<>();
	private Map <Long,Integer> map=new HashMap<>();
	private PrioritaOperativaDTO prioritaOperativa;
	private PrioritaPesata prioritaPesata;
 
	public PrioritaRisorseDTO(){
		super();
	}

	public PrioritaRisorseDTO(List<RisorsaUmanaDTO> risorseUmane) {
		super();
		int k=0;
		for (RisorsaUmanaDTO r : risorseUmane) {
			map.put(r.getId(),k++);
			risorse.add(new RisorsaUmanaNodoPianoDTO());
		}
	}

	public NodoPianoDTO getNodoPiano() {
		return nodoPiano;
	}

	public void setNodoPiano(NodoPianoDTO nodoPiano) {
		this.nodoPiano = nodoPiano;
	}


	public List<RisorsaUmanaNodoPianoDTO> getRisorse() {
		return risorse;
	}

	public void setRisorse(List<RisorsaUmanaNodoPianoDTO> risorse) {
		for (RisorsaUmanaNodoPianoDTO rn : risorse) {
			addRisorsa(rn);
		}
	}

	@Override
	public int compareTo(PrioritaRisorseDTO o) {
		return getNodoPiano().compareTo(o.getNodoPiano());
	}

	public void setPrioritaOperativa(PrioritaOperativaDTO prioritaOperativa) {
		this.prioritaOperativa=prioritaOperativa;
		
	}

	public PrioritaOperativaDTO getPrioritaOperativa() {
		return prioritaOperativa;
	}


	public Integer getIndex(RisorsaUmanaDTO risorsa) {
		return map.get(risorsa.getId());
	}

	public RisorsaUmanaNodoPianoDTO getRisorsa(RisorsaUmanaDTO risorsa) {
		Integer index = getIndex(risorsa);
		if(index==null)return null;
		return risorse.get(index);
	}

	public void addRisorsa(RisorsaUmanaNodoPianoDTO rn) {
		Integer index=map.get(rn.getRisorsaUmana().getId());
		risorse.set(index, rn);
	}

	public Map<Long, Integer> getMap() {
		return map;
	}

	public PrioritaPesata getPrioritaPesata() {
		return prioritaPesata;
	}

	public void setPrioritaPesata(PrioritaPesata prioritaPesata) {
		this.prioritaPesata = prioritaPesata;
	}

	public OrganizzazioneDTO getOrganizzazione() {
		if(getNodoPiano()!=null)
			return getNodoPiano().getOrganizzazione();
		return null;
	}

}
