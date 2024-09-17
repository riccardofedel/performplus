package com.finconsgroup.performplus.service.business.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jsoup.Jsoup;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipologiaObiettiviOperativi;
import com.finconsgroup.performplus.repository.IndicatorePianoRepository;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.ObiettivoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.StrutturaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ConsuntivoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.CreaNodoPianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoFiglioVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoSmartVM;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;
import com.finconsgroup.performplus.service.business.utils.RisorsaUmanaHelper;
import com.finconsgroup.performplus.service.dto.EntityDTO;
import com.finconsgroup.performplus.service.dto.NodoPianoDTO;
import com.finconsgroup.performplus.service.dto.OrganizzazioneDTO;
import com.finconsgroup.performplus.service.dto.PianoEasyDTO;

public class MappingNodoPianoHelper {
	private MappingNodoPianoHelper() {
	}

	public static NodoPianoSmartVM mappingToSmart(NodoPiano np) {
		if (np == null)
			return null;
		NodoPiano piano = TipoNodo.PIANO.equals(np.getTipoNodo()) ? np : np.getPiano();
		return Mapping.mapping(np, NodoPianoSmartVM.class).annoInizio(piano == null ? null : piano.getAnnoInizio())
				.annoFine(piano == null ? null : piano.getAnnoFine()).ordine(np.getOrdine())
				.strategico(Boolean.TRUE.equals(np.getStrategico())).idPadre(np.getPadre() == null ? null : np.getPadre().getId())
				.idStruttura(np.getOrganizzazione() == null ? null : np.getOrganizzazione().getId())
				.codiceCompletoStruttura(
						np.getOrganizzazione() == null ? null : np.getOrganizzazione().getCodiceCompleto());
	}
	public static NodoFiglioVM mappingToFiglio(NodoPiano np) {
		if (np == null)
			return null;
		final NodoFiglioVM out=new NodoFiglioVM();
		out.setAnno(np.getAnno());
		out.setId(np.getId());	
		out.setDenominazione(np.getDenominazione());
		out.setCodice(np.getCodice());	
		out.setCodiceCompleto(np.getCodiceCompleto().substring(11));
		out.setTipoNodo(np.getTipoNodo().label);		
		if(np.getOrganizzazione()!=null) {
			out.setIdStruttura(np.getOrganizzazione().getId());	
			out.setStruttura(np.getOrganizzazione().getIntestazione());
			if(np.getOrganizzazione().getResponsabile()!=null) {
				out.setResponsabile(RisorsaUmanaHelper.getCognomeNomeMatricola(np.getOrganizzazione().getResponsabile()));	
			}
		}
		return out;
	}
	public static NodoPianoDetailVM mappingToDetail(final NodoPiano entity,
			final NodoPianoRepository nodoPianoRepository) {
		if (entity == null)
			return null;
		final NodoPianoDetailVM out = Mapping.mapping(entity, NodoPianoDetailVM.class);
		completa(out, entity);
		if (TipoNodo.OBIETTIVO.equals(entity.getTipoNodo()))
			out.setOrganizzazioni(altreOrganizzazioni(entity, nodoPianoRepository));

		out.setResponsabili(responsabili(entity, nodoPianoRepository));

		return out;
	}

	private static List<DecodificaVM> responsabili(NodoPiano entity, NodoPianoRepository nodoPianoRepository) {
		final List<DecodificaVM> out=new ArrayList<>();
		final List<Long> doppi=new ArrayList<>();
		RisorsaUmana r=null;
		if(entity.getOrganizzazione()!=null && entity.getOrganizzazione().getResponsabile()!=null) {
			r=entity.getOrganizzazione().getResponsabile();
			out.add(new DecodificaVM(r.getId(), r.getCodiceInterno(), r.getCognome()+" "+r.getNome()));
			doppi.add(r.getId());
		}
		if(entity.getOrganizzazioni()!=null) {
			for (Organizzazione o : entity.getOrganizzazioni()) {
				if(o.getResponsabile()!=null) {
					r=o.getResponsabile();
					if(!doppi.contains(r.getId())) {
						out.add(new DecodificaVM(r.getId(), r.getCodiceInterno(), r.getCognome()+" "+r.getNome()));
						doppi.add(r.getId());
					}
				}
			}
		}
		return out;
	}

	public static ConsuntivoVM mappingToConsuntivo(final NodoPiano entity,
			ImmutablePair<Integer, Map<Integer, Valutazione>> pair, final NodoPianoRepository nodoPianoRepository,
			final IndicatorePianoRepository indicatorePianoRepository) {
		if (entity == null)
			return null;
		final ConsuntivoVM out = Mapping.mapping(entity, ConsuntivoVM.class);

		completa(out, entity);
		
		out.setOrganizzazioni(altreOrganizzazioni(entity, nodoPianoRepository));

		Valutazione v = pair.left == null || pair.right.get(pair.left) == null ? null : pair.right.get(pair.left);

		out.setStatoAvanzamentoS1(entity.getStatoAvanzamentoS1());
		out.setStatoAvanzamentoS2(entity.getStatoAvanzamentoS2());

		return out;
	}

	public static List<StrutturaVM> altreOrganizzazioni(final NodoPiano nodoPiano,
			final NodoPianoRepository repository) {
		final List<Organizzazione> orgs = nodoPiano.getOrganizzazioni();
		return MappingStrutturaHelper.mappingItemsToStruttura(orgs).stream().sorted(new Comparator<StrutturaVM>() {
			@Override
			public int compare(StrutturaVM o1, StrutturaVM o2) {
				return o1.getDenominazione().compareTo(o2.getDenominazione());
			}
		}).collect(Collectors.toList());
	}


	public static void altreOrganizzazioni(final ObiettivoVM out, final NodoPiano entity,
			final NodoPianoRepository nodoPianoRepository) {
		out.setOrganizzazioni(altreOrganizzazioni(entity, nodoPianoRepository));
	}

	public static void completa(final ObiettivoVM out, final NodoPiano entity) {

		out.setPadre(MappingNodoPianoHelper.mappingToSmart(entity.getPadre()));
		out.setOrganizzazione(Mapping.mapping(entity.getOrganizzazione(), StrutturaVM.class));



		out.setCodiceRidotto(NodoPianoHelper.ridotto(entity.getCodiceCompleto()));
		out.setPeso(entity.getPeso() == null ? 0 : entity.getPeso().intValue());

		out.setResponsabile(
					entity.getOrganizzazione() != null && entity.getOrganizzazione().getResponsabile() != null
							? MappingRisorsaHelper.getCognomeNome(entity.getOrganizzazione().getResponsabile())
							: "");
	
		out.setStrategico(Boolean.TRUE.equals(entity.getStrategico()));


		NodoPiano piano = TipoNodo.PIANO.equals(entity.getTipoNodo()) ? entity : entity.getPiano();

		out.setAnnoInizio(piano == null ? null : piano.getAnnoInizio());
		out.setAnnoFine(piano == null ? null : piano.getAnnoFine());
//		out.setIdResponsabile(entity.getResponsabile() == null ? null : entity.getResponsabile().getId());
		
		out.setTipo(entity.getTipo()==null?null:entity.getTipo().label);
		out.setModalita(entity.getModalitaAttuative()==null?null:entity.getModalitaAttuative().label);
		out.setObiettivoImpatto(TipologiaObiettiviOperativi.IMPATTO.equals(entity.getTipologie()));	
		out.setNote(out.getNote()==null?null:Jsoup.parse(out.getNote()).text());		
		out.setBloccato(Boolean.TRUE.equals(entity.getBloccato()));
	}
	public static List<NodoPianoSmartVM> mappingItemsToSmart(List<NodoPiano> items) {
		final List<NodoPianoSmartVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(r -> out.add(mappingToSmart(r)));
		return out;
	}

	public static DecodificaVM mappingToDecodifica(Object o) {
		return Mapping.mapping(o, DecodificaVM.class);
	}

	public static DecodificaVM mappingToDecodificaWithCode(Object o) {
		DecodificaVM out = Mapping.mapping(o, DecodificaVM.class);
		out.setDescrizione(out.getCodice() + " " + out.getDescrizione());
		return out;
	}

	public static List<DecodificaVM> mappingItemsToDecodifica(List<?> items) {
		final List<DecodificaVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(r -> out.add(mappingToDecodifica(r)));
		return out;
	}

	public static List<DecodificaVM> mappingItemsToDecodificaWithCode(List<?> items) {
		final List<DecodificaVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(r -> out.add(mappingToDecodificaWithCode(r)));
		return out;
	}

	public static NodoPianoDTO toDto(NodoPiano np) {
		final NodoPianoDTO out = new NodoPianoDTO();
		if (np == null)
			return out;
		out.setId(np.getId());
		out.setAnno(np.getAnno());
		out.setCodiceCompleto(np.getCodiceCompleto());
		out.setIdEnte(np.getIdEnte());
		out.setOrdine(np.getOrdine());
		out.setCodice(np.getCodice());
		out.setTipoNodo(np.getTipoNodo());
		NodoPiano piano = TipoNodo.PIANO.equals(np.getTipoNodo()) ? np : np.getPiano();
		out.setPiano(Mapping.mapping(piano, PianoEasyDTO.class));
		out.setPadre(Mapping.mapping(np.getPadre(), PianoEasyDTO.class));
		out.setDenominazione(np.getDenominazione());
		out.setNote(np.getNote());
		out.setNoteConsuntivo(np.getNoteConsuntivo());
		out.setDataModificaNota(np.getDataModificaNota());
		out.setDescrizione(np.getDescrizione());
		out.setPeso(np.getPeso());
		out.setOrganizzazione(Mapping.mapping(np.getOrganizzazione(), OrganizzazioneDTO.class));
		out.setBloccato(Boolean.TRUE.equals(np.getBloccato()));

		out.setLivelloStrategicita(np.getLivelloStrategicita());
		out.setLivelloComplessita(np.getLivelloComplessita());

		out.setInizio(np.getInizio());
		out.setScadenza(np.getScadenza());
		out.setInizioEffettivo(np.getInizioEffettivo());
		out.setScadenzaEffettiva(np.getScadenzaEffettiva());

		out.setAnnoInizio(np.getAnnoInizio());
		out.setAnnoFine(np.getAnnoFine());
		out.setApprovazione(np.getApprovazione());
		out.setTipoPiano(np.getTipoPiano());
		out.setStato(np.getStatoPiano());

		out.setPianoOrigine(Mapping.mapping(np.getPianoOrigine(), EntityDTO.class));

		return out;
	}

	public static CreaNodoPianoRequest toAggiorna(NodoPiano nodo) {
		CreaNodoPianoRequest out = new CreaNodoPianoRequest();

		out.setIdPadre(nodo.getPadre() == null ? null : nodo.getPadre().getId());
		out.setTipoNodo(nodo.getTipoNodo());
		out.setCodiceRidottoPadre(
				nodo.getPadre() == null ? null : NodoPianoHelper.ridotto(nodo.getPadre().getCodiceCompleto()));

		out.setOrganizzazione(nodo.getOrganizzazione() == null ? null : nodo.getOrganizzazione().getId());

		out.setCodice(nodo.getCodice());
		out.setDenominazione(nodo.getDenominazione());
		out.setDescrizione(nodo.getDescrizione());

		out.setNote(nodo.getNote());
		out.setPeso(nodo.getPeso());
		out.setInizio(nodo.getInizio());
		out.setScadenza(nodo.getScadenza());
		out.setStrategico(Boolean.TRUE.equals(nodo.getStrategico()));
		out.setNomeResponsabile(nodo.getOrganizzazione() == null || nodo.getOrganizzazione().getResponsabile()==null? "" :
			RisorsaUmanaHelper.getCognomeNomeMatricola(nodo.getOrganizzazione().getResponsabile()));
		out.setEnabledStrategico(TipoNodo.OBIETTIVO.equals(nodo.getTipoNodo())
				|| (nodo.getPadre() != null && TipoNodo.OBIETTIVO.equals(nodo.getPadre().getTipoNodo())
						&& Boolean.TRUE.equals(nodo.getPadre().getStrategico())));
		
		out.setPolitica(nodo.getPolitica());
		
		out.setDimensione(nodo.getDimensione());
		out.setContributors(nodo.getContributors());
		out.setStakeholders(nodo.getStakeholders());
		
		out.setFocusSemplificazione(nodo.getFocusSemplificazione());
		out.setFocusDigitalizzazione(nodo.getFocusDigitalizzazione());
		out.setFocusAccessibilita(nodo.getFocusAccessibilita());
		out.setFocusPariOpportunita(nodo.getFocusPariOpportunita());
		out.setFocusRisparmioEnergetico(nodo.getFocusRisparmioEnergetico());
		
		out.setProspettiva(nodo.getProspettiva());
		out.setInnovazione(nodo.getInnovazione());
		out.setAnnualita(nodo.getAnnualita());

		out.setFlagPnrr(nodo.getFlagPnrr());
		
		out.setTipo(nodo.getTipo());
		out.setModalita(nodo.getModalitaAttuative());
		out.setObiettivoImpatto(TipologiaObiettiviOperativi.IMPATTO.equals(nodo.getTipologie()));	
		out.setBloccato(Boolean.TRUE.equals(nodo.getBloccato()));

		out.setFlagOIV(Boolean.TRUE.equals(nodo.getFlagOIV()));
		out.setNoteOIV(nodo.getNoteOIV());
		
		if(nodo.getOrganizzazioni()!=null && !nodo.getOrganizzazioni().isEmpty()) {
			out.setOrganizzazioni(nodo.getOrganizzazioni().stream().map(t->t.getId()).collect(Collectors.toList()));		
		}
		return out;
	}

}
