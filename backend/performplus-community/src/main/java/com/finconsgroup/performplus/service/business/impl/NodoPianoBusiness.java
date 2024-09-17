package com.finconsgroup.performplus.service.business.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaNodoPiano;
import com.finconsgroup.performplus.enumeration.Movement;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipologiaObiettiviOperativi;
import com.finconsgroup.performplus.events.Publisher;
import com.finconsgroup.performplus.repository.CruscottoRepository;
import com.finconsgroup.performplus.repository.IndicatorePianoRepository;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.OrganizzazioneRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaNodoPianoRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaOrganizzazioneRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.ValutazioneRepository;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.SpostaNodoRequest;
import com.finconsgroup.performplus.rest.api.vm.UtenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.FiltroAlberoExtendedVM;
import com.finconsgroup.performplus.rest.api.vm.v2.FiltroAlberoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.ModificaDisponibilitaRisorsaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.AggiornaNodoPianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.CreaNodoPianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoFiglioVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoSmartVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NoteAssessoriRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.CalcoloPesaturaObiettivoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.CalcoloPesaturaObiettivoResponse;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.PesaturaNodoListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.PesaturaNodoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.PesaturaRamoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.risorsa.AssociaRisorsaNodoPianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.risorsa.RisorsaUmanaNodoPianoListVM;
import com.finconsgroup.performplus.service.business.IConfigurazioneBusiness;
import com.finconsgroup.performplus.service.business.INodoPianoBusiness;
import com.finconsgroup.performplus.service.business.ITemplateDataBusiness;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.DateHelper;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.MappingNodoPianoHelper;
import com.finconsgroup.performplus.service.business.utils.MappingRisorsaHelper;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;
import com.finconsgroup.performplus.service.business.utils.PesaturaHelper;
import com.finconsgroup.performplus.service.business.utils.SecurityHelper;
import com.finconsgroup.performplus.service.dto.NodoPianoDTO;
import com.finconsgroup.performplus.service.dto.NodoPianoEasyDTO;
import com.finconsgroup.performplus.service.dto.QuadraturaIndicatoriProg;
import com.finconsgroup.performplus.service.dto.QuadraturaRisorseProg;

import jakarta.validation.Valid;

@Service
@Transactional
public class NodoPianoBusiness implements INodoPianoBusiness {
	private static final String RISORSA_NON_TROVATA = "Risorsa non trovata:";
	private static final String RESPONSABILE_NON_TROVATO = "Responsabile non trovato:";
	private static final String NODO_NON_TROVATO = "Nodo non trovato:";
	private static final String OBIETTIVO_NON_TROVATO = "Obiettivo non trovato:";
	private static final String DIREZIONE_NON_TROVATA = "Direzione non trovata:";
	@Autowired
	private NodoPianoRepository nodoPianoRepository;
	@Autowired
	private OrganizzazioneRepository organizzazioneRepository;
	@Autowired
	private RisorsaUmanaRepository risorsaUmanaRepository;

	@Autowired
	private RisorsaUmanaNodoPianoRepository risorsaUmanaNodoPianoRepository;
	@Autowired
	private RisorsaUmanaOrganizzazioneRepository risorsaUmanaOrganizzazioneRepository;

	@Autowired
	private IndicatorePianoRepository indicatorePianoRepository;
	@Autowired
	private ValutazioneRepository valutazioneRepository;

	@Autowired
	private IConfigurazioneBusiness configBusiness;

	@Autowired
	private IUtenteBusiness utenteBusiness;

	@Autowired
	private ITemplateDataBusiness templateDataBusiness;

//	@Autowired
//	private ProcessoNodoPianoRepository processoNodoPianoRepository;

	@Value("${sistema.tipiNodo}")
	private TipoNodo[] tipiNodo;
	@Autowired
	private CruscottoRepository cruscottoRepository;

	@Autowired
	private Publisher publisher;

	@Override
	public void eliminaNodo(Long idNodo) throws BusinessException {
		nodoPianoRepository.deleteById(idNodo);
	}

	@Override
	public void spostaNodo(SpostaNodoRequest request) throws BusinessException {
		final NodoPiano figlio = nodoPianoRepository.findById(request.getId()).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, OBIETTIVO_NON_TROVATO + request.getId());
		});
		final NodoPiano nuovoPadre = nodoPianoRepository.findById(request.getNuovoPadre()).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, OBIETTIVO_NON_TROVATO + request.getNuovoPadre());
		});
		final Organizzazione direzione = organizzazioneRepository.findById(request.getOrganizzazione())
				.orElseThrow((() -> new BusinessException(HttpStatus.NOT_FOUND,
						DIREZIONE_NON_TROVATA + request.getOrganizzazione())));

		if (request.getNuovoPadre().equals(figlio.getPadre().getId())) {
			throw new BusinessException(HttpStatus.NOT_FOUND, "Il padre non è cambiato");
		}
		String codice = nodoPianoRepository.maxCodiceByPadreId(request.getNuovoPadre());
		if (StringUtils.isBlank(codice)) {
			codice = TipoNodo.AZIONE.equals(nuovoPadre.getTipoNodo())
					|| TipoNodo.OBIETTIVO.equals(nuovoPadre.getTipoNodo()) ? "000" : "00";
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

		figlio.setOrdine(nuovoPadre.getOrdine() < 0 ? 0 : nuovoPadre.getOrdine());
		figlio.setCodice(codice);
		figlio.setPadre(nuovoPadre);
		figlio.setCodiceCompleto(nuovoPadre.getCodiceCompleto() + "." + codice);
		figlio.setCodiceInterno(codice);
		figlio.setOrganizzazione(direzione);
		figlio.setCodiceInterno(NodoPianoHelper.codiceInterno(figlio));
		nodoPianoRepository.save(figlio);

	}

	@Override
	public void rimuoviRisorsaUmana(Long idRisorsaUmanaNodoPiano) throws BusinessException {
		RisorsaUmanaNodoPiano rsnp = risorsaUmanaNodoPianoRepository.findById(idRisorsaUmanaNodoPiano).orElse(null);
		if(rsnp==null)
			return;
		risorsaUmanaNodoPianoRepository.deleteById(idRisorsaUmanaNodoPiano);
	}

	@Override
	public void associaRisorse(Long idNodo, List<Long> selezionate) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(idNodo)
				.orElseThrow((() -> new BusinessException(HttpStatus.NOT_FOUND, NODO_NON_TROVATO + idNodo)));
		if (selezionate == null || selezionate.isEmpty())
			return;
		try {
			for (Long ris : selezionate) {
				RisorsaUmana r = risorsaUmanaRepository.findById(ris)
						.orElseThrow((() -> new BusinessException(HttpStatus.NOT_FOUND, RISORSA_NON_TROVATA + ris)));

				RisorsaUmanaNodoPiano ele = risorsaUmanaNodoPianoRepository
						.findByRisorsaUmanaIdAndNodoPianoId(r.getId(), np.getId());
				if (ele != null)
					continue;
				ele = inizializza(np, r);
				addUtilizzo(ele);
				risorsaUmanaNodoPianoRepository.save(ele);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}

	}

	@Override
	@Transactional(readOnly = true)
	public String maxCodice(Long idPadre) throws BusinessException {
		return nodoPianoRepository.maxCodiceByPadreId(idPadre);
	}

	@Override
	@Transactional(readOnly = true)
	public NodoPianoDTO getRamoStampa(Long idNodo) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(idNodo)
				.orElseThrow((() -> new BusinessException(HttpStatus.NOT_FOUND, OBIETTIVO_NON_TROVATO + idNodo)));
		List<NodoPiano> items = nodoPianoRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithAndDateDeleteIsNullOrderByCodiceCompleto(np.getIdEnte(),
						np.getAnno(), np.getCodiceCompleto());
		NodoPianoDTO out = MappingNodoPianoHelper.toDto(np);
		riempi(out, items);
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public NodoPianoSmartVM getRootOnly(Long idEnte, Integer anno) throws BusinessException {
		List<NodoPiano> root = nodoPianoRepository.findByIdEnteAndAnnoAndTipoNodoAndDateDeleteIsNull(idEnte, anno,
				TipoNodo.PIANO);
		if (root == null || root.isEmpty()) {
			throw new BusinessException(HttpStatus.NOT_FOUND, "Piano non trovato " + idEnte + " " + anno);
		}
		return Mapping.mapping(root.get(0), NodoPianoSmartVM.class);

	}

	@Override
	@Transactional(readOnly = true)
	public List<NodoPianoSmartVM> search(Long idEnte, Integer anno, String testo) throws BusinessException {
		if (StringUtils.isBlank(testo))
			return Mapping.mapping(nodoPianoRepository
					.findByIdEnteAndAnnoAndDateDeleteIsNullOrderByOrdineAscCodiceCompletoAsc(idEnte, anno),
					NodoPianoSmartVM.class);
		return mappingRamo(nodoPianoRepository
				.findByIdEnteAndAnnoAndDenominazioneStartsWithIgnoreCaseAndDateDeleteIsNullOrderByOrdineAscCodiceCompletoAsc(
						idEnte, anno, testo));
	}

	@Override
	@Transactional(readOnly = true)
	public List<NodoPianoSmartVM> children(Long idNodoPiano) throws BusinessException {
		return mapping(nodoPianoRepository.findByPadreIdAndDateDeleteIsNullOrderByCodice(idNodoPiano));
	}

	@Override
	@Transactional(readOnly = true)
	public List<NodoFiglioVM> figli(Long idNodoPiano) throws BusinessException {
		return mappingFiglio(nodoPianoRepository.findByPadreIdAndDateDeleteIsNullOrderByCodice(idNodoPiano));
	}

	@Override
	@Transactional(readOnly = true)
	public List<NodoPianoSmartVM> ramo(Long idNodoPiano) throws BusinessException {
		NodoPiano o = nodoPianoRepository.findById(idNodoPiano).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, "nodoPiano non trovato:" + idNodoPiano);
		});

		List<NodoPiano> items = nodoPianoRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithAndDateDeleteIsNullOrderByOrdineAscCodiceCompletoAsc(
						o.getIdEnte(), o.getAnno(), o.getCodiceCompleto());
		return mapping(items);
	}

	@Override
	@Transactional(readOnly = true)
	public NodoPianoDetailVM read(Long id) throws BusinessException {
		NodoPiano nodo = nodoPianoRepository.findById(id).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, "nodoPiano non trovata:" + id);
		});

		return MappingNodoPianoHelper.mappingToDetail(nodo, nodoPianoRepository).fields(templateDataBusiness
				.findByContainerAndType(nodo.getIdEnte(), "PROGRAMMAZIONE", nodo.getTipoNodo().name()).getFields());
	}

	@Override
	public void elimina(Long id) throws BusinessException {
		NodoPiano nodo = nodoPianoRepository.findById(id).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, "nodoPiano non trovata:" + id);
		});
		UtenteVM u = SecurityHelper.utente(utenteBusiness);
		nodoPianoRepository.logicalDelete(nodo.getId(), LocalDateTime.now(), u.getUsername());
		nodoPianoRepository.logicalDeleteRamo(nodo.getIdEnte(), nodo.getAnno(), nodo.getCodiceCompleto() + ".%",
				LocalDateTime.now(), u.getUsername());
	}

	@Override
	public void crea(CreaNodoPianoRequest request) throws BusinessException {
		if (request.getScadenza() == null) {
			request.setScadenza(DateHelper.fineAnno(request.getInizio().getYear()));
		}
		if (!request.getInizio().isBefore(request.getScadenza())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Data inizio deve essere precedente a data scadenza");
		}
		NodoPiano np = Mapping.mapping(request, NodoPiano.class);
		NodoPiano p = nodoPianoRepository.findById(request.getIdPadre()).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Nodo padre non trovato:" + request.getIdPadre()));
		TipoNodo tipoNodo = NodoPianoHelper.tipoNodoFiglio(p.getTipoNodo(), tipiNodo);
		if (tipoNodo == null) {
			throw new BusinessException("Non è possibile creare figli per il nodo:" + p.getId());
		}
		int count = nodoPianoRepository.countByPadreIdAndDateDeleteIsNull(p.getId());
		if (p.getTipoNodo().ordinal() >= TipoNodo.values().length - 1) {
			throw new BusinessException(HttpStatus.BAD_REQUEST,
					"Non è possibile creare figli per il Nodo:" + p.getId());
		}
		String codice = request.getCodice().replace('.', '_');
		if (nodoPianoRepository.existsByPadreIdAndCodiceAndDateDeleteIsNull(p.getId(), codice)) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Codice già utilizzato per lo stesso ramo");
		}
		np.setTipoNodo(tipoNodo);
		np.setAnno(p.getAnno());
		np.setIdEnte(p.getIdEnte());
		np.setCodice(codice);
		np.setCodiceCompleto(p.getCodiceCompleto() + "." + codice);
		np.setPadre(p);
		np.setPiano(TipoNodo.PIANO.equals(p.getTipoNodo()) ? p : p.getPiano());
		np.setOrdine(p == null || p.getOrdine() < 0 ? 0 : p.getOrdine());
		np.setCodiceInterno(NodoPianoHelper.codiceInterno(np));
		np.setTipo(request.getTipo());
		np.setModalitaAttuative(request.getModalita());
		np.setTipologie(
				Boolean.TRUE.equals(request.getObiettivoImpatto()) ? TipologiaObiettiviOperativi.IMPATTO : null);
		if (nodoPianoRepository.existsByIdEnteAndAnnoAndCodiceInternoAndDateDeleteIsNull(np.getIdEnte(), np.getAnno(),
				np.getCodiceInterno())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST,
					"Codice interno già utilizzato:" + np.getCodiceInterno());
		}
		if (request.getOrganizzazione() == null && TipoNodo.AZIONE.equals(np.getTipoNodo())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "La direzione è obbligatoria");
		}
		completa(np, request);
		nodoPianoRepository.save(np);

	}

	@Override
	public void aggiorna(Long idNodo, AggiornaNodoPianoRequest request) throws BusinessException {
		if (request.getScadenza() == null) {
			request.setScadenza(DateHelper.fineAnno(request.getInizio().getYear()));
		}

		if (!request.getInizio().isBefore(request.getScadenza())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Data inizio deve essere precedente a data scadenza");
		}
		NodoPiano np = nodoPianoRepository.findById(idNodo)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, NODO_NON_TROVATO + idNodo));
		String codice = request.getCodice().replace('.', '_');
		if (nodoPianoRepository.existsByPadreIdAndCodiceAndIdNotAndDateDeleteIsNull(np.getPadre().getId(), codice,
				np.getId())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Codice già utilizzato per lo stesso ramo");
		}
		Mapping.mapping(request, np);
		np.setInizio(request.getInizio());
		np.setScadenza(request.getScadenza());
		np.setCodice(codice);
		np.setCodiceCompleto(np.getPadre().getCodiceCompleto() + "." + codice);
		np.setOrdine(np.getPadre() == null || np.getPadre().getOrdine() < 0 ? 0 : np.getPadre().getOrdine());
		np.setCodiceInterno(NodoPianoHelper.codiceInterno(np));
		NodoPiano tmp = nodoPianoRepository.findByIdEnteAndAnnoAndCodiceInternoAndDateDeleteIsNull(np.getIdEnte(),
				np.getAnno(), np.getCodiceInterno());
		if (tmp != null && !tmp.getId().equals(np.getId())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST,
					"Codice interno già utilizzato:" + np.getCodiceInterno());
		}
		if (request.getOrganizzazione() == null && TipoNodo.AZIONE.equals(np.getTipoNodo())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "La direzione è obbligatoria");
		}
		np.setModalitaAttuative(request.getModalita());
		np.setTipo(request.getTipo());
		np.setTipologie(
				Boolean.TRUE.equals(request.getObiettivoImpatto()) ? TipologiaObiettiviOperativi.IMPATTO : null);
		completa(np, request);

		nodoPianoRepository.save(np);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean isCodiceDuplicato(Long idPadre, String codice) throws BusinessException {
		return nodoPianoRepository.existsByPadreIdAndCodiceAndDateDeleteIsNull(idPadre, codice);
	}

	@Override
	@Transactional(readOnly = true)
	public CreaNodoPianoRequest prepareDescendant(Long idPadre) throws BusinessException {
		NodoPiano padre = nodoPianoRepository.findById(idPadre)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Padre non trovato:" + idPadre));
		TipoNodo tipoNodo = NodoPianoHelper.tipoNodoFiglio(padre.getTipoNodo(), tipiNodo);
		if (tipoNodo == null) {
			throw new BusinessException("Non è possibile creare figli per il nodo:" + idPadre);
		}
		CreaNodoPianoRequest out = new CreaNodoPianoRequest();
		out.setIdPadre(idPadre);
		out.setTipoNodo(tipoNodo);
		out.setOrganizzazione(padre.getOrganizzazione() == null ? null : padre.getOrganizzazione().getId());

		out.setInizio(padre.getInizio());
		out.setScadenza(padre.getScadenza());
		String codice = nodoPianoRepository.maxCodiceByPadreId(idPadre);
		if (StringUtils.isBlank(codice)) {
			codice = TipoNodo.AZIONE.equals(tipoNodo) || TipoNodo.OBIETTIVO.equals(tipoNodo) ? "000" : "00";
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
		out.setIdPadre(padre.getId());
		out.setCodice(codice);
		out.setObiettivoImpatto(true);
//		if (padre.getFontiFinanziamento() != null)
//			out.setFontiFinanziamento(padre.getFontiFinanziamento().stream().map(FonteFinanziamento::getId).collect(Collectors.toList()));
		out.setCodiceRidottoPadre(NodoPianoHelper.ridotto(padre.getCodiceCompleto()));
		out.setFields(templateDataBusiness
				.findByContainerAndType(padre.getIdEnte(), "PROGRAMMAZIONE", out.getTipoNodo().name()).getFields());
		out.setEnabledStrategico(TipoNodo.OBIETTIVO.equals(tipoNodo) || (padre != null
				&& TipoNodo.OBIETTIVO.equals(padre.getTipoNodo()) && Boolean.TRUE.equals(padre.getStrategico())));
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public CreaNodoPianoRequest prepareModifica(Long idNodo) throws BusinessException {
		NodoPiano nodo = nodoPianoRepository.findById(idNodo)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Nodo non trovato:" + idNodo));
		CreaNodoPianoRequest out = MappingNodoPianoHelper.toAggiorna(nodo);
		out.setFields(templateDataBusiness
				.findByContainerAndType(nodo.getIdEnte(), "PROGRAMMAZIONE", nodo.getTipoNodo().name()).getFields());
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<QuadraturaIndicatoriProg> detailIndicatori(Long id) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(id)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, OBIETTIVO_NON_TROVATO + id));

		return QuadraturaIndicatoriHelper.elabora(np, nodoPianoRepository, indicatorePianoRepository,
				valutazioneRepository, null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<QuadraturaRisorseProg> detailRisorse(Long id) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(id)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, OBIETTIVO_NON_TROVATO + id));
		int oreSettimanaliMax = configBusiness.getOreMax(np.getIdEnte(), np.getAnno());

		return QuadraturaRisorsaUmanaNodoHelper.elabora(np, nodoPianoRepository, risorsaUmanaNodoPianoRepository,
				risorsaUmanaOrganizzazioneRepository, null, oreSettimanaliMax);
	}

	@Override
	@Transactional(readOnly = true)
	public PesaturaRamoVM pesaturaRamo(Long idNodoPiano) throws BusinessException {
		final NodoPiano np = nodoPianoRepository.findById(idNodoPiano)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, NODO_NON_TROVATO + idNodoPiano));
		final List<NodoPiano> items;
		if (TipoNodo.PIANO.equals(np.getTipoNodo())) {
			items = nodoPianoRepository.findByIdEnteAndAnnoAndTipoNodoAndDateDeleteIsNullOrderByCodiceCompleto(
					np.getIdEnte(), np.getAnno(), TipoNodo.AREA);
		} else if (TipoNodo.AREA.equals(np.getTipoNodo())) {
			items = nodoPianoRepository.findByPadreIdAndDateDeleteIsNullOrderByCodice(idNodoPiano);
		} else {
			items = new ArrayList<>();
		}

		final PesaturaRamoVM out = new PesaturaRamoVM();
		out.setNodo(new PesaturaNodoListVM(idNodoPiano, NodoPianoHelper.ridotto(np.getCodiceCompleto()),
				np.getDenominazione(), np.getTipoNodo(), np.getInizio(), np.getScadenza(),
				np.getPeso() == null ? 0d : np.getPeso().doubleValue()));
		final List<PesaturaNodoListVM> figli = new ArrayList<>();
		final BigDecimal[] tot = new BigDecimal[] { BigDecimal.valueOf(0d) };
		if (items != null) {
			items.forEach(d -> {
				BigDecimal p = d.getPeso() == null ? BigDecimal.valueOf(0d) : d.getPeso();
				tot[0] = tot[0].add(p);
				figli.add(new PesaturaNodoListVM(d.getId(), NodoPianoHelper.ridotto(d.getCodiceCompleto()),
						d.getDenominazione(), d.getTipoNodo(), d.getInizio(), d.getScadenza(), p.doubleValue()));
			});
		}
		figli.forEach(f -> {
			if (tot[0].doubleValue() == 0) {
				f.setPesoRelativo(0d);
			} else {
				f.setPeso(BigDecimal.valueOf(f.getPeso()).divide(tot[0], 2, RoundingMode.HALF_UP).doubleValue());
			}
		});
		out.setFigli(figli);
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public PesaturaNodoVM pesatura(Long idNodoPiano) throws BusinessException {
		final PesaturaNodoVM out = new PesaturaNodoVM();
		final NodoPiano np = nodoPianoRepository.findById(idNodoPiano)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, NODO_NON_TROVATO + idNodoPiano));
		out.setId(np.getId());
		out.setLivelloComplessita(np.getLivelloComplessita());
		out.setLivelloStrategicita(np.getLivelloStrategicita());
		out.setPeso(np.getPeso() == null ? 0d : np.getPeso().doubleValue());
//		out.setTipologia(np.getTipologia());
		PesaturaHelper.calcolaPeso(out, np.getTipoNodo());
		return out;
	}

	@Override
	public void salvaPesatura(Long idNodoPiano, CalcoloPesaturaObiettivoRequest request) throws BusinessException {
		final NodoPiano np = nodoPianoRepository.findById(idNodoPiano)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, NODO_NON_TROVATO + idNodoPiano));
		final PesaturaNodoVM p = new PesaturaNodoVM();
		p.setId(np.getId());
		p.setLivelloComplessita(request.getLivelloComplessita());
		p.setLivelloStrategicita(request.getLivelloStrategicita());
		p.setPeso(np.getPeso() == null ? 0d : np.getPeso().doubleValue());
		p.setTipologia(request.getTipologia());
		PesaturaHelper.calcolaPesoComplessivo(p, np.getTipoNodo());
		nodoPianoRepository.modificaPeso(idNodoPiano, p.getLivelloStrategicita(), p.getLivelloComplessita(),
				p.getTipologia(), p.getPeso());
	}

	@Override
	@Transactional(readOnly = true)
	public CalcoloPesaturaObiettivoResponse calcolo(Long idNodoPiano, CalcoloPesaturaObiettivoRequest request)
			throws BusinessException {
		final CalcoloPesaturaObiettivoResponse out = new CalcoloPesaturaObiettivoResponse();
		final PesaturaNodoVM p = new PesaturaNodoVM();
		final NodoPiano np = nodoPianoRepository.findById(idNodoPiano)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, NODO_NON_TROVATO + idNodoPiano));
		p.setId(np.getId());
		p.setLivelloComplessita(request.getLivelloComplessita());
		p.setLivelloStrategicita(request.getLivelloStrategicita());
		p.setPeso(np.getPeso() == null ? 0d : np.getPeso().doubleValue());
		p.setTipologia(request.getTipologia());
		PesaturaHelper.calcolaPesoComplessivo(p, np.getTipoNodo());
		out.setPeso(p.getPeso());
		out.setPesoLivelloComplessita(p.getPesoLivelloComplessita());
		out.setPesoLivelloStrategicita(p.getPesoLivelloStrategicita());
		return out;
	}

	@Override
	public void modificaDisponibilitaRisorsa(ModificaDisponibilitaRisorsaRequest request) throws BusinessException {
		RisorsaUmanaNodoPiano runp = risorsaUmanaNodoPianoRepository.findById(request.getId()).orElseThrow(
				(() -> new BusinessException(HttpStatus.NOT_FOUND, "Associazione non trovata:" + request.getId())));
		double disp = request.getDisponibilita() == null ? 0d : request.getDisponibilita();
		verificaUtilizzo(runp, disp, runp.getRisorsaUmana());
		runp.setDisponibilita(BigDecimal.valueOf(disp));
		risorsaUmanaNodoPianoRepository.save(runp);
	}

	@Override
	public void associaRisorsa(AssociaRisorsaNodoPianoRequest request) throws BusinessException {
		RisorsaUmana r = risorsaUmanaRepository.findById(request.getIdRisorsa()).orElseThrow(
				(() -> new BusinessException(HttpStatus.NOT_FOUND, RISORSA_NON_TROVATA + request.getIdNodoPiano())));
		NodoPiano np = nodoPianoRepository.findById(request.getIdNodoPiano()).orElseThrow(
				(() -> new BusinessException(HttpStatus.NOT_FOUND, OBIETTIVO_NON_TROVATO + request.getIdNodoPiano())));

		RisorsaUmanaNodoPiano ele = risorsaUmanaNodoPianoRepository.findByRisorsaUmanaIdAndNodoPianoId(r.getId(),
				request.getIdNodoPiano());
		if (ele != null) {
			throw new BusinessException(HttpStatus.FOUND, "Associazione gia' esistente");
		}
		ele = inizializza(np, r);
		addUtilizzo(ele);
		risorsaUmanaNodoPianoRepository.save(ele);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaNodoPianoListVM> getRisorseAssociate(Long idNodo) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(idNodo)
				.orElseThrow((() -> new BusinessException(HttpStatus.NOT_FOUND, OBIETTIVO_NON_TROVATO + idNodo)));
		return MappingRisorsaHelper.mappingItemsRisorsaUmanaNodoPianoToList(risorsaUmanaNodoPianoRepository
				.findByNodoPianoIdAndRisorsaUmanaPoliticoIsFalseOrderByRisorsaUmanaCognomeAscRisorsaUmanaNomeAsc(
						np.getId()));
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaListVM> getRisorseAssociabili(Long idNodo, String testo, Boolean esterna,
			Boolean soloStruttura) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(idNodo)
				.orElseThrow((() -> new BusinessException(HttpStatus.NOT_FOUND, OBIETTIVO_NON_TROVATO + idNodo)));
		List<RisorsaUmana> items = null;
		final List<Long> responsabili = new ArrayList<>();
		if (np.getOrganizzazione() != null && np.getOrganizzazione().getResponsabile() != null) {
			responsabili.add(np.getOrganizzazione().getResponsabile().getId());
		}
		if (Boolean.TRUE.equals(soloStruttura) && np.getOrganizzazione() != null) {
			final List<Long> inStruttura = new ArrayList<>();
			inStruttura.add(np.getOrganizzazione().getId());
			if (np.getOrganizzazioni() != null) {
				np.getOrganizzazioni().stream().forEach(o -> {
					inStruttura.add(o.getId());
					if (o.getResponsabile() != null) {
						responsabili.add(o.getResponsabile().getId());
					}
				});
			}
			if (StringUtils.isBlank(testo)) {
				items = risorsaUmanaRepository.cercaRisorseNonAssociateNodoInStrutture(np.getIdEnte(), np.getAnno(),
						np.getId(), inStruttura);
			} else {
				items = risorsaUmanaRepository.cercaRisorseNonAssociateNodoTestoInStrutture(np.getIdEnte(),
						np.getAnno(), np.getId(), testo.trim() + "%", inStruttura);
			}
		} else {
			if (StringUtils.isBlank(testo)) {
				items = risorsaUmanaRepository.cercaRisorseNonAssociateNodo(np.getIdEnte(), np.getAnno(), np.getId());
			} else {
				items = risorsaUmanaRepository.cercaRisorseNonAssociateNodoTesto(np.getIdEnte(), np.getAnno(),
						np.getId(), testo.trim() + "%");
			}
		}
		if (items != null && !items.isEmpty()) {
			items.removeIf(r -> responsabili.contains(r.getId()));
			if (esterna != null) {
				items.removeIf(r -> esterna.equals(r.getEsterna() == null ? false : r.getEsterna()));
			}
		}
		return MappingRisorsaHelper.mappingItemsToList(items);
	}

	@Override
	@Transactional(readOnly = true)
	public List<NodoPianoSmartVM> search(FiltroAlberoVM filter) throws BusinessException {
		FiltroAlberoExtendedVM parametri = new FiltroAlberoExtendedVM(filter);
		Organizzazione direzione = null;
		if (parametri.getIdDirezione() != null) {
			direzione = organizzazioneRepository.findById(parametri.getIdDirezione())
					.orElseThrow((() -> new BusinessException(HttpStatus.NOT_FOUND,
							DIREZIONE_NON_TROVATA + parametri.getIdDirezione())));
			parametri.setCodiceCompletoDirezione(direzione.getCodiceCompleto());
		}
		if (parametri.getIdResponsabile() != null) {
			risorsaUmanaRepository.findById(parametri.getIdResponsabile())
					.orElseThrow((() -> new BusinessException(HttpStatus.NOT_FOUND,
							RESPONSABILE_NON_TROVATO + parametri.getIdResponsabile())));
		}
		if (parametri.getIdRisorsa() != null) {
			risorsaUmanaRepository.findById(parametri.getIdRisorsa())
					.orElseThrow((() -> new BusinessException(HttpStatus.NOT_FOUND,
							RISORSA_NON_TROVATA + parametri.getIdRisorsa())));
		}

		List<NodoPiano> items = nodoPianoRepository.searchByFilter(parametri);
		return mappingRamo(items);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal pesoTotaleFigli(Long idNodoPiano) throws BusinessException {
		return nodoPianoRepository.pesoTotaleFigli(idNodoPiano);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DecodificaVM> searchProgrammi(Long idEnte, Integer anno, String testo) throws BusinessException {
		List<NodoPiano> items;
		if (StringUtils.isBlank(testo)) {
			items = nodoPianoRepository
					.findByIdEnteAndAnnoAndTipoNodoAndDateDeleteIsNullOrderByOrdineAscCodiceCompletoAsc(idEnte, anno,
							TipoNodo.AREA);
		} else {
			items = nodoPianoRepository
					.findByIdEnteAndAnnoAndTipoNodoAndDenominazioneStartsWithIgnoreCaseAndDateDeleteIsNullOrderByOrdineAscCodiceCompletoAsc(
							idEnte, anno, TipoNodo.AREA.name(), testo);
		}
		if (items == null)
			return new ArrayList<>();
		return items.stream()
				.map(p -> new DecodificaVM(p.getId(), NodoPianoHelper.ridotto(p.getCodiceCompleto()),
						NodoPianoHelper.ridotto(p.getCodiceCompleto()) + " " + p.getDenominazione()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<DecodificaVM> searchRisultatiAttesi(Long idEnte, Integer anno, String testo, Long idProgramma)
			throws BusinessException {
		FiltroAlberoExtendedVM filter = new FiltroAlberoExtendedVM();
		filter.setIdEnte(idEnte);
		filter.setAnno(anno);
		filter.setTesto(testo);
//		filter.setIdProgramma(idProgramma);
		filter.setTipiNodo(List.of(TipoNodo.OBIETTIVO));
		List<NodoPiano> items = nodoPianoRepository.searchByFilter(filter);
		if (items == null)
			return new ArrayList<>();
		return items.stream()
				.map(p -> new DecodificaVM(p.getId(), NodoPianoHelper.ridotto(p.getCodiceCompleto()),
						NodoPianoHelper.ridotto(p.getCodiceCompleto()) + " " + p.getDenominazione()))
				.collect(Collectors.toList());
	}

	@Override
	public void modificaNoteAssessori(NoteAssessoriRequest request) throws BusinessException {
		nodoPianoRepository.findById(request.getIdNodoPiano()).orElseThrow(
				(() -> new BusinessException(HttpStatus.NOT_FOUND, NODO_NON_TROVATO + request.getIdNodoPiano())));
		nodoPianoRepository.modificaNoteInterne(request.getIdNodoPiano(), request.getNoteAssessori());
	}

	@Override
	@Transactional(readOnly = true)
	public List<DecodificaVM> searchResponsabili(Long idEnte, Integer anno, String cognome) throws BusinessException {
		final List<DecodificaVM> out = new ArrayList<>();
		final List<Long> doppie = new ArrayList<>();
		String testo = StringUtils.isBlank(cognome) ? null : cognome.toLowerCase().trim();
		List<Organizzazione> direzioni = organizzazioneRepository
				.findByIdEnteAndAnnoOrderByResponsabileCognomeAscResponsabileNomeAsc(idEnte, anno);
		for (Organizzazione o : direzioni) {
			RisorsaUmana r = o.getResponsabile();
			if (r == null)
				continue;
			if (StringUtils.isNotBlank(testo) && !r.getCognome().toLowerCase().startsWith(testo)) {
				continue;
			}
			if (doppie.contains(r.getId()))
				continue;
			doppie.add(r.getId());
			out.add(new DecodificaVM(r.getId(), r.getCodiceInterno(), r.getCognome() + " " + r.getNome()));
		}
		out.sort(new Comparator<DecodificaVM>() {

			@Override
			public int compare(DecodificaVM o1, DecodificaVM o2) {
				int c = o1.getDescrizione().compareTo(o2.getDescrizione());
				if (c == 0)
					c = o1.getCodice().compareTo(o2.getCodice());
				return c;
			}
		});
		return out;
	}

	private void verificaUtilizzo(RisorsaUmanaNodoPiano old, Double disponibilita, RisorsaUmana r)
			throws BusinessException {
		BigDecimal newUtilizzo = disponibilita == null ? BigDecimal.ZERO : BigDecimal.valueOf(disponibilita);
		double b = newUtilizzo.doubleValue();
		double a = 0d;
		if (old != null) {
			BigDecimal oldUtilizzo = old.getDisponibilita() == null ? BigDecimal.ZERO : old.getDisponibilita();
			a = oldUtilizzo.doubleValue();
			if (a == b)
				return;
		}
		BigDecimal utilizzo = risorsaUmanaNodoPianoRepository.utilizzo(r.getId(), r.getIdEnte(), r.getAnno());
		if (utilizzo == null)
			utilizzo = BigDecimal.ZERO;

		double tot = utilizzo.doubleValue();

		int delta = ModelHelper.delta(tot - a + b, 100d);
		if (delta > 0) {
			throw new BusinessException("Disponibilità restante " + ModelHelper.toStringDec(100d - tot + a, 2));
		}

	}

	private void addUtilizzo(RisorsaUmanaNodoPiano runp) {
		BigDecimal utilizzo = risorsaUmanaNodoPianoRepository.utilizzo(runp.getRisorsaUmana().getId(),
				runp.getRisorsaUmana().getIdEnte(), runp.getRisorsaUmana().getAnno());
		if (utilizzo == null)
			utilizzo = BigDecimal.ZERO;
		double disp = 100d - utilizzo.doubleValue();
		if (disp < 0)
			disp = 0;
		runp.setDisponibilita(BigDecimal.valueOf(disp));
	}

	private void riempi(final NodoPianoDTO root, final List<NodoPiano> items) {
		final Map<Long, NodoPianoDTO> map = new HashMap<>();
		map.put(root.getId(), root);
		if (items == null) {
			return;
		}
		for (NodoPiano np : items) {
			if (np.getId().equals(root.getId()) || np.getPadre() == null)
				continue;
			aggiungiNp(np, map);
		}
	}

	private void aggiungiNp(final NodoPiano np, final Map<Long, NodoPianoDTO> map) {
		NodoPianoDTO p = map.get(np.getPadre().getId());
		if (p == null) {
			p = MappingNodoPianoHelper.toDto(np.getPadre());
			map.put(np.getPadre().getId(), p);
		}
		NodoPianoDTO d = map.get(np.getId());
		if (d == null) {
			d = MappingNodoPianoHelper.toDto(np);
			map.put(np.getId(), d);
			p.add(d);
		}
	}

	private RisorsaUmanaNodoPiano inizializza(NodoPiano nodo, RisorsaUmana risorsa) {
		RisorsaUmanaNodoPiano ele = new RisorsaUmanaNodoPiano();
		ele.setNodoPiano(nodo);
		ele.setRisorsaUmana(risorsa);
		return ele;
	}

	private List<NodoPianoSmartVM> mapping(List<NodoPiano> items) {
		final List<NodoPianoSmartVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(o -> out.add(MappingNodoPianoHelper.mappingToSmart(o)));
		return out;
	}

	private List<NodoFiglioVM> mappingFiglio(List<NodoPiano> items) {
		final List<NodoFiglioVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(o -> out.add(MappingNodoPianoHelper.mappingToFiglio(o)));
		return out;
	}

	private void completa(NodoPiano np, AggiornaNodoPianoRequest request) {

		if (request.getOrganizzazione() != null) {
			np.setOrganizzazione(organizzazioneRepository.findById(request.getOrganizzazione()).orElse(null));
		} else {
			if (np.getPadre() == null) {
				np.setOrganizzazione(np.getPiano().getOrganizzazione());
			} else {
				np.setOrganizzazione(np.getPadre().getOrganizzazione());
			}
		}
		if (request.getOrganizzazioni() != null && !request.getOrganizzazioni().isEmpty()) {
			List<Organizzazione> items = new ArrayList<>();
			for (Long id : request.getOrganizzazioni()) {
				Organizzazione o = organizzazioneRepository.findById(id).orElse(null);
				if (o != null && !o.getId().equals(request.getOrganizzazione())) {
					items.add(o);
				}
			}
			np.setOrganizzazioni(items);
		} else {
			np.setOrganizzazioni(null);
		}
		if (np.getPiano() == null) {
			np.setPiano(nodoPianoRepository.findTopByIdEnteAndAnnoAndTipoNodoAndDateDeleteIsNull(np.getId(),
					np.getAnno(), TipoNodo.PIANO));
		}
	}

	private List<NodoPianoSmartVM> mappingRamo(List<NodoPiano> items) {
		List<Long> padri = new ArrayList<>();
		final List<NodoPianoSmartVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(np -> padre(np, padri, out));
		return out;
	}

	private void padre(final NodoPiano np, final List<Long> padri, final List<NodoPianoSmartVM> out) {
		if (np.getPadre() != null && !padri.contains(np.getPadre().getId()))
			padre(np.getPadre(), padri, out);
		out.add(MappingNodoPianoHelper.mappingToSmart(np));
		padri.add(np.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public NodoPianoSmartVM leggSmart(Long id) throws BusinessException {
		return Mapping.mapping(nodoPianoRepository.findById(id), NodoPianoSmartVM.class);
	}

	@Override
	@Transactional(readOnly = true)
	public NodoPianoEasyDTO padreEasy(Long id) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(id).orElse(null);
		if (np == null || np.getPadre() == null)
			return null;
		NodoPiano p = np.getPadre();
		NodoPianoEasyDTO out = Mapping.mapping(p, NodoPianoEasyDTO.class);
		NodoPianoEasyDTO po = out;
		while (p.getPadre() != null) {
			p = p.getPadre();
			NodoPianoEasyDTO tmp = Mapping.mapping(p, NodoPianoEasyDTO.class);
			po.setPadre(tmp);
			po = tmp;
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public NodoPianoSmartVM leggiAreaPerOrdine(Long idEnte, Integer anno, int ordine) throws BusinessException {
		List<NodoPiano> aree = nodoPianoRepository.findByIdEnteAndAnnoAndOrdineAndTipoNodoAndDateDeleteIsNull(idEnte,
				anno, ordine, TipoNodo.AREA);
		if (aree == null || aree.isEmpty())
			return null;
		return Mapping.mapping(aree.get(0), NodoPianoSmartVM.class);
	}

	@Override
	public List<NodoPianoSmartVM> listSposta(Long idNodo) throws BusinessException {
		final NodoPiano np = nodoPianoRepository.findById(idNodo)
				.orElseThrow((() -> new BusinessException(HttpStatus.NOT_FOUND, OBIETTIVO_NON_TROVATO + idNodo)));
		if (!TipoNodo.AZIONE.equals(np.getTipoNodo())) {
			new BusinessException(HttpStatus.BAD_REQUEST, "Elemento non spostabile");
		}
		final NodoPiano p = np.getPadre();
		if (p == null) {
			new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Elemento senza padre:" + np.getCodiceInterno());
		}
		final List<NodoPiano> items = nodoPianoRepository
				.findByIdEnteAndAnnoAndTipoNodoAndDateDeleteIsNullOrderByCodiceCompleto(np.getIdEnte(), np.getAnno(),
						p.getTipoNodo());
		final List<NodoPianoSmartVM> out = new ArrayList<>();
		if (items == null)
			return out;
		for (NodoPiano t : items) {
			if (t.getId().equals(p.getId()))
				continue;
			out.add(Mapping.mapping(t, NodoPianoSmartVM.class));
		}
		return out;
	}

}
