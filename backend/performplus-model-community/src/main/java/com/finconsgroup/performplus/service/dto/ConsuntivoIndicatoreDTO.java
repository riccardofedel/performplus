package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.service.business.utils.ConsuntivoHelper;
import com.finconsgroup.performplus.service.business.utils.IndicatorePianoHelper;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;

import lombok.Data;

@SuppressWarnings("serial")
@Data

public class ConsuntivoIndicatoreDTO implements Serializable {

	private LocalDate inizioOrigine;
	private LocalDate scadenzaOrigine;

	private RelazioneIndicatoreDTO relazioneIndicatore;

	private Double raggiungimentoPerc;

	private Double raggiungimentoPesato;

	private Double pesoRelativo;

	private ConfigurazioneDTO configurazione;

	
	public ConsuntivoIndicatoreDTO(RelazioneIndicatoreDTO info, ConfigurazioneDTO configurazione) {
		this.relazioneIndicatore = info;
		this.configurazione = configurazione;
		this.raggiungimentoPerc = ConsuntivoHelper.raggiungimentoRange(this);
	}


	public ValutazioneDTO getPreventivoMin() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getPreventivoMin();
		return null;
	}

	public ValutazioneDTO getPreventivoMax() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getPreventivoMax();
		return null;
	}

	public ValutazioneDTO getPreventivo() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getPreventivo(0);
		return null;
	}

	public ValutazioneDTO getPreventivoMeno1() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getPreventivo(-1);
		return null;
	}

	public ValutazioneDTO getPreventivoMeno2() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getPreventivo(-2);
		return null;
	}

	public ValutazioneDTO getPreventivo1() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getPreventivo(1);
		return null;
	}

	public ValutazioneDTO getPreventivo2() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getPreventivo(2);
		return null;
	}

	public ValutazioneDTO getPreventivo3() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getPreventivo(3);
		return null;
	}

	public TipoNodo getTipoNodo() {
		return getRelazioneIndicatore().getNodoPiano().getTipoNodo();
	}

	public ValutazioneDTO getConsuntivo() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getConsuntivo();
		return null;
	}

	public ValutazioneDTO getConsuntivoMax() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getConsuntivoMax();
		return null;
	}

	public ValutazioneDTO getStorico() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getStorico();
		return null;
	}

	public IndicatorePianoDTO getIndicatorePiano() {
		return getRelazioneIndicatore();
	}

	public IndicatoreDTO getIndicatore() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getIndicatore();
		return null;
	}

	@Override
	public String toString() {
		return getRelazioneIndicatore().getDenominazione();
	}

	public String getNome() {
		return getRelazioneIndicatore().getDenominazione();
	}

	public String getInizio() {
		LocalDate date = getDataInizio();
		if (date == null)
			return null;
		return ModelHelper.toString(date);
	}

	public LocalDate getDataInizio() {
		return getRelazioneIndicatore().getInizio();
	}

	public String getFine() {
		LocalDate date = getDataFine();
		if (date == null)
			return null;
		return ModelHelper.toString(date);

	}

	public LocalDate getDataFine() {
		return getRelazioneIndicatore().getScadenza();
	}

	public String getValoreStorico() {
		return getValore(getStorico());
	}


	public String getValorePreventivo() {
		return getValore(getPreventivo());
	}

	public String getValorePreventivoFratto() {
		return getValoreFratto(getPreventivo());
	}

	public String getValorePreventivoMeno1Fratto() {
		return getValoreFratto(getPreventivoMeno1());
	}

	public String getValorePreventivoMeno2Fratto() {
		return getValoreFratto(getPreventivoMeno2());
	}

	public String getValorePreventivo1Fratto() {
		return getValoreFratto(getPreventivo1());
	}

	public String getValorePreventivo2Fratto() {
		return getValoreFratto(getPreventivo2());
	}

	public String getValorePreventivo3Fratto() {
		return getValoreFratto(getPreventivo3());
	}

	private String getValoreFratto(ValutazioneDTO v) {
		if (v == null)
			return null;
		String out = null;
		String num = null;
		String den = null;
		Integer[] dec=IndicatorePianoHelper.getDecimaliAll(v);

		if (v.getIndicatorePiano().getPercentuale() != null && v.getIndicatorePiano().getPercentuale())
			out = v.getValorePercStringa();
		else {
			out = v.getValoreStringa();
		}
		if (TipoFormula.TIPO_FORMULA_RAPPORTO.equals(v.getTipoFormula())) {
			if (dec[1] != null && dec[1] > 0)
				num = ModelHelper.toStringDec(v.getNumberADouble(), dec[1]);
			else
				num = v.getNumberAInteger().toString();
			
			if (dec[2] != null && dec[2] > 0)
				den = ModelHelper.toStringDec(v.getNumberBDouble(), dec[2]);
			else
				den = v.getNumberBInteger().toString();
			out += " [" + num + "/" + den + "]";
		}
		return out;
	}

	public String getValorePreventivoMeno1() {
		return getValore(getPreventivoMeno1());
	}

	public String getValorePreventivoMeno2() {
		return getValore(getPreventivoMeno2());
	}

	public String getValorePreventivo1() {
		return getValore(getPreventivo1());
	}

	public String getValorePreventivo2() {
		return getValore(getPreventivo2());
	}

	public String getValorePreventivo3() {
		return getValore(getPreventivo3());
	}

	public String getValoreConsuntivo() {
		return getValore(getConsuntivo());
	}

	public String getValoreConsuntivoMax() {
		return getValore(getConsuntivoMax());
	}

	public String getValoreConsuntivoMeno1() {
		return getValore(getConsuntivoMeno1());
	}

	public String getValoreConsuntivoMeno2() {
		return getValore(getConsuntivoMeno2());
	}

	public String toString(ValutazioneDTO valutazione) {
		Integer dec = null;
		switch (valutazione.getIndicatorePiano().getIndicatore().getTipoFormula()) {
		case TIPO_FORMULA_BULEANO:
			return boolean2String(valutazione.getValoreBooleano());
		case TIPO_FORMULA_DATA:
			return ModelHelper.toString(valutazione.getValoreTemporale());
		case TIPO_FORMULA_NUMERO:
			return valutazione.getValoreStringa();
		case TIPO_FORMULA_RAPPORTO:
			dec = IndicatorePianoHelper.getDecimali(valutazione);
			return double2String(valutazione.getValore(), dec);
		default:

			return "";
		}

	}

	private String boolean2String(Boolean bool) {
		if (bool == null)
			return "";
		return bool ? "sì" : "no";
	}

	private String double2String(Double number, int dec) {
		if (number == null)
			return "";
		DecimalFormat format = new DecimalFormat();
		format.setMaximumFractionDigits(dec);
		format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ITALY));
		return format.format(number);

	}

	public String getVariazioneStringa() {
		if (getVariazione() == null)
			return null;
		if (getIndicatore() == null)
			return null;
		int dec = IndicatorePianoHelper.getDecimali(getIndicatorePiano(),getIndicatore());
		boolean perc = getIndicatore().getPercentuale() != null && getIndicatore().getPercentuale();
		if (perc)
			return ModelHelper.toStringDec(getVariazione() * 100d, dec) + "%";
		else
			return ModelHelper.toStringDec(getVariazione(), dec);
	}

	
	public Double getVariazione() {
		Double prev = getPreventivoNodo() == null ? 0d : getPreventivoNodo();
		return getConsuntivo() == null || getConsuntivo().getValore() == null ? 0d : getConsuntivo().getValore() - prev;
	}

	public Double getPreventivoNodo() {
		if (getRelazioneIndicatore() == null)
			return null;
//		if (TipoNodo.MISSIONE.equals(getRelazioneIndicatore().getNodoPiano().getTipoNodo())) {
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

	public Double getConsuntivoNodo() {
		if (getRelazioneIndicatore() == null)
			return null;
		if (getConsuntivoMax() == null)
			return null;
		if (getConsuntivoMax().getValore() == null)
			return null;
		return getConsuntivoMax().getValore();

	}

	public String getVariazionePercStringa() {
		Double prev = getPreventivoNodo();
		if (prev == null || prev == 0)
			return "100%";
		return ((int) (100d * getVariazione() / prev)) + "%";
	}

	public Double getScostamento() {
		Double prev = getPreventivoNodo();

		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(getTipoFormula())) {
			if (prev == null || getConsuntivoMax() == null || getConsuntivoMax().getValoreBooleano() == null)
				return null;
			Double scostamento = null;
			scostamento = getConsuntivoMax().getValoreBOOL() - prev;
			return scostamento;
		}
		if (prev == null || getConsuntivoMax() == null || getConsuntivoMax().getValore() == null)
			return null;
		Double scostamento = null;
		scostamento = getConsuntivoMax().getValore() - prev;
		return scostamento;
	}

	public TipoFormula getTipoFormula() {
		if (getIndicatore() != null)
			return getIndicatore().getTipoFormula();
		return null;
	}

	public String getScostamentoString() {
		if (getScostamento() == null)
			return null;
		if (getRelazioneIndicatore().getPercentuale() != null && getRelazioneIndicatore().getPercentuale())
			return ModelHelper.toStringDec(getScostamento() * 100d, 2) + "%";
		else
			return ModelHelper.toStringDec(getScostamento());
	}

	public String getScostamentoPercentualeString() {
		if (getScostamentoPercentuale() == null)
			return null;
		return getScostamentoPercentuale() + "%";
	}

	public Integer getScostamentoPercentuale() {
		Double prev = getPreventivoNodo();
		if (prev == null)
			return null;
		Double sc = getScostamento();
		if (sc == null)
			return null;
		if (prev.doubleValue() == 0)
			return null;
		double mod = sc;
		if (mod < 0)
			mod = -mod;
		double perc = 100d * mod / prev;
		Integer percentuale = Math.round((float) perc);
		if (percentuale < 0)
			percentuale = 0;
		if (percentuale > 100)
			percentuale = 100;
		if (sc < 0)
			return -percentuale;
		return percentuale;
	}

	public LocalDate getDataInizioEff() {
		return getDataInizio();
	}

	public String getInizioEff() {
		LocalDate date = getDataInizioEff();
		if (date == null)
			return null;
		return ModelHelper.toString(date);

	}

	public LocalDate getDataFineEff() {
		return getDataFine();
	}

	public String getFineEff() {
		LocalDate date = getDataFineEff();
		if (date == null)
			return null;
		return ModelHelper.toString(date);
	}

	public String getValorePreventivoFinto() {
		return null;
	}

	public String getValoreConsuntivo1() {
		if (getConsuntivo1() == null || getConsuntivo1().getIndicatorePiano() == null) {
			return null;
		}
		if (Boolean.TRUE.equals(getConsuntivo1().getIndicatorePiano().getPercentuale())) {
			return getConsuntivo1().getValorePercStringa();
		}
		return getConsuntivo1().getValoreStringa();

	}

	private ValutazioneDTO getConsuntivoMeno1() {
		if (getRelazioneIndicatore() != null) {
			return getRelazioneIndicatore().getConsuntivoMax(-1);
		}
		return null;
	}

	private ValutazioneDTO getConsuntivoMeno2() {
		if (getRelazioneIndicatore() != null) {
			return getRelazioneIndicatore().getConsuntivoMax(-2);
		}
		return null;
	}

	private ValutazioneDTO getConsuntivo1() {
		if (getRelazioneIndicatore() != null) {
			return getRelazioneIndicatore().getConsuntivo(1, configurazione.getTipoConsuntivazione());
		}
		return null;
	}

	private ValutazioneDTO getConsuntivo2() {
		if (getRelazioneIndicatore() != null) {
			return getRelazioneIndicatore().getConsuntivo(2, configurazione.getTipoConsuntivazione());
		}
		return null;
	}

	private ValutazioneDTO getConsuntivo3() {
		if (getRelazioneIndicatore() != null) {
			return getRelazioneIndicatore().getConsuntivo(3, configurazione.getTipoConsuntivazione());
		}
		return null;
	}

	private ValutazioneDTO getConsuntivo4() {
		if (getRelazioneIndicatore() != null) {
			return getRelazioneIndicatore().getConsuntivo(4, configurazione.getTipoConsuntivazione());
		}
		return null;
	}

	private String getValore(ValutazioneDTO v) {
		if (v != null) {
			if (v.getIndicatorePiano().getPercentuale() != null && v.getIndicatorePiano().getPercentuale())
				return v.getValorePercStringa();
			return v.getValoreStringa();
		}
		return null;
	}

	public String getValoreConsuntivo2() {
		return getValore(getConsuntivo2());
	}

	public String getValoreConsuntivo3() {
		return getValore(getConsuntivo3());
	}

	public String getValoreConsuntivo4() {
		return getValore(getConsuntivo4());
	}

	public void setInizioOrigine(LocalDate inizioOrigine) {
		this.inizioOrigine = inizioOrigine;

	}

	public LocalDate getScadenzaOrigine() {
		return scadenzaOrigine;
	}

	public void setScadenzaOrigine(LocalDate scadenzaOrigine) {
		this.scadenzaOrigine = scadenzaOrigine;
	}

	public LocalDate getInizioOrigine() {
		return inizioOrigine;
	}

	public String getNote() {
		if (getRelazioneIndicatore() == null)
			return null;
		return getRelazioneIndicatore().getNote();
	}

	public Double getPeso() {
		return getRelazioneIndicatore().getPeso();

	}

	public Double getRaggiungimentoPerc() {
		return this.raggiungimentoPerc;
	}

	public void setRaggiungimentoPerc(Double raggiungimentoPerc) {
		this.raggiungimentoPerc = raggiungimentoPerc;
	}

	public Double getRaggiungimentoPesato() {
		return raggiungimentoPesato;
	}

	public void setRaggiungimentoPesato(Double raggiungimentoPesato) {
		this.raggiungimentoPesato = raggiungimentoPesato;
	}

	public ConsuntivoIndicatoreDTO raggiungimentoPesato(Double raggiungimentoPesato) {
		setRaggiungimentoPesato(raggiungimentoPesato);
		return this;
	}

	public Double getPesoRelativo() {
		return pesoRelativo;
	}

	public void setPesoRelativo(Double pesoRelativo) {
		this.pesoRelativo = pesoRelativo;
	}

	public Boolean getNonValutabile() {
		if (getRelazioneIndicatore() == null)
			return true;
		return getRelazioneIndicatore().getNonValutabile();

	}

	public Double getRaggiungimentoForzato() {
		if (getRelazioneIndicatore() == null)
			return null;
		return getRelazioneIndicatore().getRaggiungimentoForzato();

	}

	public String getRaggiungimentoForzatoPerc() {
		if (getRelazioneIndicatore() == null || getRelazioneIndicatore().getRaggiungimentoForzato() == null)
			return null;
		return ModelHelper.toString(getRelazioneIndicatore().getRaggiungimentoForzato().floatValue() / 100f);

	}

	public Boolean getSviluppoSostenibile() {
		if (getRelazioneIndicatore() == null)
			return false;
		return getRelazioneIndicatore().getSviluppoSostenibile();
	}

	public Boolean getStrategico() {
		if (getRelazioneIndicatore() == null)
			return false;
		return getRelazioneIndicatore().getStrategico();
	}

	public String getAltro() {
		if (getRelazioneIndicatore() == null) {
			return "";
		}
		if (StringUtils.isNotBlank(getRelazioneIndicatore().getNoteRaggiungimentoForzato())) {
			return (getRelazioneIndicatore().getNoteRaggiungimentoForzato() + "          ").substring(0, 10).trim() + "...";
		}
		if (StringUtils.isNotBlank(getRelazioneIndicatore().getNoteRichiestaForzatura())) {
			return (getRelazioneIndicatore().getNoteRichiestaForzatura() + "          ").substring(0, 10).trim() + "...";
		}
		return "...";

	}
}
