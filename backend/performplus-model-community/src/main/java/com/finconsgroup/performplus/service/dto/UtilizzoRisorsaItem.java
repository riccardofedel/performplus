package com.finconsgroup.performplus.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;



public class UtilizzoRisorsaItem implements Serializable {
	private static final long serialVersionUID = 5027268954805640976L;
	private boolean conObiettivi;
	private BigDecimal disponibilitaTotale;
	private Serializable info;
	private UtilizzoRisorsaItem parent;
	private List<UtilizzoRisorsaItem> children=new ArrayList<>();

	public UtilizzoRisorsaItem(RisorsaUmanaNodoPianoDTO info) {
		this.info=info;
		disponibilitaTotale = BigDecimal.ZERO;
	}

	public UtilizzoRisorsaItem(RisorsaUmanaDTO info) {
		this.info=info;
		disponibilitaTotale = BigDecimal.ZERO;
	}

	public UtilizzoRisorsaItem(TipoNodo info) {
		this.info=info;
		disponibilitaTotale = BigDecimal.ZERO;
	}

	public UtilizzoRisorsaItem() {
	}

	public RisorsaUmanaNodoPianoDTO getRisorsaUmanaNodoPiano() {
		if (getUserObject() instanceof RisorsaUmanaNodoPianoDTO ele)
			return ele;
		return null;
	}

	public boolean isRoot(){
		return getParent()==null;
	}
	

	public NodoPianoDTO getNodoPiano() {
		if (getUserObject() instanceof RisorsaUmanaNodoPianoDTO ele)
			return ele.getNodoPiano();
		return null;
	}

	public TipoNodo getTipoNodo() {
		if (getUserObject() instanceof TipoNodo ele)
			return ele;
		return null;
	}

	public RisorsaUmanaDTO getRisorsaUmana() {
		if (getUserObject() instanceof RisorsaUmanaDTO ele)
			return ele;
		return null;
	}

	public Long getIdEnte() {
		if (getNodoPiano() != null)
			return getNodoPiano().getIdEnte();
		if (getRisorsaUmana() != null)
			return getRisorsaUmana().getIdEnte();
		if (getRisorsaUmanaNodoPiano() != null)
			return getRisorsaUmanaNodoPiano().getRisorsaUmana().getIdEnte();
		return 0l;
	}

	@Override
	public String toString() {
		if (getNodoPiano() != null)
			return getNodoPiano().getDenominazione();
		if (getRisorsaUmana() != null)
			return getRisorsaUmana().getCognomeNome();
		return "";
	}

	public String getNome() {
		String denom = null;
		if (getNodoPiano() != null)
			denom = getTipo() + " " + getNodoPiano().getDenominazione();
		if (getRisorsaUmana() != null)
			denom = getRisorsaUmana().getCognomeNome();
		if (getTipoNodo() != null)
			denom = getTipoNodo().name();

		return denom;
	}

	public String getTipo() {
		if (getNodoPiano() != null) {
			return getNodoPiano().getTipoNodo().name();
		}
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

	public BigDecimal getDisponibilita() {
		if (getRisorsaUmanaNodoPiano() == null || getNodoPiano() == null)
			return getDisponibilitaTotale();
		return getRisorsaUmanaNodoPiano().getDisponibilita();
	}

	public BigDecimal getDisponibilitaTotale() {
		if (isConObiettivi())
			return disponibilitaTotale;
		return null;
	}

	public void setDisponibilitaTotale(BigDecimal disponibilitaTotale) {
		this.disponibilitaTotale = disponibilitaTotale;
	}

	public void setConObiettivi(boolean conObiettivi) {
		this.conObiettivi = conObiettivi;
	}

	public boolean isConObiettivi() {
		return conObiettivi;
	}
	public int getChildCount() {
		return children.size();
	}

	public UtilizzoRisorsaItem getChildAt(int i) {
		return children.get(i);
	}
	
	public Serializable getUserObject() {
		return info;
	}
	public UtilizzoRisorsaItem getParent() {
		return parent;
	}
	public void setParent(UtilizzoRisorsaItem parent) {
		this.parent=parent;
	}

	public void add(UtilizzoRisorsaItem node) {
		node.setParent(this);
		children.add(node);
	}
}