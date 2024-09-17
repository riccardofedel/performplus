package com.finconsgroup.performplus.service.business.utils;

import java.math.RoundingMode;

import com.finconsgroup.performplus.domain.Indicatore;
import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.service.dto.IndicatoreDTO;
import com.finconsgroup.performplus.service.dto.IndicatorePianoDTO;
import com.finconsgroup.performplus.service.dto.IndicatorePianoEasyDTO;
import com.finconsgroup.performplus.service.dto.ValutazioneDTO;

public class IndicatorePianoHelper {
	protected IndicatorePianoHelper() {
	}



	public static String getValToString(final Valutazione v) {
		boolean perc = v.getIndicatorePiano().getSpecificaPercentuale()==null? v.getIndicatorePiano().getIndicatore().getPercentuale()
				:v.getIndicatorePiano().getSpecificaPercentuale();
		if (perc)
			return getValorePercStringa(v);
		else
			return getValoreStringa(v);
	}

	public static Double getValore(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(tipoFormula))
			return getValoreBOOL(v);
		if (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula))
			return v.getValoreNumerico() == null ? null : v.getValoreNumerico().doubleValue();
		if (TipoFormula.TIPO_FORMULA_DATA.equals(tipoFormula))
			return v.getValoreTemporale() == null ? null
					: (double) java.sql.Date.valueOf(v.getValoreTemporale()).getTime();
		if (v.getValoreNumericoA() == null || v.getValoreNumericoB() == null)
			return null;
		if (v.getValoreNumericoB().doubleValue() == 0)
			return null;
		if (v.getValoreNumerico() != null)
			return v.getValoreNumerico().doubleValue();
		return v.getValoreNumericoA().divide(v.getValoreNumericoB(),getDecimali(v),RoundingMode.HALF_DOWN).doubleValue();
	}
	public static Double getValoreA(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (!TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula))
			return null;
		return v.getValoreNumericoA().doubleValue();
	}
	public static Double getValoreB(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (!TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula))
			return null;
		return v.getValoreNumericoB().doubleValue();
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
		return ModelHelper.toStringDec(getValore(v), getDecimali(v));
	}
	public static String getValoreStringaA(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (!TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula))
			return null;
		return ModelHelper.toStringDec(getValoreA(v), getDecimaliA(v));
	}
	public static String getValoreStringaB(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (!TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula))
			return null;
		return ModelHelper.toStringDec(getValoreB(v), getDecimaliB(v));
	}
	public static int getDecimali(Valutazione v) {
		Integer[] dec = getDecimaliAllZero(v);
		return dec[0];
	}
	public static int getDecimaliA(Valutazione v) {
		Integer[] dec = getDecimaliAllZero(v);
		return dec[1];
	}
	public static int getDecimaliB(Valutazione v) {
		Integer[] dec = getDecimaliAllZero(v);
		return dec[2];
	}
	public static int getDecimali(Integer[] dec) {
		return dec[0];
	}
	public static Integer[] getDecimaliAll(final IndicatorePiano ip, final Indicatore ind) {
		final Integer[] out=new Integer[]{null,null,null};
		if (ip == null ||ind==null)
			return out;
		out[0]=ip.getSpecificaDecimali()==null?
				ind.getDecimali():ip.getSpecificaDecimali();
		out[1]=ip.getSpecificaDecimaliA()==null?
				ind.getDecimaliA():ip.getSpecificaDecimaliA();
		out[2]=ip.getSpecificaDecimaliB()==null?
				ind.getDecimaliB():ip.getSpecificaDecimaliB();
		return out;
	}
	public static Integer[] getDecimaliAllZero(final IndicatorePiano ip, final Indicatore ind) {
		final Integer[] out=getDecimaliAll(ip, ind);
		if(out[0]==null)
			out[0]=0;
		if(out[1]==null)
			out[1]=0;
		if(out[2]==null)
			out[2]=0;
		return out;
	}
	public static Integer[] getDecimaliAllZero(final Valutazione v) {
		final Integer[] out=getDecimaliAll(v.getIndicatorePiano(),v.getIndicatorePiano().getIndicatore());
		if(out[0]==null)
			out[0]=0;
		if(out[1]==null)
			out[1]=0;
		if(out[2]==null)
			out[2]=0;
		return out;
	}
	public static int getDecimali(final IndicatorePiano ip, final Indicatore ind) {
		Integer[] dec = getDecimaliAllZero(ip,ind);
		return dec[0];
	}
	public static String getValorePercStringa(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (TipoFormula.TIPO_FORMULA_DATA.equals(tipoFormula))
			return null;
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(tipoFormula))
			return null;
		if (getValore(v) == null)
			return null;
		return ModelHelper.toStringDec(getValore(v) * 100d, getDecimali(v)) + "%";
	}
	public static String getValorePercStringaA(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (!TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula))
			return null;
		if (getValoreA(v) == null)
			return null;
		return ModelHelper.toStringDec(getValoreA(v) * 100d, getDecimaliA(v)) + "%";
	}
	public static String getValorePercStringaB(Valutazione v) {
		TipoFormula tipoFormula = v.getIndicatorePiano().getIndicatore().getTipoFormula();
		if (!TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula))
			return null;
		if (getValoreB(v) == null)
			return null;
		return ModelHelper.toStringDec(getValoreB(v) * 100d, getDecimaliB(v)) + "%";
	}



	public static Integer[] getDecimaliAll(ValutazioneDTO v) {
		return getDecimaliAll(v.getIndicatorePiano(), v.getIndicatorePiano().getIndicatore());
	}
	public static Integer[] getDecimaliAllZero(ValutazioneDTO v) {
		return getDecimaliAllZero(v.getIndicatorePiano(), v.getIndicatorePiano().getIndicatore());
	}

	public static Integer[] getDecimaliAllZero(final IndicatorePianoEasyDTO ip, final IndicatoreDTO ind) {
		final Integer[] out=getDecimaliAll(ip, ind);
		if(out[0]==null)
			out[0]=0;
		if(out[1]==null)
			out[1]=0;
		if(out[2]==null)
			out[2]=0;
		return out;
	}
	public static Integer[] getDecimaliAll(IndicatorePianoEasyDTO ip, IndicatoreDTO ind) {
		final Integer[] out=new Integer[]{null,null,null};
		if (ip == null ||ind==null)
			return out;
		out[0]=ip.getSpecificaDecimali()==null?
				ind.getDecimali():ip.getSpecificaDecimali();
		out[1]=ip.getSpecificaDecimaliA()==null?
				ind.getDecimaliA():ip.getSpecificaDecimaliA();
		out[2]=ip.getSpecificaDecimaliB()==null?
				ind.getDecimaliB():ip.getSpecificaDecimaliB();
		return out;
	}


	public static int getDecimali(ValutazioneDTO valutazione) {
		Integer[] dec = getDecimaliAllZero(valutazione);
		return dec[0];
	}



	public static int getDecimali(IndicatorePianoDTO ip, IndicatoreDTO ind) {
		Integer[] dec = getDecimaliAllZero(ip,ind);
		return dec[0];
	}
	public static Integer[] getDecimaliAllZero(final IndicatorePianoDTO ip, final IndicatoreDTO ind) {
		final Integer[] out=getDecimaliAll(ip, ind);
		if(out[0]==null)
			out[0]=0;
		if(out[1]==null)
			out[1]=0;
		if(out[2]==null)
			out[2]=0;
		return out;
	}
	public static Integer[] getDecimaliAll(IndicatorePianoDTO ip, IndicatoreDTO ind) {
		final Integer[] out=new Integer[]{null,null,null};
		if (ip == null ||ind==null)
			return out;
		out[0]=ip.getSpecificaDecimali()==null?
				ind.getDecimali():ip.getSpecificaDecimali();
		out[1]=ip.getSpecificaDecimaliA()==null?
				ind.getDecimaliA():ip.getSpecificaDecimaliA();
		out[2]=ip.getSpecificaDecimaliB()==null?
				ind.getDecimaliB():ip.getSpecificaDecimaliB();
		return out;
	}



	public static Integer getDecimali(IndicatorePianoEasyDTO ip, IndicatoreDTO ind) {
		Integer[] dec = getDecimaliAllZero(ip,ind);
		return dec[0];
	}
}
