package com.finconsgroup.performplus.service.business.utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.RangeIndicatore;
import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.enumeration.CalcoloConsuntivazione;
import com.finconsgroup.performplus.enumeration.Periodo;
import com.finconsgroup.performplus.enumeration.TipoConsuntivazione;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.enumeration.TipoPiano;
import com.finconsgroup.performplus.enumeration.TipoValutazione;
import com.finconsgroup.performplus.repository.RangeIndicatoreRepository;
import com.finconsgroup.performplus.repository.ValutazioneRepository;
import com.finconsgroup.performplus.rest.api.pi.vm.ConsuntivoIndicatore;
import com.finconsgroup.performplus.service.business.utils.DateHelper;
import com.finconsgroup.performplus.service.business.utils.MyDateHelper;
import com.finconsgroup.performplus.service.dto.ConsuntivoIndicatoreDTO;


public class ConsuntivoUtils {


	private ConsuntivoUtils() {
	}

	public static LocalDate fineAnno(final LocalDate date) {
		if (date == null)
			return null;
		return fineAnno(getAnno(date));
	}

	public static LocalDate fineAnno(final int anno) {
		return LocalDate.of(anno,12,31);
	}

	public static LocalDate inizioAnno(final int anno) {
		return LocalDate.of(anno,01,01);
	}

	public static int getAnno(final LocalDate inizio) {
		return inizio.getYear();
	}

	public static int getMese(final LocalDate inizio) {
		return inizio.getMonthValue();
	}


//	public static LocalDate getInizio(final int anno, final int annoInizio, final int annoFine, final NodoPiano np,
//			final NodoPiano piano) {
//		LocalDate inizioMin = np.getInizio();
//		if (inizioMin != null)
//			return inizioMin;
//		switch (np.getTipoNodo()) {
//		case PIANO:
//			return LocalDate.of(anno,01,01);
//		case OBIETTIVO:
//			return LocalDate.of(annoFine,01,01);
//		default:
//			if (anno > annoFine)
//				return null;
//			return LocalDate.of(anno,01,01);
//		}
//
//	}

	public static int meseInizio(final Periodo periodo, final TipoConsuntivazione tipoConsuntivazione) {
		switch (periodo) {
		case PERIODO_1:
		case PERIODO_2:
				return 1;
		case PERIODO_3:
		case PERIODO_4:
				return 7;
		default:
			return 1;
		}
	}

	public static int meseFine(final Periodo periodo, final TipoConsuntivazione tipoConsuntivazione) {
		switch (periodo) {
		case PERIODO_1:
		case PERIODO_2:
				return 6;
		case PERIODO_3:
		case PERIODO_4:
				return 12;
		default:
			return 12;
		}
	}

	public static int giornoFine(final Periodo periodo, final TipoConsuntivazione tipoConsuntivazione) {
		switch (periodo) {
		case PERIODO_1:
		case PERIODO_2:
				return 30;
		case PERIODO_3:
		case PERIODO_4:
			switch (tipoConsuntivazione) {
			case ANNO:
				return 31;
			case SEMESTRE:
				return 31;
			case QUADRIMESTRE:
				return 31;
			case TRIMESTRE:
				return 31;
			case BIMESTRE:
				return 31;
			case MESE:
				return 30;
			}
		default:
			return 31;
		}
	}



	public static LocalDate getFine(final int anno, final int annoInizio, final int annoFine, final NodoPiano np,
			final NodoPiano piano) {
		LocalDate fineMax = np.getScadenza();
		if (fineMax != null)
			return fineMax;
		switch (np.getTipoNodo()) {
		case PIANO:
		case OBIETTIVO:
			return LocalDate.of(annoFine,12,31);
		default:
			if (anno > annoFine)
				return null;
			return LocalDate.of(anno,12,31);
		}

	}

	public static LocalDate inizioPeriodo(final int anno, final Periodo tipoPiano,
			final TipoConsuntivazione tipoConsuntivazione) {
		return LocalDate.of(
				anno,meseInizio(tipoPiano, tipoConsuntivazione),1);
	}

	public static LocalDate finePeriodo(final int anno, final Periodo tipoPiano,
			final TipoConsuntivazione tipoConsuntivazione) {
		return LocalDate.of(anno,meseFine(tipoPiano, tipoConsuntivazione),
						giornoFine(tipoPiano, tipoConsuntivazione));
	}



	public static Periodo toPeriodo(final TipoPiano tipoPiano, final TipoConsuntivazione tipoConsuntivazione) {
		if (TipoPiano.RELAZIONE.equals(tipoPiano))
			return Periodo.PERIODO_2;
		return tipoPianoTrimestrale(ConsuntivoUtils.getPeriodo(MyDateHelper.getMese(), tipoConsuntivazione));
	}

	public static Periodo tipoPianoTrimestrale(final int periodo) {
		switch (periodo) {
		case 1:
			return Periodo.PERIODO_1;
		case 2:
			return Periodo.PERIODO_2;
		case 3:
			return Periodo.PERIODO_3;
		case 4:
			return Periodo.PERIODO_4;
		default:
			return null;
		}

	}

	public static int getPeriodo(final int mese, final TipoConsuntivazione tipoConsuntivazione) {
		int per = 0;
		switch (tipoConsuntivazione) {
		case MESE:
			return mese;
		case BIMESTRE:
			per = mese / 2;
			return (mese % 2 == 0) ? per : per + 1;
		case TRIMESTRE:
			per = mese / 3;
			return (mese % 3 == 0) ? per : per + 1;
		case QUADRIMESTRE:
			per = mese / 4;
			return (mese % 4 == 0) ? per : per + 1;
		case SEMESTRE:
			per = mese / 6;
			return (mese % 6 == 0) ? per : per + 1;
		case ANNO:
			return 12;
		}
		return 0;
	}

	public static int getPeriodo(final Periodo periodo) {
		switch (periodo) {
		case PERIODO_1:
			return 1;
		case PERIODO_2:
			return 2;
		case PERIODO_3:
			return 3;
		case PERIODO_4:
			return 4;
		default:
			return 0;
		}
	}

//	public static String getStatoObiettivoGestionale(final NodoPianoEasyDTO nodo) {
//		if (nodo == null || !TipoNodo.OBIETTIVO.equals(nodo.getTipoNodo()))
//			return null;
//		StatoObiettivoGestionale stato = nodo.getStatoObiettivoGestionale();
//		if (stato == null || StatoObiettivoGestionale.CONCLUSA_ANNO.equals(stato))
//			return null;
//		return stato.name();
//	}


	/*

	public static Double range(List<RangeIndicatoreDTO> ranges, double cons, double target, boolean asc) {
		if (ranges == null || ranges.isEmpty()) {
			return perc(0, target, cons, asc, 100d,0d,0d);
		}
		final RangeIndicatoreDTO max = ranges.get(ranges.size() - 1);
		final RangeIndicatoreDTO min = ranges.get(0);
		if (asc && cons >= max.getMassimo()) {
			return max.getValore();
		}
		if (!asc && cons <= min.getMinimo()) {
			return min.getValore();
		}
		int k=-1;
		for (RangeIndicatoreDTO r : ranges) {
			k++;
			if (cons >= r.getMinimo() && cons <= r.getMassimo()) {
				if (Boolean.TRUE.equals(r.getProporzionale()))
					return perc(r.getMinimo(), r.getMassimo(), cons, asc, r.getValore(),
							(k==0?0d:ranges.get(k-1).getValore()),
							(k<ranges.size()-1?ranges.get(k+1).getValore():0d));
				else
					return r.getValore();
			}
		}

		if (asc) {
			if (cons < ranges.get(0).getMinimo()) {
				return 0d;
			}
			if (cons > ranges.get(ranges.size() - 1).getMassimo()) {
				return ranges.get(ranges.size() - 1).getValore();
			}
		} else {
			if (cons < ranges.get(0).getMinimo()) {
				return ranges.get(0).getValore();
			}
			if (cons > ranges.get(ranges.size() - 1).getMassimo()) {
				return 0d;
			}

		}
		return 0d;
	}

*/

	public static Double raggiungimentoPesato(final Double raggiungimentoPerc, Double pesoRelativo) {
		if (raggiungimentoPerc == null || pesoRelativo == null)
			return null;
		return BigDecimal.valueOf(raggiungimentoPerc).multiply(BigDecimal.valueOf(pesoRelativo)).doubleValue();
	}

	public static Double pesoRelativo(double peso, double pesoTotale) {
		if (pesoTotale <= 0)
			return 0d;
		return BigDecimal.valueOf(peso).multiply(BigDecimal.valueOf(100d))
				.divide(BigDecimal.valueOf(pesoTotale), 2, RoundingMode.HALF_DOWN).doubleValue();
	}

	public static Double pesatoPerc(Double p, Double r) {
		if (r == null || p == null)
			return 0d;
		return BigDecimal.valueOf(p).multiply(BigDecimal.valueOf(r))
				.divide(BigDecimal.valueOf(100d), 2, RoundingMode.HALF_DOWN).doubleValue();
	}
	public static Float pesatoPerc(Float p, Float r) {
		if (r == null || p == null)
			return 0f;
		return BigDecimal.valueOf(p).multiply(BigDecimal.valueOf(r))
				.divide(BigDecimal.valueOf(100f), 2, RoundingMode.HALF_DOWN).floatValue();
	}
	public static Double raggiungimentoRange(final Boolean decrescente, final Double preventivo,
			final Double consuntivo, final TipoFormula tipoFormula, List<RangeIndicatore> ranges, boolean percentuale) {
		boolean asc = !Boolean.TRUE.equals(decrescente);
		Double target = preventivo;
		if (target == null) {
			return null;
		}
		Double cons = consuntivo;
		if (cons == null) {
			return null;
		}
		if (TipoFormula.TIPO_FORMULA_DATA.equals(tipoFormula)) {
			long diffInMillies = Math.abs(cons.longValue() - target.longValue());
			long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			if (cons < target) {
				diff = -diff;
			}
			target = 0d;
			cons = (double) diff;
		}
		if(percentuale) {
			cons=cons*100d;
			target=target*100d;
		}
		return calcolaFromRanges(ranges, cons, target, asc);
	}

	public static Double calcolaFromRanges(List<RangeIndicatore> ranges, double cons, double target, boolean asc) {
		if (ranges == null || ranges.isEmpty()) {
			return calc(target, cons, asc)*100d;
		}
		final RangeIndicatore max = ranges.get(ranges.size() - 1);
		final RangeIndicatore min = ranges.get(0);
		if (asc && cons >= max.getMassimo()) {
			return max.getValore();
		}
		if (!asc && cons <= min.getMinimo()) {
			return min.getValore();
		}
		int k=-1;
		for (RangeIndicatore r : ranges) {
			k++;
			if (cons >= r.getMinimo() && cons <= r.getMassimo()) {
				if (Boolean.TRUE.equals(r.getProporzionale()))
					return perc(r.getMinimo(), r.getMassimo(), cons, asc, r.getValore(),
							(k==0?0d:ranges.get(k-1).getValore()),
							(k<ranges.size()-1?ranges.get(k+1).getValore():0d));
				else
					return r.getValore();
			}
		}

		if (asc) {
			if (cons < ranges.get(0).getMinimo()) {
				return 0d;
			}
			if (cons > ranges.get(ranges.size() - 1).getMassimo()) {
				return ranges.get(ranges.size() - 1).getValore();
			}
		} else {
			if (cons < ranges.get(0).getMinimo()) {
				return ranges.get(0).getValore();
			}
			if (cons > ranges.get(ranges.size() - 1).getMassimo()) {
				return 0d;
			}

		}
		return 0d;
	}

	public static Double perc(double min, double max, double cons, boolean asc, double value, double valprec, double valsucc) {
		double perc;
		double agg;
		double base;
		double divide=Math.abs(max - min);
		double toMin=Math.abs(cons - min);
		double toMax=Math.abs(max - cons);
		if(valprec==valsucc) {
			perc=calc(max,value,asc)*100d;
			return Math.min(perc, 100d);
		}
		if (asc) {
			base=valprec+1;
			if(base==value)
				base=base-0.5d;
			agg=value-base;
			if (cons >= max) {
				perc = value;
			}
			else {
				perc = BigDecimal.valueOf(toMin).multiply(BigDecimal.valueOf(agg)).divide(BigDecimal.valueOf(divide), 2, RoundingMode.HALF_DOWN)
						.doubleValue();
				perc=base+perc;
			}
		} else {
			base=valsucc+1;
			if(base==value)
				base=base-0.5d;
			agg=value-base;
			if(cons<=min) {
				perc=value;
			}else {
				perc = BigDecimal.valueOf(toMax).multiply(BigDecimal.valueOf(agg)).divide(BigDecimal.valueOf(divide), 2, RoundingMode.HALF_DOWN)
						.doubleValue();
				perc=base+perc;
			}
		}
		return Math.min(perc, 100d);

	}
	public static Double calc(double target, double val, boolean asc) {
		System.out.println("-- target:" + target + ",val:" + val + ",asc:" + asc);
		BigDecimal out = null;
		double min = 0d;
		if (val==target) {
			out = BigDecimal.ONE;
			System.out.println(out);
			return out.doubleValue();
		}

		if (asc) {
			if (val > target) {
				val = target;
				out = BigDecimal.ONE;
				System.out.println(out);
				return out.doubleValue();
			}
			if ((val < 0 && target > 0)) {
				val = 0d;
			} else {
				min = val + target;
				if (min < 0) {
					val = min - val;
					target = min - target;
				}
			}
		} else {
			if (val <= target) {
				val = target;
				out = BigDecimal.ONE;
				System.out.println(out);
				return out.doubleValue();
			}
			if ((val > 0 && target < 0)) {
				val = 0d;
			} else {
				min = val + target;
				if (min > 0) {
					val = min - val;
					target = min - target;
				}
			}

		}
		if (target == 0 && val == 0) {
			out = BigDecimal.ONE;
			System.out.println(out);
			return out.doubleValue();
		}

		if (target == 0) {
			out = BigDecimal.ZERO;
			System.out.println(out);
			return out.doubleValue();
		}

		out = BigDecimal.valueOf(val).divide(BigDecimal.valueOf(target), 2, RoundingMode.HALF_UP);

		System.out.println(out);
		return out.doubleValue();
	}
//	public static Double calcolaFromRanges(List<RangeIndicatore> ranges, double cons, double target, boolean asc) {
//		if (ranges == null || ranges.isEmpty()) {
//			return perc(0, target, cons, asc, 100d);
//		}
//		final RangeIndicatore max = ranges.get(ranges.size() - 1);
//		final RangeIndicatore min = ranges.get(0);
//		if (asc && cons >= max.getMassimo()) {
//			return max.getValore();
//		}
//		if (!asc && cons <= min.getMinimo()) {
//			return min.getValore();
//		}
//		for (RangeIndicatore r : ranges) {
//			if (cons >= r.getMinimo() && cons < r.getMassimo()) {
//				if (Boolean.TRUE.equals(r.getProporzionale()))
//					return perc(r.getMinimo(), r.getMassimo(), cons, asc, r.getValore());
//				else
//					return r.getValore();
//			}
//		}
//
//		if (asc) {
//			if (cons < ranges.get(0).getMinimo()) {
//				return 0d;
//			}
//			if (cons > ranges.get(ranges.size() - 1).getMassimo()) {
//				return ranges.get(ranges.size() - 1).getValore();
//			}
//		} else {
//			if (cons < ranges.get(0).getMinimo()) {
//				return ranges.get(0).getValore();
//			}
//			if (cons > ranges.get(ranges.size() - 1).getMassimo()) {
//				return 0d;
//			}
//
//		}
//		return 0d;
//	}


	public static Double getValore(Valutazione v, TipoFormula tipoFormula) {
		if(v==null)
			return null;
		if (tipoFormula == null)
			return null;
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(tipoFormula))
			return getValoreBOOL(v);
		if (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula))
			return v.getValoreNumerico() == null ? null : v.getValoreNumerico().doubleValue();
		if (TipoFormula.TIPO_FORMULA_DATA.equals(tipoFormula))
			return getValoreTemporale(v);
		if (v.getValoreNumericoA() == null || v.getValoreNumericoB() == null)
			return null;
		if (v.getValoreNumericoB().doubleValue() == 0)
			return null;
		return v.getValoreNumericoA().doubleValue() / v.getValoreNumericoB().doubleValue();
	}

	public static Double getValoreBOOL(Valutazione v) {
		if (v.getValoreBooleano() == null)
			return null;
		return Boolean.TRUE.equals(v.getValoreBooleano()) ? 1d : 0d;
	}

	public static Double getValoreTemporale(Valutazione v) {
		if (v.getValoreTemporale() == null)
			return null;
		return (double) DateHelper.asDate(v.getValoreTemporale()).getTime();
	}
	public static void calcolaValoreNumerico(Valutazione valutazione, TipoFormula tipoFormula,
			CalcoloConsuntivazione calcoloConsuntivazione, int decimali) {
		if (TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula)
        		|| (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula)
        		&& CalcoloConsuntivazione.RAPPORTO.equals(calcoloConsuntivazione))) {
        	if(valutazione.getValoreNumericoA()!=null&&valutazione.getValoreNumericoB()!=null&& valutazione.getValoreNumericoB().doubleValue()!=0) {
        		valutazione.setValoreNumerico(valutazione.getValoreNumericoA().divide(valutazione.getValoreNumericoB(),decimali, RoundingMode.HALF_DOWN));
        	}else {
        		valutazione.setValoreNumerico(null);
        	}
        }
        if (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula)
        		&& CalcoloConsuntivazione.DIFFERENZA_CRESCENTE.equals(calcoloConsuntivazione)) {
           	if(valutazione.getValoreNumericoA()!=null&&valutazione.getValoreNumericoB()!=null&& valutazione.getValoreNumericoA().doubleValue()!=0) {
        		valutazione.setValoreNumerico((valutazione.getValoreNumericoB().subtract(valutazione.getValoreNumericoA())).divide(valutazione.getValoreNumericoA(),decimali, RoundingMode.HALF_DOWN));  
           	}else {
        		valutazione.setValoreNumerico(null);
        	}
           	       	
        }
        if (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula)
        		&& CalcoloConsuntivazione.DIFFERENZA_DECRESCENTE.equals(calcoloConsuntivazione)) {
           	if(valutazione.getValoreNumericoA()!=null&&valutazione.getValoreNumericoB()!=null&& valutazione.getValoreNumericoA().doubleValue()!=0) {
        		valutazione.setValoreNumerico((valutazione.getValoreNumericoA().subtract(valutazione.getValoreNumericoB())).divide(valutazione.getValoreNumericoA(),decimali, RoundingMode.HALF_DOWN));  
           	}else {
        		valutazione.setValoreNumerico(null);
        	}
           	       	
        }
        if (TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula)) {
           	if(valutazione.getValoreNumericoA()!=null&&valutazione.getValoreNumericoB()!=null&& valutazione.getValoreNumericoB().doubleValue()!=0) {
        		valutazione.setValoreNumerico(valutazione.getValoreNumericoA().divide(valutazione.getValoreNumericoB(),decimali, RoundingMode.HALF_DOWN));
        	}else {
        		valutazione.setValoreNumerico(null);
        	}
		} 
        if (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula)
        		&& valutazione.getValoreNumerico()==null
        		&& valutazione.getValoreNumericoA()!=null
        		&& (calcoloConsuntivazione==null|| calcoloConsuntivazione.equals(CalcoloConsuntivazione.DEFAULT))) {
			valutazione.setValoreNumerico(valutazione.getValoreNumericoA());
		}
	}
	public static ConsuntivoIndicatore consuntivoIndicatore(final IndicatorePiano indicatorePiano
			, final Integer anno
			, final ValutazioneRepository valutazioneRepository
			, final RangeIndicatoreRepository rangeIndicatoreRepository) {
		final ConsuntivoIndicatore out=new ConsuntivoIndicatore();
		out.setTipoFormula(indicatorePiano.getIndicatore().getTipoFormula());
		out.setDesc(Boolean.TRUE.equals(indicatorePiano.getDecrescente()));
		Valutazione p1=valutazioneRepository.findByIndicatorePianoIdAndTipoValutazioneAndAnnoAndPeriodo(indicatorePiano.getId(), TipoValutazione.PREVENTIVO
				, anno, 2);
		Valutazione p2=valutazioneRepository.findByIndicatorePianoIdAndTipoValutazioneAndAnnoAndPeriodo(indicatorePiano.getId(), TipoValutazione.PREVENTIVO
				, anno, 4);
		Valutazione c1=valutazioneRepository.findByIndicatorePianoIdAndTipoValutazioneAndAnnoAndPeriodo(indicatorePiano.getId()
				, TipoValutazione.CONSUNTIVO
				, anno, 2);
		Valutazione c2=valutazioneRepository.findByIndicatorePianoIdAndTipoValutazioneAndAnnoAndPeriodo(indicatorePiano.getId()
				, TipoValutazione.CONSUNTIVO
				, anno, 4);
		out.setPeso(indicatorePiano.getPeso() == null ? null : indicatorePiano.getPeso().floatValue());
		out.setStategico(Boolean.TRUE.equals(indicatorePiano.getStrategico()));
		out.setSostenibile(Boolean.TRUE.equals(indicatorePiano.getSviluppoSostenibile()));
		out.setPercentualeEsogena(indicatorePiano.getRaggiungimentoForzato());
		out.setPercentualeEsogenaProposta(indicatorePiano.getRichiestaForzatura());
		out.setNonValutabile(Boolean.TRUE.equals(indicatorePiano.getNonValutabile()));
		out.setPercentuale(indicatorePiano.getSpecificaPercentuale() == null
				? Boolean.TRUE.equals(indicatorePiano.getIndicatore().getPercentuale())
				: indicatorePiano.getSpecificaPercentuale());

		if(p2!=null)
			out.setTarget(ConsuntivoUtils.getValore(p2, out.getTipoFormula()));
		else
			out.setTarget(ConsuntivoUtils.getValore(p1, out.getTipoFormula()));
		if(c2!=null)
			out.setCons(ConsuntivoUtils.getValore(c2, out.getTipoFormula()));
		else
			out.setCons(ConsuntivoUtils.getValore(c1, out.getTipoFormula()));

		List<RangeIndicatore> ranges = rangeIndicatoreRepository
				.findByIndicatorePianoIdOrderByMinimoAsc(indicatorePiano.getId());
		Double ragg = ConsuntivoUtils.raggiungimentoRange(out.isDesc(), out.getTarget(), out.getCons(), out.getTipoFormula(),
				ranges, out.isPercentuale());
		ragg=PIHelper.perc(ragg,anno)*100d;
		out.setRaggiungimentoEffettivo(ragg);

		if (out.isNonValutabile()) {
			out.setRaggiungimentoPesato(0d);
		} else {
			if (out.getPercentualeEsogena() != null) {
				double[] ris = ConsuntivoUtils.valoreEsogeno(out.getRaggiungimentoPesatoEffettivo(),
						out.getPercentualeEsogena());
				out.setIncrementoEsogeno(ris[1]);
				out.setRaggiungimento(ris[0]);
			} else {
				out.setRaggiungimento(ragg);
			}
			//forzatura peso 100.0 (non utilizzato)
//			if (out.getPeso() != null) {
				out.setRaggiungimentoPesato(
						ConsuntivoUtils.pesatoPerc(ragg,100.0));
				out.setRaggiungimentoPesatoEffettivo(ConsuntivoUtils
						.pesatoPerc(out.getRaggiungimento(), 100.0));
//			}

		}
		return out;
	}
	public static double[] valoreEsogeno(Double valore, Double percEsogena) {
		if(percEsogena==null|| percEsogena<=0)
			return  new double[] {100d,0d,0d};
		double d=valore==null||valore<0?100d:100d-valore;
		if(d<=0) return new double[] {100d,0d,d};
		double incrementoEsogeno=BigDecimal.valueOf(d).multiply(BigDecimal.valueOf(percEsogena)).divide(BigDecimal.valueOf(100d), 2, RoundingMode.HALF_DOWN).doubleValue();
		double valoreEsogeno=(valore==null?0d:valore)+incrementoEsogeno;
		if(valoreEsogeno<0)valoreEsogeno=0d;
		if(valoreEsogeno>100)valoreEsogeno=100d;
		return new double[] {valoreEsogeno,incrementoEsogeno,d};
	}
}