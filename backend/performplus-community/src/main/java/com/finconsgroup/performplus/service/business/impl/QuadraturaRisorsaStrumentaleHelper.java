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
import com.finconsgroup.performplus.service.dto.QuadraturaRisorsaStrumentale;
import com.finconsgroup.performplus.service.dto.RisorsaStrumentaleOrganizzazioneDTO;

public class QuadraturaRisorsaStrumentaleHelper {
	public List<QuadraturaRisorsaStrumentale> elaboro(List<RisorsaStrumentaleOrganizzazioneDTO> risorse,
			OrganizzazioneDTO organizzazione) throws Exception {

		/*
		 * estraggo le risorse e per ogni figlio verifico se utilizzato nel ramo e se sì
		 * lo aggiungo
		 */
		List<QuadraturaRisorsaStrumentale> output = new ArrayList<>();
		List<Long> ris = new ArrayList<>();
		List<String> doppie = new ArrayList<>();
		boolean aggiungiOrg = false;
		if (risorse != null) {
			for (RisorsaStrumentaleOrganizzazioneDTO risorsa : risorse) {
				if (!doppie.contains(risorsa.getRisorsaStrumentale().getId().toString())) {
					doppie.add(risorsa.getRisorsaStrumentale().getId().toString());
					ris.add(risorsa.getRisorsaStrumentale().getId());
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

		Map<String, QuadraturaRisorsaStrumentale> trovato = new HashMap<>();
		QuadraturaRisorsaStrumentale elem = null;
		if (risorse != null) {
			for (RisorsaStrumentaleOrganizzazioneDTO risorsa : risorse) {
				elem = getElem(trovato, risorsa, output);
				elem.setDisponibilitaTotale(elem.getDisponibilitaTotale()
						+ (risorsa.getDisponibilita() == null ? 0 : risorsa.getDisponibilita()));

				elem.getOrgs().add(risorsa.getOrganizzazione().getIntestazione());

				QuadraturaRisorsaStrumentale q = null;
				for (OrganizzazioneDTO figlio : elementi) {
					if (!contiene(figlio, risorsa))
						continue;
					q = getElem(figlio, trovato, risorsa, output);
					q.setDisponibilitaTotale(q.getDisponibilitaTotale()
							+ (risorsa.getDisponibilita() == null ? 0 : risorsa.getDisponibilita()));
					if (!q.getIdOrganizzazione().equals(risorsa.getOrganizzazione().getId())) {
						q.getOrgs().add(risorsa.getOrganizzazione().getIntestazione());
					}

				}

			}
		}

		/*
		 * controllo sovrapposizioni
		 */
		output.sort(Comparator.naturalOrder());

		for (QuadraturaRisorsaStrumentale q : output) {
			int delta = ModelHelper.delta(q.getDisponibilitaTotale(), 100d);
			if (delta > 0) {
				q.setOver(Over.OVER);
				q.setAvvertimento("Utilizzato anche da:" + q.orgs2String());
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
		for (QuadraturaRisorsaStrumentale q : output) {
			if (q.getIdOrganizzazione() != null) {
				q.setDescConservazione(null);
				q.setDisponibile(null);
				q.setDescRisorsaStrumentale(null);
				q.setDescTipologia(null);
				q.setQuantita(null);
				q.setValore(null);
				q.setValoreTotale(null);
			} else {
				gruppo++;
			}
			q.setGruppo(gruppo);
		}

		return output;
	}

	private QuadraturaRisorsaStrumentale getElem(OrganizzazioneDTO figlio,
			Map<String, QuadraturaRisorsaStrumentale> trovato, RisorsaStrumentaleOrganizzazioneDTO risorsa,
			List<QuadraturaRisorsaStrumentale> output) {
		String key = risorsa.getRisorsaStrumentale().getId() + "/" + figlio.getId();
		QuadraturaRisorsaStrumentale q = trovato.get(key);
		if (q == null) {
			q = new QuadraturaRisorsaStrumentale(figlio, risorsa);
			q.setGruppo(gruppo(output) + 1);
			output.add(q);
			trovato.put(key, q);
			// System.out.println(q.getDescRisorsaStrumentale() + "/"
			// + q.getDescOrganizzazione());
		}
		return q;
	}

	private QuadraturaRisorsaStrumentale getElem(Map<String, QuadraturaRisorsaStrumentale> trovato,
			RisorsaStrumentaleOrganizzazioneDTO risorsa, List<QuadraturaRisorsaStrumentale> output) {
		String key = risorsa.getRisorsaStrumentale().getId().toString();
		QuadraturaRisorsaStrumentale elem = trovato.get(key);
		if (elem == null) {
			elem = new QuadraturaRisorsaStrumentale(risorsa.getRisorsaStrumentale());
			elem.setGruppo(gruppo(output));
			output.add(elem);
			trovato.put(key, elem);
			// System.out.println(elem.getDescRisorsaStrumentale() + "/"
			// + elem.getDescOrganizzazione());
		}
		return elem;
	}

	public static boolean contiene(OrganizzazioneDTO figlio, RisorsaStrumentaleOrganizzazioneDTO r) {
		/*
		 * uno dei suoi figli è l'uo della risorsa
		 */
		return OrganizzazioneHelper.contiene(figlio, r.getOrganizzazione());
	}

	private int gruppo(List<QuadraturaRisorsaStrumentale> output) {
		int n = output.size() - 1;
		for (int i = n; i >= 0; i--) {
			if (output.get(i).getGruppo() > 0) {
				return output.get(i).getGruppo();
			}
		}
		return 1;
	}

	public ArrayList<QuadraturaRisorsaStrumentale> elaboroStampe(List<RisorsaStrumentaleOrganizzazioneDTO> risorse,
			OrganizzazioneDTO organizzazione) {
		/*
		 * estraggo le risorse e per ogni figlio verifico se utilizzato nel ramo e se sì
		 * lo aggiungo
		 */
		ArrayList<QuadraturaRisorsaStrumentale> output = new ArrayList<>();
		List<Long> ris = new ArrayList<>();
		List<String> doppie = new ArrayList<>();
		if (risorse != null) {
			for (RisorsaStrumentaleOrganizzazioneDTO risorsa : risorse) {
				if (!doppie.contains(risorsa.getRisorsaStrumentale().getId().toString())) {
					doppie.add(risorsa.getRisorsaStrumentale().getId().toString());
					ris.add(risorsa.getRisorsaStrumentale().getId());

				}
			}
		}
		List<OrganizzazioneDTO> elementi = OrganizzazioneHelper.contiene(organizzazione);
		if (elementi != null && !elementi.isEmpty()) {
			elementi.remove(0);
		}
		if (elementi == null)
			elementi = new ArrayList<>();

		Map<String, QuadraturaRisorsaStrumentale> trovato = new HashMap<>();
		QuadraturaRisorsaStrumentale elem = null;
		if (risorse != null) {
			for (RisorsaStrumentaleOrganizzazioneDTO risorsa : risorse) {
				elem = getElem(trovato, risorsa, output);
				elem.setDisponibilitaTotale(elem.getDisponibilitaTotale()
						+ (risorsa.getDisponibilita() == null ? 0 : risorsa.getDisponibilita()));

				elem.getOrgs().add(risorsa.getOrganizzazione().getIntestazione());

				QuadraturaRisorsaStrumentale q = null;
				for (OrganizzazioneDTO figlio : elementi) {
					if (!contiene(figlio, risorsa))
						continue;
					q = getElem(figlio, trovato, risorsa, output);
					q.setDisponibilitaTotale(q.getDisponibilitaTotale()
							+ (risorsa.getDisponibilita() == null ? 0 : risorsa.getDisponibilita()));
					if (!q.getIdOrganizzazione().equals(risorsa.getOrganizzazione().getId())) {
						q.getOrgs().add(risorsa.getOrganizzazione().getIntestazione());
					}

				}

			}
		}

		/*
		 * controllo sovrapposizioni
		 */
		output.sort(Comparator.naturalOrder());

		for (QuadraturaRisorsaStrumentale q : output) {
			int delta = ModelHelper.delta(q.getDisponibilitaTotale(), 100d);
			if (delta > 0) {
				q.setOver(Over.OVER);
				q.setAvvertimento("Utilizzato anche da:" + q.orgs2String());
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
}
