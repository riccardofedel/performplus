package com.finconsgroup.performplus.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

@SuppressWarnings("serial")
public class PrioritaOperativaItem implements Serializable {
	/**
	 * 
	 */
	private BigDecimal prioritaTotale;
	private boolean conObiettivi;
	private Serializable info;
	private List<PrioritaOperativaItem> children=new ArrayList<>();

	public PrioritaOperativaItem(PrioritaOperativaDTO info) {
		this.info=info;
	}

	public PrioritaOperativaItem(OrganizzazioneDTO info) {
		this.info=info;
		prioritaTotale = BigDecimal.ZERO;
	}

	public PrioritaOperativaDTO getPrioritaOperativa() {
		if (getUserObject() instanceof PrioritaOperativaDTO ele)
			return ele;
		return null;
	}

	public NodoPianoDTO getNodoPiano() {
		if (getUserObject() instanceof PrioritaOperativaDTO ele)
			return ele.getNodoPiano();
		return null;
	}

	public OrganizzazioneDTO getOrganizzaione() {
		if (getUserObject() instanceof OrganizzazioneDTO ele)
			return ele;
		return null;
	}

	public Long getIdEnte() {
		if (getNodoPiano() != null)
			return getNodoPiano().getIdEnte();
		if (getOrganizzaione() != null)
			return getOrganizzaione().getIdEnte();
		if (getPrioritaOperativa() != null)
			return getPrioritaOperativa().getOrganizzazione().getIdEnte();
		return 0l;
	}

	@Override
	public String toString() {
		if (getNodoPiano() != null)
			return getNodoPiano().getDenominazione();
		if (getOrganizzaione() != null)
			return getOrganizzaione().getIntestazione();
		return "";
	}

	public String getNome() {
		String denom = null;
		if (getNodoPiano() != null)
			denom = getTipo() + " " + getNodoPiano().getDenominazione();
		if (getOrganizzaione() != null)
			denom = getTipo() + " " + getOrganizzaione().getIntestazione();
		return denom;
	}

	public String getTipo() {
		if (getNodoPiano() != null) {
			return getNodoPiano().getTipoNodo().name();
		}
		if (getOrganizzaione() != null)
			return getOrganizzaione().getLivello().name();
		return null;
	}

	public String getInizio() {
		if (getNodoPiano() == null)
			return null;
		return ModelHelper.toString(getNodoPiano().getInizio());
	}

	public String getFine() {
		if (getNodoPiano() == null)
			return null;
		return ModelHelper.toString(getNodoPiano().getScadenza());
	}


	public BigDecimal getPriorita() {
		if (getPrioritaOperativa() == null || getNodoPiano() == null)
			return getPrioritaTotale();
		return getPrioritaOperativa().getPriorita();
	}

	public BigDecimal getPrioritaTotale() {
		if (isConObiettivi())
			return prioritaTotale;
		return null;
	}


	public void setPrioritaTotale(BigDecimal prioritaTotale) {
		this.prioritaTotale = prioritaTotale;
	}

	public void setConObiettivi(boolean conObiettivi) {
		this.conObiettivi = conObiettivi;
	}

	public boolean isConObiettivi() {
		return conObiettivi;
	}
	public void add(PrioritaOperativaItem node) {
		children.add(node);
		
	}

	public int getChildCount() {
		return children.size();
	}

	public PrioritaOperativaItem getChildAt(int i) {
		return children.get(i);
	}
	
	public Serializable getUserObject() {
		return info;
	}

}