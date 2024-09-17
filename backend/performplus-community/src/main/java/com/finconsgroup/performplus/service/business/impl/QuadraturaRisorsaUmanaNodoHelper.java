package com.finconsgroup.performplus.service.business.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaNodoPiano;
import com.finconsgroup.performplus.domain.RisorsaUmanaOrganizzazione;
import com.finconsgroup.performplus.enumeration.Over;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaNodoPianoRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaOrganizzazioneRepository;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;
import com.finconsgroup.performplus.service.dto.QuadraturaRisorseProg;

public class QuadraturaRisorsaUmanaNodoHelper {

	public static List<QuadraturaRisorseProg> elabora(NodoPiano np, NodoPianoRepository nodoPianoRepository,
			RisorsaUmanaNodoPianoRepository risorsaUmanaNodoPianoRepository, 
			RisorsaUmanaOrganizzazioneRepository risorsaUmanaOrganizzazioneRepository,
			List<Long> filtroNodi,
			int oreSettimanaliMax) {
		List<QuadraturaRisorseProg> output = new ArrayList<>();

		List<Long> doppieRisorse = new ArrayList<>();
		List<RisorsaUmana> risorse = new ArrayList<>();
		Map<Long, List<NodoPiano>> map = new HashMap<>();
		Map<Long, List<String>> orgs = new HashMap<>();
		Map<String, Double> disp = new HashMap<>();
		List<RisorsaUmanaNodoPiano> itemsRunp = risorsaUmanaNodoPianoRepository
				.findByNodoPianoCodiceCompletoStartsWithAndNodoPianoDateDeleteIsNull(np.getCodiceCompleto());
		if (itemsRunp != null) {
			for (RisorsaUmanaNodoPiano runp : itemsRunp) {
				RisorsaUmana ru = runp.getRisorsaUmana();
				Double d = runp.getDisponibilita() == null ? 0d : runp.getDisponibilita().doubleValue();
				NodoPiano f = runp.getNodoPiano();
				if (runp.getNodoPiano().getId().equals(np.getId())) {
					if (!doppieRisorse.contains(ru.getId())) {
						doppieRisorse.add(ru.getId());
						risorse.add(ru);
					}
				}

				List<NodoPiano> m = map.get(ru.getId());
				if (m == null) {
					m=new ArrayList<>();
					map.put(ru.getId(), m);
				}
				if (!m.contains(f))
					m.add(f);

				Double md = disp.get(ru.getId() + "/" + f.getId());
				if (md == null) {
					disp.put(ru.getId() + "/" + f.getId(), d);
				}

			}
		}
		for (RisorsaUmana r : risorse) {
			List<String> mo=new ArrayList<>();
			List<RisorsaUmanaOrganizzazione> list = risorsaUmanaOrganizzazioneRepository.findByRisorsaUmanaId(r.getId());
			for (RisorsaUmanaOrganizzazione ro : list) {
				String s = ro.getOrganizzazione().getIntestazione();
				mo.add(s);
			}

			orgs.put(r.getId(),mo);

		}

		Map<String, QuadraturaRisorseProg> trovato = new HashMap<>();
		QuadraturaRisorseProg elem;
		for (RisorsaUmana r : risorse) {
			elem = getElem(trovato, r, oreSettimanaliMax, output);
			String ks = r.getId() + "/";
			Double tot = 0d;
			for (String key : disp.keySet()) {
				if (key.startsWith(ks)) {
					Double d = disp.get(key);
					if (d != null)
						tot += d;
				}
			}
			elem.setDisponibilitaTotale(tot);
			elem.setOrgs(orgs.get(r.getId()));
			List<NodoPiano> figli = map.get(r.getId());
			for (NodoPiano f : figli) {
				getElem(f, trovato, r, oreSettimanaliMax, output);
			}
		}

		/*
		 * controllo sovrapposizioni
		 */
		output.sort(Comparator.naturalOrder());

		for (QuadraturaRisorseProg q : output) {
			int delta = ModelHelper.delta(q.getDisponibilitaTotale(), 100d);
			if (delta > 0) {
				q.setOver(Over.OVER);
				q.setAvvertimento("Utilizzato anche da:" + q.orgs2String());
			} else if (q.getDisponibilitaTotale() <= 0) {
				q.setOver(Over.NO_DISP);
				q.setAvvertimento("Non disponibile");
			} else if (delta < 0) {
				if (q.isNodoPiano()) {
					q.setOver(Over.UTILIZZO_PARZIALE);
					q.setAvvertimento("Utilizzo parziale");
				} else {
					q.setOver(Over.SOTTO_UTILIZZATO);
					q.setAvvertimento("Sotto utilizzato");
				}
			} else {
				q.setOver(Over.OK);
			}
		}
		/*
		 * pulisci
		 */
		int gruppo = 0;
		for (QuadraturaRisorseProg q : output) {
			if (q.getIdNodoPiano() != null) {
				q.setEsterna(null);
				q.setDisponibile(null);
				q.setDescRisorsaUmana(null);
				q.setParttime(null);
				q.setMesi(null);
				q.setOreParttime(null);
				q.setOrganizzazioni(null);
			} else {
				gruppo++;
			}
			q.setGruppo(gruppo);
		}
		return output;

	}

	private static QuadraturaRisorseProg getElem(NodoPiano f, Map<String, QuadraturaRisorseProg> trovato,
			RisorsaUmana risorsa, int oreSettimanaliMax, List<QuadraturaRisorseProg> output) {
		String key = risorsa.getId() + "/" + f.getId();
		QuadraturaRisorseProg q = trovato.get(key);
		if (q == null) {
			q = new QuadraturaRisorseProg(f, risorsa, oreSettimanaliMax);
			output.add(q);
			trovato.put(key, q);
		}
		return q;
	}

	private static QuadraturaRisorseProg getElem(Map<String, QuadraturaRisorseProg> trovato, RisorsaUmana risorsa,
			int oreSettimanaliMax, List<QuadraturaRisorseProg> output) {
		String key = risorsa.getId().toString();
		QuadraturaRisorseProg elem = trovato.get(key);
		if (elem == null) {
			elem = new QuadraturaRisorseProg(risorsa, oreSettimanaliMax);
			output.add(elem);
			trovato.put(key, elem);
		}
		return elem;
	}

}
