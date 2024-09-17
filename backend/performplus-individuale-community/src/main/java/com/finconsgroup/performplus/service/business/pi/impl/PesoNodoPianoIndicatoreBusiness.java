package com.finconsgroup.performplus.service.business.pi.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.domain.RegistrazioneIndicatorePiano;
import com.finconsgroup.performplus.domain.RegistrazioneNodoPiano;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaNodoPiano;
import com.finconsgroup.performplus.enumeration.TipoRegolamento;
import com.finconsgroup.performplus.repository.IndicatorePianoRepository;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.RegistrazioneIndicatorePianoRepository;
import com.finconsgroup.performplus.repository.RegistrazioneNodoPianoRepository;
import com.finconsgroup.performplus.repository.RegistrazioneRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaNodoPianoRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.SchedaValutazioneIndicatoreRepository;
import com.finconsgroup.performplus.repository.SchedaValutazioneNodoRepository;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaPesiRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaPeso;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaPesoIndicatorePianoRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaPesoNodoPianoRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.PesoIndicatoreRegistrazioneVM;
import com.finconsgroup.performplus.rest.api.pi.vm.PesoNodoPianoRegistrazioneVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.pi.IPesoNodoPianoIndicatoreBusiness;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;

@Service
@Transactional
public class PesoNodoPianoIndicatoreBusiness implements IPesoNodoPianoIndicatoreBusiness {

	@Autowired
	private RegistrazioneIndicatorePianoRepository registrazioneIndicatorePianoRepository;
	@Autowired
	private RegistrazioneNodoPianoRepository registrazioneNodoPianoRepository;
	@Autowired
	private NodoPianoRepository nodoPianoRepository;
	@Autowired
	private IndicatorePianoRepository indicatorePianoRepository;
	@Autowired
	private RisorsaUmanaRepository risorsaUmanaRepository;
	@Autowired
	private RegistrazioneRepository registrazioneRepository;
	@Autowired
	private RisorsaUmanaNodoPianoRepository risorsaUmanaNodoPianoRepository;
	@Autowired
	private SchedaValutazioneIndicatoreRepository schedaValutazioneIndicatoreRepository;
	@Autowired
	private SchedaValutazioneNodoRepository schedaValutazioneNodoRepository;

	private static final Logger logger = LoggerFactory.getLogger(PesoNodoPianoIndicatoreBusiness.class);

	@Override
	public void aggiornaPesoNodo(AggiornaPesoNodoPianoRegistrazioneRequest request) throws BusinessException {
		if (request.getPeso() == null || request.getPeso().floatValue() < 0 || request.getPeso().floatValue() > 100) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "peso non valido:" + request.getPeso());
		}
		RegistrazioneNodoPiano val = null;
		final Registrazione registrazione = registrazioneRepository.findById(request.getIdRegistrazione())
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
						"registrazione non trovata:" + request.getIdRegistrazione()));
		NodoPiano nodo = nodoPianoRepository.findById(request.getIdNodo()).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "nodo non trovato:" + request.getIdNodo()));
		val = registrazioneNodoPianoRepository.findByRegistrazioneAndNodoPiano(registrazione, nodo);
		BigDecimal pesoOld = BigDecimal.ZERO;
		if (request.getId() != null) {
			RegistrazioneNodoPiano old = registrazioneNodoPianoRepository.findById(request.getId())
					.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
							"peso nodo registrazione non trovato:" + request.getId()));
			if (val != null && !old.getId().equals(val.getId())) {
				throw new BusinessException(HttpStatus.BAD_REQUEST,
						"Registrazione o nodo non corrispondono ad elemento da monificare:" + request.getId());
			}
			if (old.getPeso() != null && request.getPeso().floatValue() == old.getPeso().floatValue()) {
				return;
			}
			val = old;
		}
		if (val != null && val.getPeso() != null)
			pesoOld = val.getPeso();

		BigDecimal peso = BigDecimal.valueOf(request.getPeso());
		BigDecimal totalePeso = registrazioneNodoPianoRepository.sumByRegistrazioneId(registrazione.getId());
		if (totalePeso == null)
			totalePeso = BigDecimal.ZERO;
		totalePeso = totalePeso.add(peso).subtract(pesoOld);
		if (totalePeso.floatValue() > 100) {
			throw new BusinessException(HttpStatus.BAD_REQUEST,
					"peso totale nodi valutato maggiore di 100:" + totalePeso.floatValue());
		}
		if (val == null) {
			val = new RegistrazioneNodoPiano();
			val.setNodoPiano(nodo);
			val.setRegistrazione(registrazione);
			val.setPeso(peso);
			if (Boolean.TRUE.equals(registrazione.getResponsabile())) {
				val.setTipo(TipoRegolamento.INDIVIDUALE);
			} else {
				if (risorsaUmanaNodoPianoRepository.existsByRisorsaUmanaAndNodoPiano(registrazione.getValutato(),
						nodo)) {
					val.setTipo(TipoRegolamento.INDIVIDUALE);
				} else {
					val.setTipo(TipoRegolamento.STRUTTURA);
				}
			}
			val = registrazioneNodoPianoRepository.save(val);
		} else {
			registrazioneNodoPianoRepository.updatePeso(peso, val.getId());
		}
	}

	@Override
	public void aggiornaPesoIndicatore(AggiornaPesoIndicatorePianoRegistrazioneRequest request)
			throws BusinessException {
		if (request.getPeso() == null || request.getPeso().floatValue() < 0 || request.getPeso().floatValue() > 100) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "peso non valido:" + request.getPeso());
		}
		RegistrazioneIndicatorePiano val = null;
		RegistrazioneIndicatorePiano old = null;
		RegistrazioneNodoPiano registrazioneNodoPiano;
		final Registrazione registrazione = registrazioneRepository.findById(request.getIdRegistrazione())
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
						"registrazione non trovata:" + request.getIdRegistrazione()));
		IndicatorePiano ip = indicatorePianoRepository.findById(request.getIdIndicatorePiano())
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
						"indicatore non trovato:" + request.getIdIndicatorePiano()));
		if (request.getId() != null) {
			old = registrazioneIndicatorePianoRepository.findById(request.getId())
					.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
							"peso indicatore registrazione non trovato:" + request.getId()));
			registrazioneNodoPiano = old.getRegistrazioneNodoPiano();

		} else {
			registrazioneNodoPiano = registrazioneNodoPianoRepository.findByRegistrazioneAndNodoPiano(registrazione,
					ip.getNodoPiano());
			if (registrazioneNodoPiano == null) {
				registrazioneNodoPiano = new RegistrazioneNodoPiano();
				registrazioneNodoPiano.setNodoPiano(ip.getNodoPiano());
				registrazioneNodoPiano.setPeso(BigDecimal.ZERO);
				registrazioneNodoPiano.setRegistrazione(registrazione);
				registrazioneNodoPiano
						.setTipo(Boolean.TRUE.equals(registrazione.getResponsabile()) ? TipoRegolamento.INDIVIDUALE
								: TipoRegolamento.STRUTTURA);
				registrazioneNodoPiano = registrazioneNodoPianoRepository.save(registrazioneNodoPiano);
			}
		}

		val = registrazioneIndicatorePianoRepository
				.findByRegistrazioneNodoPianoAndIndicatorePiano(registrazioneNodoPiano, ip);
		BigDecimal pesoOld = BigDecimal.ZERO;
		if (old != null) {
			if (val != null && !old.getId().equals(val.getId())) {
				throw new BusinessException(HttpStatus.BAD_REQUEST,
						"Registrazione o indicatore non corrispondono ad elemento da monificare:" + request.getId());
			}
			if (old.getPeso() != null && request.getPeso().floatValue() == old.getPeso().floatValue()) {
				return;
			}

			val = old;
		}
		if (val != null && val.getPeso() != null)
			pesoOld = val.getPeso();
		BigDecimal peso = BigDecimal.valueOf(request.getPeso());
		BigDecimal totalePeso = registrazioneIndicatorePianoRepository
				.sumByRegistrazioneNodoPianoId(registrazioneNodoPiano.getId());
		if (totalePeso == null)
			totalePeso = BigDecimal.ZERO;
		totalePeso = totalePeso.add(peso).subtract(pesoOld);
		if (totalePeso.floatValue() > 100) {
			throw new BusinessException(HttpStatus.BAD_REQUEST,
					"peso totale indicatori nodo registrazione maggiore di 100:" + totalePeso.floatValue());
		}

		if (val == null) {
			val = new RegistrazioneIndicatorePiano();
			val.setIndicatorePiano(ip);
			val.setRegistrazioneNodoPiano(registrazioneNodoPiano);
			val.setPeso(peso);
			val = registrazioneIndicatorePianoRepository.save(val);
		} else {
			registrazioneIndicatorePianoRepository.updatePeso(peso, val.getId());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<PesoNodoPianoRegistrazioneVM> elenco(Long idValutatore, Long idValutato, Long idRegistrazione)
			throws BusinessException {
		/*
		 * elenco obiettivi delle strutture delle registrazioni elenco obiettivi a cui è
		 * associato
		 */
		logger.info("------------>elenco");
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		final List<NodoPiano> nodi = new ArrayList<>();
		final List<PesoNodoPianoRegistrazioneVM> out = new ArrayList<>();
		final Map<Long, PesoNodoPianoRegistrazioneVM> map = new HashMap<>();

		RisorsaUmana valutatore = risorsaUmanaRepository.findById(idValutatore).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "valutatore non trovato:" + idValutatore));
		RisorsaUmana valutato = risorsaUmanaRepository.findById(idValutato)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "valutato non trovato:" + idValutato));
		if (!valutatore.getId().equals(registrazione.getValutatore().getId())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, valutatore.getCognome() + " " + valutatore.getNome()
					+ " non è il valutatore della registrazione:" + idRegistrazione);
		}
		if (!valutato.getId().equals(registrazione.getValutato().getId())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, valutato.getCognome() + " " + valutato.getNome()
					+ " non è il valutato della registrazione:" + idRegistrazione);
		}
		logger.info("----Elabora nodo:" + registrazione.getId());
		elaboraNodo(registrazione, nodi, valutato, out, map);
		for (NodoPiano np : nodi) {
			elaboraIndicatore(registrazione, np, valutato, out, map);
		}
		System.out.println("----Elabora nodi:" + out.size());
		for (PesoNodoPianoRegistrazioneVM pn : out) {
			logger.info("----Elabora nodi:" + pn.getIdNodo() + " indicatrori:"
					+ (pn.getIndicatori() == null ? 0 : pn.getIndicatori().size()));
		}
		return out.stream().filter(t -> t.getIndicatori() != null && !t.getIndicatori().isEmpty())
				.sorted(new Comparator<PesoNodoPianoRegistrazioneVM>() {
					@Override
					public int compare(PesoNodoPianoRegistrazioneVM o1, PesoNodoPianoRegistrazioneVM o2) {
						int c = (o1.getDenominazioneNodo()).compareTo(o2.getDenominazioneNodo());
						if (c != 0)
							return c;
						return Float.valueOf(o2.getPeso() == null ? 0f : o2.getPeso())
								.compareTo(Float.valueOf(o1.getPeso() == null ? 0f : o1.getPeso()));
					}
				}).collect(Collectors.toList());
	}

	@Override
	public void aggiornaPesi(AggiornaPesiRegistrazioneRequest request) throws BusinessException {
		final Registrazione registrazione = registrazioneRepository.findById(request.getIdRegistrazione())
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
						"registrazione non trovata:" + request.getIdRegistrazione()));
		List<RegistrazioneNodoPiano> oldRnp = registrazioneNodoPianoRepository.findByRegistrazione(registrazione);
		List<RegistrazioneIndicatorePiano> oldRip = registrazioneIndicatorePianoRepository
				.findByRegistrazioneNodoPianoRegistrazione(registrazione);
		if (request.getNodi() != null) {
			float totf = 0f;
			for (AggiornaPeso ap : request.getNodi()) {
				if (ap.getPeso() != null && (ap.getPeso().floatValue() < 0 || ap.getPeso().floatValue() > 100)) {
					throw new BusinessException(HttpStatus.BAD_REQUEST, "peso non valido:" + ap.getPeso());
				}
				totf += ap.getPeso() == null ? 0f : ap.getPeso().floatValue();
			}
			if (totf > 100) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, "peso totale nodi maggiore di 100:" + totf);
			}

			for (AggiornaPeso ap : request.getNodi()) {
				AggiornaPesoNodoPianoRegistrazioneRequest req = new AggiornaPesoNodoPianoRegistrazioneRequest();
				Long val = aggiornaPesoNodoValido(ap.getId(), registrazione,
						ap.getPeso() == null ? 0f : ap.getPeso().floatValue());
				oldRnp.removeIf(t -> t.getId().equals(val));
			}
		}

		if (request.getIndicatori() != null) {
			Map<Long, Float> pesi = new HashMap<>();
			for (AggiornaPeso ap : request.getIndicatori()) {
				if (ap.getPeso() != null && (ap.getPeso().floatValue() < 0 || ap.getPeso().floatValue() > 100)) {
					throw new BusinessException(HttpStatus.BAD_REQUEST, "peso non valido:" + ap.getPeso());
				}
				IndicatorePiano ip = indicatorePianoRepository.findById(ap.getId()).orElseThrow(
						() -> new BusinessException(HttpStatus.NOT_FOUND, "indicatore non trovato:" + ap.getId()));
				Long np = ip.getNodoPiano().getId();
				Float p = pesi.get(np);
				if (p == null) {
					p = 0f;
					pesi.put(np, p);
				}
				float v = p.floatValue();
				v += ap.getPeso() == null ? 0f : ap.getPeso().floatValue();
				pesi.put(np, v);
			}
			for (Float v : pesi.values()) {
				if (v > 100) {
					throw new BusinessException(HttpStatus.BAD_REQUEST,
							"peso totale indicatori nodo maggiore di 100:" + v);
				}
			}
			for (AggiornaPeso ap : request.getIndicatori()) {
				Long val = aggiornaPesoIndicatoreValido(ap.getId(), registrazione,
						ap.getPeso() == null ? 0f : ap.getPeso().floatValue());
				oldRip.removeIf(t -> t.getId().equals(val));
			}
		}
		for (RegistrazioneIndicatorePiano rn : oldRip) {
			eliminaDipendenze(rn);
		}
		for (RegistrazioneNodoPiano rn : oldRnp) {
			eliminaDipendenze(rn);
		}
	}

	private void eliminaDipendenze(RegistrazioneNodoPiano rn) {
		schedaValutazioneNodoRepository.deleteByNodoPianoAndSchedaValutazioneRegistrazione(rn.getNodoPiano(),
				rn.getRegistrazione());
		registrazioneNodoPianoRepository.deleteById(rn.getId());
	}

	private void eliminaDipendenze(RegistrazioneIndicatorePiano rn) {
		schedaValutazioneIndicatoreRepository
				.deleteByIndicatorePianoAndSchedaValutazioneNodoSchedaValutazioneRegistrazione(rn.getIndicatorePiano(),
						rn.getRegistrazioneNodoPiano().getRegistrazione());
		registrazioneIndicatorePianoRepository.deleteById(rn.getId());
	}

	private Long aggiornaPesoNodoValido(Long idNodo, Registrazione registrazione, Float pesof)
			throws BusinessException {
		RegistrazioneNodoPiano val = null;
		NodoPiano nodo = nodoPianoRepository.findById(idNodo)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "nodo non trovato:" + idNodo));
		val = registrazioneNodoPianoRepository.findByRegistrazioneAndNodoPiano(registrazione, nodo);
		BigDecimal peso = BigDecimal.valueOf(pesof);
		if (val == null) {
			val = new RegistrazioneNodoPiano();
			val.setNodoPiano(nodo);
			val.setRegistrazione(registrazione);
			val.setPeso(peso);
//			val.setTipo(AggiornaSchedePIHelper.tipoRegolamento(registrazione.getResponsabile()));
			val = registrazioneNodoPianoRepository.save(val);
		} else {
			registrazioneNodoPianoRepository.updatePeso(peso, val.getId());
		}
		return val.getId();
	}

	private Long aggiornaPesoIndicatoreValido(Long idIndicatorePiano, Registrazione registrazione, float pesof)
			throws BusinessException {
		RegistrazioneIndicatorePiano val = null;

		IndicatorePiano ip = indicatorePianoRepository.findById(idIndicatorePiano).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "indicatore non trovato:" + idIndicatorePiano));
		RegistrazioneNodoPiano registrazioneNodoPiano = registrazioneNodoPianoRepository
				.findByRegistrazioneAndNodoPiano(registrazione, ip.getNodoPiano());
		if (registrazioneNodoPiano == null) {
			new BusinessException(HttpStatus.NOT_FOUND,
					"registrazione nodo piano non trovato:" + registrazione.getId() + ", " + ip.getNodoPiano().getId());
		}

		val = registrazioneIndicatorePianoRepository
				.findByRegistrazioneNodoPianoAndIndicatorePiano(registrazioneNodoPiano, ip);
		BigDecimal peso = BigDecimal.valueOf(pesof);

		if (val == null) {
			val = new RegistrazioneIndicatorePiano();
			val.setIndicatorePiano(ip);
			val.setRegistrazioneNodoPiano(registrazioneNodoPiano);
			val.setPeso(peso);
			val = registrazioneIndicatorePianoRepository.save(val);
		} else {
			registrazioneIndicatorePianoRepository.updatePeso(peso, val.getId());
		}
		return val.getId();
	}

	private void elaboraNodo(final Registrazione r, final List<NodoPiano> nodi, final RisorsaUmana valutato,
			List<PesoNodoPianoRegistrazioneVM> out, final Map<Long, PesoNodoPianoRegistrazioneVM> map) {
		Organizzazione o = r.getOrganizzazione();
		boolean responsabile = Boolean.TRUE.equals(r.getResponsabile());
		boolean interim = Boolean.TRUE.equals(r.getInterim());
		boolean inattiva = Boolean.TRUE.equals(r.getInattiva());
		if (o == null || inattiva || (responsabile && interim))
			return;
		if (responsabile || (o.getResponsabile() != null && o.getResponsabile().getId().equals(valutato.getId()))) {
			List<NodoPiano> items = nodoPianoRepository
					.findByOrganizzazioneIdAndDateDeleteIsNullOrderByCodiceCompleto(o.getId());

			if (items != null) {
				items.forEach(np -> {
					if (!np.getCodiceInterno().startsWith("9.2.") && !map.containsKey(np.getId())) {
						PesoNodoPianoRegistrazioneVM p = new PesoNodoPianoRegistrazioneVM(r.getId(), np.getId(),
								NodoPianoHelper.ridotto(np.getCodiceCompleto()) + " " + np.getDenominazione(),
								np.getOrganizzazione().getId(), np.getOrganizzazione().getIntestazione(),
								np.getOrganizzazione().getResponsabile().getId(),
								np.getOrganizzazione().getResponsabile().getCognome() + " "
										+ np.getOrganizzazione().getResponsabile().getNome(),
								np.getTipoNodo(), r.getInizioValidita(), r.getFineValidita());
						p.setTipoRegolamento(TipoRegolamento.INDIVIDUALE);
						map.put(np.getId(), p);
						nodi.add(np);
						out.add(p);
					}
				});
			}
		} else {
			List<RisorsaUmanaNodoPiano> items2 = risorsaUmanaNodoPianoRepository
					.findByRisorsaUmanaIdAndNodoPianoDateDeleteIsNull(valutato.getId());
			if (items2 != null) {
				items2.forEach(runp -> {
					NodoPiano np = runp.getNodoPiano();
					if (!map.containsKey(np.getId())) {
						PesoNodoPianoRegistrazioneVM p = new PesoNodoPianoRegistrazioneVM(r.getId(), np.getId(),
								NodoPianoHelper.ridotto(np.getCodiceCompleto()) + " " + np.getDenominazione(),
								np.getOrganizzazione().getId(), np.getOrganizzazione().getIntestazione(),
								np.getOrganizzazione().getResponsabile().getId(),
								np.getOrganizzazione().getResponsabile().getCognome() + " "
										+ np.getOrganizzazione().getResponsabile().getNome(),
								np.getTipoNodo(), r.getInizioValidita(), r.getFineValidita());
						p.setTipoRegolamento(TipoRegolamento.STRUTTURA);
						map.put(np.getId(), p);
						nodi.add(np);
						out.add(p);
					}
				});
			}
		}
		for (PesoNodoPianoRegistrazioneVM p : out) {
			RegistrazioneNodoPiano vnp = registrazioneNodoPianoRepository
					.findByRegistrazioneIdAndNodoPianoId(p.getIdRegistrazione(), p.getIdNodo());
			if (vnp != null) {
				p.setId(vnp.getId());
				p.setPeso(vnp.getPeso() == null ? 0f : vnp.getPeso().floatValue());
			} else {
				p.setPeso(0f);
			}
			// System.out.println(p);
		}
	}

	private void elaboraIndicatore(final Registrazione r, final NodoPiano np, final RisorsaUmana valutato,
			final List<PesoNodoPianoRegistrazioneVM> out, final Map<Long, PesoNodoPianoRegistrazioneVM> map) {
		final PesoNodoPianoRegistrazioneVM p = map.get(np.getId());
		p.setSommaPesi(0f);
		List<IndicatorePiano> items = indicatorePianoRepository.findByNodoPianoId(np.getId());
		items.removeIf(ind -> ind.getTipoIndicatore() != null
				&& "Indicatore di Valore Pubblico".equalsIgnoreCase(ind.getTipoIndicatore()));
		if (items != null && !items.isEmpty()) {
			p.setIndicatori(items
					.stream().map(
							ip -> new PesoIndicatoreRegistrazioneVM(ip.getId(), r.getId(),
									StringUtils.isBlank(ip.getSpecifica()) ? ip.getIndicatore().getDenominazione()
											: ip.getSpecifica()))
					.sorted(new Comparator<PesoIndicatoreRegistrazioneVM>() {
						@Override
						public int compare(PesoIndicatoreRegistrazioneVM o1, PesoIndicatoreRegistrazioneVM o2) {
							return o1.getDenominazione().compareTo(o2.getDenominazione());
						}

					}).collect(Collectors.toList()));
			p.getIndicatori().forEach(ind -> {
				RegistrazioneIndicatorePiano vip = registrazioneIndicatorePianoRepository
						.findByRegistrazioneNodoPianoRegistrazioneIdAndIndicatorePianoId(r.getId(),
								ind.getIdIndicatorePiano());
				if (vip != null) {
					ind.setId(vip.getId());
					ind.setPeso(vip.getPeso() == null ? 0f : vip.getPeso().floatValue());
				} else {
					ind.setPeso(0f);
				}
				p.setSommaPesi(p.getSommaPesi() + ind.getPeso());
			});
		}
	}

	@Override
	@Transactional(readOnly = true)
	public PesoNodoPianoRegistrazioneVM prioritaNodo(Long id, Long idRegistrazione, Long idNodo)
			throws BusinessException {
		NodoPiano np;
		Registrazione reg;
		RegistrazioneNodoPiano rnp = null;
		if (id != null) {
			rnp = registrazioneNodoPianoRepository.findById(id)
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "priorita non trovata:" + id));
			np = rnp.getNodoPiano();
			reg = rnp.getRegistrazione();
		} else {
			reg = registrazioneRepository.findById(idRegistrazione)
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
							"registrazione non trovata:" + idRegistrazione));
			np = nodoPianoRepository.findById(idNodo)
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "nodo non trovato:" + idNodo));
		}
		PesoNodoPianoRegistrazioneVM p = new PesoNodoPianoRegistrazioneVM(reg.getId(), np.getId(),
				np.getDenominazione(), np.getOrganizzazione().getId(), np.getOrganizzazione().getIntestazione(),
				np.getOrganizzazione().getResponsabile().getId(),
				np.getOrganizzazione().getResponsabile().getCognome() + " "
						+ np.getOrganizzazione().getResponsabile().getNome(),
				np.getTipoNodo(), reg.getInizioValidita(), reg.getFineValidita());
		if (rnp != null && p != null) {
			p.setId(rnp.getId());
			p.setPeso(rnp.getPeso() == null ? null : rnp.getPeso().floatValue());
		}

		return p;
	}

	@Override
	@Transactional(readOnly = true)
	public PesoIndicatoreRegistrazioneVM prioritaIndicatore(final Long id, final Long idRegistrazione,
			final Long idIndicatorePiano) throws BusinessException {
		Registrazione reg;
		RegistrazioneNodoPiano registrazioneNodoPiano;
		IndicatorePiano ip;
		RegistrazioneIndicatorePiano rip = null;
		if (id != null) {
			rip = registrazioneIndicatorePianoRepository.findById(id)
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "priorita non trovata:" + id));
			ip = rip.getIndicatorePiano();
			registrazioneNodoPiano = rip.getRegistrazioneNodoPiano();
			reg = registrazioneNodoPiano.getRegistrazione();
		} else {
			reg = registrazioneRepository.findById(idRegistrazione)
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
							"registrazione non trovata:" + idRegistrazione));
			ip = indicatorePianoRepository.findById(idIndicatorePiano).orElseThrow(
					() -> new BusinessException(HttpStatus.BAD_REQUEST, "indicatore non trovato:" + idIndicatorePiano));
			registrazioneNodoPiano = registrazioneNodoPianoRepository.findByRegistrazioneAndNodoPiano(reg,
					ip.getNodoPiano());

		}
		PesoIndicatoreRegistrazioneVM p = new PesoIndicatoreRegistrazioneVM(ip.getId(), reg.getId(), ip.getSpecifica());
		if (rip != null && p != null) {
			p.setId(rip.getId());
			p.setPeso(rip.getPeso() == null ? null : rip.getPeso().floatValue());
		}
		return p;
	}

}
