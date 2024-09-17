package com.finconsgroup.performplus.service.business.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.CategoriaRisorsaUmana;
import com.finconsgroup.performplus.domain.ContrattoRisorsaUmana;
import com.finconsgroup.performplus.domain.Incarico;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.ProfiloRisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaOrganizzazione;
import com.finconsgroup.performplus.enumeration.TipoFunzione;
import com.finconsgroup.performplus.repository.CategoriaRisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.ContrattoRisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.IncaricoRepository;
import com.finconsgroup.performplus.repository.OrganizzazioneRepository;
import com.finconsgroup.performplus.repository.ProfiloRisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaOrganizzazioneRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaRepository;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.rest.api.vm.RisorsaUmanaSmartVM;
import com.finconsgroup.performplus.rest.api.vm.v2.AggiornaRisorsaUmanaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.CreaRisorsaUmanaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AggiornaAmministratoreRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AmministratoreDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AmministratoreListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.CreaAmministratoreRequest;
import com.finconsgroup.performplus.service.business.IRisorsaUmanaBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.MappingRisorsaHelper;
import com.finconsgroup.performplus.service.dto.RicercaRisorseUmane;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaDTO;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaOrganizzazioneDTO;

@Service
@Transactional
public class RisorsaUmanaBusiness implements IRisorsaUmanaBusiness {
	private static final String RISORSA_NON_TROVATA = "Risorsa non trovata:";
	private static final String ORGANIZZAZIONE_NON_TROVATA = "Organizzazione non trovata:";
	private static final String AMMINISTRATORE_ESISTENTE_CON_NOME_COGNOME = "Amministratore già esistente con questi nome cognome";
	private static final String ELEMENTO_ESISTENTE_CON_CODICE_INTERNO = "Elemento già esistente con questo codice interno";
	private static final String ELEMENTO_ESISTENTE_CON_CODICE_FISCALE = "Elemento già esistente con questo codice fiscale";
	private static final String RISORSA_ESISTENTE_CON_NOME_COGNOME = "Risorsa già esistente con questi nome cognome";
	@Autowired
	private RisorsaUmanaRepository risorsaUmanaRepository;
	@Autowired
	private ContrattoRisorsaUmanaRepository contrattoRisorsaUmanaRepository;
	@Autowired
	private CategoriaRisorsaUmanaRepository categoriaRisorsaUmanaRepository;
	@Autowired
	private ProfiloRisorsaUmanaRepository profiloRisorsaUmanaRepository;
	@Autowired
	private RisorsaUmanaOrganizzazioneRepository risorsaUmanaOrganizzazioneRepository;

	@Autowired
	private OrganizzazioneRepository organizzazioneRepository;

	@Autowired
	private IncaricoRepository incaricoRepository;

	@Override
	@Transactional(readOnly = true)
	public RisorsaUmanaDTO getRisorsaUmana(Long idEnte, Integer anno, String codiceInterno) throws BusinessException {
		return Mapping.mapping(risorsaUmanaRepository.findByIdEnteAndAnnoAndCodiceInterno(idEnte, anno, codiceInterno),
				RisorsaUmanaDTO.class);
	}

	@Override
	public RisorsaUmanaDTO crea(RisorsaUmanaDTO dto) throws BusinessException {
		int maxOrdine = risorsaUmanaRepository.maxOrdineByIdEnteAndAnnoAndDisponibile(dto.getIdEnte(), dto.getAnno(),
				dto.getDisponibile());
		dto.setOrdine(maxOrdine + 1);
		dto.setId(null);
		return Mapping.mapping(risorsaUmanaRepository.save(Mapping.mapping(dto, RisorsaUmana.class)),
				RisorsaUmanaDTO.class);
	}

	@Override
	public RisorsaUmanaDTO aggiorna(RisorsaUmanaDTO dto) throws BusinessException {
		return Mapping.mapping(risorsaUmanaRepository.save(Mapping.mapping(dto, RisorsaUmana.class)),
				RisorsaUmanaDTO.class);
	}

	@Override
	public void elimina(RisorsaUmanaDTO dto) throws BusinessException {
		risorsaUmanaRepository.deleteById(dto.getId());
	}

	@Override

	public void elimina(Long id) throws BusinessException {

		risorsaUmanaRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public RisorsaUmanaDTO leggi(Long id) throws BusinessException {
		return Mapping.mapping(risorsaUmanaRepository.findById(id), RisorsaUmanaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> cerca(RisorsaUmanaDTO parametri) throws BusinessException {
		return Mapping.mapping(
				risorsaUmanaRepository.findAll(Example.of(Mapping.mapping(parametri, RisorsaUmana.class))),
				RisorsaUmanaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> list(Long idEnte, Integer anno) throws BusinessException {
		return Mapping.mapping(risorsaUmanaRepository.findByIdEnteAndAnno(idEnte, anno), RisorsaUmanaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public int count(Long idEnte, Integer anno) throws BusinessException {
		return risorsaUmanaRepository.countByIdEnteAndAnno(idEnte, anno);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> getRisorseUmane(Long idEnte, Integer anno) throws BusinessException {
		return Mapping.mapping(risorsaUmanaRepository.findByIdEnteAndAnnoAndPolitico(idEnte, anno, false),
				RisorsaUmanaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> getRisorsePolitiche(Long idEnte, Integer anno) throws BusinessException {
		return Mapping.mapping(risorsaUmanaRepository.findByIdEnteAndAnnoAndPolitico(idEnte, anno, true),
				RisorsaUmanaDTO.class);
	}




	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> cercoPerOrganizzazione(Long idOrganizzazione, Boolean esterne)
			throws BusinessException {
		final List<RisorsaUmanaDTO> out = new ArrayList<>();
		List<RisorsaUmanaOrganizzazione> items = risorsaUmanaOrganizzazioneRepository
				.findByOrganizzazioneIdAndRisorsaUmanaEsternaAndRisorsaUmanaPoliticoIsFalse(idOrganizzazione, esterne);
		List<Long> doppi = new ArrayList<>();
		if (items != null) {
			for (RisorsaUmanaOrganizzazione ro : items) {
				if (doppi.contains(ro.getRisorsaUmana().getId())) {
					continue;
				}
				doppi.add(ro.getOrganizzazione().getId());
				out.add(Mapping.mapping(ro.getRisorsaUmana(), RisorsaUmanaDTO.class));
			}
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> getRisorseUmaneDiAlberoUo(Long idOrganizzazione) throws BusinessException {
		final List<RisorsaUmanaDTO> out = new ArrayList<>();
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione).orElseThrow(() -> {
			throw new BusinessException(ORGANIZZAZIONE_NON_TROVATA + idOrganizzazione);
		});
		List<Long> ids = new ArrayList<>();
		ids.add(o.getId());
		List<Organizzazione> orgs = organizzazioneRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(idOrganizzazione, o.getAnno(),
						o.getCodiceCompleto());
		if (orgs != null) {
			for (Organizzazione c : orgs) {
				ids.add(c.getId());
			}
		}
		List<RisorsaUmanaOrganizzazione> items = risorsaUmanaOrganizzazioneRepository
				.findByOrganizzazioneIdInAndRisorsaUmanaPoliticoIsFalse(ids);
		List<Long> doppi = new ArrayList<>();
		if (items != null) {
			for (RisorsaUmanaOrganizzazione ro : items) {
				if (doppi.contains(ro.getRisorsaUmana().getId())) {
					continue;
				}
				doppi.add(ro.getOrganizzazione().getId());
				out.add(Mapping.mapping(ro.getRisorsaUmana(), RisorsaUmanaDTO.class));
			}
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> getRisorseUmane(Long idEnte, Integer anno, boolean esterne) throws BusinessException {
		return Mapping.mapping(
				risorsaUmanaRepository.findByIdEnteAndAnnoAndEsternaAndPoliticoIsFalse(idEnte, anno, esterne),
				RisorsaUmanaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> cercaNonAssociate(Long idOrganizzazione, Boolean esterne) throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione).orElseThrow(() -> {
			throw new BusinessException(ORGANIZZAZIONE_NON_TROVATA + idOrganizzazione);
		});
		return Mapping.mapping(
				risorsaUmanaRepository.cercaNonAssociateOrgEsterne(o.getIdEnte(), o.getAnno(), o.getId(), esterne),
				RisorsaUmanaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> cercaNonAssociate(Long idOrganizzazione, Boolean esterne, String testo)
			throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione).orElseThrow(() -> {
			throw new BusinessException(ORGANIZZAZIONE_NON_TROVATA + idOrganizzazione);
		});
		return Mapping.mapping(risorsaUmanaRepository.cercaNonAssociateOrgEsterneTesto(o.getIdEnte(), o.getAnno(),
				o.getId(), esterne, testo.trim() + "%"), RisorsaUmanaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> getRisorseUmane(Long idEnte, Integer anno, Boolean esterne, String testo)
			throws BusinessException {
		return Mapping.mapping(
				risorsaUmanaRepository.findByIdEnteAndAnnoAndEsternaAndPoliticoIsFalseAndCognomeContainsIgnoreCase(
						idEnte, anno, 0, testo),
				RisorsaUmanaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> getRisorsePolitiche(Long idEnte, Integer anno, String testo) throws BusinessException {
		return Mapping.mapping(risorsaUmanaRepository.findByIdEnteAndAnnoAndPoliticoIsTrueAndCognomeContainsIgnoreCase(
				idEnte, anno, testo), RisorsaUmanaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public RisorsaUmanaDTO getSindaco(Long idEnte, Integer anno) throws BusinessException {

		List<RisorsaUmanaDTO> list = Mapping.mapping(
				risorsaUmanaRepository.findByIdEnteAndAnnoAndFunzione(idEnte, 0, TipoFunzione.SINDACO),
				RisorsaUmanaDTO.class);

		if (list == null)
			return null;
		if (list.size() != 1)
			throw new BusinessException("Sindaco non trovato");

		return list.get(0);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaOrganizzazioneDTO> getRisorseUmaneOrganizzazione(Long idEnte, Integer anno) {

		return Mapping.mapping(
				risorsaUmanaOrganizzazioneRepository.findByRisorsaUmanaIdEnteAndRisorsaUmanaAnno(idEnte, anno),
				RisorsaUmanaOrganizzazioneDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaOrganizzazioneDTO> getRisorseUmaneRamo(Long idOrganizzazione) throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione).orElseThrow(() -> {
			throw new BusinessException(ORGANIZZAZIONE_NON_TROVATA + idOrganizzazione);
		});

		List<Long> ids = new ArrayList<>();
		ids.add(o.getId());
		List<Organizzazione> orgs = organizzazioneRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(o.getIdEnte(), o.getAnno(),
						o.getCodiceCompleto());
		if (orgs != null) {
			for (Organizzazione c : orgs) {
				ids.add(c.getId());
			}
		}
		return Mapping.mapping(
				risorsaUmanaOrganizzazioneRepository.findByOrganizzazioneIdInAndRisorsaUmanaPoliticoIsFalse(ids),
				RisorsaUmanaOrganizzazioneDTO.class);

	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaDTO> cercaPerCognome(Long idEnte, Integer anno, String testo) throws BusinessException {
		return Mapping.mapping(
				risorsaUmanaRepository.findByIdEnteAndAnnoAndCognomeContainsIgnoreCase(idEnte, anno, testo),
				RisorsaUmanaDTO.class);

	}

	@Override
	@Transactional(readOnly = true)
	public RisorsaUmanaDTO cercaPerCodiceInterno(Long idEnte, Integer anno, String codiceInterno)
			throws BusinessException {
		return Mapping.mapping(risorsaUmanaRepository.findByIdEnteAndAnnoAndCodiceInterno(idEnte, anno, codiceInterno),
				RisorsaUmanaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaSmartVM> search(Long idEnte, Integer anno, boolean politico, String testo)
			throws BusinessException {
		if (StringUtils.isBlank(testo))
			return Mapping.mapping(risorsaUmanaRepository.findByIdEnteAndAnnoAndPoliticoOrderByCognomeAscNomeAsc(idEnte,
					anno, politico), RisorsaUmanaSmartVM.class);
		return Mapping.mapping(organizzazioneRepository
				.findByIdEnteAndAnnoAndIntestazioneStartsWithIgnoreCaseOrderByCodiceCompleto(idEnte, anno, testo),
				RisorsaUmanaSmartVM.class);
	}

	@Override
	public RisorsaUmanaDetailVM creaRisorsaUmana(CreaRisorsaUmanaRequest request) throws BusinessException {
		LocalDate oggi=LocalDate.now();
		LocalDate inizio = request.getInizioValidita()==null?oggi:request.getInizioValidita();
		LocalDate fine=request.getFineValidita()==null?LocalDate.of(9999, 12, 31):request.getFineValidita();
		if(!inizio.isBefore(fine) || !fine.isAfter(oggi)) {
			throw new BusinessException(HttpStatus.BAD_REQUEST,"Date inzio/fine validità non valide");
		}
		if (StringUtils.isNotBlank(request.getCodiceInterno())
				&& risorsaUmanaRepository.existsByIdEnteAndAnnoAndCodiceInterno(request.getIdEnte(), request.getAnno(),
						request.getCodiceInterno())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, ELEMENTO_ESISTENTE_CON_CODICE_INTERNO);
		}
		if (StringUtils.isNotBlank(request.getCodiceFiscale())
				&& risorsaUmanaRepository.existsByIdEnteAndAnnoAndCodiceFiscale(request.getIdEnte(), request.getAnno(),
						request.getCodiceFiscale())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, ELEMENTO_ESISTENTE_CON_CODICE_FISCALE);
		}
		if (risorsaUmanaRepository.existsByIdEnteAndAnnoAndCodiceInternoAndCognomeAndNomeAndPolitico(
				request.getIdEnte(), request.getAnno(), request.getCodiceInterno(), request.getCognome(),
				request.getNome(), false)) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, RISORSA_ESISTENTE_CON_NOME_COGNOME);
		}
		RisorsaUmana r = risorsaUmanaRepository.save(MappingRisorsaHelper.mappingFromRequest(request,
				categoriaRisorsaUmanaRepository, profiloRisorsaUmanaRepository, contrattoRisorsaUmanaRepository
				, incaricoRepository));
		return MappingRisorsaHelper.mappingToDetail(r);
	}

	@Override
	public void aggiornaRisorsaUmana(Long id, AggiornaRisorsaUmanaRequest request) throws BusinessException {
		LocalDate oggi=LocalDate.now();
		LocalDate inizio = request.getInizioValidita()==null?oggi:request.getInizioValidita();
		LocalDate fine=request.getFineValidita()==null?LocalDate.of(9999, 12, 31):request.getFineValidita();
		if(!inizio.isBefore(fine) || !fine.isAfter(oggi)) {
			throw new BusinessException(HttpStatus.BAD_REQUEST,"Date inzio/fine validità non valide");
		}
		RisorsaUmana r1 = null;
		if (StringUtils.isNotBlank(request.getCodiceInterno())) {
			r1 = risorsaUmanaRepository.findByIdEnteAndAnnoAndCodiceInterno(request.getIdEnte(), request.getAnno(),
					request.getCodiceInterno());
			if (r1 != null && !r1.getId().equals(id)) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, ELEMENTO_ESISTENTE_CON_CODICE_INTERNO);

			}
		}
		if (StringUtils.isNotBlank(request.getCodiceFiscale())) {
			r1 = risorsaUmanaRepository.findTopByIdEnteAndAnnoAndCodiceFiscaleOrderByInizioValiditaDescFineValiditaDesc(request.getIdEnte(), request.getAnno(),
					request.getCodiceFiscale());
			if (r1 != null && !r1.getId().equals(id)) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, ELEMENTO_ESISTENTE_CON_CODICE_FISCALE);

			}
		}
		r1 = risorsaUmanaRepository.findByIdEnteAndAnnoAndCodiceInternoAndCognomeAndNomeAndPolitico(request.getIdEnte(),
				request.getAnno(), request.getCodiceInterno(), request.getCognome(), request.getNome(), false);
		if (r1 != null && !r1.getId().equals(id)) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, RISORSA_ESISTENTE_CON_NOME_COGNOME);

		}
		request.setInizioValidita(inizio);
		request.setFineValidita(fine);
		RisorsaUmana r = MappingRisorsaHelper.mappingFromRequest(request, categoriaRisorsaUmanaRepository,
				profiloRisorsaUmanaRepository, contrattoRisorsaUmanaRepository, incaricoRepository);
		r.setId(id);
		risorsaUmanaRepository.save(r);
	}

	@Override
	@Transactional(readOnly = true)
	public RisorsaUmanaDetailVM leggiRisorsaUmana(Long id) {
		RisorsaUmana r = risorsaUmanaRepository.findById(id).orElse(null);
		return MappingRisorsaHelper.mappingToDetail(r);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<RisorsaUmanaListVM> search(Long idEnte, Integer anno, Boolean interno, String cognome,
			Boolean soloAttiveAnno,
			Pageable pageable) throws BusinessException {
		Page<RisorsaUmana> page = null;
		Integer year = anno == null ? LocalDate.now().getYear() : anno;
		final RicercaRisorseUmane filter = new RicercaRisorseUmane();
		filter.setIdEnte(idEnte);
		filter.setAnno(year);
		filter.setEsterna(interno == null ? false : !interno);
		filter.setTestoRicerca(cognome);
		filter.setSoloAttiveAnno(!Boolean.FALSE.equals(soloAttiveAnno));
		page = risorsaUmanaRepository.search(filter, pageable);
		Page<RisorsaUmanaListVM> out = page.map(new Function<RisorsaUmana, RisorsaUmanaListVM>() {
			@Override
			public RisorsaUmanaListVM apply(RisorsaUmana r) {
				return MappingRisorsaHelper.mappingToList(r);
			}
		});
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public AmministratoreDetailVM leggiAmministratore(Long id) throws BusinessException {
		RisorsaUmana r = risorsaUmanaRepository.findById(id).orElse(null);
		return MappingRisorsaHelper.mappingToDetailAmm(r);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AmministratoreListVM> searchAmministratore(Long idEnte, Integer anno, String cognome, Pageable pageable)
			throws BusinessException {
		Page<RisorsaUmana> page = null;
		Integer year = anno == null ? LocalDate.now().getYear() : anno;
		if (StringUtils.isNotBlank(cognome)) {
			page = risorsaUmanaRepository.findByIdEnteAndAnnoAndCognomeStartsWithIgnoreCaseAndPoliticoIsTrue(idEnte,
					year, cognome, pageable);
		} else {
			page = risorsaUmanaRepository.findByIdEnteAndAnnoAndPoliticoIsTrue(idEnte, year, pageable);
		}
		Page<AmministratoreListVM> out = page.map(new Function<RisorsaUmana, AmministratoreListVM>() {
			@Override
			public AmministratoreListVM apply(RisorsaUmana r) {
				return MappingRisorsaHelper.mappingToListAmm(r);
			}
		});
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AmministratoreListVM> searchAmministratore(Long idEnte, Integer anno, String cognome)
			throws BusinessException {
		List<RisorsaUmana> items = null;
		Integer year = anno == null ? LocalDate.now().getYear() : anno;
		if (StringUtils.isNotBlank(cognome)) {
			items = risorsaUmanaRepository
					.findByIdEnteAndAnnoAndCognomeStartsWithIgnoreCaseAndPoliticoIsTrueOrderByCognomeAscNomeAsc(idEnte,
							year, cognome);
		} else {
			items = risorsaUmanaRepository.findByIdEnteAndAnnoAndPoliticoIsTrueOrderByCognomeAscNomeAsc(idEnte, year);
		}
		return MappingRisorsaHelper.mappingItemsToListAmm(items);
	}

	@Override
	public AmministratoreDetailVM creaAmministratore(CreaAmministratoreRequest request) throws BusinessException {
		if (StringUtils.isNotBlank(request.getCodiceInterno())
				&& risorsaUmanaRepository.existsByIdEnteAndAnnoAndCodiceInterno(request.getIdEnte(), request.getAnno(),
						request.getCodiceInterno())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, ELEMENTO_ESISTENTE_CON_CODICE_INTERNO);
		}
		if (risorsaUmanaRepository.existsByIdEnteAndAnnoAndCodiceInternoAndCognomeAndNomeAndPolitico(
				request.getIdEnte(), request.getAnno(), request.getCodiceInterno(), request.getCognome(),
				request.getNome(), true)) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, AMMINISTRATORE_ESISTENTE_CON_NOME_COGNOME);
		}
		RisorsaUmana r = risorsaUmanaRepository.save(MappingRisorsaHelper.mappingFromRequest(request));
		return MappingRisorsaHelper.mappingToDetailAmm(r);
	}

	@Override
	public void aggiornaAmministratore(Long id, AggiornaAmministratoreRequest request) throws BusinessException {
		RisorsaUmana r1 = null;
		if (StringUtils.isNotBlank(request.getCodiceInterno())) {
			r1 = risorsaUmanaRepository.findByIdEnteAndAnnoAndCodiceInterno(request.getIdEnte(), request.getAnno(),
					request.getCodiceInterno());
			if (r1 != null && !r1.getId().equals(id)) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, ELEMENTO_ESISTENTE_CON_CODICE_INTERNO);

			}
		}
		r1 = risorsaUmanaRepository.findByIdEnteAndAnnoAndCodiceInternoAndCognomeAndNomeAndPolitico(request.getIdEnte(),
				request.getAnno(), request.getCodiceInterno(), request.getCognome(), request.getNome(), true);
		if (r1 != null && !r1.getId().equals(id)) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, AMMINISTRATORE_ESISTENTE_CON_NOME_COGNOME);

		}
		RisorsaUmana r = MappingRisorsaHelper.mappingFromRequest(request);
		r.setId(id);
		risorsaUmanaRepository.save(r);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrganizzazioneSmartVM> getOrganizzazioniResponsabile(Long idRisorsa) throws BusinessException {
		RisorsaUmana r = risorsaUmanaRepository.findById(idRisorsa)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, RISORSA_NON_TROVATA + idRisorsa));
		List<Organizzazione> resp = organizzazioneRepository.findByResponsabileAndIdEnteAndAnno(r, r.getIdEnte(),
				r.getAnno());
		return Mapping.mapping(resp, OrganizzazioneSmartVM.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrganizzazioneSmartVM> getOrganizzazioni(Long idRisorsa) throws BusinessException {
		RisorsaUmana r = risorsaUmanaRepository.findById(idRisorsa)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, RISORSA_NON_TROVATA + idRisorsa));
		final List<OrganizzazioneSmartVM> out = new ArrayList<>();
		final List<Long> doppie = new ArrayList<>();
		List<RisorsaUmanaOrganizzazione> items = risorsaUmanaOrganizzazioneRepository.findByRisorsaUmana(r);
		for (RisorsaUmanaOrganizzazione ro : items) {
			if (doppie.contains(ro.getOrganizzazione().getId()))
				continue;
			doppie.add(ro.getOrganizzazione().getId());
			out.add(Mapping.mapping(ro.getOrganizzazione(), OrganizzazioneSmartVM.class));
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DecodificaVM> searchResponsabili(Long idEnte, Integer anno, String cognome) throws BusinessException {
		final List<DecodificaVM> out = new ArrayList<>();
		final List<Long> doppie = new ArrayList<>();
		String testo = StringUtils.isBlank(cognome) ? null : cognome.toLowerCase().trim();
		List<Organizzazione> items = organizzazioneRepository
				.findByIdEnteAndAnnoOrderByResponsabileCognomeAscResponsabileNomeAsc(idEnte, anno);
		for (Organizzazione ro : items) {
			RisorsaUmana r = ro.getResponsabile();
			if (r == null)
				continue;
			if (StringUtils.isNotBlank(cognome) && !r.getCognome().toLowerCase().startsWith(testo)) {
				continue;
			}
			if (doppie.contains(r.getId()))
				continue;
			doppie.add(r.getId());
			out.add(new DecodificaVM(r.getId(), r.getCodiceInterno(), r.getCognome() + " " + r.getNome()));
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DecodificaVM> searchRisorse(Long idEnte, Integer anno, String cognome) throws BusinessException {
		List<RisorsaUmana> items;
		if (StringUtils.isBlank(cognome)) {
			items = risorsaUmanaRepository.findByIdEnteAndAnnoAndPoliticoIsFalseOrderByCognomeAscNomeAsc(idEnte, anno);
		} else {
			items = risorsaUmanaRepository
					.findByIdEnteAndAnnoAndCognomeStartsWithIgnoreCaseAndPoliticoIsFalseOrderByCognomeAscNomeAsc(idEnte,
							anno, cognome);
		}
		if (items == null)
			return new ArrayList<>();
		return items.stream()
				.map(p -> new DecodificaVM(p.getId(), p.getCodiceInterno(), p.getCognome() + " " + p.getNome()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public RisorsaUmanaSmartVM leggiSmart(Long id) throws BusinessException {
		return Mapping.mapping(risorsaUmanaRepository.findById(id), RisorsaUmanaSmartVM.class);

	}

	@Override
	public List<DecodificaVM> incarichi(Long idEnte) throws BusinessException {
		List<Incarico> items = incaricoRepository.findByIdEnteOrderByCodice(idEnte);
		if(items!=null)
			return items.stream().map(i->new DecodificaVM(i.getId(),i.getCodice(),i.getDescrizione())).collect(Collectors.toList());
		return null;
	}

	@Override
	public List<DecodificaVM> categorie(Long idEnte) throws BusinessException {
		List<CategoriaRisorsaUmana> items = categoriaRisorsaUmanaRepository.findByIdEnteOrderByCodice(idEnte);
		if(items!=null)
			return items.stream().map(i->new DecodificaVM(i.getId(),i.getCodice(),i.getDescrizione())).collect(Collectors.toList());
		return null;
	}

	@Override
	public List<DecodificaVM> contratti(Long idEnte) throws BusinessException {
		List<ContrattoRisorsaUmana> items = contrattoRisorsaUmanaRepository.findByIdEnteOrderByCodice(idEnte);
		if(items!=null)
			return items.stream().map(i->new DecodificaVM(i.getId(),i.getCodice(),i.getDescrizione())).collect(Collectors.toList());
		return null;
	}

	@Override
	public List<DecodificaVM> profili(Long idEnte) throws BusinessException {
		List<ProfiloRisorsaUmana> items = profiloRisorsaUmanaRepository.findByIdEnteOrderByCodice(idEnte);
		if(items!=null)
			return items.stream().map(i->new DecodificaVM(i.getId(),i.getCodice(),i.getDescrizione())).collect(Collectors.toList());
		return null;
	}

}
