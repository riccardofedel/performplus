package com.finconsgroup.performplus.service.business.utils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PreventivoHelper {
//	public static void preventiviRA(final IndicatorePiano ip, final int n, final LocalDate dataDa, final LocalDate dataA, ValutazioneRepository valutazioneRepository, TipoConsuntivazione tipoConsuntivazione) throws BusinessException{
//		final Map<String, List<Integer>> map = PreventivoHelper.preventiviProgressivi(n, dataDa, dataA);
//		map.forEach((a, b) -> {
//			int year = b.get(0);
//			int p = b.get(1);
//			Integer somma = b.get(2);
//			if (n == 4 || (n == 2 && (p == 2 || p == 4))) {
//				Valutazione v = new Valutazione();
//				v.setAnno(year);
//				v.setPeriodo(p);
//				v.setTipoValutazione(TipoValutazione.PREVENTIVO);
//				v.setValoreNumerico(BigDecimal.valueOf(somma).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
//				v.setIndicatorePiano(ip);
//				v.setInizio(ConsuntivoHelper.inizioPeriodo(v.getAnno(), v.getPeriodo(), tipoConsuntivazione));
//				v.setScadenza(ConsuntivoHelper.finePeriodo(v.getAnno(), v.getPeriodo(), tipoConsuntivazione));
//				v.setDataRilevazione(v.getScadenza());
//				valutazioneRepository.save(v);
//			}
//		});
//
//	}

	public static Map<String, List<Integer>> preventiviProgressivi(int n, LocalDate dataDa, LocalDate dataA) {
		final Map<String, List<Integer>> map = new TreeMap<>();
		int annoA = dataA.getYear();

		int meseA = dataA.getMonthValue();
//		int m1 = meseA % 3;

		LocalDate fine = LocalDate.of(annoA, 12, 31);
		LocalDate date = dataDa.plusMonths(0);
		int meseDa = date.getMonthValue();
		if (meseDa < 3)
			meseDa = 3;
		else {
			int m1 = meseDa % 3;

			if (m1 > 0) {
				date = date.minusMonths(m1);
			}
		}
		date = date.withDayOfMonth(date.lengthOfMonth());

		int conta = conta(date, fine);
		double delta = conta == 0 ? 100d : 100d / (double) conta;

		int somma = 0;
		while (!date.isAfter(fine)) {
			somma = add(somma, delta);
			int year = date.getYear();
			int p = periodo(date);
			if (somma > 100 || (year == annoA && p == 4)) {
				somma = 100;
			}
			map.put(year + "/" + p, List.of(year, p, somma));

			date = date.plusMonths(3);
		}
		return map;

	}

	private static int periodo(LocalDate date) {
		int p = date.getMonthValue();
		int m1 = p % 3;
		p = p / 3;
		if (m1 > 0)
			p = p + 1;
		return p;
	}

	private static int add(int somma, double delta) {
		double sum = somma + delta;
		int d = (int) Math.round(sum);
		int m1 = d % 5;
		if (m1 > 2)
			d = d - m1 + 5;
		else
			d = d - m1;
		return d;
	}

	private static int conta(LocalDate date, LocalDate fine) {
		int conta = 0;
		while (!date.isAfter(fine)) {
			conta += 1;
			date = date.plusMonths(3);
		}
		return conta;
	}
}
