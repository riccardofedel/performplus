package com.finconsgroup.performplus.service.business.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.enumeration.CalcoloConsuntivazione;
import com.finconsgroup.performplus.enumeration.StatoIndicatore;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoValutazione;
import com.finconsgroup.performplus.repository.IndicatorePianoRepository;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.ValutazioneRepository;
import com.finconsgroup.performplus.service.business.utils.ConsuntivoHelper;
import com.finconsgroup.performplus.service.business.utils.DateHelper;
import com.finconsgroup.performplus.service.business.utils.IndicatorePianoHelper;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;
import com.finconsgroup.performplus.service.business.utils.OrganizzazioneHelper;
import com.finconsgroup.performplus.service.business.utils.PreventivoHelper;
import com.finconsgroup.performplus.service.business.utils.RisorsaUmanaHelper;
import com.finconsgroup.performplus.service.dto.IndicatorePianoDTO;
import com.finconsgroup.performplus.service.dto.NodoPianoDTO;
import com.finconsgroup.performplus.service.dto.QuadraturaIndicatoriProg;

public class QuadraturaIndicatoriHelper {

	public static List<QuadraturaIndicatoriProg> elabora(NodoPianoDTO nodo,
			IndicatorePianoRepository indicatorePianoRepository, ValutazioneRepository valutazioneRepository,
			List<Long> filtroNodi) {
		List<QuadraturaIndicatoriProg> output = new ArrayList<>();

		if (nodo.getTipoNodo() != TipoNodo.PIANO) {
			QuadraturaIndicatoriProg q = aggiungi(nodo, output, indicatorePianoRepository, valutazioneRepository);
			q.setPrefix(nodo.getTipoNodo().name());
		}
		List<NodoPianoDTO> list = nodo.getFigli();
		if (list != null) {
			for (NodoPianoDTO figlio : list) {
				if (NodoPianoHelper.contenuto(figlio, filtroNodi))
					aggiungi(figlio, output, indicatorePianoRepository, valutazioneRepository);
			}
		}

		int gruppo = 0;
		for (QuadraturaIndicatoriProg q : output) {
			if (q.getIdIndicatorePiano() == null) {
				gruppo++;
			}
			q.setGruppo(gruppo);
		}
		return output;
	}

	public static ArrayList<QuadraturaIndicatoriProg> elaboraStampa(NodoPianoDTO nodo,
			IndicatorePianoRepository indicatorePianoManager, ValutazioneRepository valutazioneRepository) {
		ArrayList<QuadraturaIndicatoriProg> output = new ArrayList<>();

		if (nodo.getTipoNodo() != TipoNodo.PIANO) {
			QuadraturaIndicatoriProg q = aggiungi(nodo, output, indicatorePianoManager, valutazioneRepository);
			q.setPrefix(nodo.getTipoNodo().name());
		}

		if (output != null && !output.isEmpty())
			output.remove(0);
		return output;
	}

	private static QuadraturaIndicatoriProg aggiungi(NodoPianoDTO nodo, List<QuadraturaIndicatoriProg> output,
			IndicatorePianoRepository indicatorePianoManager, ValutazioneRepository valutazioneRepository) {
		QuadraturaIndicatoriProg q = new QuadraturaIndicatoriProg(nodo);
		output.add(q);
		List<IndicatorePianoDTO> risorse = Mapping.mapping(indicatorePianoManager.findByNodoPianoId(nodo.getId()),
				IndicatorePianoDTO.class);
		if (risorse != null) {
			for (IndicatorePianoDTO ind : risorse) {
				QuadraturaIndicatoriProg r = new QuadraturaIndicatoriProg(ind);
				if (q.getStato() == null || StatoIndicatore.OK.equals(q.getStato())) {
					if (r.getStato() != null && !StatoIndicatore.OK.equals(r.getStato())) {
						q.setStato(r.getStato());
					}
				}
				output.add(r);
			}
		}
		if (q.getStato() == null || StatoIndicatore.OK.equals(q.getStato())) {
			if (risorse == null || risorse.isEmpty())
				q.setStato(StatoIndicatore.MANCANO_INDICATORI);
			else
				q.setStato(StatoIndicatore.OK);
		}
		return q;
	}

	public static List<QuadraturaIndicatoriProg> elabora(NodoPiano nodo, NodoPianoRepository nodoPianoRepository,
			IndicatorePianoRepository indicatorePianoRepository, ValutazioneRepository valutazioneRepository,
			List<Long> filtroNodi) {
		List<QuadraturaIndicatoriProg> output = new ArrayList<>();

		if (nodo.getTipoNodo() != TipoNodo.PIANO) {
			QuadraturaIndicatoriProg q = aggiungi(nodo, output, indicatorePianoRepository, valutazioneRepository);
			q.setPrefix(nodo.getTipoNodo().name());
		}
//		List<NodoPiano> list = nodoPianoRepository.findByPadreIdAndDateDeleteIsNullOrderByCodice(nodo.getId());
//		if (list != null) {
//			for (NodoPiano figlio : list) {
//				if (NodoPianoHelper.contenuto(figlio, filtroNodi))
//					aggiungi(figlio, output, indicatorePianoRepository, valutazioneRepository);
//			}
//		}

		int gruppo = 0;
		for (QuadraturaIndicatoriProg q : output) {
			if (q.getIdIndicatorePiano() == null) {
				gruppo++;
			}
			q.setGruppo(gruppo);
		}
		return output;
	}

	private static QuadraturaIndicatoriProg aggiungi(NodoPiano nodo, List<QuadraturaIndicatoriProg> output,
			IndicatorePianoRepository indicatorePianoRepository, ValutazioneRepository valutazioneRepository) {
		QuadraturaIndicatoriProg q = newQuadraturaIndicatoriProg(nodo);
		output.add(q);
		List<IndicatorePiano> risorse = indicatorePianoRepository.findByNodoPianoIdOrderBySpecificaAscIndicatoreDenominazioneAsc(nodo.getId());
		if (risorse != null) {
			for (IndicatorePiano ind : risorse) {
				List<Valutazione> vals = valutazioneRepository
						.findByIndicatorePianoIdOrderByAnnoAscPeriodoAscDataRilevazioneAsc(ind.getId());
				if (vals != null && !vals.isEmpty()) {
					vals.removeIf(v -> TipoValutazione.CONSUNTIVO.equals(v.getTipoValutazione()) );
				}
				QuadraturaIndicatoriProg r = newQuadraturaIndicatoriProg(ind, nodo.getInizio(), nodo.getScadenza(),
						nodo.getAnno(), nodo.getPiano().getAnnoInizio(), nodo.getPiano().getAnnoFine(), vals);
				if (q.getStato() == null || StatoIndicatore.OK.equals(q.getStato())) {
					if (r.getStato() != null && !StatoIndicatore.OK.equals(r.getStato())) {
						q.setStato(r.getStato());
					}
				}
				r.setScadenzaIndicatoreSt(DateHelper.toString(r.getScadenzaIndicatore()));				
				output.add(r);
			}
		}
		if (q.getStato() == null || StatoIndicatore.OK.equals(q.getStato())) {
			if (risorse == null || risorse.isEmpty())
				q.setStato(StatoIndicatore.MANCANO_INDICATORI);
			else
				q.setStato(StatoIndicatore.OK);
		}
		return q;
	}

	private static QuadraturaIndicatoriProg newQuadraturaIndicatoriProg(NodoPiano nodo) {
		final QuadraturaIndicatoriProg out = new QuadraturaIndicatoriProg();
		out.setDescNodoPiano(nodo.getDenominazione());
		out.setNome(nodo.getDenominazione());
		out.setIdNodoPiano(nodo.getId());
		out.setCodice(NodoPianoHelper.getNomeTipoNodo(nodo.getTipoNodo()) + " "
				+ NodoPianoHelper.ridotto(nodo.getCodiceCompleto()));
		out.setOrganizzazione(nodo.getOrganizzazione() == null ? "" : nodo.getOrganizzazione().getIntestazione());
		out.setResponsabili(nodo.getOrganizzazione() == null || nodo.getOrganizzazione().getResponsabile()==null? "" :
			RisorsaUmanaHelper.getCognomeNomeMatricola(nodo.getOrganizzazione().getResponsabile()));
		out.setInizioValidita(nodo.getInizio());
		out.setFineValidita(nodo.getScadenza());
		return out;
	}

	public static QuadraturaIndicatoriProg newQuadraturaIndicatoriProg(IndicatorePiano indicatore, LocalDate inizio,
			LocalDate scadenza, int annoIncorso, int annoInizio, int annoFine, List<Valutazione> valutazioni) {
		final QuadraturaIndicatoriProg out = new QuadraturaIndicatoriProg();
		out.setIdNodoPiano(indicatore.getNodoPiano().getId());
		out.setPrefix("");
		out.setIdIndicatore(indicatore.getIndicatore().getId());
		out.setIdIndicatorePiano(indicatore.getId());
		out.setNome(indicatore.getIndicatore().getDenominazione());
		out.setIndicatore(StringUtils.isBlank(indicatore.getSpecifica()) ? indicatore.getIndicatore().getDenominazione()
				: indicatore.getSpecifica());
		out.setOrganizzazione(indicatore.getOrganizzazione()==null?null:indicatore.getOrganizzazione().getIntestazione());
		final int annoDa = ConsuntivoHelper.getAnno(indicatore.getNodoPiano().getInizio());
		final int annoA = ConsuntivoHelper.getAnno(indicatore.getNodoPiano().getScadenza());
		int da = Math.max(annoDa, annoInizio);
		int a = Math.min(annoA, annoFine);

		Map<Integer, String> map = new HashMap<>();
		final List<Valutazione> targets=valutazioni.stream().filter(v->TipoValutazione.PREVENTIVO.equals(v.getTipoValutazione())).collect(Collectors.toList());
		final List<Valutazione> storici=valutazioni.stream().filter(v->TipoValutazione.STORICO.equals(v.getTipoValutazione())).collect(Collectors.toList());
		if (targets != null && !targets.isEmpty()) {
			for (Valutazione v : targets) {
					map.put(v.getAnno(), IndicatorePianoHelper.getValToString(v));
			}
		}
		
	
		if (storici != null && !storici.isEmpty()) {
			for (Valutazione v : storici) {
                if(out.getAnno()==null || out.getAnno()<v.getAnno()) {
                	out.setAnno(v.getAnno());
                	out.setStorico(IndicatorePianoHelper.getValToString(v));
                }
			}
		}
		String val = null;
		Map<String, List<Integer>> map1 = null;
		for (int anno = da; anno <= a; anno++) {
			val = map.get(anno);
			if( anno==annoIncorso ) {
				out.setTarget(val);
			}
			List<Integer> b = null;
			if (val == null && map1 != null)
				b = map1.get(anno + "/" + 4);
			if (b != null && b.size() == 3) {
				val = b.get(2) + "%";
			}
			int d = anno - annoInizio;
			if (d < 0)
				d = 0;
			else if (d > 5)
				d = 5;

			switch (d) {
			case 0:
				out.setPreventivo1(val);
				break;
			case 1:
				out.setPreventivo2(val);
				break;
			case 2:
				out.setPreventivo3(val);
				break;
			case 3:
				out.setPreventivo4(val);
				break;
			case 4:
				out.setPreventivo5(val);
				break;
			case 5:
				out.setPreventivo6(val);
				break;
			}
		}
		out.setTipoIndicatore(indicatore.getTipoIndicatore());
		out.setScadenzaIndicatore(indicatore.getScadenzaIndicatore());
		out.setPeso(indicatore.getPeso()==null?null:indicatore.getPeso().intValue());
		return out;
	}

}
