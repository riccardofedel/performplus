package com.finconsgroup.performplus.service.dto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public class PrioritaOperativaReport {
	private BigDecimal prioritaTotale;
	private boolean conObiettivi;
	private PrioritaOperativaDTO prioritaOperativa;
	private OrganizzazioneDTO organizzazione;
	private List<PrioritaOperativaReport> figli = new ArrayList<>();

	public PrioritaOperativaReport(PrioritaOperativaDTO info) {
		prioritaOperativa = info;
	}

	public PrioritaOperativaReport(OrganizzazioneDTO info) {
		organizzazione = info;
		prioritaTotale = BigDecimal.ZERO;
	}

	public PrioritaOperativaDTO getPrioritaOperativa() {
		return prioritaOperativa;
	}

	public NodoPianoDTO getNodoPiano() {
		if (prioritaOperativa != null)
			return prioritaOperativa.getNodoPiano();
		return null;
	}

	public OrganizzazioneDTO getOrganizzaione() {
		return organizzazione;
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
		if (denom == null)
			return null;
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

	public OrganizzazioneDTO getOrganizzazione() {
		return organizzazione;
	}

	public void setOrganizzazione(OrganizzazioneDTO organizzazione) {
		this.organizzazione = organizzazione;
	}

	public List<PrioritaOperativaReport> getFigli() {
		return figli;
	}

	public void setFigli(List<PrioritaOperativaReport> figli) {
		this.figli = figli;
	}

	public void add(PrioritaOperativaReport p) {
		figli.add(p);
	}

	public void setPrioritaOperativa(PrioritaOperativaDTO prioritaOperativa) {
		this.prioritaOperativa = prioritaOperativa;
	}

	public String getOrganizzazioneSettore() {
		if (organizzazione == null||organizzazione.getLivello()==null)
			return null;
		switch (organizzazione.getLivello()) {
		case SUPERIORE:
			return organizzazione.getCodice() + " " + organizzazione.getIntestazione();
		case MEDIO:
			return organizzazione.getPadre().getCodice() + " " + organizzazione.getPadre().getIntestazione();
		case INFERIORE:
			return organizzazione.getPadre().getPadre().getCodice() + " "
					+ organizzazione.getPadre().getPadre().getIntestazione();
		}
		return null;
	}

	public String getOrganizzazioneServizio() {
		if (organizzazione == null||organizzazione.getLivello()==null)
			return null;
		switch (organizzazione.getLivello()) {
		case MEDIO:
			return organizzazione.getCodice() + " " + organizzazione.getIntestazione();
		case INFERIORE:
			return organizzazione.getPadre().getCodice() + " " + organizzazione.getPadre().getIntestazione();
		}
		return null;
	}

	public String getOrganizzazioneUfficio() {
		if (organizzazione == null||organizzazione.getLivello()==null)
			return null;
		switch (organizzazione.getLivello()) {
		case INFERIORE:
			return organizzazione.getCodice() + " " + organizzazione.getIntestazione();
		}
		return null;
	}

	public String getDenominazione() {
		if (getNodoPiano() == null)
			return null;

		return getNodoPiano().getCodiceRidotto() + " " + getNodoPiano().getTipoNodo().name() + " "
				+ getNodoPiano().getDenominazione();
	}

	public String getIntestatzione() {
		if (getOrganizzaione() == null)
			return null;

		return getOrganizzaione().getCodiceRidotto() + " " + getOrganizzaione().getLivello().name() + " "
				+ getOrganizzaione().getIntestazione();
	}
}