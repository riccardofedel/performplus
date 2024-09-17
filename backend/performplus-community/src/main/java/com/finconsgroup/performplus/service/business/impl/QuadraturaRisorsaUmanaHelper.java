package com.finconsgroup.performplus.service.business.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finconsgroup.performplus.enumeration.Over;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;
import com.finconsgroup.performplus.service.business.utils.OrganizzazioneHelper;
import com.finconsgroup.performplus.service.dto.OrganizzazioneDTO;
import com.finconsgroup.performplus.service.dto.QuadraturaRisorsaUmana;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaDTO;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaOrganizzazioneDTO;

public class QuadraturaRisorsaUmanaHelper {
	public List<QuadraturaRisorsaUmana> elaboro(List<RisorsaUmanaDTO> tutteLeRisorse,
			List<RisorsaUmanaOrganizzazioneDTO> risorseUO, OrganizzazioneDTO organizzazione, int oreSettimanaliMax)
			throws Exception {

		/*
		 * estraggo le risorse e per ogni figlio verifico se utilizzato nel ramo e se sì
		 * lo aggiungo
		 */
		List<QuadraturaRisorsaUmana> output = new ArrayList<>();
		List<Long> ris = new ArrayList<Long>();
		List<String> doppie = new ArrayList<>();
		boolean aggiungiOrg = false;
		if (tutteLeRisorse != null) {
			for (RisorsaUmanaDTO risorsa : tutteLeRisorse) {
				if (!doppie.contains(risorsa.getId().toString())) {
					doppie.add(risorsa.getId().toString());
					ris.add(risorsa.getId());
				}
			}
			if (risorseUO != null) {
				for (RisorsaUmanaOrganizzazioneDTO risorsa : risorseUO) {
					if (risorsa.getOrganizzazione().getId().equals(organizzazione.getId()))
						aggiungiOrg = true;
				}
			}
		} else if (risorseUO != null) {
			for (RisorsaUmanaOrganizzazioneDTO risorsa : risorseUO) {
				if (!doppie.contains(risorsa.getRisorsaUmana().getId().toString())) {
					doppie.add(risorsa.getRisorsaUmana().getId().toString());
					ris.add(risorsa.getRisorsaUmana().getId());
					if (risorsa.getOrganizzazione().getId().equals(organizzazione.getId()))
						aggiungiOrg = true;
				}
			}
		}
		List<OrganizzazioneDTO> figli = organizzazione.getFigli();
		List<OrganizzazioneDTO> elementi = figli;
		if (aggiungiOrg) {
			elementi = new ArrayList<>();
			elementi.add(organizzazione);
			if (figli != null)
				for (OrganizzazioneDTO o : figli) {
					elementi.add(o);
				}
		}
		if (elementi == null)
			elementi = new ArrayList<>();

		Map<String, QuadraturaRisorsaUmana> trovato = new HashMap<>();
		QuadraturaRisorsaUmana elem = null;
		if (tutteLeRisorse != null) {
			for (RisorsaUmanaDTO risorsa : tutteLeRisorse) {
				elem = getElem(trovato, risorsa, oreSettimanaliMax, output);

			}
		}

		if (risorseUO != null) {
			for (RisorsaUmanaOrganizzazioneDTO risorsaUO : risorseUO) {
				elem = getElem(trovato, risorsaUO, oreSettimanaliMax, output);
				elem.setDisponibilitaTotale(elem.getDisponibilitaTotale()
						+ (risorsaUO.getDisponibilita() == null ? 0 : risorsaUO.getDisponibilita()));

				elem.getOrgs().add(risorsaUO.getOrganizzazione().getIntestazione());
				elem.setUtilizzato(true);
				QuadraturaRisorsaUmana q = null;
				for (OrganizzazioneDTO figlio : elementi) {
					if (!contiene(figlio, risorsaUO))
						continue;
					q = getElem(figlio, trovato, risorsaUO, oreSettimanaliMax, output);
					q.setDisponibilitaTotale(q.getDisponibilitaTotale()
							+ (risorsaUO.getDisponibilita() == null ? 0 : risorsaUO.getDisponibilita()));
					if (!q.getIdOrganizzazione().equals(risorsaUO.getOrganizzazione().getId())) {
						q.getOrgs().add(risorsaUO.getOrganizzazione().getIntestazione());
					}

				}

			}
		}

		/*
		 * controllo sovrapposizioni
		 */
		output.sort(Comparator.naturalOrder());

		for (QuadraturaRisorsaUmana q : output) {
			int delta = ModelHelper.delta(q.getDisponibilitaTotale(), 100d);
			if (delta > 0) {
				q.setOver(Over.OVER);
				q.setAvvertimento("Utilizzato anche da:" + q.orgs2String());
			} else if (!q.isUtilizzato() && q.getIdOrganizzazione() == null) {
				q.setOver(Over.NESS);
				q.setAvvertimento("Non utilizzato");
			} else if (q.getDisponibilitaTotale() <= 0) {
				q.setOver(Over.NO_DISP);
				q.setAvvertimento("Non disponibile");
			} else if (delta < 0) {
				if (q.isOrganizzazione()) {
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
		for (QuadraturaRisorsaUmana q : output) {
			if (q.getIdOrganizzazione() != null) {
				q.setEsterna(null);
				q.setDisponibile(null);
				q.setDescRisorsaUmana(null);
				q.setParttime(null);
				q.setMesi(null);
				q.setOreParttime(null);
			} else {
				gruppo++;
			}
			q.setGruppo(gruppo);
		}

		return output;
	}

	public ArrayList<QuadraturaRisorsaUmana> elaboroStampe(List<RisorsaUmanaDTO> tutteLeRisorse,
			List<RisorsaUmanaOrganizzazioneDTO> risorseUO, OrganizzazioneDTO organizzazione, int oreSettimanaliMax)
			throws Exception {

		/*
		 * estraggo le risorse e per ogni figlio verifico se utilizzato nel ramo e se sì
		 * lo aggiungo
		 */
		ArrayList<QuadraturaRisorsaUmana> output = new ArrayList<>();
		List<Long> ris = new ArrayList<>();
		List<String> doppie = new ArrayList<>();
		if (tutteLeRisorse != null) {
			for (RisorsaUmanaDTO risorsa : tutteLeRisorse) {
				if (!doppie.contains(risorsa.getId().toString())) {
					doppie.add(risorsa.getId().toString());
					ris.add(risorsa.getId());
				}
			}
		}
		List<OrganizzazioneDTO> elementi = OrganizzazioneHelper.contiene(organizzazione);
		if (elementi != null && !elementi.isEmpty()) {
			elementi.remove(0);
		}
		if (elementi == null)
			elementi = new ArrayList<>();

		Map<String, QuadraturaRisorsaUmana> trovato = new HashMap<>();
		QuadraturaRisorsaUmana elem = null;
		if (tutteLeRisorse != null) {
			for (RisorsaUmanaDTO risorsa : tutteLeRisorse) {
				elem = getElem(trovato, risorsa, oreSettimanaliMax, output);

			}
		}

		if (risorseUO != null) {
			for (RisorsaUmanaOrganizzazioneDTO risorsaUO : risorseUO) {
				elem = getElem(trovato, risorsaUO, oreSettimanaliMax, output);
				elem.setDisponibilitaTotale(elem.getDisponibilitaTotale()
						+ (risorsaUO.getDisponibilita() == null ? 0 : risorsaUO.getDisponibilita()));

				elem.getOrgs().add(risorsaUO.getOrganizzazione().getIntestazione());
				elem.setUtilizzato(true);
				QuadraturaRisorsaUmana q = null;
				for (OrganizzazioneDTO figlio : elementi) {
					if (!contiene(figlio, risorsaUO))
						continue;
					q = getElem(figlio, trovato, risorsaUO, oreSettimanaliMax, output);
					q.setDisponibilitaTotale(q.getDisponibilitaTotale()
							+ (risorsaUO.getDisponibilita() == null ? 0 : risorsaUO.getDisponibilita()));
					if (!q.getIdOrganizzazione().equals(risorsaUO.getOrganizzazione().getId())) {
						q.getOrgs().add(risorsaUO.getOrganizzazione().getIntestazione());
					}

				}

			}
		}

		/*
		 * controllo sovrapposizioni
		 */
		output.sort(Comparator.naturalOrder());

		for (QuadraturaRisorsaUmana q : output) {
			int delta = ModelHelper.delta(q.getDisponibilitaTotale(), 100d);
			if (delta > 0) {
				q.setOver(Over.OVER);
				q.setAvvertimento("Utilizzato anche da:" + q.orgs2String());
			} else if (!q.isUtilizzato() && q.getIdOrganizzazione() == null) {
				q.setOver(Over.NESS);
				q.setAvvertimento("Non utilizzato");
			} else if (q.getDisponibilitaTotale() <= 0) {
				q.setOver(Over.NO_DISP);
				q.setAvvertimento("Non disponibile");
			} else if (delta < 0) {
				if (q.isOrganizzazione()) {
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

		return output;
	}

	private QuadraturaRisorsaUmana getElem(OrganizzazioneDTO figlio, Map<String, QuadraturaRisorsaUmana> trovato,
			RisorsaUmanaOrganizzazioneDTO risorsa, int oreSettimanaliMax, List<QuadraturaRisorsaUmana> output) {
		String key = risorsa.getRisorsaUmana().getId() + "/" + figlio.getId();
		QuadraturaRisorsaUmana q = trovato.get(key);
		if (q == null) {
			q = new QuadraturaRisorsaUmana(figlio, risorsa, oreSettimanaliMax);
			output.add(q);
			trovato.put(key, q);
			// System.out.println(q.getDescRisorsaUmana() + "/"
			// + q.getDescOrganizzazione());
		}
		return q;
	}

	private QuadraturaRisorsaUmana getElem(Map<String, QuadraturaRisorsaUmana> trovato,
			RisorsaUmanaOrganizzazioneDTO risorsa, int oreSettimanaliMax, List<QuadraturaRisorsaUmana> output) {
		String key = risorsa.getRisorsaUmana().getId().toString();
		QuadraturaRisorsaUmana elem = trovato.get(key);
		if (elem == null) {
			elem = new QuadraturaRisorsaUmana(risorsa.getRisorsaUmana(), oreSettimanaliMax);
			output.add(elem);
			trovato.put(key, elem);
			// System.out.println(elem.getDescRisorsaUmana() + "/"
			// + elem.getDescOrganizzazione());
		}
		return elem;
	}

	private QuadraturaRisorsaUmana getElem(Map<String, QuadraturaRisorsaUmana> trovato, RisorsaUmanaDTO risorsa,
			int oreSettimanaliMax, List<QuadraturaRisorsaUmana> output) {
		String key = risorsa.getId().toString();
		QuadraturaRisorsaUmana elem = trovato.get(key);
		if (elem == null) {
			elem = new QuadraturaRisorsaUmana(risorsa, oreSettimanaliMax);
			output.add(elem);
			trovato.put(key, elem);
			// System.out.println(elem.getDescRisorsaUmana() + "/"
			// + elem.getDescOrganizzazione());
		}
		return elem;
	}

	public static boolean contiene(OrganizzazioneDTO figlio, RisorsaUmanaOrganizzazioneDTO r) {
		/*
		 * uno dei suoi figli è l'uo della risorsa
		 */
		if (OrganizzazioneHelper.contiene(figlio, r.getOrganizzazione()))
			return true;

		return false;
	}

	private int gruppo(List<QuadraturaRisorsaUmana> output) {
		int n = output.size() - 1;
		for (int i = n; i >= 0; i--) {
			if (output.get(i).getGruppo() > 0)
				return output.get(i).getGruppo();
		}
		return 1;
	}
}
