package com.finconsgroup.performplus.service.business.utils;

import java.math.BigDecimal;

import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.enumeration.TipoFormula;

public class ValutazioneHelper {
	private ValutazioneHelper() {
	}

	public static String getValToString(final Valutazione v) {
		boolean perc = v.getIndicatorePiano().getSpecificaPercentuale() == null
				? Boolean.TRUE.equals(v.getIndicatorePiano().getIndicatore().getPercentuale())
				: v.getIndicatorePiano().getSpecificaPercentuale();
		if (perc)
			return getValorePercStringa(v);
		else
			return getValoreStringa(v);
	}
	public static String getValToStringA(final Valutazione v) {
		int dec=v.getIndicatorePiano().getIndicatore().getDecimali()==null?0:v.getIndicatorePiano().getIndicatore().getDecimali();
			return getValoreStringa(v.getValoreNumericoA(),dec);
	}
	public static String getValToStringB(final Valutazione v) {
		int dec=v.getIndicatorePiano().getIndicatore().getDecimali()==null?0:v.getIndicatorePiano().getIndicatore().getDecimali();
			return getValoreStringa(v.getValoreNumericoB(),dec);
	}
	public static Double getValore(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(tipoFormula))
			return getValoreBOOL(v);
		if (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula))
			return v.getValoreNumerico() == null ? null : v.getValoreNumerico().doubleValue();
		if (TipoFormula.TIPO_FORMULA_DATA.equals(tipoFormula))
			return (double) (DateHelper.asDate(v.getValoreTemporale())).getTime();
		if (v.getValoreNumericoA() == null || v.getValoreNumericoB() == null)
			return null;
		if (v.getValoreNumericoB().doubleValue() == 0)
			return null;
		return v.getValoreNumericoA().doubleValue() / v.getValoreNumericoB().doubleValue();
	}

	public static Double getValoreA(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula) && v.getValoreNumericoA() != null)
			return v.getValoreNumericoA().doubleValue();
		return null;
	}
	public static Double getValoreB(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula) && v.getValoreNumericoB() != null)
			return v.getValoreNumericoB().doubleValue();
		return null;
	}


	public static Double getValoreBOOL(Valutazione v) {
		return Boolean.TRUE.equals(v.getValoreBooleano()) ? 1d : 0d;
	}

	public static String getValoreStringa(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (TipoFormula.TIPO_FORMULA_DATA.equals(tipoFormula))
			return ModelHelper.toString(v.getValoreTemporale());
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(tipoFormula)) {
			if (v.getValoreBooleano() == null)
				return "";
			return Boolean.TRUE.equals(v.getValoreBooleano()) ? "Sì" : "No";
		}
		if (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula))
			return ModelHelper.toStringDec(getValore(v), v.getIndicatorePiano().getIndicatore().getDecimali());
		if (v.getValoreNumericoA() == null || v.getValoreNumericoB() == null)
			return null;
		if (v.getValoreNumericoB().doubleValue() == 0)
			return null;
		return ModelHelper.toStringDec(getValore(v), v.getIndicatorePiano().getIndicatore().getDecimali());
	}

	public static String getValorePercStringa(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (TipoFormula.TIPO_FORMULA_DATA.equals(tipoFormula))
			return null;
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(tipoFormula))
			return null;
		if (getValore(v) == null)
			return null;
		return ModelHelper.toStringDec(getValore(v) * 100d, v.getIndicatorePiano().getIndicatore().getDecimali()) + "%";
	}
	public static String getValorePercStringa(BigDecimal d, int dec) {

		if (d == null)
			return null;
		return ModelHelper.toStringDec(d.doubleValue() * 100d, dec) + "%";
	}
	public static String getValoreStringa(BigDecimal d, int dec) {
		if (d == null)
			return null;
		return ModelHelper.toStringDec(d.doubleValue(), dec);
	}
	
	
}
