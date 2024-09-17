package com.finconsgroup.performplus.service.business.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.enumeration.Periodicita;
import com.finconsgroup.performplus.enumeration.TipoValutazione;
import com.finconsgroup.performplus.rest.api.vm.IndicatoreVM;
import com.finconsgroup.performplus.rest.api.vm.ValutazioneVM;
import com.finconsgroup.performplus.rest.api.vm.v2.IndicatorePianoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.ValoreDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatorePianoListVM;
import com.finconsgroup.performplus.service.business.utils.DateHelper;
import com.finconsgroup.performplus.service.business.utils.IndicatorePianoHelper;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.MyDateHelper;

public class MappingIndicatorePianoHelper extends IndicatorePianoHelper{
	private MappingIndicatorePianoHelper() {
		super();
	}

	public static List<IndicatorePianoListVM> mappingToList(final List<IndicatorePiano> items,
			final Periodicita periodicitaTarget) {
		final List<IndicatorePianoListVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(ip -> out.add(mappingToList(ip).periodicitaTarget(periodicitaTarget)));
		return out;
	}

	public static IndicatorePianoListVM mappingToList(IndicatorePiano ip) {
		final IndicatorePianoListVM out = new IndicatorePianoListVM();
		out.setId(ip.getId());
		out.setDenominazione(
				StringUtils.isBlank(ip.getSpecifica()) ? ip.getIndicatore().getDenominazione() : ip.getSpecifica());
		out.setPeso(ip.getPeso() == null ? null : ip.getPeso().floatValue());
		out.setStrategico(Boolean.TRUE.equals(ip.getStrategico()));
		out.setSviluppoSostenibile(ip.getSviluppoSostenibile());
		out.setDimensione(ip.getDimensione());
		out.setFonte(ip.getFonte());
		out.setTipoIndicatore(ip.getTipoIndicatore());
		
		out.setPercentualeRaggiungimentoForzata(ip.getPercentualeRaggiungimentoForzata());
		out.setScadenzaIndicatore(ip.getScadenzaIndicatore());
		out.setScadenzaIndicatoreSt(DateHelper.toString(out.getScadenzaIndicatore()));				

		out.setBaseline(ip.getBaseline());
		out.setProspettiva(ip.getProspettiva());

		out.setUnitaMisura(ip.getUnitaMisura());
		return out;
	}

	public static void mapping(Valutazione v, IndicatorePianoListVM out, int anno, int annoInizio, int annoFine) {
		int year = v.getAnno() != null ? v.getAnno()
				: (v.getDataRilevazione() == null ? MyDateHelper.getAnno() : v.getDataRilevazione().getYear());
		int d = year - annoInizio;
		if (d < 0)
			d = 0;
		else if (d > 5)
			d = 5;
		if (TipoValutazione.STORICO.equals(v.getTipoValutazione())) {
			String value = getValToString(v);
			if (out.getAnno() == null || (out.getAnno() < v.getAnno() && StringUtils.isNotBlank(value))) {
				out.setValoreStorico(value);
				out.setAnno(v.getAnno());
			}
			return;
		}
		if (TipoValutazione.PREVENTIVO.equals(v.getTipoValutazione())) {
			if (v.getAnno().equals(anno)) {
				out.setTarget(getValToString(v));
			}
			switch (d) {
				case 0:
					out.setTarget1(getValToString(v));
					break;
				case 1:
					out.setTarget2(getValToString(v));
					break;
				case 2:
					out.setTarget3(getValToString(v));
					break;
				case 3:
					out.setTarget4(getValToString(v));
					break;
				case 4:
					out.setTarget5(getValToString(v));
					break;
				case 5:
					out.setTarget6(getValToString(v));
					break;
			}
		}
		
	}

	public static ValoreDetailVM mapping(Valutazione v) {
		final ValoreDetailVM out = Mapping.mapping(v, ValoreDetailVM.class);
		out.setValoreNumerico(v.getValoreNumerico() == null ? null : v.getValoreNumerico().doubleValue());
		out.setValoreNumericoA(v.getValoreNumericoA() == null ? null : v.getValoreNumericoA().doubleValue());
		out.setValoreNumericoB(v.getValoreNumericoB() == null ? null : v.getValoreNumericoB().doubleValue());
		out.setPercentuale(v.getIndicatorePiano().getSpecificaPercentuale() == null
				? v.getIndicatorePiano().getIndicatore().getPercentuale()
				: v.getIndicatorePiano().getSpecificaPercentuale());
		if (out.getValoreNumerico() == null && out.getValoreNumericoA() != null)
			out.setValoreNumerico(out.getValoreNumericoA());
		return out;
	}

	public static Integer[] getDecimaliAll(ValutazioneVM v) {
		return getDecimaliAll(v.getIndicatorePiano(), v.getIndicatorePiano().getIndicatore());
	}
	public static Integer[] getDecimaliAllZero(ValutazioneVM v) {
		return getDecimaliAllZero(v.getIndicatorePiano(), v.getIndicatorePiano().getIndicatore());
	}

	public static Integer[] getDecimaliAllZero(final IndicatorePianoVM ip, final IndicatoreVM ind) {
		final Integer[] out=getDecimaliAll(ip, ind);
		if(out[0]==null)
			out[0]=0;
		if(out[1]==null)
			out[1]=0;
		if(out[2]==null)
			out[2]=0;
		return out;
	}
	public static Integer[] getDecimaliAll(IndicatorePianoVM ip, IndicatoreVM ind) {
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


	public static int getDecimali(ValutazioneVM valutazione) {
		Integer[] dec = getDecimaliAllZero(valutazione);
		return dec[0];
	}

	public static Integer getDecimali(IndicatorePianoVM ip, IndicatoreVM ind) {
		Integer[] dec = getDecimaliAllZero(ip,ind);
		return dec[0];
	}
}
