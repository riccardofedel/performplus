package com.finconsgroup.performplus.service.dto;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class IndicatorePianoEasyDTO extends EntityDTO {

	private NodoPianoEasyDTO nodoPiano;
	private IndicatoreDTO indicatore;
	private String specifica;
	private String specificaNumeratore;
	private String specificaDenominatore;
	private String note;
	private Double peso;
	private Boolean specificaPercentuale;
	private Boolean decrescente;
	private String decrescenteInt; // uso gui
	private Boolean strategico;
	private Boolean sviluppoSostenibile;
	private Double pesoRelativo;
	private LocalDate inizio;
	private LocalDate scadenza;
	private Double raggiungimentoForzato;
	private Boolean nonValutabile;
	private Double richiestaForzatura;
	private String noteRichiestaForzatura;
	private String noteRaggiungimentoForzato;

	private Integer specificaDecimali;
	private Integer specificaDecimaliA;
	private Integer specificaDecimaliB;

	private RaggruppamentoIndicatori raggruppamento = RaggruppamentoIndicatori.SPECIFICO;

	public String getDenominazione() {
		if (getIndicatore() == null)
			return "";
		if (getSpecifica() == null)
			return getIndicatore().getDenominazione();
		else
			return getSpecifica();
	}

	public String getNumeratore() {
		if (getIndicatore() == null)
			return "";
		if (getSpecificaNumeratore() == null)
			return getIndicatore().getNomeValoreA();
		else
			return getSpecificaNumeratore();
	}

	public String getDenominatore() {
		if (getIndicatore() == null)
			return "";
		if (getSpecificaDenominatore() == null)
			return getIndicatore().getNomeValoreB();
		else
			return getSpecificaDenominatore();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof IndicatorePianoEasyDTO ele) {
			if (ele.getId() == null && getId() == null)
				return super.equals(object);
			if (ele.getId() == null)
				return false;
			return getId() != null && getId().longValue() == ele.getId().longValue();
		}
		return false;
	}

	public Boolean getPercentuale() {
		return getSpecificaPercentuale() == null ? getIndicatore().getPercentuale() : getSpecificaPercentuale();
	}

	public void setPercentuale(Boolean b) {
		// SOLO PER REFLECTION HELPER
	}

	public Integer getAnno() {
		if (getNodoPiano() == null)
			return null;
		return getNodoPiano().getAnno();
	}

	public IndicatorePianoEasyDTO raggruppa() {
		if (Boolean.TRUE.equals(getSviluppoSostenibile()) && Boolean.TRUE.equals(getStrategico())) {
			this.raggruppamento = RaggruppamentoIndicatori.STRATEGICO_SOSTENIBILE;
		} else if (Boolean.TRUE.equals(getSviluppoSostenibile())) {
			this.raggruppamento = RaggruppamentoIndicatori.SVILUPPO_SOSTENIBILE;
		} else if (Boolean.TRUE.equals(getStrategico())) {
			this.raggruppamento = RaggruppamentoIndicatori.STRATEGICO;
		} else {
			this.raggruppamento = RaggruppamentoIndicatori.SPECIFICO;
		}

		return this;
	}
}
