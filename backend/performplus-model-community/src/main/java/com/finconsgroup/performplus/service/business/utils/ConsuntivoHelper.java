package com.finconsgroup.performplus.service.business.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.finconsgroup.performplus.domain.RangeIndicatore;
import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.enumeration.CalcoloConsuntivazione;
import com.finconsgroup.performplus.enumeration.Periodo;
import com.finconsgroup.performplus.enumeration.TipoConsuntivazione;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoPiano;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.ConsuntivoIndicatoreDTO;
import com.finconsgroup.performplus.service.dto.NodoPianoDTO;
import com.finconsgroup.performplus.service.dto.PianoDTO;
import com.finconsgroup.performplus.service.dto.RangeIndicatoreDTO;

public class ConsuntivoHelper {
	public static final String INIZIO_ANNO_01_01 = "-01-01";
	public static final String FINE_ANNO_12_31 = "-12-31";

	protected ConsuntivoHelper() {
		super();
	}

	public static LocalDate fineAnno(final LocalDate date) {
		if (date == null)
			return null;
		return fineAnno(getAnno(date));
	}

	public static LocalDate fineAnno(final int anno) {
		return LocalDate.of(anno, 12, 31);
	}

	public static LocalDate inizioAnno(final int anno) {
		return LocalDate.of(anno, 01, 01);
	}

	public static int getAnno(final LocalDate inizio) {
		return inizio.getYear();
	}

	public static int getMese(final LocalDate inizio) {
		return inizio.getMonthValue();
	}

	public static LocalDate getInizio(final int anno, final int annoInizio, final int annoFine, final NodoPianoDTO np,
			final PianoDTO piano) {
		return np.getInizio();

	}

	public static int meseInizio(final Periodo periodo, final TipoConsuntivazione tipoConsuntivazione) {
		switch (periodo) {
		case PERIODO_1:
			return Calendar.JANUARY;
		case PERIODO_2:
			switch (tipoConsuntivazione) {
			case ANNO:
				return Calendar.JANUARY;
			case SEMESTRE:
				return Calendar.JULY;
			case QUADRIMESTRE:
				return Calendar.MAY;
			case TRIMESTRE:
				return Calendar.APRIL;
			case BIMESTRE:
				return Calendar.MARCH;
			case MESE:
				return Calendar.FEBRUARY;
			default:
				return Calendar.JULY;
			}
		case PERIODO_3:
			switch (tipoConsuntivazione) {
			case ANNO:
				return Calendar.JANUARY;
			case SEMESTRE:
				return Calendar.JULY;
			case QUADRIMESTRE:
				return Calendar.SEPTEMBER;
			case TRIMESTRE:
				return Calendar.JULY;
			case BIMESTRE:
				return Calendar.MAY;
			case MESE:
				return Calendar.MARCH;
			default:
				return Calendar.JULY;
			}
		case PERIODO_4:
			switch (tipoConsuntivazione) {
			case ANNO:
				return Calendar.JANUARY;
			case SEMESTRE:
				return Calendar.JULY;
			case QUADRIMESTRE:
				return Calendar.SEPTEMBER;
			case TRIMESTRE:
				return Calendar.OCTOBER;
			case BIMESTRE:
				return Calendar.JULY;
			case MESE:
				return Calendar.APRIL;
			default:
				return Calendar.JULY;
			}

		default:
			return Calendar.JANUARY;
		}
	}

	public static int meseFine(final Periodo periodo, final TipoConsuntivazione tipoConsuntivazione) {
		switch (periodo) {
		case PERIODO_1:
			switch (tipoConsuntivazione) {
			case ANNO:
				return Calendar.DECEMBER;
			case SEMESTRE:
				return Calendar.JUNE;
			case QUADRIMESTRE:
				return Calendar.APRIL;
			case TRIMESTRE:
				return Calendar.MARCH;
			case BIMESTRE:
				return Calendar.FEBRUARY;
			case MESE:
				return Calendar.JANUARY;
			default:
				return Calendar.JUNE;
			}
		case PERIODO_2:
			switch (tipoConsuntivazione) {
			case ANNO:
				return Calendar.DECEMBER;
			case SEMESTRE:
				return Calendar.DECEMBER;
			case QUADRIMESTRE:
				return Calendar.AUGUST;
			case TRIMESTRE:
				return Calendar.JUNE;
			case BIMESTRE:
				return Calendar.APRIL;
			case MESE:
				return Calendar.FEBRUARY;
			default:
				return Calendar.DECEMBER;
			}
		case PERIODO_3:
			switch (tipoConsuntivazione) {
			case ANNO:
				return Calendar.DECEMBER;
			case SEMESTRE:
				return Calendar.DECEMBER;
			case QUADRIMESTRE:
				return Calendar.DECEMBER;
			case TRIMESTRE:
				return Calendar.SEPTEMBER;
			case BIMESTRE:
				return Calendar.JUNE;
			case MESE:
				return Calendar.MARCH;
			default:
				return Calendar.DECEMBER;
			}
		case PERIODO_4:
			switch (tipoConsuntivazione) {
			case ANNO:
				return Calendar.DECEMBER;
			case SEMESTRE:
				return Calendar.DECEMBER;
			case QUADRIMESTRE:
				return Calendar.DECEMBER;
			case TRIMESTRE:
				return Calendar.DECEMBER;
			case BIMESTRE:
				return Calendar.AUGUST;
			case MESE:
				return Calendar.APRIL;
			default:
				return Calendar.DECEMBER;
			}
		default:
			return Calendar.DECEMBER;
		}
	}

	public static int giornoFine(final Periodo periodo, final TipoConsuntivazione tipoConsuntivazione) {
		switch (periodo) {
		case PERIODO_1:
			switch (tipoConsuntivazione) {
			case ANNO:
				return 31;
			case SEMESTRE:
				return 30;
			case QUADRIMESTRE:
				return 30;
			case TRIMESTRE:
				return 31;
			case BIMESTRE:
				return 28;
			case MESE:
				return 31;
			default:
				return 30;
			}
		case PERIODO_2:
			switch (tipoConsuntivazione) {
			case ANNO:
				return 31;
			case SEMESTRE:
				return 31;
			case QUADRIMESTRE:
				return 31;
			case TRIMESTRE:
				return 30;
			case BIMESTRE:
				return 30;
			case MESE:
				return 28;
			default:
				return 31;
			}
		case PERIODO_3:
			switch (tipoConsuntivazione) {
			case ANNO:
				return 31;
			case SEMESTRE:
				return 31;
			case QUADRIMESTRE:
				return 31;
			case TRIMESTRE:
				return 30;
			case BIMESTRE:
				return 30;
			case MESE:
				return 31;
			default:
				return 31;
			}
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
			default:
				return 31;
			}
		default:
			return 31;
		}
	}

	public static LocalDate getFine(final int anno, final int annoInizio, final int annoFine, final NodoPianoDTO np,
			final PianoDTO piano) {
		return np.getScadenza();

	}

	public static LocalDate inizioPeriodo(final int anno, final Periodo tipoPiano,
			final TipoConsuntivazione tipoConsuntivazione) {
		return LocalDate.of(anno, meseInizio(tipoPiano, tipoConsuntivazione) + 1, 1);
	}

	public static LocalDate finePeriodo(final int anno, final Periodo tipoPiano,
			final TipoConsuntivazione tipoConsuntivazione) {
		return LocalDate.of(anno, meseFine(tipoPiano, tipoConsuntivazione) + 1,
				giornoFine(tipoPiano, tipoConsuntivazione));
	}

	public static LocalDate inizioPeriodo(final PianoDTO piano, final TipoConsuntivazione tipoConsuntivazione) {
		return inizioPeriodo(piano.getAnno(), toPeriodo(piano.getTipoPiano(), tipoConsuntivazione),
				tipoConsuntivazione);
	}

	public static LocalDate finePeriodo(final PianoDTO piano, final TipoConsuntivazione tipoConsuntivazione) {
		return finePeriodo(piano.getAnno(), toPeriodo(piano.getTipoPiano(), tipoConsuntivazione), tipoConsuntivazione);
	}

	public static Periodo toPeriodo(final TipoPiano tipoPiano, final TipoConsuntivazione tipoConsuntivazione) {
		if (TipoPiano.RELAZIONE.equals(tipoPiano))
			return Periodo.PERIODO_4;
		return tipoPeriodo(ConsuntivoHelper.getPeriodo(MyDateHelper.getMese(), tipoConsuntivazione));
	}

	public static Periodo tipoPeriodo(final int periodo) {
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
			return 1;
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

//	public static LocalDate getDataInizioEff(final NodoPianoDTO np) {
//		if (np == null || TipoNodo.PIANO.equals(np.getTipoNodo()))
//			return null;
//		if (TipoNodo.AZIONE.equals(np.getTipoNodo())) {
//			LocalDate ini = np.getInizioEffettivo();
//			return ini;
//		}
//		if (TipoNodo.RISULTATO_ATTESO.equals(np.getTipoNodo())) {
//			LocalDate ini = np.getInizioEffettivo();
//			if (ini == null)
//				ini = np.getInizio();
//			return ini;
//
//		}
//
//		return getDataInizio(np);
//	}

//	public static LocalDate obiettivoGestionaleDataInizio(final NodoPianoDTO nodo) {
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
//		return getDataInizio(nodo);
//	}

	public static LocalDate getDataFineEff(final NodoPianoDTO np) {
		if (np == null || TipoNodo.PIANO.equals(np.getTipoNodo()))
			return null;

		LocalDate sca = np.getScadenzaEffettiva();
		if (sca == null)
			sca = np.getScadenza();
		return sca;

	}

//	public static LocalDate obiettivoGestionaleDataFine(final NodoPianoDTO nodo) {
//		LocalDate dt = null;
//		List<NodoPianoDTO> list = NodoPianoHelper.contiene(nodo);
//		for (NodoPianoDTO np : list) {
//			if (TipoNodo.RISULTATO_ATTESO.equals(np.getTipoNodo())) {
//				LocalDate sca = np.getScadenzaEffettiva();
//				if (sca == null)
//					sca = np.getScadenza();
//				if (dt == null || sca.isAfter(dt))
//					dt = sca;
//			}
//		}
//		if (dt != null)
//			return dt;
//		return getDataFine(nodo);
//	}

	public static LocalDate getDataFine(NodoPianoDTO np) {
		return np.getScadenza();
	}

	public static LocalDate getDataInizio(NodoPianoDTO np) {
		return np.getInizio();
	}

	public static boolean mostraTarget1(final NodoPianoDTO nodo, final PianoDTO piano) {
		if (nodo == null)
			return false;
		if (piano == null)
			return false;
		return piano.getAnnoFine() >= piano.getAnno() + 1;
	}

	public static boolean mostraTarget2(final NodoPianoDTO nodo, final PianoDTO piano) {
		if (nodo == null)
			return false;
		if (piano == null)
			return false;
		return piano.getAnnoFine() >= piano.getAnno() + 2;
	}

	public static boolean mostraTarget3(final NodoPianoDTO nodo, final PianoDTO piano) {
		if (nodo == null)
			return false;
		if (piano == null)
			return false;
		return piano.getAnnoFine() > piano.getAnno() + 2;
	}

	public static boolean mostraTarget4(final NodoPianoDTO nodo, final PianoDTO piano) {
		if (nodo == null)
			return false;
		if (piano == null)
			return false;
		return piano.getAnnoFine() > piano.getAnno() + 3;
	}

	public static boolean mostraTarget5(final NodoPianoDTO nodo, final PianoDTO piano) {
		if (nodo == null)
			return false;
		if (piano == null)
			return false;
		return piano.getAnnoFine() > piano.getAnno() + 4;
	}

	public static boolean mostraTarget6(final NodoPianoDTO nodo, final PianoDTO piano) {
		if (nodo == null)
			return false;
		if (piano == null)
			return false;
		return piano.getAnnoFine() > piano.getAnno() + 5;
	}

	public static Double raggiungimento(final ConsuntivoIndicatoreDTO consuntivoIndicatore) {
		Double target = consuntivoIndicatore.getPreventivoNodo();

		boolean asc = !Boolean.TRUE.equals(consuntivoIndicatore.getIndicatorePiano().getDecrescente());

		if (target == null)
			return 0d;
		Double min = null;
		Double val = null;
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(consuntivoIndicatore.getTipoFormula())) {
			if (consuntivoIndicatore.getIndicatorePiano() != null
					&& consuntivoIndicatore.getIndicatorePiano().getStorico() != null
					&& consuntivoIndicatore.getIndicatorePiano().getStorico().getValoreBooleano() != null)
				min = consuntivoIndicatore.getIndicatorePiano().getStorico().getValoreBOOL();
			if (consuntivoIndicatore.getConsuntivoMax() != null)
				val = consuntivoIndicatore.getConsuntivoMax().getValoreBOOL();
		} else {
			if (consuntivoIndicatore.getIndicatorePiano() != null
					&& consuntivoIndicatore.getIndicatorePiano().getStorico() != null
					&& consuntivoIndicatore.getIndicatorePiano().getStorico().getValore() != null)
				min = consuntivoIndicatore.getIndicatorePiano().getStorico().getValore();
			if (consuntivoIndicatore.getConsuntivoMax() != null)
				val = consuntivoIndicatore.getConsuntivoMax().getValore();
		}
		if (min == null)
			min = 0d;
		if (val == null)
			return 0d;
		if (val.equals(target))
			return 100d;

		if (asc) {
			if (val >= target || target == 0) {
				return 100d;
			}
			if (val < 0) {
				val = 0d;
			}
			return (val * 100) / target;
		} else {
			if (val <= target || val == 0) {
				return 100d;
			}
			if (target < 0) {
				target = 0d;
			}
			return (target * 100) / val;
		}

	}

	public static Double raggiungimentoRange(final ConsuntivoIndicatoreDTO consuntivoIndicatore) {
		boolean asc = !Boolean.TRUE.equals(consuntivoIndicatore.getIndicatorePiano().getDecrescente());
		Double target = consuntivoIndicatore.getPreventivoNodo();
		if (target == null) {
			return null;
		}
		Double cons = consuntivoIndicatore.getConsuntivoNodo();
		if (cons == null) {
			return null;
		}
		if (TipoFormula.TIPO_FORMULA_DATA.equals(consuntivoIndicatore.getIndicatore().getTipoFormula())) {
			long diffInMillies = Math.abs(cons.longValue() - target.longValue());
			long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			if (cons < target) {
				diff = -diff;
			}
			target = 0d;
			cons = (double) diff;
		}
		return range(consuntivoIndicatore.getIndicatorePiano().getRanges(), cons, target, asc);
	}

	public static Double range(List<RangeIndicatoreDTO> ranges, double cons, double target, boolean asc) {
		if (ranges == null || ranges.isEmpty()) {
			return perc(0, target, cons, asc, 100d);
		}
		final RangeIndicatoreDTO max = ranges.get(ranges.size() - 1);
		final RangeIndicatoreDTO min = ranges.get(0);
		if (asc && cons >= max.getMassimo()) {
			return max.getValore();
		}
		if (!asc && cons <= min.getMinimo()) {
			return min.getValore();
		}
		for (RangeIndicatoreDTO r : ranges) {
			if (cons >= r.getMinimo() && cons < r.getMassimo()) {
				if (Boolean.TRUE.equals(r.getProporzionale()))
					return perc(r.getMinimo(), r.getMassimo(), cons, asc, r.getValore());
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

	public static Double perc(double min, double max, double cons, boolean asc, double value) {
		double perc;
		double divide = Math.abs(max - min);
		if (min >= max)
			perc = value;
		else if (asc) {
			double meno = cons - min;
			perc = BigDecimal.valueOf(meno).multiply(BigDecimal.valueOf(value))
					.divide(BigDecimal.valueOf(divide), 2, RoundingMode.HALF_DOWN).doubleValue();
		} else {
			double meno = max - cons;
			perc = BigDecimal.valueOf(meno).multiply(BigDecimal.valueOf(value))
					.divide(BigDecimal.valueOf(divide), 2, RoundingMode.HALF_DOWN).doubleValue();

		}
		return Math.min(perc, 100d);

	}

	public static Double raggiungimentoPesato(final ConsuntivoIndicatoreDTO consuntivoIndicatore, Double pesoTotale) {
		if (consuntivoIndicatore.getPeso() == null || consuntivoIndicatore.getRaggiungimentoPerc() == null
				|| pesoTotale == null || pesoTotale <= 0)
			return null;
		BigDecimal d = BigDecimal.valueOf(consuntivoIndicatore.getRaggiungimentoPerc())
				.multiply(BigDecimal.valueOf(pesoRelativo(consuntivoIndicatore.getPeso(), pesoTotale)));
		d.setScale(2, RoundingMode.HALF_DOWN);
		return d.doubleValue();
	}

	public static Double raggiungimentoPesato(final Double raggiungimentoPerc, Double pesoRelativo) {
		if (raggiungimentoPerc == null || pesoRelativo == null)
			return null;
		BigDecimal d = BigDecimal.valueOf(raggiungimentoPerc).multiply(BigDecimal.valueOf(pesoRelativo));
		d.setScale(2, RoundingMode.HALF_DOWN);
		return d.doubleValue();
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

	public static LocalDate finePeriodo(Integer anno, Integer periodo, TipoConsuntivazione tipoConsuntivazione) {
		return finePeriodo(anno, 
				TipoConsuntivazione.TRIMESTRE.equals(tipoConsuntivazione)?
				tipoPianoTrimestrale(periodo):tipoPianoSemestrale(periodo), tipoConsuntivazione);
	}

	public static LocalDate inizioPeriodo(Integer anno, Integer periodo, TipoConsuntivazione tipoConsuntivazione) { 
		return inizioPeriodo(anno, TipoConsuntivazione.TRIMESTRE.equals(tipoConsuntivazione)?
				tipoPianoTrimestrale(periodo):tipoPianoSemestrale(periodo), tipoConsuntivazione);
	}

	public static Periodo tipoPianoTrimestrale(int periodo) {
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
	public static Periodo tipoPianoSemestrale(int periodo) {
		switch (periodo) {
		case 1:
		case 2:
			return Periodo.PERIODO_2;
		case 3:
		case 4:
			return Periodo.PERIODO_4;
		default:
			return null;
		}

	}
	public static Double raggiungimentoRange(final Boolean decrescente, final Double preventivo,
			final Double consuntivo, final TipoFormula tipoFormula, List<RangeIndicatore> ranges) {
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
		return calcolaFromRanges(ranges, cons, target, asc);
	}

	public static Double calcolaFromRanges(List<RangeIndicatore> ranges, double cons, double target, boolean asc) {
		if (ranges == null || ranges.isEmpty()) {
			return perc(0, target, cons, asc, 100d);
		}
		final RangeIndicatore max = ranges.get(ranges.size() - 1);
		final RangeIndicatore min = ranges.get(0);
		if (asc && cons >= max.getMassimo()) {
			return max.getValore();
		}
		if (!asc && cons <= min.getMinimo()) {
			return min.getValore();
		}
		for (RangeIndicatore r : ranges) {
			if (cons >= r.getMinimo() && cons < r.getMassimo()) {
				if (Boolean.TRUE.equals(r.getProporzionale()))
					return perc(r.getMinimo(), r.getMassimo(), cons, asc, r.getValore());
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

	public static Double getValore(Valutazione p, TipoFormula tipoFormula) {
		if (tipoFormula == null)
			return null;
		switch (tipoFormula) {
		case TIPO_FORMULA_BULEANO:
			return getValoreBOOL(p);
		case TIPO_FORMULA_NUMERO:
			return p.getValoreNumerico() == null ? null : p.getValoreNumerico().doubleValue();
		case TIPO_FORMULA_DATA:
			return p.getValoreTemporale() == null ? null : (double) java.sql.Date.valueOf(p.getValoreTemporale()).getTime();
		case TIPO_FORMULA_RAPPORTO:
			if (p.getValoreNumericoA() == null || p.getValoreNumericoB() == null)
				return null;
			if (p.getValoreNumericoB().doubleValue() == 0)
				return null;
			return  
					p.getValoreNumericoA().divide(p.getValoreNumericoB(),IndicatorePianoHelper.getDecimali(p),RoundingMode.HALF_DOWN).doubleValue();
		default:
			return null;
		}

	}

	public static Double getValoreBOOL(Valutazione v) {
		if (v.getValoreBooleano() == null)
			return null;
		return Boolean.TRUE.equals(v.getValoreBooleano()) ? 1d : 0d;
	}

	public static Double getValoreTemporale(Valutazione v) {
		if (v.getValoreTemporale() == null)
			return null;
		return (double) java.sql.Date.valueOf(v.getValoreTemporale()).getTime();
	}

	public static void calcolaValoreNumerico(Valutazione valutazione, TipoFormula tipoFormula,
			CalcoloConsuntivazione calcoloConsuntivazione, int decimali) {
		if (TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula)
				|| (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula)
						&& CalcoloConsuntivazione.RAPPORTO.equals(calcoloConsuntivazione))) {
			if (valutazione.getValoreNumericoA() != null && valutazione.getValoreNumericoB() != null
					&& valutazione.getValoreNumericoB().doubleValue() > 0) {
				valutazione.setValoreNumerico(valutazione.getValoreNumericoA().divide(valutazione.getValoreNumericoB(),
						decimali, RoundingMode.HALF_DOWN));
			} else {
				valutazione.setValoreNumerico(null);
			}
		}
		if (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula)
				&& CalcoloConsuntivazione.DIFFERENZA_CRESCENTE.equals(calcoloConsuntivazione)) {
			if (valutazione.getValoreNumericoA() != null && valutazione.getValoreNumericoB() != null
					&& valutazione.getValoreNumericoB().doubleValue() > 0) {
				valutazione
						.setValoreNumerico((valutazione.getValoreNumericoA().subtract(valutazione.getValoreNumericoB()))
								.divide(valutazione.getValoreNumericoB(), decimali, RoundingMode.HALF_DOWN));
			} else {
				valutazione.setValoreNumerico(null);
			}

		}
		if (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula)
				&& CalcoloConsuntivazione.DIFFERENZA_DECRESCENTE.equals(calcoloConsuntivazione)) {
			if (valutazione.getValoreNumericoA() != null && valutazione.getValoreNumericoB() != null
					&& valutazione.getValoreNumericoA().doubleValue() > 0) {
				valutazione
						.setValoreNumerico((valutazione.getValoreNumericoB().subtract(valutazione.getValoreNumericoA()))
								.divide(valutazione.getValoreNumericoA(), decimali, RoundingMode.HALF_DOWN));
			} else {
				valutazione.setValoreNumerico(null);
			}

		}
		if (TipoFormula.TIPO_FORMULA_RAPPORTO.equals(tipoFormula)) {
			if (valutazione.getValoreNumericoA() != null && valutazione.getValoreNumericoB() != null
					&& valutazione.getValoreNumericoB().doubleValue() > 0) {
				valutazione.setValoreNumerico(valutazione.getValoreNumericoA().divide(valutazione.getValoreNumericoB(),
						decimali, RoundingMode.HALF_DOWN));
			} else {
				valutazione.setValoreNumerico(null);
			}
		}
		if (TipoFormula.TIPO_FORMULA_NUMERO.equals(tipoFormula)
				&& (calcoloConsuntivazione == null || calcoloConsuntivazione.equals(CalcoloConsuntivazione.DEFAULT))) {
			if (valutazione.getValoreNumerico() == null)
				valutazione.setValoreNumerico(valutazione.getValoreNumericoA());
		}
	}

	public static Integer periodo(Periodo p) throws BusinessException {
		if (p == null)
			return null;
		switch (p) {
		case PERIODO_1:
			return 1;
		case PERIODO_2:
			return 2;
		case PERIODO_3:
			return 3;
		case PERIODO_4:
			return 4;
		}
		return null;
	}

	public static String descPeriodoTrimestre(Integer p) {
		if (p == null)
			return "";
		switch (p) {
		case 1:
			return "Primo Trimestre";
		case 2:
			return "Secondo Trimestre";
		case 3:
			return "Terzo Trimestre";
		case 4:
			return "Quarto Trimestre";
		default:
			return "";
		}
	}
	public static String descPeriodoSemestre(Integer p) {
		if (p == null)
			return "";
		switch (p) {
		case 1:
		case 2:
			return "Primo Semestre";
		case 3:
		case 4:
			return "Secondo Semestre";
		default:
			return "";
		}
	}
}