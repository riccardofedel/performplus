package com.finconsgroup.performplus.service.business.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.Cruscotto;
import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.Profilo;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaNodoPiano;
import com.finconsgroup.performplus.domain.RisorsaUmanaOrganizzazione;
import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.enumeration.StatoPiano;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoPiano;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.repository.CruscottoRepository;
import com.finconsgroup.performplus.repository.IndicatorePianoRepository;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.OrganizzazioneRepository;
import com.finconsgroup.performplus.repository.ProfiloRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaNodoPianoRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaOrganizzazioneRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.ValutazioneRepository;
import com.finconsgroup.performplus.rest.api.vm.PianoVM;
import com.finconsgroup.performplus.service.business.IArchivioBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;

@Service
@Transactional
public class ArchivioBusiness implements IArchivioBusiness {
	@Autowired
	private NodoPianoRepository nodoPianoRepository;

	@Autowired
	private OrganizzazioneRepository organizzazioneRepository;
	@Autowired
	private ValutazioneRepository valutazioneManager;
	@Autowired
	private IndicatorePianoRepository indicatorePianoManager;
	@Autowired
	private PrioritaOperativaBusiness prioritaOperativBusiness;
	@Autowired
	private RisorsaUmanaNodoPianoRepository risorsaUmanaNodoPianoRepository;
	@Autowired
	private RisorsaUmanaOrganizzazioneRepository risorsaUmanaOrganizzazioneRepository;

	@Autowired
	private RisorsaUmanaRepository risorsaUmanaRepository;
	@Autowired
	private CruscottoRepository cruscottoRepository;
	@Autowired
	private ProfiloRepository profiloRepository;

	private static final Logger logger = LoggerFactory.getLogger(ArchivioBusiness.class);

	public static final String SUFFIX_PRS_VARIATO = "_var";

	@Override
	public PianoVM archiviazione(Long idPiano) throws BusinessException {
		NodoPiano nuovo = null;
		NodoPiano piano = null;
		try {
			piano = nodoPianoRepository.findById(idPiano).orElse(null);

			nuovo = clona(piano);

			Cruscotto cruscotto=cruscottoRepository.findByIdEnteAndAnno(piano.getIdEnte(), piano.getAnno()).orElse(null);
			if(cruscotto!=null) {
				cruscotto.setDataInizializzazioneAnno(LocalDate.now());
				cruscotto=cruscottoRepository.save(cruscotto);
			}
			cruscotto=new Cruscotto();
			cruscotto.setIdEnte(nuovo.getIdEnte());
			cruscotto.setAnno(nuovo.getAnno());
			cruscotto=cruscottoRepository.save(cruscotto);
		} catch (Exception e) {
			logger.error("archiviazione", e);
			return null;
		}
		return Mapping.mapping(nuovo, PianoVM.class);
	}

	public NodoPiano clona(NodoPiano piano) throws Exception {
		NodoPiano clone = Mapping.mapping(piano, NodoPiano.class);
		clear(clone);
		clone.setPianoOrigine(piano.getPianoOrigine() == null ? piano : piano.getPianoOrigine());
		clone.setTipoPiano(TipoPiano.PIANO);
		clone.setStatoPiano(StatoPiano.ATTIVO);
		clone.setIdEnte(piano.getIdEnte());
		clone.setApprovazione(null);
		int anno = piano.getAnno() + 1;
		clone.setCodice("piano_" + anno);
		clone.setAnno(anno);
		clone.setInizio(null);
		if(clone.getDescrizione()!=null) {
		clone.setDescrizione(clone.getDescrizione().replace(piano.getAnno().toString(), Integer.toString(anno)));
		}
		
		clone.setCodiceCompleto(clone.getCodice());

		clonaRisorse(piano, anno);
		Organizzazione clonaEnte = clonaOrgs(piano, anno);
		clone.setOrganizzazione(clonaEnte);
		
		clone.setDenominazione(clone.getTipoPiano().name() + " " + anno);
		NodoPiano pianoClone = nodoPianoRepository.save(clone);
		NodoPiano entity = nodoPianoRepository.findById(piano.getId())
				.orElseThrow(() -> new Exception("piano not trovato:" + piano.getId()));

		List<Long[]> nodi = new ArrayList<>();
		clonaNodi(entity, pianoClone, nodi);

		// clona risorseUmane nodo
		for (Long[] longs : nodi) {
			clonaRisorseUmaneNodo(longs[0], longs[1]);
		}

		// clona indicatori nodo
		for (Long[] longs : nodi) {
			clonaIndicatori(longs[0], longs[1]);
		}

		// clona prioritaOperative
		for (Long[] longs : nodi) {
			clonaPrioritaOperative(longs[0], longs[1]);
		}
		clonaProfiliAdmin(piano.getIdEnte(),piano.getAnno(),anno);
		
		return Mapping.mapping(pianoClone, NodoPiano.class);
	}

	private void clonaRisorse(NodoPiano piano, int anno) throws Exception {
		List<RisorsaUmana> risorse = risorsaUmanaRepository.findByIdEnteAndAnno(piano.getIdEnte(), piano.getAnno());
		if (risorse == null)
			return;
		for (RisorsaUmana r : risorse) {
			clonaRisorsa(r, anno);
		}
	}

	private void clonaRisorsa(RisorsaUmana r, int anno) throws Exception {
		RisorsaUmana clone = Mapping.mapping(r, RisorsaUmana.class);
		clone.setAnno(anno);
		clone.setId(null);
		clone = risorsaUmanaRepository.save(clone);
		List<Profilo> profili = profiloRepository.findByIdEnteAndAnnoAndRisorsaUmanaId(r.getIdEnte(), r.getAnno(),
				r.getId());
		if (profili != null) {
			for (Profilo p : profili) {
				Profilo cloneP = Mapping.mapping(p, Profilo.class);
				cloneP.setAnno(anno);
				cloneP.setId(null);
				cloneP.setRisorsaUmana(clone);
				cloneP.setOrganizzazione(p.getOrganizzazione()==null?null:direzione(r.getIdEnte(), anno,p.getOrganizzazione().getCodiceCompleto()));
				profiloRepository.save(cloneP);
			}
		}
	}
	private Organizzazione direzione(Long idEnte, int anno, String codiceCompleto) {
		return organizzazioneRepository.findByIdEnteAndAnnoAndCodiceCompleto(idEnte, anno, codiceCompleto);
	}

	private void clonaProfiliAdmin(long idEnte,int annoPrec,int anno) throws Exception {
		List<Profilo> profili = profiloRepository.findByIdEnteAndAnnoAndRisorsaUmanaIsNull(idEnte, annoPrec);
		if (profili != null) {
			for (Profilo p : profili) {
				Profilo cloneP = Mapping.mapping(p, Profilo.class);
				cloneP.setAnno(anno);
				cloneP.setId(null);
				cloneP.setRisorsaUmana(null);
				cloneP.setOrganizzazione(p.getOrganizzazione()==null?null:direzione(idEnte, anno,p.getOrganizzazione().getCodiceCompleto()));
				profiloRepository.save(cloneP);
			}
		}
	}
	private Organizzazione clonaOrgs(NodoPiano piano, int anno) throws Exception {
		List<Long[]> orgs = new ArrayList<>();
		Organizzazione ente = organizzazioneRepository.findTopByIdEnteAndAnnoAndLivello(piano.getIdEnte(),
				piano.getAnno(), Livello.ENTE);
		Organizzazione clonaEnte = Mapping.mapping(ente, Organizzazione.class);
		clear(clonaEnte);
		clonaEnte.setAnno(anno);
		if (ente.getResponsabile() != null) {
			clonaEnte.setResponsabile(
					risorsaUmanaRepository.findByIdEnteAndAnnoAndCodiceInternoAndCognomeAndNomeAndPolitico(
							piano.getIdEnte(), anno, ente.getResponsabile().getCodiceInterno(),
							ente.getResponsabile().getCognome(), ente.getResponsabile().getNome(), false));
		}
		clonaEnte = organizzazioneRepository.save(clonaEnte);
		clonaOrgs(ente, clonaEnte, orgs);

		for (Long[] longs : orgs) {
			clonaRisorseUmaneOrg(longs[0], longs[1]);
		}
		return clonaEnte;
	}

	private void clonaOrgs(Organizzazione entity, Organizzazione entityClone, List<Long[]> orgs) throws Exception {
		orgs.add(new Long[] { entity.getId(), entityClone.getId() });
		List<Organizzazione> figli = organizzazioneRepository.findByPadreIdOrderByCodice(entity.getId());
		if (figli == null)
			return;
		for (Organizzazione o : figli) {
			Organizzazione nuovo = clonaOrg(o, entityClone);
			clonaOrgs(o, nuovo, orgs);
		}
	}

	private void clonaIndicatori(Long idNodo, Long idClone) throws Exception {
		NodoPiano clone = nodoPianoRepository.findById(idClone).orElse(null);
		List<IndicatorePiano> indicatori = indicatorePianoManager.findByNodoPianoId(idNodo);
		if (indicatori == null)
			return;
		for (IndicatorePiano ip : indicatori) {
			clona(ip, clone);
		}
	}

	private void clonaRisorseUmaneNodo(Long idNodo, Long idClone) throws Exception {
		NodoPiano clone = nodoPianoRepository.findById(idClone).orElse(null);
		List<RisorsaUmanaNodoPiano> risorse = risorsaUmanaNodoPianoRepository.findByNodoPianoId(idNodo);
		if (risorse == null)
			return;
		for (RisorsaUmanaNodoPiano ris : risorse) {
			clona(ris, clone);
		}
	}

	private void clonaRisorseUmaneOrg(Long idOrg, Long idClone) throws Exception {
		Organizzazione clone = organizzazioneRepository.findById(idClone).orElse(null);
		List<RisorsaUmanaOrganizzazione> risorse = risorsaUmanaOrganizzazioneRepository.findByOrganizzazioneId(idOrg);
		if (risorse == null)
			return;
		for (RisorsaUmanaOrganizzazione ris : risorse) {
			clona(ris, clone);
		}
	}

	private void clonaPrioritaOperative(Long idNodo, Long idClone) throws Exception {
		prioritaOperativBusiness.clona(idNodo, idClone);

	}

	private void clonaNodi(NodoPiano entity, NodoPiano entityClone, List<Long[]> nodi) throws Exception {
		nodi.add(new Long[] { entity.getId(), entityClone.getId() });
		List<NodoPiano> figli = nodoPianoRepository.findByPadreIdAndDateDeleteIsNull(entity.getId());
		if (figli == null)
			return;
		for (NodoPiano np : figli) {
			NodoPiano nuovo = clonaNodo(np, entityClone);
			clonaNodi(np, nuovo, nodi);
		}
	}

	private NodoPiano clonaNodo(NodoPiano entity, NodoPiano clonePadre) throws Exception {
		NodoPiano clone = Mapping.mapping(entity, NodoPiano.class);
		clear(clone);
		clone.setPadre(clonePadre);
		clone.setPiano(TipoNodo.PIANO.equals(clonePadre.getTipoNodo()) ? clonePadre : clonePadre.getPiano());
		clone.setCodiceCompleto(getCodiceCompleto(clone));

		clone.setAnno(clonePadre.getAnno());

		if (entity.getOrganizzazione() != null) {
			Organizzazione o = entity.getOrganizzazione();
			Organizzazione c = organizzazioneRepository.findByIdEnteAndAnnoAndCodiceCompleto(o.getIdEnte(),
					clone.getAnno(), o.getCodiceCompleto());
			if (c != null)
				clone.setOrganizzazione(c);

		}

		clone = nodoPianoRepository.save(clone);
		return clone;
	}

	private Organizzazione clonaOrg(Organizzazione entity, Organizzazione clonePadre) throws Exception {
		Organizzazione clone = Mapping.mapping(entity, Organizzazione.class);
		clear(clone);
		clone.setPadre(clonePadre);
		clone.setCodiceCompleto(getCodiceCompleto(clone));
		clone.setAnno(clonePadre.getAnno());
		if (entity.getResponsabile() != null) {
			clone.setResponsabile(
					risorsaUmanaRepository.findByIdEnteAndAnnoAndCodiceInternoAndCognomeAndNomeAndPolitico(
							entity.getIdEnte(), clone.getAnno(), entity.getResponsabile().getCodiceInterno(),
							entity.getResponsabile().getCognome(), entity.getResponsabile().getNome(), false));
		}

		clone = organizzazioneRepository.save(clone);
		return clone;
	}

	private void clear(NodoPiano clone) {
		clone.setId(null);
		clone.setPadre(null);
		clone.setPiano(null);
		clone.setOrganizzazioni(null);
		clone.setOrganizzazione(null);
		clone.setNoteConsuntivo(null);
		clone.setPianoOrigine(null);
	}

	private void clear(Organizzazione clone) {
		clone.setId(null);
		clone.setPadre(null);
		clone.setResponsabile(null);
		clone.setAnno(null);

	}

	private String succ(String codice) {
		String c = SUFFIX_PRS_VARIATO + "_";
		int k = codice.indexOf(c);
		int nro = 1;
		if (k > 0) {
			String s = codice.substring(k + c.length());
			nro = Integer.parseInt(s);
			nro++;
			return codice.substring(0, k) + c + nro;
		}
		return codice + c + nro;
	}

	private void clona(RisorsaUmanaNodoPiano ris, NodoPiano clone) throws Exception {
		RisorsaUmanaNodoPiano entity = new RisorsaUmanaNodoPiano();
		Mapping.mapping(ris, entity);
		entity.setId(null);
		entity.setNodoPiano(clone);
		RisorsaUmana r = entity.getRisorsaUmana();
		entity.setRisorsaUmana(risorsaUmanaRepository.findByIdEnteAndAnnoAndCodiceInternoAndCognomeAndNomeAndPolitico(
				r.getIdEnte(), clone.getAnno(), r.getCodiceInterno(), r.getCognome(), r.getNome(), r.getPolitico()));
		if (entity.getRisorsaUmana() != null)
			risorsaUmanaNodoPianoRepository.save(entity);

	}

	private void clona(RisorsaUmanaOrganizzazione ris, Organizzazione clone) throws Exception {
		RisorsaUmanaOrganizzazione entity = new RisorsaUmanaOrganizzazione();
		Mapping.mapping(ris, entity);
		entity.setId(null);
		entity.setOrganizzazione(clone);
		RisorsaUmana r = entity.getRisorsaUmana();
		entity.setRisorsaUmana(risorsaUmanaRepository.findByIdEnteAndAnnoAndCodiceInternoAndCognomeAndNomeAndPolitico(
				r.getIdEnte(), clone.getAnno(), r.getCodiceInterno(), r.getCognome(), r.getNome(), r.getPolitico()));
		if (entity.getRisorsaUmana() != null)
			risorsaUmanaOrganizzazioneRepository.save(entity);

	}

	private void clona(IndicatorePiano indicatorePiano, NodoPiano clone) throws Exception {
		IndicatorePiano entity = Mapping.mapping(indicatorePiano, IndicatorePiano.class);
		entity.setId(null);
		entity.setNodoPiano(clone);
		entity = indicatorePianoManager.save(entity);

		List<Valutazione> valutazioni = valutazioneManager.findByIndicatorePianoIdOrderByAnnoAscPeriodoAsc(indicatorePiano.getId());
		clonaValutazioni(valutazioni, entity);
	}

	private void clonaValutazioni(List<Valutazione> valutazioni, IndicatorePiano clone) throws Exception {
		if (valutazioni == null)
			return;

		for (Valutazione val : valutazioni) {
			clona(val, clone);
		}

	}

	private void clona(Valutazione val, IndicatorePiano clone) {
		Valutazione entity = Mapping.mapping(val, Valutazione.class);
		entity.setId(null);
		entity.setIndicatorePiano(clone);
		valutazioneManager.save(entity);

	}

	public String getCodiceCompleto(NodoPiano np) {
		String code = np.getCodice() == null ? "" : np.getCodice();
		if (np.getPadre() == null || np.getPadre().getId().equals(np.getId()))
			return code;
		return getCodiceCompleto(np.getPadre()) + "." + code;
	}

	public String getCodiceCompleto(Organizzazione org) {
		String code = org.getCodice() == null ? "" : org.getCodice();
		if (org.getPadre() == null || org.getPadre().getId().equals(org.getId()))
			return code;
		return getCodiceCompleto(org.getPadre()) + "." + code;
	}
}
