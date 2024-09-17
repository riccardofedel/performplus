package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.service.business.utils.IndicatorePianoHelper;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;

public class RelazioneIndicatoreItem implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 3979253854447978372L;


	private Date inizioOrigine;
	private Date scadenzaOrigine;
	private Serializable info;
	private List<RelazioneIndicatoreItem> children = new ArrayList<>();

	private ConfigurazioneDTO configurazione;

	public RelazioneIndicatoreItem(RelazioneIndicatoreDTO info,ConfigurazioneDTO configurazione) {
		this.info = info;
		this.configurazione=configurazione;
	}

	public RelazioneIndicatoreItem(NodoPianoDTO info, ConfigurazioneDTO configurazione) {
		this.info = info;
		this.configurazione=configurazione;
	}

	public RelazioneIndicatoreDTO getRelazioneIndicatore() {
		if (getUserObject() instanceof RelazioneIndicatoreDTO ele)
			return ele;
		return null;
	}

	public NodoPianoDTO getNodoPiano() {
		if (getUserObject() instanceof NodoPianoDTO ele)
			return ele;
		return null;
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

	public ValutazioneDTO getPreventivo() {
		if (getRelazioneIndicatore() != null)
			return getRelazioneIndicatore().getPreventivo(0);
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
		if (getNodoPiano() != null)
			return getNodoPiano().getTipoNodo();
		if (getIndicatorePiano() != null)
			return getIndicatorePiano().getNodoPiano().getTipoNodo();
		return null;
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

	public IndicatorePianoEasyDTO getIndicatorePiano() {
		if (getPreventivoMin() != null)
			return getPreventivoMin().getIndicatorePiano();
		return null;
	}

	public IndicatoreDTO getIndicatore() {
		if (getIndicatorePiano() != null)
			return getIndicatorePiano().getIndicatore();
		return null;
	}

	@Override
	public String toString() {
		if (getNodoPiano() != null) {
			return getNodoPiano().getDenominazione();
		}
		if (getIndicatorePiano() != null)
			return getIndicatorePiano().getDenominazione();
		return "";
	}

	public String getNome() {
		String denom = null;
		if (getNodoPiano() != null) {
			denom = getNodoPiano().getDenominazione();
			denom = denom.replace("Obiettivo Strategico", "Obiett. Strat.");
			denom = denom.replace("Obiettivo Operativo Direzionale", "Obiett. Op. Dir.");
			denom = denom.replace("Obiettivo Operativo Esecutivo", "Obiett. Op. Esec.");
			if (!TipoNodo.PIANO.equals(getNodoPiano().getTipoNodo()))
				denom = getNodoPiano().getCodice() + " " + denom;
		} else if (getIndicatorePiano() != null)
			denom = getIndicatorePiano().getDenominazione();
		return denom;
	}

	public String getInizio() {
		LocalDate date = getDataInizio();
		if (date == null)
			return null;
		return ModelHelper.toString(date);
	}

	public LocalDate getDataInizio() {
		if (getNodoPiano() != null) {
				return getNodoPiano().getInizio();
		}
		if (getIndicatorePiano() != null)
			return getIndicatorePiano().getInizio();
		return null;
	}

	public String getFine() {
		LocalDate date = getDataFine();
		if (date == null)
			return null;
		return ModelHelper.toString(date);

	}

	public LocalDate getDataFine() {
		if (getNodoPiano() != null) {
				return getNodoPiano().getScadenza();
		}

		if (getIndicatorePiano() != null)
			return getIndicatorePiano().getScadenza();
		return null;
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
		if (v.getIndicatorePiano().getPercentuale() != null && v.getIndicatorePiano().getPercentuale())
			out = v.getValorePercStringa();
		else {
			out = v.getValoreStringa();
		}
		
		if (TipoFormula.TIPO_FORMULA_RAPPORTO.equals(v.getTipoFormula())) {
			Integer[]dec=IndicatorePianoHelper.getDecimaliAll(v);
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

	private String getValore(ValutazioneDTO v) {
		if (v != null) {
			if (v.getIndicatorePiano().getPercentuale() != null && v.getIndicatorePiano().getPercentuale())
				return v.getValorePercStringa();
			return v.getValoreStringa();
		}
		return null;
	}

	public String toString(ValutazioneDTO valutazione) {
		switch (valutazione.getIndicatorePiano().getIndicatore().getTipoFormula()) {
		case TIPO_FORMULA_BULEANO:
			return boolean2String(valutazione.getValoreBooleano());
		case TIPO_FORMULA_DATA:
			return ModelHelper.toString(valutazione.getValoreTemporale());
		case TIPO_FORMULA_NUMERO:
		case TIPO_FORMULA_RAPPORTO:
			return double2String(valutazione.getValore(), IndicatorePianoHelper.getDecimali(valutazione));
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
		boolean perc = getIndicatore().getPercentuale() != null && getIndicatore().getPercentuale();
		if (perc)
			return ModelHelper.toStringDec(getVariazione() * 100d, IndicatorePianoHelper.getDecimali(this.getIndicatorePiano(),this.getIndicatore())) + "%";
		else
			return ModelHelper.toStringDec(getVariazione(), IndicatorePianoHelper.getDecimali(this.getIndicatorePiano(),this.getIndicatore()));
	}
	


	public Double getVariazione() {
		Double prev = getPreventivoNodo();
		return getConsuntivo().getValore() == null ? 0d : getConsuntivo().getValore() - (prev == null ? 0d : prev);
	}

	public Double getPreventivoNodo() {
		if (getIndicatorePiano() == null)
			return null;
//		if (TipoNodo.MISSIONE.equals(getIndicatorePiano().getNodoPiano().getTipoNodo())) {
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

	public String getVariazionePercStringa() {
		Double prev = getPreventivoNodo();
		if (prev == null || prev == 0)
			return "100%";
		return ((int) (100d * getVariazione() / prev)) + "%";
	}

	public Double getScostamento() {
		Double prev = getPreventivoNodo();
		if (prev == null || getConsuntivoMax() == null || getConsuntivoMax().getValore() == null)
			return null;
		Double scostamento = null;
		scostamento = getConsuntivoMax().getValore() - prev;
		return scostamento;
	}

	public String getScostamentoString() {
		if (getScostamento() == null)
			return null;
		if (getIndicatorePiano().getPercentuale() != null && getIndicatorePiano().getPercentuale())
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

//	public LocalDate getDataInizioEff() {
//		if (getNodoPiano() == null || TipoNodo.PIANO.equals(getNodoPiano().getTipoNodo()))
//			return null;
//		if (TipoNodo.RISULTATO_ATTESO.equals(getNodoPiano().getTipoNodo())) {
//			LocalDate ini = getNodoPiano().getInizioEffettivo();
//
//			return ini;
//		}
//		if (TipoNodo.AZIONE.equals(getNodoPiano().getTipoNodo())
//				|| TipoNodo.MISSIONE.equals(getNodoPiano().getTipoNodo())) {
//			return obiettivoGestionaleDataInizio(getNodoPiano());
//		}
//		return getDataInizio();
//	}
//
//	public String getInizioEff() {
//		LocalDate date = getDataInizioEff();
//		if (date == null)
//			return null;
//		return ModelHelper.toString(date);
//
//	}

//	private LocalDate obiettivoGestionaleDataInizio(NodoPianoDTO nodo) {
//		LocalDate dt = null;
//		List<NodoPianoDTO> list = NodoPianoHelper.contiene(nodo);
//		for (NodoPianoDTO np : list) {
//			if (TipoNodo.RISULTATO_ATTESO.equals(np.getTipoNodo())) {
//				LocalDate ini = np.getInizioEffettivo();
//				if (ini == null)
//					ini = np.getInizio();
//				if (dt == null || ini.isBefore(dt))
//					dt = ini;
//			}
//		}
//		if (dt != null)
//			return dt;
//		return getDataInizio();
//	}


	public LocalDate getDataFineEff() {

		if(getNodoPiano().getScadenzaEffettiva()!=null) {

			return getNodoPiano().getScadenzaEffettiva();
		}
		
		return getNodoPiano().getScadenza();
	}

	public String getFineEff() {
		LocalDate date = getDataFineEff();
		if (date == null)
			return null;
		return ModelHelper.toString(date);
	}

//	private LocalDate obiettivoGestionaleDataFine(NodoPianoDTO nodo) {
//		LocalDate dt = null;
//		List<NodoPianoDTO> list = NodoPianoHelper.contiene(nodo);
//		for (NodoPianoDTO np : list) {
//			if (TipoNodo.RISULTATO_ATTESO.equals(np.getTipoNodo())) {
//				LocalDate sca = np.getScadenzaEffettiva();
//				if (sca == null)
//					sca = ((LineaStrategicaDTO) np).getScadenza();
//				if (dt == null || sca.isAfter(dt))
//					dt = sca;
//			}
//		}
//		if (dt != null)
//			return dt;
//		return getDataFine();
//	}


	public String getValorePreventivoFinto() {
		return null;
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
			return getRelazioneIndicatore().getConsuntivo(1,configurazione.getTipoConsuntivazione());
		}
		return null;
	}

	private ValutazioneDTO getConsuntivo2() {
		if (getRelazioneIndicatore() != null) {
			return getRelazioneIndicatore().getConsuntivo(2,configurazione.getTipoConsuntivazione());
		}
		return null;
	}

	private ValutazioneDTO getConsuntivo3() {
		if (getRelazioneIndicatore() != null) {
			return getRelazioneIndicatore().getConsuntivo(3,configurazione.getTipoConsuntivazione());
		}
		return null;
	}

	private ValutazioneDTO getConsuntivo4() {
		if (getRelazioneIndicatore() != null) {
			return getRelazioneIndicatore().getConsuntivo(4,configurazione.getTipoConsuntivazione());
		}
		return null;
	}

	public String getValoreConsuntivoMeno1() {
		return getValore(getConsuntivoMeno1());
	}

	public String getValoreConsuntivoMeno2() {
		return getValore(getConsuntivoMeno2());
	}

	public String getValoreConsuntivo1() {
		return getValore(getConsuntivo1());
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

	public void setInizioOrigine(Date inizioOrigine) {
		this.inizioOrigine = inizioOrigine;

	}

	public Date getScadenzaOrigine() {
		return scadenzaOrigine;
	}

	public void setScadenzaOrigine(Date scadenzaOrigine) {
		this.scadenzaOrigine = scadenzaOrigine;
	}

	public Date getInizioOrigine() {
		return inizioOrigine;
	}

	public String getNote() {
		if (getIndicatorePiano() == null)
			return null;
		return getIndicatorePiano().getNote();
	}


	public void add(RelazioneIndicatoreItem node) {
		children.add(node);
	}

	public Serializable getUserObject() {
		return info;
	}

	public int getChildCount() {
		return children.size();
	}

	public RelazioneIndicatoreItem getChildAt(int i) {
		return children.get(i);
	}


}