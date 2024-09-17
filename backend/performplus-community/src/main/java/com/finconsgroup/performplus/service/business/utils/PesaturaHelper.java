package com.finconsgroup.performplus.service.business.utils;

import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.PesaturaNodoVM;

public class PesaturaHelper {
	public static void calcolaPeso(final PesaturaNodoVM p, final TipoNodo tipoNodo) {
		double peso = (p.getPesoLivelloComplessita() == null ? 0d : p.getPesoLivelloComplessita())
				+ (p.getPesoLivelloStrategicita() == null ? 0d : p.getPesoLivelloStrategicita());
		if (!TipoNodo.OBIETTIVO.equals(tipoNodo)) {
			p.setPeso(peso);
			return;
		}
		double mult;
		if (p.getTipologia() == null) {
			p.setPeso(0d);
			return;
		}
		switch (p.getTipologia()) {
		case MANTENIMENTO:
			mult = 0.5d;
			break;
		case SVILUPPO:
			mult = 0.75d;
			break;
		case IMPATTO:
			mult = 1d;
			break;
		default:
			mult = 0d;
		}
		peso = peso * mult;
		p.setPeso(peso);
	}

	public static void calcolaPesoComplessita(final PesaturaNodoVM p, final TipoNodo tipoNodo) {
		if (p.getLivelloComplessita() == null) {
			p.setPesoLivelloComplessita(0d);
			return;
		}
		switch (p.getLivelloComplessita()) {
		case BASSA:
			p.setPesoLivelloComplessita(20d);
			break;
		case MEDIA:
			p.setPesoLivelloComplessita(30d);
			break;
		case ALTA:
			p.setPesoLivelloComplessita(40d);
			break;
		default:
			p.setPesoLivelloComplessita(0d);
		}
	}

	public static void calcolaPesoStrategico(final PesaturaNodoVM p, final TipoNodo tipoNodo) {
		if (p.getLivelloStrategicita() == null) {
			p.setPesoLivelloStrategicita(0d);
			return;
		}
		switch (p.getLivelloStrategicita()) {
		case BASSA:
			p.setPesoLivelloStrategicita(30d);
			break;
		case MEDIA:
			p.setPesoLivelloStrategicita(50d);
			break;
		case ALTA:
			p.setPesoLivelloStrategicita(60d);
			break;
		default:
			p.setPesoLivelloStrategicita(0d);
		}
	}
	public static void calcolaPesoComplessivo(final PesaturaNodoVM p, final TipoNodo tipoNodo) {
		PesaturaHelper.calcolaPesoStrategico(p,tipoNodo);
		PesaturaHelper.calcolaPesoComplessita(p,tipoNodo);
		PesaturaHelper.calcolaPeso(p,tipoNodo);
	}

}