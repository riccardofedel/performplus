package com.finconsgroup.performplus.service.business.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.finconsgroup.performplus.domain.Indicatore;
import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.RangeIndicatore;
import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.enumeration.Periodicita;
import com.finconsgroup.performplus.enumeration.TipoConsuntivazione;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoValutazione;
import com.finconsgroup.performplus.repository.IndicatorePianoRepository;
import com.finconsgroup.performplus.repository.IndicatoreRepository;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.OrganizzazioneRepository;
import com.finconsgroup.performplus.repository.RangeIndicatoreRepository;
import com.finconsgroup.performplus.repository.ValutazioneRepository;
import com.finconsgroup.performplus.rest.api.vm.ForzatureAdminRequest;
import com.finconsgroup.performplus.rest.api.vm.ForzatureRequest;
import com.finconsgroup.performplus.rest.api.vm.IndicatoreVM;
import com.finconsgroup.performplus.rest.api.vm.v2.StrutturaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.ValoreDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.AggiornaIndicatorePianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.CreaIndicatorePianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatorePianoDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatorePianoListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatoreRangeVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatoreTargetVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.SaveIndicatoreRangeRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.SaveIndicatoreTargetRequest;
import com.finconsgroup.performplus.service.business.IIndicatorePianoBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.ConsuntivoHelper;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.MappingIndicatorePianoHelper;
import com.finconsgroup.performplus.service.dto.IndicatorePianoDTO;
import com.finconsgroup.performplus.service.dto.RangeIndicatoreDTO;
import com.finconsgroup.performplus.service.dto.ValutazioneDTO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IndicatorePianoBusiness implements IIndicatorePianoBusiness {
	@Autowired
	private IndicatorePianoRepository indicatorePianoRepository;
	@Autowired
	private NodoPianoRepository nodoPianoRepository;
	@Autowired
	private IndicatoreRepository indicatoreRepository;
	@Autowired
	private RangeIndicatoreRepository rangeIndicatoreRepository;
	@Autowired
	private ValutazioneRepository valutazioneRepository;
	@Autowired
	private OrganizzazioneRepository organizzazioneRepository;

	private static final String NON_CORRETTO = " non corretto";
	private static final String RANGE_NRO = "Range n.ro ";

	public static final String AVANZAMENTO = "Avanzamento";
	public static final String AVANZAMENTO_CALCOLATO = "Avanzamento calcolato";

	private static final Logger logger = LoggerFactory.getLogger(IndicatorePianoBusiness.class);

	@Value("${sistema.tipiNodo}")
	private TipoNodo[] tipiNodo;
	@Value("${sistema.target}")
	private Periodicita[] periodiTarget;
	@Value("${sistema.rendicontazione}")
	private Periodicita[] periodiRend;

	@Override
	public IndicatorePianoDTO crea(IndicatorePianoDTO dto) throws BusinessException {
//		verificaRanges(dto);
		dto.setId(null);
		IndicatorePiano e = indicatorePianoRepository.save(Mapping.mapping(dto, IndicatorePiano.class));
		aggiornaConsuntivi(e, dto);
		aggiornaPreventivi(e, dto);
		aggiornaStorici(e, dto);
//		aggiornaRange(e, dto);
		IndicatorePianoDTO out = Mapping.mapping(e, IndicatorePianoDTO.class);
		aggiungiValori(out);
//		aggiungiRanges(out);
		return out;
	}

	@Override
	public IndicatorePianoDTO aggiorna(IndicatorePianoDTO dto) throws BusinessException {
//		verificaRanges(dto);
		IndicatorePiano e = indicatorePianoRepository.save(Mapping.mapping(dto, IndicatorePiano.class));
		aggiornaConsuntivi(e, dto);
		aggiornaPreventivi(e, dto);
		aggiornaStorici(e, dto);
//		aggiornaRange(e, dto);
		IndicatorePianoDTO out = Mapping.mapping(e, IndicatorePianoDTO.class);
		aggiungiValori(out);
//		aggiungiRanges(out);
		return out;
	}

//	private void verificaRanges(IndicatorePianoDTO dto) throws BusinessException {
//		if (dto.getRanges() == null || dto.getRanges().isEmpty()) {
//			return;
//		}
//		boolean crescente = !Boolean.TRUE.equals(dto.getDecrescente());
//		int k = 0;
//		RangeIndicatoreDTO prec = null;
//		Map<Long, Integer> pos = new Hashtable<>();
//		for (RangeIndicatoreDTO r : dto.getRanges()) {
//			k += 1;
//			if (r.getMinimo() == null || r.getMassimo() == null || r.getValore() == null
//					|| r.getMinimo() > r.getMassimo()) {
//				throw new BusinessException(RANGE_NRO + k + NON_CORRETTO);
//			}
//			if (r.getId() == null)
//				r.setId((long) -k);
//			pos.put(r.getId(), k);
//		}
//		dto.getRanges().sort(new Comparator<RangeIndicatoreDTO>() {
//
//			@Override
//			public int compare(RangeIndicatoreDTO o1, RangeIndicatoreDTO o2) {
//				if (o1.getMinimo().compareTo(o2.getMinimo()) > 0) {
//					return 1;
//				} else if (o1.getMinimo().compareTo(o2.getMinimo()) < 0) {
//					return -1;
//				} else if (o1.getMassimo().compareTo(o2.getMassimo()) > 0) {
//					return 1;
//				} else if (o1.getMassimo().compareTo(o2.getMassimo()) < 0) {
//					return -1;
//				}
//				return 0;
//			}
//		});
//
//		k = 0;
//		prec = null;
//		for (RangeIndicatoreDTO r : dto.getRanges()) {
//			k += 1;
//			if (prec != null) {
//				if (prec.getMassimo() >= r.getMinimo()) {
//					throw new BusinessException(RANGE_NRO + pos.get(r.getId()) + NON_CORRETTO);
//				}
//				if (crescente) {
//					if (prec.getValore() >= r.getValore()) {
//						throw new BusinessException(RANGE_NRO + pos.get(r.getId()) + NON_CORRETTO);
//					}
//				} else {
//					if (r.getValore() >= prec.getValore()) {
//						throw new BusinessException(RANGE_NRO + pos.get(r.getId()) + NON_CORRETTO);
//					}
//				}
//			}
//			if (r.getId() < 0)
//				r.setId(null);
//			prec = r;
//
//		}
//	}

	@Override
	public void aggiornaPeso(IndicatorePianoDTO dto) throws BusinessException {
		indicatorePianoRepository.aggiornaPeso(dto.getId(), dto.getPeso());
	}

	@Override
	public void elimina(IndicatorePianoDTO dto) throws BusinessException {
		valutazioneRepository.deleteByIndicatorePianoId(dto.getId());
		rangeIndicatoreRepository.deleteByIndicatorePianoId(dto.getId());
		indicatorePianoRepository.deleteById(dto.getId());
	}

	@Override
	public void elimina(Long id) throws BusinessException {
		rangeIndicatoreRepository.deleteByIndicatorePianoId(id);
		valutazioneRepository.deleteByIndicatorePianoId(id);
		indicatorePianoRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public IndicatorePianoDTO leggi(Long id) throws BusinessException {
		IndicatorePianoDTO dto = Mapping.mapping(indicatorePianoRepository.findById(id), IndicatorePianoDTO.class);
		aggiungiValori(dto);
//		aggiungiRanges(dto);
		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<IndicatorePianoDTO> cerca(IndicatorePianoDTO parametri) throws BusinessException {
		return Mapping.mapping(
				indicatorePianoRepository.findAll(Example.of(Mapping.mapping(parametri, IndicatorePiano.class))),
				IndicatorePianoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<IndicatorePianoDTO> list(Long idEnte, Integer anno) throws BusinessException {
		return Mapping.mapping(indicatorePianoRepository
				.findByNodoPianoIdEnteAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(idEnte, anno),
				IndicatorePianoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public int count(Long idEnte, Integer anno) throws BusinessException {
		return indicatorePianoRepository.countByNodoPianoIdEnteAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(idEnte,
				anno);
	}

	@Override
	@Transactional(readOnly = true)
	public List<IndicatorePianoDTO> cercaPerNodo(Long idNodo) throws BusinessException {
		return Mapping.mapping(indicatorePianoRepository.findByNodoPianoId(idNodo), IndicatorePianoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Double peso(Long idNodo) throws BusinessException {
		List<IndicatorePiano> items = indicatorePianoRepository.findByNodoPianoId(idNodo);
		if (items == null)
			return 0d;
		Double tot = 0d;
		for (IndicatorePiano np : items) {
			if (np.getPeso() != null) {
				tot += np.getPeso().doubleValue();
			}
		}
		return tot;
	}

	@Override
	public IndicatorePianoDTO modificaForzatureAdmin(ForzatureAdminRequest request) {
		indicatorePianoRepository.modificaForzatura(request.getIdIndicatorePiano(), request.getRaggiungimentoForzato(),
				request.getNonValutabile(), request.getNoteRaggiungimentoForzato());
		return Mapping.mapping(indicatorePianoRepository.findById(request.getIdIndicatorePiano()),
				IndicatorePianoDTO.class);

	}

	@Override
	public IndicatorePianoDTO modificaForzature(ForzatureRequest request) {
		indicatorePianoRepository.modificaRichiestaForzatura(request.getIdIndicatorePiano(),
				request.getRichiestaForzatura(), request.getNoteRichiestaForzatura());
		return Mapping.mapping(indicatorePianoRepository.findById(request.getIdIndicatorePiano()),
				IndicatorePianoDTO.class);
	}

	private void aggiungiValori(IndicatorePianoDTO indicatore) {
		List<ValutazioneDTO> items = Mapping.mapping(
				valutazioneRepository.findByIndicatorePianoIdOrderByAnnoAscPeriodoAsc(indicatore.getId()),
				ValutazioneDTO.class);
		List<ValutazioneDTO> consuntivi = new ArrayList<>();
		List<ValutazioneDTO> preventivi = new ArrayList<>();
		List<ValutazioneDTO> storici = new ArrayList<>();
		ValutazioneDTO benchmark = null;

		if (items != null) {
			for (ValutazioneDTO item : items) {
				switch (item.getTipoValutazione()) {
					case STORICO:
						storici.add(item);
						break;
					case PREVENTIVO:
						preventivi.add(item);
						break;
					case CONSUNTIVO:
						consuntivi.add(item);
						break;
				}
			}
		}
		indicatore.addConsuntivi(consuntivi);
		indicatore.addPreventivi(preventivi);
		indicatore.addStorici(storici);


	}

//	private void aggiungiRanges(final IndicatorePianoDTO indicatore) {
//		indicatore.getRanges().clear();
//		indicatore.getRanges().addAll(Mapping.mapping(
//				rangeIndicatoreRepository.findByIndicatorePianoId(indicatore.getId()), RangeIndicatoreDTO.class));
//	}

//	void aggiornaRange(final IndicatorePiano indicatore, final IndicatorePianoDTO dto) {
//		List<RangeIndicatore> items = rangeIndicatoreRepository.findByIndicatorePianoId(indicatore.getId());
//		List<RangeIndicatoreDTO> nuovi = dto.getRanges();
//		if (nuovi.isEmpty()) {
//			if (items != null && !items.isEmpty()) {
//				rangeIndicatoreRepository.deleteByIndicatorePianoId(indicatore.getId());
//			}
//			return;
//		} else if (items == null || items.isEmpty()) {
//			for (RangeIndicatoreDTO r : nuovi) {
//				RangeIndicatore e = Mapping.mapping(r, RangeIndicatore.class);
//				e.setIndicatorePiano(indicatore);
//				rangeIndicatoreRepository.save(e);
//			}
//			return;
//		}
//		int n1 = nuovi.size();
//		int n2 = items.size();
//		int n = Math.min(n1, n2);
//		for (int i = 0; i < n; i++) {
//			if (!corrisponde(nuovi.get(i), items.get(i))) {
//				items.get(i).setMinimo(nuovi.get(i).getMinimo());
//				items.get(i).setMassimo(nuovi.get(i).getMassimo());
//				items.get(i).setValore(nuovi.get(i).getValore());
//				items.get(i).setProporzionale(nuovi.get(i).getProporzionale());
//				rangeIndicatoreRepository.save(items.get(i));
//			}
//		}
//		if (n1 > n2) {
//			for (int i = n; i < n1; i++) {
//				RangeIndicatore e = Mapping.mapping(nuovi.get(i), RangeIndicatore.class);
//				e.setIndicatorePiano(indicatore);
//				rangeIndicatoreRepository.save(e);
//			}
//		} else if (n1 < n2) {
//			for (int i = n; i < n2; i++) {
//				rangeIndicatoreRepository.deleteById(items.get(i).getId());
//			}
//		}
//	}

	boolean corrisponde(final RangeIndicatoreDTO nuovo, final RangeIndicatore item) {
		return item.getId().equals(nuovo.getId()) && item.getMinimo().equals(nuovo.getMinimo())
				&& item.getMassimo().equals(nuovo.getMassimo()) && item.getValore().equals(nuovo.getValore())
				&& item.getProporzionale().equals(nuovo.getProporzionale());
	}

	void aggiornaConsuntivi(final IndicatorePiano indicatore, final IndicatorePianoDTO dto) {
		if (dto.getConsuntivi() == null || dto.getConsuntivi().isEmpty())
			return;
		for (ValutazioneDTO consuntivo : dto.getConsuntivi()) {
			Valutazione e = Mapping.mapping(consuntivo, Valutazione.class);
			e.setIndicatorePiano(indicatore);
			e.setTipoValutazione(TipoValutazione.CONSUNTIVO);
			inizializzaZero(e, indicatore);
			valutazioneRepository.save(e);
		}
	}

	void aggiornaStorici(final IndicatorePiano indicatore, final IndicatorePianoDTO dto) {
		if (dto.getStorici() == null || dto.getStorici().isEmpty())
			return;
		for (ValutazioneDTO storico : dto.getStorici()) {
			Valutazione e = Mapping.mapping(storico, Valutazione.class);
			e.setIndicatorePiano(indicatore);
			e.setTipoValutazione(TipoValutazione.STORICO);
			date(e, dto);
			inizializzaZero(e, indicatore);
			valutazioneRepository.save(e);
		}
	}

	private void date(final Valutazione valutazione, final IndicatorePianoDTO dto) {
		if (valutazione.getInizio() == null)
			valutazione.setInizio(ConsuntivoHelper.inizioAnno(dto.getAnno()));
		if (valutazione.getScadenza() == null)
			valutazione.setScadenza(ConsuntivoHelper.fineAnno(dto.getAnno()));
	}


	private void inizializzaZero(final Valutazione val, final IndicatorePiano indicatore) {
		if (val == null || indicatore == null)
			return;
		if (val.getValoreNumerico() == null
				&& (TipoFormula.TIPO_FORMULA_NUMERO.equals(indicatore.getIndicatore().getTipoFormula())))
			val.setValoreNumerico(BigDecimal.ZERO);
		if (val.getValoreNumericoA() == null
				&& TipoFormula.TIPO_FORMULA_RAPPORTO.equals(indicatore.getIndicatore().getTipoFormula()))
			val.setValoreNumericoA(BigDecimal.ZERO);

	}

	void aggiornaPreventivi(final IndicatorePiano indicatore, final IndicatorePianoDTO dto) {
		if (dto.getPreventivi() == null || dto.getPreventivi().isEmpty())
			return;
		for (ValutazioneDTO preventivo : dto.getPreventivi()) {
			Valutazione e = Mapping.mapping(preventivo, Valutazione.class);
			e.setIndicatorePiano(indicatore);
			e.setTipoValutazione(TipoValutazione.PREVENTIVO);
			date(e, dto);
			inizializzaZero(e, indicatore);
			valutazioneRepository.save(e);

		}
	}

	@Override
	@Transactional(readOnly = true)
	public IndicatorePianoDetailVM read(Long id) throws BusinessException {
		IndicatorePiano ip = indicatorePianoRepository.findById(id)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Indicatore non trovato:" + id));
		IndicatorePianoDetailVM out = Mapping.mapping(ip, IndicatorePianoDetailVM.class);
		out.setSpecifica(ip.getSpecifica() == null ? ip.getIndicatore().getDenominazione() : ip.getSpecifica());
		out.setSpecificaNumeratore(ip.getSpecificaNumeratore() == null ? ip.getIndicatore().getNomeValoreA()
				: ip.getSpecificaNumeratore());
		out.setSpecificaDenominatore(ip.getSpecificaDenominatore() == null ? ip.getIndicatore().getNomeValoreB()
				: ip.getSpecificaDenominatore());
		out.setSpecificaPercentuale(ip.getSpecificaPercentuale() == null ? ip.getIndicatore().getPercentuale()
				: ip.getSpecificaPercentuale());
		out.setDecrescente(ip.getDecrescente() == null ? ip.getIndicatore().getDecrescente() : ip.getDecrescente());
		out.setIndicatore(Mapping.mapping(ip.getIndicatore(), IndicatoreVM.class));
		out.setIdNodoPiano(ip.getNodoPiano().getId());
		out.setPeriodicitaTarget(periodicitaTarget(ip.getNodoPiano().getTipoNodo()));
		out.setSpecificaDecimali(
				ip.getSpecificaDecimali() == null ? ip.getIndicatore().getDecimali() : ip.getSpecificaDecimali());
		out.setSpecificaDecimaliA(
				ip.getSpecificaDecimaliA() == null ? ip.getIndicatore().getDecimaliA() : ip.getSpecificaDecimaliA());
		out.setSpecificaDecimaliB(
				ip.getSpecificaDecimaliB() == null ? ip.getIndicatore().getDecimaliB() : ip.getSpecificaDecimaliB());
		out.setOrganizzazione(Mapping.mapping(ip.getOrganizzazione(), StrutturaVM.class));
		return out;
	}

	@Override
	public Long create(Long idNodoPiano, CreaIndicatorePianoRequest request) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(idNodoPiano)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Nodo non trovato:" + idNodoPiano));
		Indicatore ind = indicatoreRepository.findById(request.getIdIndicatore())
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
						"Indicatore non trovato:" + request.getIdIndicatore()));
		IndicatorePiano ip = Mapping.mapping(request, IndicatorePiano.class);
		ip.setOrganizzazione(request.getOrganizzazione() == null ? null
				: organizzazioneRepository.findById(request.getOrganizzazione()).orElse(null));
		ip.setNodoPiano(np);
		ip.setIndicatore(ind);
		ip = indicatorePianoRepository.save(ip);
		return ip.getId();
	}

	@Override
	public void update(Long id, AggiornaIndicatorePianoRequest request) {
		IndicatorePiano ip = indicatorePianoRepository.findById(id).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Indicatore obiettivo non trovato:" + id));
		Indicatore ind = indicatoreRepository.findById(request.getIdIndicatore())
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
						"Indicatore non trovato:" + request.getIdIndicatore()));
		boolean pulisci = !ip.getIndicatore().getTipoFormula().equals(ind.getTipoFormula());
		boolean a = Boolean.FALSE.equals(ip.getDecrescente());
		boolean b = Boolean.FALSE.equals(request.getDecrescente());
		boolean pulisciRange = (a != b);
		Mapping.mapping(request, ip);
		ip.setIndicatore(ind);
		ip.setOrganizzazione(request.getOrganizzazione() == null ? null
				: organizzazioneRepository.findById(request.getOrganizzazione()).orElse(null));
		ip = indicatorePianoRepository.save(ip);
		if (pulisci) {
			valutazioneRepository.deleteByIndicatorePianoId(ip.getId());
		}
		if (pulisci || pulisciRange) {
			rangeIndicatoreRepository.deleteByIndicatorePianoId(ip.getId());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public IndicatoreTargetVM readTarget(Long idIndicatorePiano) throws BusinessException {
		final IndicatorePiano ip = indicatorePianoRepository.findById(idIndicatorePiano)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
						"Indicatore obiettivo non trovato:" + idIndicatorePiano));
		List<Valutazione> valutazioni = valutazioneRepository
				.findByIndicatorePianoIdOrderByAnnoAscPeriodoAscDataRilevazioneAsc(idIndicatorePiano);
		IndicatoreTargetVM out = new IndicatoreTargetVM();
		final Periodicita periodicitaTarget = periodicitaTarget(ip.getNodoPiano().getTipoNodo());
		out.setFormula(ip.getIndicatore().getTipoFormula());
		out.setDenominazione(
				StringUtils.isAllBlank(ip.getSpecifica()) ? ip.getIndicatore().getDenominazione() : ip.getSpecifica());
		out.setNomeNumeratore(StringUtils.isAllBlank(ip.getSpecificaNumeratore()) ? ip.getIndicatore().getNomeValoreA()
				: ip.getSpecificaNumeratore());
		out.setNomeDenominatore(
				StringUtils.isAllBlank(ip.getSpecificaDenominatore()) ? ip.getIndicatore().getNomeValoreB()
						: ip.getSpecificaDenominatore());
		out.setPercentuale(ip.getSpecificaPercentuale() == null ? ip.getIndicatore().getPercentuale()
				: ip.getSpecificaPercentuale());
		out.setDecrescente(ip.getDecrescente() == null ? ip.getIndicatore().getDecrescente() : ip.getDecrescente());
		out.setIdIndicatorePiano(ip.getId());
		out.setPeso(ip.getPeso());
		out.setAnno(ip.getNodoPiano().getAnno());
		out.setAnnoInizio(ip.getNodoPiano().getPiano().getAnnoInizio());
		out.setAnnoFine(ip.getNodoPiano().getPiano().getAnnoFine());
		List<ValoreDetailVM> targets = new ArrayList<>();
		List<ValoreDetailVM> storici = new ArrayList<>();

		out.setPeriodicitaTarget(periodicitaTarget);
		out.setUnitaMisura(ip.getUnitaMisura());
		if (Periodicita.NESSUNO.equals(periodicitaTarget)) {
			return out;
		}
		if (Periodicita.FINALE.equals(periodicitaTarget)) {
			out.setAnnoInizioTarget(out.getAnnoFine());
			out.setAnnoFineTarget(out.getAnnoFine());
		} else {
			out.setAnnoInizioTarget(out.getAnnoInizio());
			out.setAnnoFineTarget(out.getAnnoFine());
		}

		Integer anno = ip.getNodoPiano().getAnno();

		if (valutazioni != null) {
			for (Valutazione v : valutazioni) {
				switch (v.getTipoValutazione()) {
					case STORICO:
						storici.add(MappingIndicatorePianoHelper.mapping(v));
						break;
					case PREVENTIVO:
						targets.add(MappingIndicatorePianoHelper.mapping(v));
						break;
				}

			}
		}

		out.setTargets(completaTargets(targets, ip.getIndicatore(), ip.getNodoPiano(), periodicitaTarget));

		out.setStorici(completaStorici(storici, ip.getIndicatore(), ip.getNodoPiano(), periodicitaTarget));

		int annoMax = 0;
		if (Periodicita.STORICO.equals(periodicitaTarget)) {
			for (ValoreDetailVM v : out.getStorici()) {
				if (v.getAnno() > annoMax) {
					annoMax = v.getAnno();
					out.setStorico(v);
				}
			}
		}


		return out;
	}

	private Periodicita periodicitaTarget(final TipoNodo tipoNodo) {
		int tipoNodoIndex = 0;
		for (int i = 0; i < tipiNodo.length; i++) {
			if (tipiNodo[i].equals(tipoNodo)) {
				tipoNodoIndex = i;
				break;
			}
		}
		return periodiTarget[tipoNodoIndex];
	}

	private ValoreDetailVM completa(final ValoreDetailVM v, final Integer anno) {
		ValoreDetailVM detail = new ValoreDetailVM();
		detail.setAnno(anno);
		detail.setPeriodo(4);
		detail.setEnabled(true);

		if (v != null) {
			detail.setValoreNumerico(v.getValoreNumerico());
			detail.setValoreNumericoA(v.getValoreNumericoA());
			detail.setValoreBooleano(v.getValoreBooleano());
			detail.setValoreNumericoB(v.getValoreNumericoB());
			detail.setValoreTemporale(v.getValoreTemporale());
			detail.setPercentuale(v.getPercentuale());
		}
		return detail;
	}

	private List<ValoreDetailVM> completaTargets(final List<ValoreDetailVM> targets, final Indicatore indicatore,
			final NodoPiano nodoPiano, final Periodicita periodicitaTarget) {
		int n=2;
		final boolean strategico = Boolean.TRUE.equals(nodoPiano.getStrategico());
		final int annoDa = ConsuntivoHelper.getAnno(nodoPiano.getInizio());
		final int annoA = ConsuntivoHelper.getAnno(nodoPiano.getScadenza());
		final int annoInizio = Periodicita.FINALE.equals(periodicitaTarget) ? nodoPiano.getPiano().getAnnoFine()
				: nodoPiano.getPiano().getAnnoInizio();
		final int annoFine = nodoPiano.getPiano().getAnnoFine();
		final int annoIncorso = nodoPiano.getAnno();
		int periodoFinale = ConsuntivoHelper.getPeriodo(nodoPiano.getScadenza().getMonthValue(),
				TipoConsuntivazione.SEMESTRE);
		if(periodoFinale>1)periodoFinale=4;
		int da = Math.max(annoDa, annoInizio);
		int a = Math.min(annoA, annoFine);
		List<ValoreDetailVM> items = new ArrayList<>();

		Map<String, ValoreDetailVM> map = new HashMap<>();
		for (int anno = annoInizio; anno <= annoFine; anno++) {
			for (int periodo = 1; periodo <= 4; periodo++) {
				ValoreDetailVM v = new ValoreDetailVM();
				v.setAnno(anno);
				v.setPeriodo(periodo);
				if (anno > a || anno < da || (anno == a && periodo > periodoFinale)) {
					v.setEnabled(false);
				}
				if (!(periodo == 2 || periodo == 4 || (anno == a && periodo == periodoFinale))) {
					v.setEnabled(false);
				}
				if (Boolean.TRUE.equals(v.getEnabled()) && Periodicita.ANNUALE.equals(periodicitaTarget)) {
					if ((periodo != 4 && anno < a) || (periodo != periodoFinale && anno == a)) {
						v.setEnabled(false);
					}
				}
				if (Boolean.TRUE.equals(v.getEnabled()) && Periodicita.FINALE.equals(periodicitaTarget)) {
					if (periodo != periodoFinale || anno != a) {
						v.setEnabled(false);
					}
				}
				if (Periodicita.NESSUNO.equals(periodicitaTarget) || Periodicita.STORICO.equals(periodicitaTarget)) {
					v.setEnabled(false);
				}
				items.add(v);
				map.put(anno + "/" + periodo, v);
			}

		}
		for (ValoreDetailVM t : targets) {
			ValoreDetailVM v = map.get(t.getAnno() + "/" + t.getPeriodo());
			if (v != null && (strategico || t.getPeriodo() == 2 || t.getPeriodo() == 4
					|| (t.getAnno().intValue() == a && t.getPeriodo() == periodoFinale))) {
				v.setValoreNumerico(t.getValoreNumerico());
				v.setValoreNumericoA(t.getValoreNumericoA());
				v.setValoreBooleano(t.getValoreBooleano());
				v.setValoreNumericoB(t.getValoreNumericoB());
				v.setValoreTemporale(t.getValoreTemporale());
				v.setPercentuale(t.getPercentuale());

			}
		}
		return items;
	}



	private List<ValoreDetailVM> completaStorici(final List<ValoreDetailVM> storici, final Indicatore indicatore,
			final NodoPiano nodoPiano, final Periodicita periodicitaTarget) {
		final List<ValoreDetailVM> items = new ArrayList<>();

		final int annoDa = ConsuntivoHelper.getAnno(nodoPiano.getInizio());

		Map<Integer, ValoreDetailVM> map = new HashMap<>();
		ValoreDetailVM v = new ValoreDetailVM();
		v.setAnno(annoDa);
		v.setPeriodo(4);
		items.add(v);
		map.put(annoDa, v);

		if (storici == null) {
			return items;
		}
		for (ValoreDetailVM t : storici) {
			v = map.get(t.getAnno());
			if (v != null) {
				v.setValoreNumerico(t.getValoreNumerico());
				v.setValoreNumericoA(t.getValoreNumericoA());
				v.setValoreBooleano(t.getValoreBooleano());
				v.setValoreNumericoB(t.getValoreNumericoB());
				v.setValoreTemporale(t.getValoreTemporale());
				v.setPercentuale(t.getPercentuale());
			} else {
				items.add(t);
			}
		}
		return items;
	}

	@Override
	@Transactional(readOnly = true)
	public List<IndicatoreRangeVM> readRange(Long idIndicatorePiano) throws BusinessException {
		final IndicatorePiano ip = indicatorePianoRepository.findById(idIndicatorePiano)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
						"Indicatore obiettivo non trovato:" + idIndicatorePiano));
		final List<RangeIndicatore> ranges = rangeIndicatoreRepository.findByIndicatorePianoId(idIndicatorePiano);
		final List<IndicatoreRangeVM> out = Mapping.mapping(ranges, IndicatoreRangeVM.class);
		if (Boolean.TRUE.equals(ip.getDecrescente())) {
			out.sort(Comparator.comparing(IndicatoreRangeVM::getMinimo).reversed());
		} else {
			out.sort(Comparator.comparing(IndicatoreRangeVM::getMinimo));
		}
		return out;
	}

	@Override
	public void saveTarget(Long idIndicatorePiano, SaveIndicatoreTargetRequest request) throws BusinessException {
		final IndicatorePiano ip = indicatorePianoRepository.findById(idIndicatorePiano)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
						"Indicatore obiettivo non trovato:" + idIndicatorePiano));
		Integer anno = ip.getNodoPiano().getAnno();
		Integer annoFine = ip.getNodoPiano().getPiano().getAnnoFine();
		if (annoFine > ip.getNodoPiano().getScadenza().getYear()) {
			annoFine = ip.getNodoPiano().getScadenza().getYear();
		}

		valida(ip.getNodoPiano().getTipoNodo(), request.getTargets(), anno, annoFine);

		if (request.getStorico() != null && request.getStorico().getAnno() > ip.getNodoPiano().getAnno()) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Anno UDD non puo essere superiore al " + anno);
		}

		aggiornaTarget(ip, request);
	}

	private void valida(final TipoNodo tipoNodo, final List<ValoreDetailVM> targets, final Integer anno,
			final Integer annoFine) throws BusinessException{
		if (targets == null || targets.isEmpty()) {
			if (!TipoNodo.AREA.equals(tipoNodo)) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, "Mancano target");
			}
		}
		if (TipoNodo.AZIONE.equals(tipoNodo)) {
			boolean trovato = false;
			for (ValoreDetailVM v : targets) {
				if (v.getAnno().equals(anno)) {
					trovato = v.getValoreBooleano()!=null||v.getValoreNumerico()!=null ||
							v.getValoreNumericoA()!=null||v.getValoreTemporale()!=null;			
					break;
				}
			}
			if (!trovato) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, "Mancano target anno " + anno);

			}
		}
		if (TipoNodo.OBIETTIVO.equals(tipoNodo)) {
			boolean trovato = false;
			for (ValoreDetailVM v : targets) {
				if (v.getAnno().equals(annoFine)) {
					trovato = v.getValoreBooleano()!=null||v.getValoreNumerico()!=null ||
							v.getValoreNumericoA()!=null||v.getValoreTemporale()!=null;	
					break;
				}
			}
			if (!trovato) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, "Mancano target anno " + annoFine);
			}
		}

	}


	@Override
	public void saveRange(Long idIndicatorePiano, SaveIndicatoreRangeRequest request) throws BusinessException {
		final IndicatorePiano ip = indicatorePianoRepository.findById(idIndicatorePiano)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
						"Indicatore obiettivo non trovato:" + idIndicatorePiano));
		verificaRanges(request.getRanges(), !Boolean.TRUE.equals(ip.getDecrescente()));
		aggiornaRanges(ip, request.getRanges());
	}

	@Override
	public List<IndicatorePianoListVM> indicatori(Long idNodoPiano) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(idNodoPiano)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Nodo non trovato:" + idNodoPiano));
		List<IndicatorePianoListVM> out = null;
		List<Valutazione> valutazioni = valutazioneRepository
				.findByIndicatorePianoNodoPianoIdOrderByIndicatorePianoIdAscAnnoAscPeriodoAsc(idNodoPiano);
		int anno = np.getAnno();
		int annoInizio = np.getPiano().getAnnoInizio();
		int annoScadenza = np.getScadenza().getYear();
		int annoFine = np.getPiano().getAnnoFine();
		 int periodoFinale = ConsuntivoHelper.getPeriodo(np.getScadenza().getMonthValue(),
				TipoConsuntivazione.SEMESTRE);
		 if(periodoFinale>1)periodoFinale=4;

		List<IndicatorePiano> items = indicatorePianoRepository.findByNodoPianoId(idNodoPiano);
		if (items == null || items.isEmpty())
			return new ArrayList<>();
		final Periodicita periodicitaTarget = periodicitaTarget(np.getTipoNodo());
		out = MappingIndicatorePianoHelper.mappingToList(items, periodicitaTarget);
		Map<Long, IndicatorePianoListVM> map = new HashMap<>();
		for (Valutazione v : valutazioni) {
			if (TipoValutazione.PREVENTIVO.equals(v.getTipoValutazione()) && v.getPeriodo() != null
					&& v.getPeriodo() < 4 && v.getAnno().intValue() < annoScadenza)
				continue;
			if (TipoValutazione.PREVENTIVO.equals(v.getTipoValutazione()) && v.getPeriodo() != null
					&& v.getPeriodo() != periodoFinale && v.getAnno().intValue() == annoScadenza)
				continue;
			IndicatorePiano ip = v.getIndicatorePiano();
			IndicatorePianoListVM item = map.get(ip.getId());
			if (item == null) {
				for (IndicatorePianoListVM d : out) {
					if (d.getId().equals(v.getIndicatorePiano().getId())) {
						item = d;
						break;
					}
				}
				map.put(ip.getId(), item);
			}
			MappingIndicatorePianoHelper.mapping(v, item, anno, annoInizio, annoFine);
		}

		final float[] pesoTot = new float[] { 0 };
		logger.info("---- pesoTot:{}, out: {}", pesoTot[0], out);
		out.forEach(ip -> pesoTot[0] = pesoTot[0] + (ip.getPeso() == null ? 0f : ip.getPeso()));
		out.forEach(ip -> ip.setPesoRelativo(
				ip.getPeso() == null ? 0f : ConsuntivoHelper.pesoRelativo(ip.getPeso(), pesoTot[0]).floatValue()));
		out.sort(Comparator.comparing(IndicatorePianoListVM::getDenominazione));
		return out;
	}

	private void aggiornaRanges(final IndicatorePiano indicatore, final List<IndicatoreRangeVM> nuovi) {
		List<RangeIndicatore> items = rangeIndicatoreRepository.findByIndicatorePianoId(indicatore.getId());
		if (nuovi.isEmpty()) {
			if (items != null && !items.isEmpty()) {
				rangeIndicatoreRepository.deleteByIndicatorePianoId(indicatore.getId());
			}
			return;
		} else if (items == null || items.isEmpty()) {
			for (IndicatoreRangeVM r : nuovi) {
				RangeIndicatore e = Mapping.mapping(r, RangeIndicatore.class);
				e.setIndicatorePiano(indicatore);
				rangeIndicatoreRepository.save(e);
			}
			return;
		}
		int n1 = nuovi.size();
		int n2 = items.size();
		int n = Math.min(n1, n2);
		for (int i = 0; i < n; i++) {
			if (!corrisponde(nuovi.get(i), items.get(i))) {
				items.get(i).setMinimo(nuovi.get(i).getMinimo());
				items.get(i).setMassimo(nuovi.get(i).getMassimo());
				items.get(i).setValore(nuovi.get(i).getValore());
				items.get(i).setProporzionale(nuovi.get(i).getProporzionale());
				rangeIndicatoreRepository.save(items.get(i));
			}
		}
		if (n1 > n2) {
			for (int i = n; i < n1; i++) {
				RangeIndicatore e = Mapping.mapping(nuovi.get(i), RangeIndicatore.class);
				e.setIndicatorePiano(indicatore);
				rangeIndicatoreRepository.save(e);
			}
		} else if (n1 < n2) {
			for (int i = n; i < n2; i++) {
				rangeIndicatoreRepository.deleteById(items.get(i).getId());
			}
		}
	}

	private boolean corrisponde(final IndicatoreRangeVM nuovo, final RangeIndicatore item) {
		return item.getId().equals(nuovo.getId()) && item.getMinimo().equals(nuovo.getMinimo())
				&& item.getMassimo().equals(nuovo.getMassimo()) && item.getValore().equals(nuovo.getValore())
				&& item.getProporzionale().equals(nuovo.getProporzionale());
	}

	private void aggiornaTarget(final IndicatorePiano ip, final SaveIndicatoreTargetRequest request) {
		TipoFormula formula = ip.getIndicatore().getTipoFormula();
		Integer decimali = MappingIndicatorePianoHelper.getDecimali(ip, ip.getIndicatore());
		boolean percentuale = ip.getSpecificaPercentuale() == null ? ip.getIndicatore().getPercentuale()
				: ip.getSpecificaPercentuale();
		if (percentuale) {
			if (request.getStorico() != null && request.getStorico().getValoreNumerico() != null
					&& request.getStorico().getValoreNumerico() > 1) {
				request.getStorico().setValoreNumerico(BigDecimal.valueOf(request.getStorico().getValoreNumerico())
						.divide(BigDecimal.valueOf(100d), decimali, RoundingMode.HALF_DOWN).doubleValue());
			}
			if (request.getTargets() != null) {
				for (ValoreDetailVM t : request.getTargets()) {
					if (t.getValoreNumerico() != null && t.getValoreNumerico() > 1) {
						t.setValoreNumerico(BigDecimal.valueOf(t.getValoreNumerico())
								.divide(BigDecimal.valueOf(100d), decimali, RoundingMode.HALF_DOWN).doubleValue());
					}
				}
			}
		}
		aggiorna(request.getStorico(), ip, TipoValutazione.STORICO, formula, decimali);
		if (request.getTargets() != null) {
			validate(request.getTargets());
			for (ValoreDetailVM t : request.getTargets()) {
				aggiorna(t, ip, TipoValutazione.PREVENTIVO, formula, decimali);
			}
		}
		aggiornaPeso(ip, request);
	}

	private void validate(List<ValoreDetailVM> targets) throws BusinessException {
		double p = 0d;
		targets.sort(new Comparator<ValoreDetailVM>() {

			@Override
			public int compare(ValoreDetailVM o1, ValoreDetailVM o2) {
				int c = o1.getAnno().compareTo(o2.getAnno());
				if (c == 0)
					c = o1.getPeriodo().compareTo(o2.getPeriodo());
				return c;
			}
		});
		for (ValoreDetailVM v : targets) {
			if (v.getValoreNumerico() != null) {
				if (p > v.getValoreNumerico())
					throw new BusinessException(HttpStatus.BAD_REQUEST, "I valori target non sono crescenti");
				p = v.getValoreNumerico();
			}

		}

	}

	private void aggiornaPeso(final IndicatorePiano ip, final SaveIndicatoreTargetRequest request) {
		Double peso = request.getPeso() == null ? 100d : request.getPeso();
		if (!peso.equals(ip.getPeso()))
			indicatorePianoRepository.aggiornaPeso(ip.getId(), request.getPeso());
	}

	private void aggiorna(final ValoreDetailVM v, final IndicatorePiano ip, final TipoValutazione tipoValutazione,
			final TipoFormula formula, final int decimali) throws BusinessException {
		if (v == null)
			return;
		if (Boolean.FALSE.equals(v.getEnabled())) {
			return;
		}
		Valutazione old = null;
		if (v.getId() != null) {
			old = valutazioneRepository.findById(v.getId()).orElseThrow(
					() -> new BusinessException(HttpStatus.NOT_FOUND, "valutazione non trovata:" + v.getId()));
		}

		if (!v.isValid()) {
			if (v.getId() != null) {
				valutazioneRepository.deleteById(v.getId());
			}
			return;
		}

		NodoPiano np = ip.getNodoPiano();
		Integer anno = np.getAnno();
		Valutazione val = null;
		Valutazione top = null;
		Valutazione valAnnoPeriodo = null;

		if (TipoValutazione.STORICO.equals(tipoValutazione)) {
			v.setPeriodo(4);
		}
		valAnnoPeriodo = valutazioneRepository.findByIndicatorePianoIdAndTipoValutazioneAndAnnoAndPeriodo(ip.getId(),
				tipoValutazione, v.getAnno(), v.getPeriodo());

		if (old != null) {
			val = old;
		} else if (valAnnoPeriodo != null) {
			val = valAnnoPeriodo;
		} else {
			val = new Valutazione();
			val.setAnno(v.getAnno());
			val.setDataRilevazione(
					ConsuntivoHelper.finePeriodo(v.getAnno(), v.getPeriodo(), TipoConsuntivazione.SEMESTRE));
			val.setIndicatorePiano(ip);
			val.setTipoValutazione(tipoValutazione);
			val.setInizio(ConsuntivoHelper.inizioPeriodo(v.getAnno(), v.getPeriodo(), TipoConsuntivazione.SEMESTRE));
			val.setScadenza(ConsuntivoHelper.finePeriodo(v.getAnno(), v.getPeriodo(), TipoConsuntivazione.SEMESTRE));
			val.setPeriodo(v.getPeriodo());
		}

		if (valAnnoPeriodo != null && val != null && !valAnnoPeriodo.getId().equals(val.getId())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST,
					"Valutazione esistente per anno:" + val.getAnno() + " e periodo:" + val.getPeriodo());
		}

		if (TipoValutazione.STORICO.equals(tipoValutazione)) {
			top = valutazioneRepository.findTopByIndicatorePianoIdAndTipoValutazioneAndAnnoGreaterThan(ip.getId(),
					tipoValutazione, v.getAnno());
			if (top != null && top.getAnno() > val.getAnno()) {
				valutazioneRepository.deleteById(top.getId());
			}
		}

		val.setAnno(v.getAnno());
		val.setValoreBooleano(v.getValoreBooleano());
		val.setValoreNumerico(v.getValoreNumerico() == null ? null : BigDecimal.valueOf(v.getValoreNumerico()));
		val.setValoreNumericoA(v.getValoreNumericoA() == null ? null : BigDecimal.valueOf(v.getValoreNumericoA()));
		val.setValoreNumericoB(v.getValoreNumericoB() == null ? null : BigDecimal.valueOf(v.getValoreNumericoB()));
		val.setValoreTemporale(v.getValoreTemporale());

		if (TipoFormula.TIPO_FORMULA_RAPPORTO.equals(formula)) {
			if (v.getValoreNumericoB() == null || v.getValoreNumericoB() == 0 || v.getValoreNumericoA() == null
					|| v.getValoreNumericoA() == 0) {
				val.setValoreNumerico(BigDecimal.valueOf(0d));
			} else {
				val.setValoreNumerico(
						val.getValoreNumericoA().divide(val.getValoreNumericoB(), decimali, RoundingMode.HALF_DOWN));
			}
		}

		valutazioneRepository.save(val);

	}

	private void verificaRanges(final List<IndicatoreRangeVM> ranges, final boolean crescente)
			throws BusinessException {
		if (ranges == null || ranges.isEmpty()) {
			return;
		}
		int k = 0;
		IndicatoreRangeVM prec = null;
		Map<Long, Integer> pos = new Hashtable<>();
		for (IndicatoreRangeVM r : ranges) {
			k += 1;
			if (r.getMinimo() == null || r.getMassimo() == null || r.getValore() == null
					|| r.getMinimo() > r.getMassimo()) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, RANGE_NRO + k + NON_CORRETTO);
			}
			if (r.getId() == null)
				r.setId((long) -k);
			pos.put(r.getId(), k);
		}
		ranges.sort(new Comparator<IndicatoreRangeVM>() {

			@Override
			public int compare(IndicatoreRangeVM o1, IndicatoreRangeVM o2) {
				if (o1.getMinimo().compareTo(o2.getMinimo()) > 0) {
					return 1;
				} else if (o1.getMinimo().compareTo(o2.getMinimo()) < 0) {
					return -1;
				} else if (o1.getMassimo().compareTo(o2.getMassimo()) > 0) {
					return 1;
				} else if (o1.getMassimo().compareTo(o2.getMassimo()) < 0) {
					return -1;
				}
				return 0;
			}
		});

		k = 0;
		prec = null;
		for (IndicatoreRangeVM r : ranges) {
			k += 1;
			if (prec != null) {
				if (prec.getMassimo() >= r.getMinimo()) {
					throw new BusinessException(HttpStatus.BAD_REQUEST, RANGE_NRO + pos.get(r.getId()) + NON_CORRETTO);
				}
				if (crescente) {
					if (prec.getValore() >= r.getValore()) {
						throw new BusinessException(HttpStatus.BAD_REQUEST,
								RANGE_NRO + pos.get(r.getId()) + NON_CORRETTO);
					}
				} else {
					if (r.getValore() >= prec.getValore()) {
						throw new BusinessException(HttpStatus.BAD_REQUEST,
								RANGE_NRO + pos.get(r.getId()) + NON_CORRETTO);
					}
				}
			}
			if (r.getId() < 0)
				r.setId(null);
			prec = r;

		}
	}

}
