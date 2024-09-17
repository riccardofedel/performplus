package com.finconsgroup.performplus.service.business.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.finconsgroup.performplus.domain.ContenutoContesto;
import com.finconsgroup.performplus.domain.Contesto;
import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.rest.api.vm.v2.AggiornaIntroduzioneRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.ElementoIntroduzioneVM;
import com.finconsgroup.performplus.rest.api.vm.v2.IntroduzioneContenutoResponse;
import com.finconsgroup.performplus.rest.api.vm.v2.IntroduzioneVM;
import com.finconsgroup.performplus.service.dto.ContestoDTO;

public class MappingContestoHelper {

	public static IntroduzioneVM mappingIntroduzione(List<ContenutoContesto> items, Long id,
			Map<String, List<String>> attributiIntroduzione) {
		final IntroduzioneVM out = new IntroduzioneVM();
		out.setIdPiano(id);
		out.setElementi(new ArrayList<>());
		if (items == null || items.isEmpty())
			return out;
		for (String gruppo : attributiIntroduzione.keySet()) {
			List<String> list = attributiIntroduzione.get(gruppo);
			ContenutoContesto c;
			for (String nome : list) {
				c = cerca(items, nome, gruppo);
				if (c == null) {
					c = new ContenutoContesto(gruppo, nome, "");
				}
				out.getElementi()
						.add(new ElementoIntroduzioneVM(c.getId(), c.getGruppo(), c.getNome(), c.getContenuto()));
			}
		}
		return out;
	}

	public static ContestoDTO mappingContenuto(List<ContenutoContesto> items, NodoPiano piano) {
		final ContestoDTO out = new ContestoDTO();
		if (piano == null) {
			return out;
		}
		out.setId(piano.getId());
		out.setPiano(out);
		if (items == null || items.isEmpty())
			return out;
		for (ContenutoContesto c : items) {
			switch (c.getNome().toLowerCase()) {
			case "premesse":
			case "premessa":
			case "introduzione":
				out.setPremesse(c.getContenuto());
				break;
			case "linee":
			case "linee e obiettivi strategici":
			case "organi di governo":
				out.setLinee(c.getContenuto());
				break;
			case "analisicontesto":
			case "analisi di contesto":
				out.setAnalisiContesto(c.getContenuto());
				break;
			case "analisistrategica":
			case "analisi strategica":
			case "linee programmatiche":
				out.setAnalisiStrategica(c.getContenuto());
				break;
			case "obiettivi":
			case "obiettivi della finanza pubblica individuati dal governo":
			case "scenario macro economico":
				out.setObiettivi(c.getContenuto());
				break;
			case "popolazione":
			case "documento di economia  e finanza":
				out.setPopolazione(c.getContenuto());
				break;
			case "partecipate":
			case "sistema delle partecipate":
			case "organizzazione e gestione servizi pubblici locali":
				out.setPartecipate(c.getContenuto());
				break;
			case "investimenti":
			case "organismi interni e partecipazioni":
				out.setInvestimenti(c.getContenuto());
				break;
			case "tributi":
			case "tributi e tariffe dei servizi pubblici":
				out.setTributi(c.getContenuto());
				break;
			case "fabbisogni":
			case "fabbisogni di spesa":
			case "societa' partecipate":
				out.setFabbisogni(c.getContenuto());
				break;
			case "patrimonio":
			case "situazione finanziaria del comune":
				out.setPatrimonio(c.getContenuto());
				break;
			case "finanziamento":
			case "finanziamento e indebitamento":
			case "i dipendenti del comune":
				out.setFinanziamento(c.getContenuto());
				break;
			case "equilibri":
			case "equilibri di bilancio":
			case "strumenti di rendicontazione":
				out.setEquilibri(c.getContenuto());
				break;
			case "risorse":
			case "risorse umane e struttura organizzativa":
				out.setRisorse(c.getContenuto());
				break;
			case "nazionale":
			case "ccenario nazionale":
			case "missioni programmi e obiettivi strategici":
				out.setNazionale(c.getContenuto());
				break;
			case "regionale":
			case "scenario regionale":
				out.setRegionale(c.getContenuto());
				break;
			case "singola":
			case "pagina singola di progetto":
				out.setSingola(c.getContenuto());
				break;
			case "territorio":
			case "economia lombarda":
				out.setTerritorio(c.getContenuto());
				break;
			}

		}
		out.setId(null);
		return out;
	}

	public static List<ContenutoContesto> mappingChanged(final AggiornaIntroduzioneRequest request,
			List<ContenutoContesto> items, final NodoPiano piano) {
		List<ContenutoContesto> out = new ArrayList<>();
		String c = null;
		ContenutoContesto cc = null;
		if (request.getElemento() == null) {
			return out;
		}
		ElementoIntroduzioneVM e = request.getElemento();
		c = e.getContenuto();
		cc = cerca(items, e.getNome(), e.getGruppo());
		if (cc == null || !(cc.getContenuto() == null ? "" : cc.getContenuto()).equals(c)) {
			out.add(modifica(cc, c, piano, e.getNome(), e.getGruppo()));
		}
		return out;
	}

	public static List<ContenutoContesto> mappingFromContesto(final Contesto contesto) {
		List<ContenutoContesto> out = new ArrayList<>();
		String c = null;
		NodoPiano piano = contesto.getPiano();
		ContenutoContesto cc = null;


		return out;
	}

	private static ContenutoContesto modifica(final ContenutoContesto cc, final String c, final NodoPiano piano,
			final String nome, final String gruppo) {
		ContenutoContesto p = cc;
		if (p == null) {
			p = new ContenutoContesto();
			p.setNome(nome);
			p.setGruppo(gruppo);
			p.setPiano(piano);
		}
		p.setContenuto(c);

		return p;
	}

	private static ContenutoContesto cerca(final List<ContenutoContesto> items, final String nome,
			final String gruppo) {
		if (items == null || items.isEmpty() || StringUtils.isBlank(nome))
			return null;
		for (ContenutoContesto c : items) {
			if (c.getNome().equals(nome) && (gruppo == null ? "" : gruppo).equals(c.getGruppo()))
				return c;
		}
		return null;
	}

	public static IntroduzioneContenutoResponse mappingContenuto(ContenutoContesto c, NodoPiano piano) {
		return new IntroduzioneContenutoResponse(c.getId(),c.getGruppo(),c.getNome(),c.getContenuto()).idPiano(piano.getId());
	}
}
