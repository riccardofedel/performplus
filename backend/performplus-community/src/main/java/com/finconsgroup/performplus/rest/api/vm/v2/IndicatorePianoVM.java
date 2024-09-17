package com.finconsgroup.performplus.rest.api.vm.v2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finconsgroup.performplus.enumeration.Periodo;
import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.enumeration.TipoConsuntivazione;
import com.finconsgroup.performplus.enumeration.TipoValutazione;
import com.finconsgroup.performplus.rest.api.vm.ConfigurazioneVM;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.rest.api.vm.IndicatoreVM;
import com.finconsgroup.performplus.rest.api.vm.NodoPianoEasyVM;
import com.finconsgroup.performplus.rest.api.vm.RangeIndicatoreVM;
import com.finconsgroup.performplus.rest.api.vm.ValutazioneVM;
import com.finconsgroup.performplus.service.business.utils.ConsuntivoHelper;
import com.finconsgroup.performplus.service.business.utils.MappingIndicatorePianoHelper;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class IndicatorePianoVM extends EntityVM {

	private NodoPianoEasyVM nodoPiano;
	private IndicatoreVM indicatore;
	private String specifica;
	private String specificaNumeratore;
	private String specificaDenominatore;
	private Integer specificaDecimali;
	private Integer specificaDecimaliA;
	private Integer specificaDecimaliB;
	private String note;
	private int nroValutazioni;
	private Double peso;
	private List<ValutazioneVM> consuntivi = new ArrayList<>();
	private List<ValutazioneVM> preventivi = new ArrayList<>();
	private ValutazioneVM storico;
	private ValutazioneVM baseline;
	private ValutazioneVM benchmark;
	private Boolean specificaPercentuale;
	private Boolean decrescente;
	private String decrescenteInt; // uso gui
	private Boolean strategico;
	private Boolean sviluppoSostenibile;
	private List<RangeIndicatoreVM> ranges = new ArrayList<>();
	private Double pesoRelativo;
	private LocalDate inizio;
	private LocalDate scadenza;
	private Double raggiungimentoForzato;
	private Boolean nonValutabile;
	private Double richiestaForzatura;
	private String noteRichiestaForzatura;
	private String noteRaggiungimentoForzato;
	private String unitaMisura;

	
	private RaggruppamentoIndicatori raggruppamento = RaggruppamentoIndicatori.SPECIFICO;

	private ConfigurazioneVM configurazione=new ConfigurazioneVM();


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

	public ValutazioneVM getPrevisione1() {
		return getPrevisione(1);
	}

	public ValutazioneVM getPrevisione2() {
		return getPrevisione(2);
	}

	public ValutazioneVM getPrevisione3() {
		return getPrevisione(3);
	}

	public ValutazioneVM getPrevisione4() {
		return getPrevisione(4);
	}

	public ValutazioneVM getPrevisione5() {
		return getPrevisione(5);
	}

	protected ValutazioneVM getPrevisione(int i) {
		return getPreventivo(i - 1);
	}

	public ValutazioneVM getConsuntivo1() {
		return getConsuntivo(1, configurazione.getTipoConsuntivazione());
	}

	public ValutazioneVM getConsuntivo2() {
		return getConsuntivo(2, configurazione.getTipoConsuntivazione());
	}

	public ValutazioneVM getConsuntivo3() {
		return getConsuntivo(3, configurazione.getTipoConsuntivazione());
	}

	public ValutazioneVM getConsuntivo4() {
		return getConsuntivo(4, configurazione.getTipoConsuntivazione());
	}

	public ValutazioneVM getConsuntivo5() {
		return getConsuntivo(5, configurazione.getTipoConsuntivazione());
	}


	public Boolean getPercentuale() {
		return getSpecificaPercentuale() == null ? getIndicatore().getPercentuale() : getSpecificaPercentuale();
	}

	public void setPercentuale(Boolean b) {
		// SOLO PER REFLECTION HELPER
	}

	@JsonIgnore
	public String getTargetStoricoStringa() {
		if (getIndicatore() == null)
			return null;
		if (getStorico() == null)
			return null;
		Boolean perc = getPercentuale();
		if (perc == null)
			perc = indicatore.getPercentuale() != null && indicatore.getPercentuale();
		if (perc)
			return getStorico().getValorePercStringa();
		else
			return getStorico().getValoreStringa();

	}
	@JsonIgnore
	public String getTargetStringa() {
		if (getIndicatore() == null)
			return null;
		if (getPrevisione1() == null)
			return null;
		Boolean perc = getPercentuale();
		if (perc == null)
			perc = indicatore.getPercentuale() != null && indicatore.getPercentuale();
		if (perc)
			return getPrevisione1().getValorePercStringa();
		else
			return getPrevisione1().getValoreStringa();
	}
	@JsonIgnore
	public String getVariazionePercStringa() {
		if (getVariazione() == null)
			return null;
		if (getStorico() == null || getStorico().getValore() == null || getStorico().getValore() == 0)
			return "100%";
		return ((int) (100d * getVariazione() / getStorico().getValore())) + "%";
	}
	
	public void addPreventivi(List<ValutazioneVM> listPreventivi) {
		if (listPreventivi == null)
			return;
		for (ValutazioneVM v : listPreventivi) {
			addPreventivo(v);
		}
	}

	
	public void addConsuntivi(List<ValutazioneVM> listConsuntivi) {
		if (listConsuntivi == null)
			return;
		for (ValutazioneVM v : listConsuntivi) {
			addConsuntivo(v);
		}
	}
	
	public void add(List<ValutazioneVM> items, ValutazioneVM v) {
		if (v == null)
			return;
		for (ValutazioneVM val : items) {
			if (v.getId() != null && val.getId() != null && v.getId().equals(val.getId()))
				return;
		}
		items.add(v);
	}


	public void addValutazione(ValutazioneVM valutazione) {
		if (valutazione == null || valutazione.getTipoValutazione() == null)
			return;
		switch (valutazione.getTipoValutazione()) {
		case PREVENTIVO:
			addPreventivo(valutazione);
			break;
		case CONSUNTIVO:
			addConsuntivo(valutazione);
			break;
		case STORICO:
			setStorico(valutazione);
			break;
		}

	}

	@JsonIgnore
	public ValutazioneVM getConsuntivo() {
		return getConsuntivoMax();
	}
	@JsonIgnore
	public ValutazioneVM getConsuntivoMax() {
		Integer anno = getAnno(consuntivi);
		if (anno == null)
			return null;
		return getConsuntivoAnnoMax(anno);
	}
	@JsonIgnore
	public ValutazioneVM getConsuntivoMax(int i) {
		Integer anno = getAnno(consuntivi);
		if (anno == null)
			return null;
		anno += i;
		return getConsuntivoAnnoMax(anno);
	}
	@JsonIgnore
	public ValutazioneVM getConsuntivoAnnoMax(int anno) {
		if (consuntivi.isEmpty())
			return null;
		ValutazioneVM val = null;
		for (ValutazioneVM rel : consuntivi) {
			if (rel.getDataRilevazione() == null || ConsuntivoHelper.getAnno(rel.getDataRilevazione()) != anno
					|| rel.getValore() == null)
				continue;
			if (val == null || rel.getDataRilevazione().isAfter(val.getDataRilevazione()))
				val = rel;
		}
		return val;
	}

	public void setConsuntivo(ValutazioneVM consuntivo) {
		addConsuntivo(consuntivo);
	}

	public void addConsuntivo(ValutazioneVM consuntivo) {
		if (manca(consuntivi, consuntivo))
			consuntivi.add(consuntivo);
	}

	private boolean manca(List<ValutazioneVM> items, ValutazioneVM v) {
		List<Long> togli = new ArrayList<>();
		boolean manca = true;
		for (ValutazioneVM c : items) {
			if (v.getId() != null && v.getId().equals(c.getId())) {
				manca = false;
			} else if (TipoValutazione.PREVENTIVO.equals(v.getTipoValutazione()) && v.getInizio() != null
					&& c.getInizio() != null && v.getInizio().equals(c.getInizio())) {
				if (v.getDateInsert().isAfter(c.getDateInsert())) {
					togli.add(c.getId());
				} else {
					manca = false;
				}
			} else if (TipoValutazione.STORICO.equals(v.getTipoValutazione())) {
					if (v.getDateInsert().isAfter(c.getDateInsert())) {
						togli.add(c.getId());
					} else {
						manca = false;
					}
			} 
		}
		if (!togli.isEmpty())
			items.removeIf(c -> togli.contains(c.getId()));
		return manca;
	}


	public void setPreventivo(ValutazioneVM preventivo) {
		addPreventivo(preventivo);
	}

	public void addPreventivo(ValutazioneVM preventivo) {
		if (manca(preventivi, preventivo))
			preventivi.add(preventivo);
	}
	@JsonIgnore
	public ValutazioneVM getConsuntivo(int trimestre, TipoConsuntivazione tipoConsuntivazione) {
		Integer anno = getAnno();
		if (anno == null)
			anno = getAnno(consuntivi);
		if (anno == null)
			return null;
		return getConsuntivo(trimestre, anno);
	}
	@JsonIgnore
	public ValutazioneVM getPreventivo() {
		Integer anno = getAnno(preventivi);
		if (anno == null)
			return null;
		return getPreventivoAnno(anno);
	}
	@JsonIgnore
	private Integer getAnno(List<ValutazioneVM> items) {
		Integer anno = null;
		try {
			anno = getAnno();
			if (anno != null)
				return anno;
			if (items == null || items.isEmpty())
				return null;
			if (items.get(0).getIndicatorePiano() == null)
				return null;
			return items.get(0).getIndicatorePiano().getAnno();
		} catch (Throwable t) {
			System.err.println(t.getMessage());
			return null;
		}
	}
	@JsonIgnore
	public Integer getAnno() {
		if (getNodoPiano() == null)
			return null;
		return  getNodoPiano().getAnno();
	}
	@JsonIgnore
	public ValutazioneVM getConsuntivo(int trimestre, int anno) {
		if (consuntivi.isEmpty())
			return null;
		Periodo tp = ConsuntivoHelper.tipoPianoTrimestrale(trimestre);
		ValutazioneVM val = null;
		for (ValutazioneVM rel : consuntivi) {
			if (tp.equals(rel.getPeriodo()) && anno==rel.getAnno().intValue()){
					return  rel;
			}
		}
		return val;
	}
	@JsonIgnore
	public ValutazioneVM getPreventivoAnno(int anno) {
		if (preventivi.isEmpty())
			return null;
		for (ValutazioneVM rel : preventivi) {
			if (rel.getInizio() == null)
				continue;
			if (ConsuntivoHelper.getAnno(rel.getInizio()) == anno) {
				return rel;
			}
		}
		return null;
	}

	@JsonIgnore
	public ValutazioneVM getPreventivo(int i) {
		Integer anno = getAnno();
		if (anno == null)
			anno = getAnno(preventivi);
		if (anno == null)
			return null;
		anno += i;
		return getPreventivoAnno(anno);
	}

	@JsonIgnore
	public ValutazioneVM getPreventivoMin() {
		if (preventivi.isEmpty())
			return null;
		ValutazioneVM val = null;
		for (ValutazioneVM rel : preventivi) {
			if (rel.getInizio() == null)
				continue;
			if (val == null || ConsuntivoHelper.getAnno(rel.getInizio()) < ConsuntivoHelper.getAnno(val.getInizio()))
				val = rel;
		}
		return val;
	}
	@JsonIgnore
	public ValutazioneVM getPreventivoMax() {
		if (preventivi.isEmpty())
			return null;
		ValutazioneVM val = null;
		for (ValutazioneVM rel : preventivi) {
			if (rel.getInizio() == null)
				continue;
			if (val == null || ConsuntivoHelper.getAnno(rel.getInizio()) > ConsuntivoHelper.getAnno(val.getInizio()))
				val = rel;
		}
		return val;
	}
	@JsonIgnore
	public String getVariazioneStringa() {
		if (getVariazione() == null)
			return null;
		if (getIndicatore() == null)
			return null;
		boolean perc = getIndicatore().getPercentuale() != null && getIndicatore().getPercentuale();
		if (perc)
			return ModelHelper.toStringDec(getVariazione() * 100d, MappingIndicatorePianoHelper.getDecimali(this, this.getIndicatore())) + "%";
		else
			return ModelHelper.toStringDec(getVariazione(),  MappingIndicatorePianoHelper.getDecimali(this, this.getIndicatore()));
	}

	@JsonIgnore
	public Double getVariazione() {
		double prev = getPreventivoNodo() == null ? 0d : getPreventivoNodo();
		return getConsuntivo() == null || getConsuntivo().getValore() == null ? 0d : getConsuntivo().getValore() - prev;
	}
	@JsonIgnore
	public Double getPreventivoNodo() {
		if (getNodoPiano() == null)
			return null;
//		if (TipoNodo.PROGRAMMA.equals(getNodoPiano().getTipoNodo())) {
//			if (getPreventivoMax() == null)
//				return null;
//			if (getPreventivoMax().getValore() == null)
//				return null;
//			return getPreventivoMax().getValore();
//		} else {
			if (getPreventivo() == null)
				return null;
			if (getPreventivo().getValore() == null)
				return null;
			return getPreventivo().getValore();
//		}
	}

	@JsonIgnore
	public void addRanges(List<RangeIndicatoreVM> ranges) {
		if (ranges == null || ranges.isEmpty())
			return;
		getRanges().addAll(ranges);
	}


	@JsonIgnore
	public IndicatorePianoVM raggruppa() {
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
