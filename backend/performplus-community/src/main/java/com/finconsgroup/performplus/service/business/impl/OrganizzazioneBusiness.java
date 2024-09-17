package com.finconsgroup.performplus.service.business.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.RisorsaStrumentale;
import com.finconsgroup.performplus.domain.RisorsaStrumentaleOrganizzazione;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaOrganizzazione;
import com.finconsgroup.performplus.enumeration.Over;
import com.finconsgroup.performplus.enumeration.TipoStruttura;
import com.finconsgroup.performplus.enumeration.TipologiaStruttura;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.repository.OrganizzazioneRepository;
import com.finconsgroup.performplus.repository.RisorsaStrumentaleOrganizzazioneRepository;
import com.finconsgroup.performplus.repository.RisorsaStrumentaleRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaOrganizzazioneRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaRepository;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEnumVM;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.rest.api.vm.v2.DipendenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.ModificaDisponibilitaRisorsaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.PersonaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.AggiornaStrutturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.CreaStrutturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.DisponibilitaStrutturaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.OrganizzazioneDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa.AssociaRisorsaOrganizzazioneRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa.RisorsaUmanaOrganizzazioneListVM;
import com.finconsgroup.performplus.service.business.IOrganizzazioneBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.ConsuntivoHelper;
import com.finconsgroup.performplus.service.business.utils.DateHelper;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.MappingRisorsaHelper;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;
import com.finconsgroup.performplus.service.business.utils.OrganizzazioneHelper;
import com.finconsgroup.performplus.service.business.utils.RisorsaUmanaHelper;
import com.finconsgroup.performplus.service.dto.ConfigurazioneDTO;
import com.finconsgroup.performplus.service.dto.OrganizzazioneDTO;
import com.finconsgroup.performplus.service.dto.QuadraturaRisorsaStrumentale;
import com.finconsgroup.performplus.service.dto.QuadraturaRisorsaUmana;
import com.finconsgroup.performplus.service.dto.RicercaRisorseUmaneOrg;
import com.finconsgroup.performplus.service.dto.RisorsaStrumentaleOrganizzazioneDTO;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaDTO;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaOrganizzazioneDTO;

@Service
@Transactional
public class OrganizzazioneBusiness implements IOrganizzazioneBusiness {
	private static final String RISORSA_NON_TROVATA = "Risorsa non trovata:";
	private static final String RISORSA_UTILIZZATA = "La risorsa è già utilizzata al ";
	private static final String STRUTTURA_NON_TROVATA = "Struttura non trovata:";
	@Autowired
	private OrganizzazioneRepository organizzazioneRepository;
	@Autowired
	private RisorsaUmanaRepository risorsaUmanaRepository;
	@Autowired
	private RisorsaStrumentaleRepository risorsaStrumentaleRepository;
	@Autowired
	private RisorsaUmanaOrganizzazioneRepository risorsaUmanaOrganizzazioneRepository;
	@Autowired
	private RisorsaStrumentaleOrganizzazioneRepository risorsaStrumentaleOrganizzazioneRepository;

	@Autowired
	private ConfigurazioneBusiness configBusiness;

	@Override

	public void elimina(Long id) throws BusinessException {
		organizzazioneRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public OrganizzazioneSmartVM getRootOnly(Long idEnte, Integer anno) throws BusinessException {
		List<Organizzazione> items = organizzazioneRepository.findByIdEnteAndAnnoOrderByCodiceCompleto(idEnte, anno);
		if (items == null || items.isEmpty()) {
			throw new BusinessException(HttpStatus.NOT_FOUND, "Non trovato ente per" + idEnte + "e " + anno);
		}
		LocalDate now = LocalDate.now();
		Integer year = now.getYear();
		final LocalDate max = anno < year ? LocalDate.of(anno, 12, 31) : now;
		items.removeIf(t -> t.getInizioValidita().isAfter(max) && t.getFineValidita().isBefore(max));
		return mapping(items.get(0));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isCodiceDuplicato(Long idPadre, String codice) throws BusinessException {
		return organizzazioneRepository.existsByPadreIdAndCodice(idPadre, codice);
	}

	@Override
	public void rimuoviRisorsaUmana(Long idRisorsaUmanaOrganizzazione) throws BusinessException {
		risorsaUmanaOrganizzazioneRepository.deleteById(idRisorsaUmanaOrganizzazione);

	}

	@Override
	public void associaRisorse(Long idOrganizzazione, List<Long> selezionate) throws BusinessException {
		if (selezionate == null || selezionate.isEmpty())
			return;
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione)
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione));
		for (Long ris : selezionate) {
			RisorsaUmanaOrganizzazione ele = Mapping.mapping(
					risorsaUmanaOrganizzazioneRepository.leggi(ris, idOrganizzazione, LocalDate.now(), LocalDate.now()),
					RisorsaUmanaOrganizzazione.class);
			if (ele != null)
				continue;
			try {
				RisorsaUmana r = risorsaUmanaRepository.findById(ris).orElseThrow(() -> {
					throw new BusinessException("Risorsa umana non trovata:" + ris);
				});
				ele = inizializza(Mapping.mapping(o, Organizzazione.class), Mapping.mapping(r, RisorsaUmana.class));
				aggiorna(r.getId(), r.getCodiceFiscale(), o.getId());
				ele.setDisponibilita(100);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
			Mapping.mapping(
					risorsaUmanaOrganizzazioneRepository.save(Mapping.mapping(ele, RisorsaUmanaOrganizzazione.class)),
					RisorsaUmanaOrganizzazione.class);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<QuadraturaRisorsaUmana> quadraturaRisorseUmane(Long idOrganizzazione) throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione)
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione));

		int anno = o.getAnno();
		LocalDate ini = ConsuntivoHelper.inizioAnno(anno);
		LocalDate fine = ConsuntivoHelper.fineAnno(anno);
		if (DateHelper.getAnno() == anno) {
			ini = DateHelper.now();
			fine = ini;
		} else if (DateHelper.getAnno() > anno) {
			ini = ConsuntivoHelper.fineAnno(anno);
			fine = ConsuntivoHelper.fineAnno(anno);
		}
		return quadraturaRisorseUmane(idOrganizzazione, anno, ini, fine, null);
	}

	@Override
	@Transactional(readOnly = true)
	public String maxCodice(Long idPadre) throws BusinessException {
		return organizzazioneRepository.maxCodice(idPadre);
	}

	@Override

	public void rimuoviRisorsaStrumentale(Long idRisorsaStrumentaleOrganizzazione) throws BusinessException {
		risorsaStrumentaleOrganizzazioneRepository.deleteById(idRisorsaStrumentaleOrganizzazione);

	}

	@Override
	public void associaRisorseStrumentali(Long idOrganizzazione, List<Long> selezionate) throws BusinessException {
		if (selezionate == null || selezionate.isEmpty())
			return;
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione)
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione));
		for (Long ris : selezionate) {
			RisorsaStrumentaleOrganizzazione ele = Mapping.mapping(risorsaStrumentaleOrganizzazioneRepository
					.findByRisorsaStrumentaleIdAndOrganizzazioneId(ris, idOrganizzazione),
					RisorsaStrumentaleOrganizzazione.class);
			if (ele != null)
				continue;
			try {
				RisorsaStrumentale r = risorsaStrumentaleRepository.findById(ris).orElseThrow(() -> {
					throw new BusinessException("Risorsa stumentale non trovata:" + ris);
				});
				ele = new RisorsaStrumentaleOrganizzazione();
				ele.setRisorsaStrumentale(r);
				ele.setOrganizzazione(o);
				addUtilizzo(ele);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
			risorsaStrumentaleOrganizzazioneRepository
					.save(Mapping.mapping(ele, RisorsaStrumentaleOrganizzazione.class));
		}

	}

	private void addUtilizzo(RisorsaStrumentaleOrganizzazione dto) {
		Integer utilizzo = risorsaStrumentaleOrganizzazioneRepository.utilizzo(dto.getRisorsaStrumentale().getId());
		if (utilizzo == null)
			utilizzo = 0;
		int disp = 100 - utilizzo;
		if (disp < 0)
			disp = 0;
		if (dto.getDisponibilita() == null || disp < dto.getDisponibilita())
			dto.setDisponibilita(disp);
	}

	@Override
	@Transactional(readOnly = true)
	public List<QuadraturaRisorsaStrumentale> quadraturaRisorseStrumentali(Long idOrganizzazione)
			throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione).orElse(null);

		List<Organizzazione> orgs = organizzazioneRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(o.getIdEnte(), o.getAnno(),
						o.getCodiceCompleto());
		int anno = o.getAnno();
		LocalDate now = LocalDate.now();
		Integer year = now.getYear();
		final LocalDate max = anno < year ? LocalDate.of(anno, 12, 31) : now;
		orgs.removeIf(t -> t.getInizioValidita().isAfter(max) && t.getFineValidita().isBefore(max));

		final List<Long> items = new ArrayList<>();
		orgs.forEach(t -> items.add(t.getId()));
		List<RisorsaStrumentaleOrganizzazione> risorse = Mapping.mapping(
				risorsaStrumentaleOrganizzazioneRepository.findByOrganizzazioneIdIn(items),
				RisorsaStrumentaleOrganizzazione.class);
		try {
			return new QuadraturaRisorsaStrumentaleHelper().elaboro(
					Mapping.mapping(risorse, RisorsaStrumentaleOrganizzazioneDTO.class),
					Mapping.mapping(o, OrganizzazioneDTO.class));
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<QuadraturaRisorsaUmana> quadraturaRisorseUmane(Long idOrganizzazione, String testo)
			throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione)
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione));

		int anno = o.getAnno();
		LocalDate ini = ConsuntivoHelper.inizioAnno(anno);
		LocalDate fine = ConsuntivoHelper.fineAnno(anno);
		if (DateHelper.getAnno() == anno) {
			ini = DateHelper.now();
			fine = ini;
		} else if (DateHelper.getAnno() > anno) {
			ini = ConsuntivoHelper.fineAnno(anno);
			fine = ConsuntivoHelper.fineAnno(anno);
		}
		return quadraturaRisorseUmane(idOrganizzazione, anno, ini, fine, testo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<QuadraturaRisorsaUmana> quadraturaRisorseUmane(Long idOrganizzazione, Integer anno,
			LocalDate inizioValidita, LocalDate fineValidita, String testo) throws BusinessException {
		List<Organizzazione> ramo = getRamo(idOrganizzazione);
		LocalDate now = LocalDate.now();
		Integer year = now.getYear();
		final LocalDate max = anno < year ? LocalDate.of(anno, 12, 31) : now;
		ramo.removeIf(t -> t.getInizioValidita().isAfter(max) && t.getFineValidita().isBefore(max));
		Organizzazione o = ramo.get(0);
		int oreSettimanaliMax = configBusiness.getOreMax(o.getIdEnte(), anno);
		List<Long> items = new ArrayList<>();
		ramo.forEach(t -> items.add(t.getId()));
		List<RisorsaUmana> tutteLeRisorse = null;
		if (Livello.ENTE.equals(o.getLivello())) {
			List<RisorsaUmana> listR = null;
			if (StringUtils.isBlank(testo)) {
				listR = risorsaUmanaRepository.findByIdEnteAndAnnoAndPoliticoIsFalse(o.getIdEnte(), o.getAnno());

			} else {
				listR = risorsaUmanaRepository.findByIdEnteAndAnnoAndCognomeContainsIgnoreCaseAndPoliticoIsFalse(
						o.getIdEnte(), o.getAnno(), testo);
			}
			tutteLeRisorse = Mapping.mapping(listR, RisorsaUmana.class);
		}
		List<RisorsaUmanaOrganizzazione> list = null;
		if (StringUtils.isBlank(testo)) {
			list = risorsaUmanaOrganizzazioneRepository.findByOrganizzazioneIdInAndRisorsaUmanaPolitico(items, false);

		} else {
			list = risorsaUmanaOrganizzazioneRepository
					.findByOrganizzazioneIdInAndRisorsaUmanaCognomeContainsIgnoreCaseAndRisorsaUmanaPolitico(items,
							testo, 0);
		}
		List<RisorsaUmanaOrganizzazione> risorseUO = Mapping.mapping(list, RisorsaUmanaOrganizzazione.class);

		try {
			return new QuadraturaRisorsaUmanaHelper().elaboro(Mapping.mapping(tutteLeRisorse, RisorsaUmanaDTO.class),
					Mapping.mapping(risorseUO, RisorsaUmanaOrganizzazioneDTO.class),
					Mapping.mapping(o, OrganizzazioneDTO.class), oreSettimanaliMax);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public List<Organizzazione> getRamo(Long idOrganizzazione) throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione)
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione));

		List<Organizzazione> out = organizzazioneRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(o.getIdEnte(), o.getAnno(),
						o.getCodiceCompleto());
		out.add(0, o);
		return out;
	}

	public Organizzazione getRoot(Long idEnte, Integer anno) throws BusinessException {
		List<Organizzazione> items = organizzazioneRepository.findByIdEnteAndAnnoOrderByCodiceCompleto(idEnte, anno);
		if (items == null || items.isEmpty()) {
			throw new BusinessException(HttpStatus.NOT_FOUND, "Non trovato ente per" + idEnte + "e " + anno);
		}
		return items.get(0);
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<QuadraturaRisorsaUmana> quadraturaRisorseUmaneStampe(Long idEnte, Integer anno)
			throws BusinessException {
		int oreSettimanaliMax = configBusiness.getOreMax(idEnte, anno);
		Organizzazione ente = getRoot(idEnte, anno);

		List<RisorsaUmana> tutteLeRisorse = Mapping.mapping(
				risorsaUmanaRepository.findByIdEnteAndAnnoOrderByCognomeAscNomeAsc(idEnte, anno), RisorsaUmana.class);

		List<RisorsaUmanaOrganizzazione> risorseUO = Mapping.mapping(
				risorsaUmanaOrganizzazioneRepository.findByRisorsaUmanaIdEnteAndRisorsaUmanaAnno(idEnte, anno),
				RisorsaUmanaOrganizzazione.class);

		try {
			return new QuadraturaRisorsaUmanaHelper().elaboroStampe(
					Mapping.mapping(tutteLeRisorse, RisorsaUmanaDTO.class),
					Mapping.mapping(risorseUO, RisorsaUmanaOrganizzazioneDTO.class),
					Mapping.mapping(ente, OrganizzazioneDTO.class), oreSettimanaliMax);

		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/*
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public List<RisorsaUmanaOrganizzazione>
	 * getRisorseUmaneInterne(Long idOrganizzazione, String testo) { return
	 * Mapping.mapping(risorsaUmanaOrganizzazioneRepository
	 * .findByOrganizzazioneIdAndRisorsaUmanaEsternaAndRisorsaUmanaCognomeContainsIgnoreCaseAndRisorsaUmanaPoliticoIsFalse(
	 * 
	 * idOrganizzazione, false, testo), RisorsaUmanaOrganizzazione.class); }
	 * 
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public List<RisorsaUmanaOrganizzazione>
	 * getRisorseUmaneEsterne(Long idOrganizzazione, String testo) { return
	 * Mapping.mapping(risorsaUmanaOrganizzazioneRepository
	 * .findByOrganizzazioneIdAndRisorsaUmanaEsternaAndRisorsaUmanaCognomeContainsIgnoreCaseAndRisorsaUmanaPoliticoIsFalse(
	 * idOrganizzazione, true, testo), RisorsaUmanaOrganizzazione.class); }
	 * 
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public List<RisorsaStrumentaleOrganizzazione>
	 * getRisorseStrumentali(Long idOrganizzazione, String testo) throws
	 * BusinessException { return
	 * Mapping.mapping(risorsaStrumentaleOrganizzazioneRepository
	 * .findByOrganizzazioneIdAndRisorsaStrumentaleDescrizioneContainsIgnoreCase(
	 * idOrganizzazione, testo), RisorsaStrumentaleOrganizzazione.class);
	 * 
	 * }
	 */
	@Override
	@Transactional(readOnly = true)
	public List<QuadraturaRisorsaStrumentale> quadraturaRisorseStrumentali(Long idOrganizzazione, String testo)
			throws BusinessException {
		Organizzazione organizzazione = organizzazioneRepository.findById(idOrganizzazione).orElse(null);
		List<Long> items = new ArrayList<>();
		estraiItems(organizzazione, items);

		List<RisorsaStrumentaleOrganizzazione> risorse = Mapping.mapping(
				risorsaStrumentaleOrganizzazioneRepository
						.findByOrganizzazioneIdInAndRisorsaStrumentaleDescrizioneContainsIgnoreCase(items, testo),
				RisorsaStrumentaleOrganizzazione.class);
		try {
			return new QuadraturaRisorsaStrumentaleHelper().elaboro(
					Mapping.mapping(risorse, RisorsaStrumentaleOrganizzazioneDTO.class),
					Mapping.mapping(organizzazione, OrganizzazioneDTO.class));
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<QuadraturaRisorsaStrumentale> quadraturaRisorseStrumentaliStampe(Long idEnte, Integer anno)
			throws BusinessException {
		Organizzazione organizzazione = getRoot(idEnte, anno);

		List<RisorsaStrumentaleOrganizzazione> risorse = Mapping.mapping(risorsaStrumentaleOrganizzazioneRepository
				.findByOrganizzazioneIdEnteAndOrganizzazioneAnno(idEnte, anno), RisorsaStrumentaleOrganizzazione.class);
		try {
			return new QuadraturaRisorsaStrumentaleHelper().elaboroStampe(
					Mapping.mapping(risorse, RisorsaStrumentaleOrganizzazioneDTO.class),
					Mapping.mapping(organizzazione, OrganizzazioneDTO.class));
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/*
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public List<RisorsaUmanaOrganizzazione>
	 * getAllRisorseUmane(Long idEnte, Integer anno) {
	 * 
	 * List<RisorsaUmanaOrganizzazione> list = Mapping.mapping(
	 * risorsaUmanaOrganizzazioneRepository.
	 * findByRisorsaUmanaIdEnteAndRisorsaUmanaAnno(idEnte, anno),
	 * RisorsaUmanaOrganizzazione.class);
	 * 
	 * if (list != null) Collections.sort(list);
	 * 
	 * return list; }
	 */
	@Override
	@Transactional(readOnly = true)
	public List<OrganizzazioneDTO> ramoStampa(Long idOrganizzazione) throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione)
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione));

		List<Organizzazione> items = organizzazioneRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(o.getIdEnte(), o.getAnno(),
						o.getCodiceCompleto());

		return items.stream().map(org -> Mapping.mapping(org, OrganizzazioneDTO.class)).collect(Collectors.toList());
	}

	/*
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public Organizzazione
	 * cercaPerIntestazione(Long idEnte, Integer anno, String intestazione) throws
	 * BusinessException { return
	 * Mapping.mapping(organizzazioneRepository.findByIdEnteAndAnnoAndIntestazione(
	 * idEnte, anno, intestazione), Organizzazione.class); }
	 * 
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public List<Organizzazione>
	 * cercaPerResponsabile(Long idRisorsa) throws BusinessException { RisorsaUmana
	 * resp = risorsaUmanaRepository.findById(idRisorsa) .orElseThrow(() -> new
	 * BusinessException("Responsabile non trovato:" + idRisorsa)); return
	 * Mapping.mapping(organizzazioneRepository.findByResponsabileIdAndAnno(
	 * idRisorsa, resp.getAnno()), Organizzazione.class);
	 * 
	 * }
	 * 
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public List<Organizzazione> getSettori(Long
	 * idEnte, Integer anno) throws BusinessException { Organizzazione root =
	 * getEnte(idEnte, anno); return getFigli(root.getId()); }
	 */
	@Override
	@Transactional(readOnly = true)
	public List<OrganizzazioneSmartVM> direzioni(Long idEnte, Integer anno, String testo) throws BusinessException {
		if (StringUtils.isBlank(testo))
			return Mapping.mapping(organizzazioneRepository.findByIdEnteAndAnnoAndLivelloOrderByCodiceCompleto(idEnte,
					anno, Livello.MEDIO), OrganizzazioneSmartVM.class);
		return mapping(organizzazioneRepository
				.findByIdEnteAndAnnoAndLivelloAndIntestazioneStartsWithIgnoreCaseOrderByCodiceCompleto(idEnte, anno,
						Livello.MEDIO.ordinal(), testo));
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrganizzazioneSmartVM> search(Long idEnte, Integer anno, String testo) throws BusinessException {
		if (StringUtils.isBlank(testo))
			return Mapping.mapping(organizzazioneRepository.findByIdEnteAndAnnoOrderByCodiceCompleto(idEnte, anno),
					OrganizzazioneSmartVM.class);
		return mapping(organizzazioneRepository
				.findByIdEnteAndAnnoAndIntestazioneStartsWithIgnoreCaseOrderByCodiceCompleto(idEnte, anno, testo));
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrganizzazioneSmartVM> children(Long idOrganizzazione) throws BusinessException {
		return mapping(organizzazioneRepository.findByPadreIdOrderByCodice(idOrganizzazione));
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrganizzazioneSmartVM> ramo(Long idOrganizzazione) throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione).orElseThrow(() -> {
			throw new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione);
		});

		List<Organizzazione> items = organizzazioneRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(o.getIdEnte(), o.getAnno(),
						o.getCodiceCompleto());
		return mapping(items);
	}

	@Override
	@Transactional(readOnly = true)
	public OrganizzazioneDetailVM read(Long id) throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(id).orElseThrow(() -> {
			throw new BusinessException(STRUTTURA_NON_TROVATA + id);
		});
		return mappingDetail(o);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaOrganizzazioneListVM> getRisorseAssociate(Long idOrganizzazione) throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione).orElseThrow(() -> {
			throw new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione);
		});

		return MappingRisorsaHelper.mappingItemsRisorsaUmanaOrganizzazioneToList(risorsaUmanaOrganizzazioneRepository
				.findByOrganizzazioneIdAndRisorsaUmanaPoliticoIsFalse(idOrganizzazione));
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaOrganizzazioneListVM> getRisorseAssociateAttuali(Long idOrganizzazione)
			throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione).orElseThrow(() -> {
			throw new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione);
		});

		RicercaRisorseUmaneOrg parametri = new RicercaRisorseUmaneOrg();
		parametri.setAnno(o.getAnno());
		parametri.setIdEnte(o.getIdEnte());
		parametri.setOrganizzazioni(List.of(o.getId()));
		return MappingRisorsaHelper
				.mappingItemsRisorsaUmanaOrganizzazioneToList(risorsaUmanaOrganizzazioneRepository.list(parametri));
	}

	@Override
	@Transactional(readOnly = true)
	public CreaStrutturaRequest prepareDescendant(Long idPadre) throws BusinessException {
		Organizzazione padre = organizzazioneRepository.findById(idPadre).orElseThrow(() -> {
			throw new BusinessException(STRUTTURA_NON_TROVATA + idPadre);
		});
		if (padre.getLivello().ordinal() >= Livello.values().length - 1) {
			throw new BusinessException("Non è possibile creare figli per la struttura:" + idPadre);
		}

		String codice = organizzazioneRepository.maxCodice(idPadre);
		if (StringUtils.isBlank(codice)) {
			codice = "01";
		} else {
			codice = codice.trim();
		}
		int l = codice.length();
		if (StringUtils.isNumeric(codice.trim())) {
			Integer n = Integer.valueOf(codice);
			n += 1;
			String code = Integer.toString(n);
			if (code.length() >= l) {
				codice = code;
			} else {
				codice = StringUtils.leftPad(code, l, '0');
			}
		}
		final CreaStrutturaRequest out = new CreaStrutturaRequest();
		out.setLivello(Livello.values()[padre.getLivello().ordinal()+1]);
		out.setIdPadre(padre.getId());
		out.setCodice(codice);
		out.setIdResponsabile(padre.getResponsabile() == null ? null : padre.getResponsabile().getId());
		return out;
	}

	@Override
	public void modificaDisponibilitaRisorsa(ModificaDisponibilitaRisorsaRequest request) throws BusinessException {
		RisorsaUmanaOrganizzazione ro = risorsaUmanaOrganizzazioneRepository.findById(request.getId())
				.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
						"Associazione risorsaUmana/Struttura non trovata:" + request.getId()));
		Integer utilizzo = risorsaUmanaOrganizzazioneRepository.utilizzo(ro.getRisorsaUmana().getId());
		if (utilizzo == null)
			utilizzo = 0;
		int prec = ro.getDisponibilita() == null ? 0 : ro.getDisponibilita();
		int nuovo = request.getDisponibilita() == null ? 0 : request.getDisponibilita();
		int calc = utilizzo - prec + nuovo;
		if (calc > 100) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, RISORSA_UTILIZZATA + (utilizzo - prec) + "%");
		}
		risorsaUmanaOrganizzazioneRepository.modificaDisponibilita(request.getDisponibilita(), request.getId());

	}

	@Override
	public void associaRisorsa(AssociaRisorsaOrganizzazioneRequest request) throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(request.getIdOrganizzazione())
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + request.getIdOrganizzazione()));

		RisorsaUmana r = risorsaUmanaRepository.findById(request.getIdRisorsa())
				.orElseThrow(() -> new BusinessException(RISORSA_NON_TROVATA + request.getIdRisorsa()));
		aggiorna(r.getId(), r.getCodiceFiscale(), o.getId());
		RisorsaUmanaOrganizzazione ele = new RisorsaUmanaOrganizzazione();
		ele.setOrganizzazione(o);
		ele.setRisorsaUmana(r);
		ele.setInizioValidita(LocalDate.now());
		ele.setFineValidita(DateHelper.max());
		ele.setDisponibilita(100);
		risorsaUmanaOrganizzazioneRepository.save(ele);
	}

	@Override
	public void crea(CreaStrutturaRequest request) throws BusinessException {
		Organizzazione p = organizzazioneRepository.findById(request.getIdPadre())
				.orElseThrow(() -> new BusinessException("Padre non trovato:" + request.getIdPadre()));
		if (p.getLivello().ordinal() >= Livello.values().length - 1) {
			throw new BusinessException(HttpStatus.BAD_REQUEST,
					"Non è possibile creare figli per la Struttura:" + p.getId());
		}

		RisorsaUmana r = null;
		if (request.getIdResponsabile() != null)
			r = risorsaUmanaRepository.findById(request.getIdResponsabile())
					.orElseThrow(() -> new BusinessException(RISORSA_NON_TROVATA + request.getIdResponsabile()));
		LocalDate inizio = request.getInizioValidita() == null ? LocalDate.now() : request.getInizioValidita();
		LocalDate fine = request.getFineValidita() == null ? DateHelper.MAX : request.getFineValidita();
		LocalDate oggi = DateHelper.now();
		if (!inizio.isBefore(fine) || !fine.isAfter(oggi)) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Date inzio/fine validità non valide");
		}
		Organizzazione o = new Organizzazione();
		int count = organizzazioneRepository.countByPadreId(p.getId());
		String codice = request.getCodice().replace('.', '_');
		if (organizzazioneRepository.existsByPadreIdAndCodice(p.getId(), codice)) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Codice già utilizzato per lo stesso ramo");
		}
		o.setAnno(p.getAnno());
		o.setIdEnte(p.getIdEnte());
		o.setLivello(Livello.values()[p.getLivello().ordinal() + 1]);
		o.setCodice(codice);
		o.setCodiceCompleto(p.getCodiceCompleto() + "." + codice);
		o.setOrdine(count + 1);
		o.setIntestazione(request.getIntestazione());
		o.setDescrizione(request.getDescrizione());
		o.setResponsabile(r);
		o.setCodiceInterno(request.getCodiceInterno());
		o.setPadre(p);
		o.setInizioValidita(inizio);
		o.setFineValidita(fine);
		o.setInterim(request.getInterim());
		if(o.getLivello().equals(Livello.SUPERIORE)) {
			//
		}else if(o.getLivello().equals(Livello.MEDIO)) {
			o.setTipologiaStruttura(TipologiaStruttura.DIPARTIMENTO);
		}else {
			o.setTipologiaStruttura(request.getTipologiaStruttura());
			if(TipologiaStruttura.STRUTT_COMP.equals(o.getTipologiaStruttura())) {
				o.setTipoStruttura(request.getTipoStruttura());
			}
		}
		
		organizzazioneRepository.save(o);
	}

	@Override
	public void aggiorna(AggiornaStrutturaRequest request) throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(request.getId())
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + request.getId()));
		String oldCodceCompleto = o.getCodiceCompleto();
		RisorsaUmana r = null;
		if (request.getIdResponsabile() != null)
			r = risorsaUmanaRepository.findById(request.getIdResponsabile())
					.orElseThrow(() -> new BusinessException(RISORSA_NON_TROVATA + request.getIdResponsabile()));

		String codice = request.getCodice().replace('.', '_');
		if (o.getPadre() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Struttura senza padre");
		}

		if (organizzazioneRepository.existsByPadreIdAndCodiceAndIdNot(o.getPadre().getId(), codice, o.getId())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Codice già utilizzato per lo stesso ramo");
		}
		LocalDate inizio = request.getInizioValidita() == null ? LocalDate.now() : request.getInizioValidita();
		LocalDate fine = request.getFineValidita() == null ? DateHelper.MAX : request.getFineValidita();
		LocalDate oggi = DateHelper.now();
		if (!inizio.isBefore(fine) || !fine.isAfter(oggi)) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Date inzio/fine validità non valide");
		}

		o.setCodice(codice);
		o.setCodiceCompleto(o.getPadre().getCodiceCompleto() + "." + request.getCodice());
		o.setIntestazione(request.getIntestazione());
		o.setDescrizione(request.getDescrizione());
		o.setResponsabile(r);
		o.setCodiceInterno(request.getCodiceInterno());
		o.setInizioValidita(inizio);
		o.setFineValidita(fine);
		o.setInterim(request.getInterim());
		if(o.getLivello().equals(Livello.SUPERIORE)) {
			//
		}else if(o.getLivello().equals(Livello.MEDIO)) {
			o.setTipologiaStruttura(TipologiaStruttura.DIPARTIMENTO);
		}else {
			o.setTipologiaStruttura(request.getTipologiaStruttura());
			if(TipologiaStruttura.STRUTT_COMP.equals(o.getTipologiaStruttura())) {
				o.setTipoStruttura(request.getTipoStruttura());
			}
		}
		o = organizzazioneRepository.save(o);
		if (!o.getCodiceCompleto().equals(oldCodceCompleto)) {
			organizzazioneRepository.modificaPathFigli(o.getIdEnte(), o.getAnno(), oldCodceCompleto,
					o.getCodiceCompleto());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaListVM> getRisorseAssociabili(Long idOrganizzazione, String testo)
			throws BusinessException {
		 List<RisorsaUmanaListVM> out=null;
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione)
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione));
		final LocalDate min=DateHelper.inizioAnno(o.getAnno());
		final LocalDate max=DateHelper.fineAnno(o.getAnno());
		if (StringUtils.isBlank(testo)) {
			out= MappingRisorsaHelper.mappingItemsToList(
					risorsaUmanaRepository.cercaRisorseNonAssociateOrg(o.getIdEnte(), o.getAnno(), idOrganizzazione));
		} else {
			out= MappingRisorsaHelper.mappingItemsToList(risorsaUmanaRepository.cercaRisorseNonAssociateOrgTesto(
					o.getIdEnte(), o.getAnno(), idOrganizzazione, testo.trim() + "%"));
		}
		return out.stream().filter(r->!(r.getInizioValidita().isAfter(max)|| r.getFineValidita().isBefore(min))).collect(Collectors.toList());
	}
	@Override
	@Transactional(readOnly = true)
	public OrganizzazioneSmartVM readSmart(Long idOrganizzazione) throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione)
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione));
		return mapping(o);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DipendenteVM> quadraturaRisorseUmaneRamo(Long idOrganizzazione) {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione)
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + idOrganizzazione));
		LocalDate fineval = o.getAnno() < LocalDate.now().getYear() ? LocalDate.of(o.getAnno(), 12, 31)
				: LocalDate.now().plusDays(1);
		int oreSettimanaliMax = configBusiness.getOreMax(o.getIdEnte(), o.getAnno());
		final List<DipendenteVM> out = new ArrayList<>();
		// leggo trutture ramo
		// per ogni struttura leggo dipendenti associati
		// calcolo disp
		List<Organizzazione> orgs = organizzazioneRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(o.getIdEnte(), o.getAnno(),
						o.getCodiceCompleto() + ".");
		if (orgs == null || orgs.isEmpty()) {
			orgs = new ArrayList<>();
			if (!Livello.ENTE.equals(o.getLivello()))
				orgs.add(o);
		} else {
			if (!Livello.ENTE.equals(o.getLivello()))
				orgs.add(0, o);
		}
		if (orgs.isEmpty())
			return out;
		final List<Long> ids = new ArrayList<>();
		final Map<Long, DipendenteVM> mapD = new HashMap<>();
		final Map<String, Organizzazione> mapS = new HashMap<>();
		orgs.forEach(c -> {
			ids.add(c.getId());
			mapS.put(c.getCodiceCompleto(), c);
		});
		List<RisorsaUmanaOrganizzazione> risorse = risorsaUmanaOrganizzazioneRepository
				.findByOrganizzazioneIdInAndRisorsaUmanaPoliticoOrderByRisorsaUmanaCognomeAscRisorsaUmanaNomeAsc(ids,
						false);
		if (risorse != null)
			risorse.removeIf(r -> r.getFineValidita() == null || r.getFineValidita().isBefore(fineval)
					|| r.getInizioValidita() == null || r.getInizioValidita().isAfter(fineval)
					|| (r.getRisorsaUmana().getFineValidita() != null
							&& r.getRisorsaUmana().getFineValidita().isBefore(fineval))
					|| (r.getRisorsaUmana().getInizioValidita() != null
							&& r.getRisorsaUmana().getInizioValidita().isAfter(fineval)));

		if (risorse == null || risorse.isEmpty())
			return out;

		final Map<String, DisponibilitaStrutturaVM> mapDS = new HashMap<>();
		for (RisorsaUmanaOrganizzazione ro : risorse) {
			DipendenteVM r = mapD.get(ro.getRisorsaUmana().getId());
			if (r == null) {
				r = new DipendenteVM(ro.getRisorsaUmana());
				mapD.put(r.getId(), r);
				out.add(r);
			}
			Organizzazione org = null;
			DisponibilitaStrutturaVM ds = null;
			String[] codici = ro.getOrganizzazione().getCodiceCompleto().split("\\.");
			List<DisponibilitaStrutturaVM> ramo = new ArrayList<>();
			if (codici != null && codici.length > 1) {
				String c = codici[0];
				for (int i = 1; i < codici.length - 1; i++) {
					c = c + "." + codici[i];
					org = mapS.get(c);
					if (org == null) {
						org = organizzazioneRepository.findByIdEnteAndAnnoAndCodiceCompleto(o.getIdEnte(), o.getAnno(),
								c);
						if (org == null) {
							throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,
									"Non trovato Struttura con codice completo:" + c);
						}
						mapS.put(c, org);
					}
					ds = mapDS.get(r.getId() + "/" + org.getId());
					if (ds == null) {
						ds = new DisponibilitaStrutturaVM(org);
						mapDS.put(r.getId() + "/" + org.getId(), ds);
						r.getDisponibilita().add(ds);
					}
					ramo.add(ds);
				}
			}
			org = ro.getOrganizzazione();
			ds = mapDS.get(r.getId() + "/" + org.getId());
			if (ds == null) {
				ds = new DisponibilitaStrutturaVM(org);
				mapDS.put(r.getId() + "/" + org.getId(), ds);
				r.getDisponibilita().add(ds);
				ramo.add(ds);
			}
			float contributo = ro.getDisponibilita() == null ? 0 : ((float) ro.getDisponibilita() / 100f);
			float contributoTemporale = getEffettivaTemporale(ro.getRisorsaUmana(), oreSettimanaliMax);
			float contributoEffettivo = getEffettivaFinale(contributo, contributoTemporale * 100f);
			for (DisponibilitaStrutturaVM d : ramo) {
				d.setContributo(d.getContributo() + contributo);
				d.setContributoTemporale(contributoTemporale);
				d.setContributoEffettivo(d.getContributoEffettivo() + contributoEffettivo);
				if (d.getContributoEffettivo() < 1) {
					d.setUtilizzo(Over.UTILIZZO_PARZIALE);
				} else {
					d.setUtilizzo(Over.OK);
				}
			}
			r.setContributo(r.getContributo() + contributo);
			r.setContributoTemporale(contributoTemporale);
			r.setContributoEffettivo(r.getContributoEffettivo() + contributoEffettivo);
			if (r.getContributoEffettivo() < 1) {
				r.setUtilizzo(Over.UTILIZZO_PARZIALE);
			} else {
				r.setUtilizzo(Over.OK);
			}
			r.setInizioValidita(ro.getInizioValidita());
			r.setFineValidita(ro.getFineValidita());
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrganizzazioneSmartVM> LivelloSuperiore(Long idEnte, Integer anno) throws BusinessException {
		List<Organizzazione> items = organizzazioneRepository.findByIdEnteAndAnnoAndLivelloOrderByCodiceCompleto(idEnte,
				anno, Livello.SUPERIORE);
		return Mapping.mapping(items, OrganizzazioneSmartVM.class);
	}

	float getEffettivaTemporale(RisorsaUmana risorsa, int oreSettimanaliMax) {
		if (oreSettimanaliMax == 0)
			return 0;
		int percentualeTempo = 100;
		int mesi = risorsa.getMesi() == null ? 12 : risorsa.getMesi();
		boolean part = Boolean.TRUE.equals(risorsa.getPartTime());
		if (part) {
			int ore = risorsa.getOrePartTime() == null ? 0 : risorsa.getOrePartTime();
			percentualeTempo = (ore * 100) / oreSettimanaliMax;
		}
		float perc = (float) mesi / (float) 12 * (float) percentualeTempo;
		return perc / 100f;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DecodificaVM> getResponsabiliAzione(Long idOrganizzazione, String testo) throws BusinessException {
		List<DecodificaVM> out = new ArrayList<>();
		List<RisorsaUmanaOrganizzazione> items = null;
		if (StringUtils.isBlank(testo)) {
			items = risorsaUmanaOrganizzazioneRepository.findByOrganizzazioneId(idOrganizzazione);
		} else {
			items = risorsaUmanaOrganizzazioneRepository
					.findByOrganizzazioneIdInAndRisorsaUmanaCognomeContainsIgnoreCaseAndRisorsaUmanaPolitico(
							List.of(idOrganizzazione), testo, 0);
		}
		if (items != null) {
			out = items.stream()
					.map(r -> new DecodificaVM(r.getRisorsaUmana().getId(), r.getRisorsaUmana().getCodiceFiscale(),
							r.getRisorsaUmana().getCognome() + " " + r.getRisorsaUmana().getNome()))
					.collect(Collectors.toList());
		}
		out.sort(new Comparator<DecodificaVM>() {

			@Override
			public int compare(DecodificaVM o1, DecodificaVM o2) {
				return o1.getDescrizione().compareTo(o2.getDescrizione());
			}

		});
		return out;
	}

	float getEffettivaFinale(float disponibilitaTotale, float effettivaTemporale) {
		return effettivaTemporale * disponibilitaTotale / 100f;
	}

	private RisorsaUmanaOrganizzazione inizializza(Organizzazione organizzazione, RisorsaUmana risorsaUmana) {
		RisorsaUmanaOrganizzazione ele = new RisorsaUmanaOrganizzazione();
		ele.setOrganizzazione(organizzazione);
		ele.setRisorsaUmana(risorsaUmana);
		ele.setInizioValidita(LocalDate.now());
		ele.setFineValidita(DateHelper.max());
		return ele;
	}

	private List<OrganizzazioneSmartVM> mapping(List<Organizzazione> items) {
		final List<OrganizzazioneSmartVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(o -> out.add(mapping(o)));
		return out;
	}

	private OrganizzazioneSmartVM mapping(Organizzazione o) {
		return Mapping.mapping(o, OrganizzazioneSmartVM.class)
				.nomeResponsabile(RisorsaUmanaHelper.getCognomeNomeMatricola(o.getResponsabile()))
				.idPadre(o.getPadre() == null ? null : o.getPadre().getId());
	}

	private OrganizzazioneDetailVM mappingDetail(Organizzazione entity) {
		if (entity == null)
			return null;
		final OrganizzazioneDetailVM out = Mapping.mapping(entity, OrganizzazioneDetailVM.class);
		out.setIdPadre(entity.getPadre() == null ? null : entity.getPadre().getId());
		out.setCodiceCompletoPadre(entity.getPadre() == null ? null : entity.getPadre().getCodiceCompleto());
		out.setResponsabile(Mapping.mapping(entity.getResponsabile(), PersonaVM.class));
		out.setCodice(entity.getCodice());
		out.setCodiceRidotto(OrganizzazioneHelper.ridotto(out.getCodiceCompleto()));
		out.setInizioValidita(entity.getInizioValidita());
		out.setFineValidita(entity.getFineValidita());
		out.setInterim(entity.getInterim());
		out.setTipologiaStruttura(entity.getTipologiaStruttura()==null?null:new DecodificaEnumVM<TipologiaStruttura>(entity.getTipologiaStruttura()));
		out.setTipoStruttura(entity.getTipoStruttura()==null?null:new DecodificaEnumVM<TipoStruttura>(entity.getTipoStruttura()));

		return out;
	}

	private void riempi(final Organizzazione root, final List<Organizzazione> items) {
		final Map<Long, OrganizzazioneDTO> map = new HashMap<>();
		map.put(root.getId(), Mapping.mapping(root, OrganizzazioneDTO.class));
		if (items == null) {
			return;
		}
		items.stream().filter(np -> !np.getId().equals(root.getId()) && np.getPadre() != null).forEach(np

		-> aggiungiOrg(np, map));
	}

	private void aggiungiOrg(final Organizzazione o, final Map<Long, OrganizzazioneDTO> map) {

		OrganizzazioneDTO p = null;
		if (o.getPadre() != null) {
			p = map.get(o.getPadre().getId());
			if (p == null) {
				p = Mapping.mapping(o.getPadre(), OrganizzazioneDTO.class);
				map.put(o.getPadre().getId(), p);
			}
		}
		OrganizzazioneDTO d = map.get(o.getId());
		if (d == null) {
			d = Mapping.mapping(o.getPadre(), OrganizzazioneDTO.class);
			map.put(o.getId(), d);
			if (p != null)
				p.add(d);
		}
	}

	/*
	 * organizzazioni ammesse dal padre e valide per il tipoNodo come principali o
	 * coinvolte
	 */
	private List<OrganizzazioneDTO> getOrganizzazioni(NodoPiano nodoPiano, boolean principale)
			throws BusinessException {
		List<OrganizzazioneDTO> out = new ArrayList<>();
		List<Livello> livelli = new ArrayList<>();
		List<Organizzazione> aree = new ArrayList<>();
		ConfigurazioneDTO config = configBusiness.leggiPerIdEnteAnno(nodoPiano.getIdEnte(), nodoPiano.getAnno());
		Livello livelloMax = config.getMaxLivello();
		Long idEnte = nodoPiano.getIdEnte();
		if (livelloMax == null)
			livelloMax = Livello.INFERIORE;
		switch (nodoPiano.getTipoNodo()) {
		case PIANO:
//		case PROGRAMMA:
			throw new BusinessException("Livello non gestito per funzione getOrganizzazioni()");
		case OBIETTIVO:
			if (Livello.SUPERIORE.ordinal() >= Livello.SUPERIORE.ordinal() - 1
					&& Livello.SUPERIORE.ordinal() <= livelloMax.ordinal())
				livelli.add(Livello.SUPERIORE);
			if (Livello.MEDIO.ordinal() >= Livello.SUPERIORE.ordinal() - 1
					&& Livello.MEDIO.ordinal() <= livelloMax.ordinal())
				livelli.add(Livello.MEDIO);
			break;
		case AZIONE:
			if (Livello.SUPERIORE.ordinal() < livelloMax.ordinal()) {
				for (int index = Livello.SUPERIORE.ordinal(); index < livelloMax.ordinal(); index++) {
					livelli.add((Livello) ModelHelper.int2Enum(index, Livello.class));
				}
			}
			livelli.add(livelloMax);
			break;
		}
		if (nodoPiano.getPadre() != null) {
			NodoPiano padre = nodoPiano.getPadre();
//			if (!principale)
//				aree = padre.getOrganizzazioni();
//			else {
			aree = new ArrayList<>();
			aree.add(padre.getOrganizzazione());

//			}
		}

		if (aree == null) {
			aree = new ArrayList<>();
			aree.add(getRoot(nodoPiano.getIdEnte(), nodoPiano.getAnno()));
		}

		List<Long> doppie = new ArrayList<>();
		for (Organizzazione a : aree) {
			List<Organizzazione> ramo = getRamo(a.getId());
			estraiUo(ramo, out, livelli, doppie);
		}
		out.sort(Comparator.naturalOrder());
		return out;
	}

	private void estraiUo(List<Organizzazione> orgs, List<OrganizzazioneDTO> items, List<Livello> livelli,
			List<Long> doppie) {
		if (orgs == null)
			return;
		for (Organizzazione o : orgs) {
			OrganizzazioneDTO dto = Mapping.mapping(o, OrganizzazioneDTO.class);
			if (LivelloContenuto(dto, livelli)) {
				if (doppie.contains(o.getId())) {
					continue;
				}
				items.add(dto);
				doppie.add(dto.getId());
			}
		}
	}

	private boolean LivelloContenuto(OrganizzazioneDTO organizzazione, List<Livello> livelli) {
		for (Livello livello : livelli) {
			if (organizzazione.getLivello() == livello)
				return true;
		}
		return false;
	}

	private void estraiItems(Organizzazione organizzazione, List<Long> items) {
		if (organizzazione == null)
			return;
		items.add(organizzazione.getId());
		List<Organizzazione> figli = organizzazioneRepository.findByPadreIdOrderByCodice(organizzazione.getId());
		if (figli == null || figli.isEmpty())
			return;
		for (Organizzazione uo : figli) {
			estraiItems(uo, items);
		}
	}

	private void aggiorna(Long idRisorsa, String codiceFiscale, Long idOrg) throws BusinessException {
		final LocalDate oggi = LocalDate.now();
		List<RisorsaUmanaOrganizzazione> items = risorsaUmanaOrganizzazioneRepository
				.findByRisorsaUmanaCodiceFiscaleOrderByInizioValidita(codiceFiscale);
		for (RisorsaUmanaOrganizzazione ro : items) {
			if (!ro.getFineValidita().isBefore(oggi)) {
				if (ro.getRisorsaUmana().getId().equals(idRisorsa) && ro.getOrganizzazione().getId().equals(idOrg)) {
					throw new BusinessException(HttpStatus.BAD_REQUEST, "Elemento esistente");
				} else {
					ro.setFineValidita(oggi.minusDays(1));
					if (ro.getInizioValidita().isBefore(ro.getFineValidita())) {
						ro.setInizioValidita(ro.getFineValidita());
					}
					risorsaUmanaOrganizzazioneRepository.save(ro);
				}
			}
		}

	}

	@Override
	public Page<DipendenteVM> getDipendenti(Long idStruttura, String cognome, Pageable pageable)
			throws BusinessException {

		return getDipendenti(idStruttura, cognome, null, pageable);

	}

	@Override
	public Page<DipendenteVM> getDipendenti(Long idStruttura, String cognome, List<Long> strutture, Pageable pageable)
			throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idStruttura)
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + idStruttura));
		final int oreSettimanaliMax = configBusiness.getOreMax(o.getIdEnte(), o.getAnno());
		Page<RisorsaUmanaOrganizzazione> page = null;
		final List<Long> orgs = new ArrayList<>();
		if (strutture == null || strutture.isEmpty()) {
			if (!Livello.ENTE.equals(o.getLivello())) {
				if (!Livello.ENTE.equals(o.getLivello())) {
					orgs.add(o.getId());
				}
//				final List<Long> items = organizzazioneRepository
//						.findIdByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(o.getIdEnte(),
//								o.getAnno(), o.getCodiceCompleto() + ".%");
//				if (items != null && !items.isEmpty()) {
//					for (Long org : items) {
//						orgs.add(org);
//					}
//				}
			}
		} else {
			orgs.addAll(strutture);
			for (Long idS : strutture) {
				final List<Long> items = organizzazioneRepository.findRamoById(idS);
				if (items != null && !items.isEmpty()) {
					for (Long org : items) {
						if (!orgs.contains(org))
							orgs.add(org);
					}
				}
			}
			if (!Livello.ENTE.equals(o.getLivello())) {
				if (orgs.contains(o.getId())) {
					orgs.clear();
					orgs.add(o.getId());
				} else {
					return new PageImpl<DipendenteVM>(new ArrayList<>(),Pageable.ofSize(10),0);
				}
			}
		}
		final RicercaRisorseUmaneOrg parametri = new RicercaRisorseUmaneOrg();
		parametri.setIdEnte(o.getIdEnte());
		parametri.setAnno(o.getAnno());
		parametri.setTestoRicerca(cognome);
		parametri.setOrganizzazioni(orgs);
		parametri.setPolitico(false);

		page = risorsaUmanaOrganizzazioneRepository.search(parametri, pageable);
		Page<DipendenteVM> out = page.map(new Function<RisorsaUmanaOrganizzazione, DipendenteVM>() {
			@Override
			public DipendenteVM apply(RisorsaUmanaOrganizzazione ro) {
				return map(ro, oreSettimanaliMax);
			}
		});
		return out;

	}

	private DipendenteVM map(RisorsaUmanaOrganizzazione ro, int oreSettimanaliMax) {

		final DipendenteVM r = new DipendenteVM(ro);

		float contributo = ro.getDisponibilita() == null ? 0 : ((float) ro.getDisponibilita() / 100f);
		float contributoTemporale = getEffettivaTemporale(ro.getRisorsaUmana(), oreSettimanaliMax);
		float contributoEffettivo = getEffettivaFinale(contributo, contributoTemporale * 100f);
		final DisponibilitaStrutturaVM d = r.getDisponibilita().get(0);
		d.setContributo(contributo);
		d.setContributoTemporale(contributoTemporale);
		d.setContributoEffettivo(contributoEffettivo);
		if (d.getContributoEffettivo() < 1) {
			d.setUtilizzo(Over.UTILIZZO_PARZIALE);
		} else {
			d.setUtilizzo(Over.OK);
		}

		r.setContributo(r.getContributo() + contributo);
		r.setContributoTemporale(contributoTemporale);
		r.setContributoEffettivo(r.getContributoEffettivo() + contributoEffettivo);
		if (r.getContributoEffettivo() < 1) {
			r.setUtilizzo(Over.UTILIZZO_PARZIALE);
		} else {
			r.setUtilizzo(Over.OK);
		}
		r.setInizioValidita(ro.getInizioValidita());
		r.setFineValidita(ro.getFineValidita());
		return r;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> ramoId(Long id) throws BusinessException {
		List<Long> out = new ArrayList<>();
		out.add(id);
		List<Long> items = organizzazioneRepository.findRamoById(id);
		if (items != null)
			out.addAll(items);
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> ramoCompletoId(Long id) throws BusinessException {
		List<Long> out = new ArrayList<>();
		Organizzazione o = organizzazioneRepository.findById(id)
				.orElseThrow(() -> new BusinessException(STRUTTURA_NON_TROVATA + id));
		String[] a = o.getCodiceCompleto().split("\\.");
		List<Long> items = null;
		String code = a[0];
		Long isS = organizzazioneRepository.findIdByIdEnteAndAnnoAndCodiceCompleto(o.getIdEnte(), o.getAnno(), code);
		out.add(isS);
		for (int i = 1; i < a.length; i++) {
			code = code + "." + a[i];
			isS = organizzazioneRepository.findIdByIdEnteAndAnnoAndCodiceCompleto(o.getIdEnte(), o.getAnno(), code);
			out.add(isS);
		}
		items = organizzazioneRepository.findRamoById(id);

		if (items != null)
			out.addAll(items);
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> codiciCompleti(List<Long> ids) throws BusinessException {
		List<String> out = new ArrayList<>();
		if (ids == null || ids.isEmpty())
			return out;
		out = organizzazioneRepository.findCodiceCompletoByInId(ids);
		return out;
	}
}
