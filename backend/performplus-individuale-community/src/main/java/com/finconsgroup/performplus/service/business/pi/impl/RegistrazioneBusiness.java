package com.finconsgroup.performplus.service.business.pi.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.SchedaValutato;
import com.finconsgroup.performplus.domain.SchedaValutazione;
import com.finconsgroup.performplus.domain.ValutazioneRegistrazione;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.pi.utils.CalcoloValutazioneHelper;
import com.finconsgroup.performplus.pi.utils.MappingRegistrazione;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.OrganizzazioneRepository;
import com.finconsgroup.performplus.repository.QuestionarioRepository;
import com.finconsgroup.performplus.repository.RegistrazioneRepository;
import com.finconsgroup.performplus.repository.RegolamentoRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.SchedaValutatoRepository;
import com.finconsgroup.performplus.repository.SchedaValutazioneRepository;
import com.finconsgroup.performplus.repository.ValutazioneRegistrazioneRepository;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.CreaRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.ParametriRicercaRegistrazione;
import com.finconsgroup.performplus.rest.api.pi.vm.RegistrazioneVM;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEasyVM;
import com.finconsgroup.performplus.rest.api.vm.RisorsaUmanaSmartVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.pi.IRegistrazioneBusiness;
import com.finconsgroup.performplus.service.business.utils.DateHelper;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.OrganizzazioneHelper;
import com.finconsgroup.performplus.service.dto.RicercaRisorseUmane;

import jakarta.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrazioneBusiness implements IRegistrazioneBusiness {
	@Autowired
	private RegistrazioneRepository registrazioneRepository;
	@Autowired
	private NodoPianoRepository nodoPianoRepository;
	@Autowired
	private RegolamentoRepository regolamentoRepository;
	@Autowired
	private QuestionarioRepository questionarioRepository;
	@Autowired
	private OrganizzazioneRepository organizzazioneRepository;
	@Autowired
	private RisorsaUmanaRepository risorsaUmanaRepository;
	@Autowired
	private ValutazioneRegistrazioneRepository valutazioneRegistrazioneRepository;
	@Autowired
	private SchedaValutazioneRepository schedaValutazioneRepository;
	@Autowired
	private SchedaValutatoRepository schedaValutatoRepository;

	@Override
	@Transactional(readOnly = true)
	public List<RegistrazioneVM> listPerQuestionario(Long idQuestionario, Integer anno) throws BusinessException {
		final List<Registrazione> items = registrazioneRepository
				.findByQuestionarioIdAndAnnoOrderByValutatoCognomeAsc(idQuestionario, anno);
		return items.stream().filter(r->!Boolean.TRUE.equals(r.getInattiva())).map(r -> MappingRegistrazione.mapping(r)).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<RegistrazioneVM> listPerValutatore(Long idValutatore) throws BusinessException {
		final List<Registrazione> items = registrazioneRepository
				.findByValutatoreIdOrderByValutatoCognomeAsc(idValutatore);
		return items.stream().filter(r->!Boolean.TRUE.equals(r.getInattiva())).map(r -> MappingRegistrazione.mapping(r)).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<RegistrazioneVM> listPerValutato(Long idValutato) throws BusinessException {
		final List<Registrazione> items = registrazioneRepository.findByValutatoIdOrderByValutatoCognomeAsc(idValutato);
		return items.stream().filter(r->!Boolean.TRUE.equals(r.getInattiva())).map(r -> MappingRegistrazione.mapping(r)).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<RegistrazioneVM> leggi(Long idValutatore, Long idValutato) throws BusinessException {
		final List<Registrazione> items = registrazioneRepository
				.findByValutatoreIdAndValutatoIdOrderByValutatoCognomeAsc(idValutatore, idValutato);
		return items.stream().map(r -> MappingRegistrazione.mapping(r)).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaSmartVM> getValutati(Long idEnte, Integer anno, String cognome, Long idValutatore, Long idStruttura,Boolean interim, Boolean inattiva)
			throws BusinessException {
		ParametriRicercaRegistrazione filter = new ParametriRicercaRegistrazione();
		filter.setIdEnte(idEnte);
		filter.setAnno(anno);
		filter.setCognomeValutato(cognome);
		filter.setIdValutatore(idValutatore);
		filter.setInterim(interim);
		filter.setInattiva(inattiva);
		filter.setIdStruttura(idStruttura);		
		List<Registrazione> items = registrazioneRepository.cerca(filter);
		final List<RisorsaUmanaSmartVM> out = new ArrayList<>();
		final List<Long> doppi = new ArrayList<>();
		if (items != null) {
			items.stream()
			.filter(t->!Boolean.TRUE.equals(t.getInterim()) || !Boolean.TRUE.equals(t.getInattiva()))
			.forEach(t -> {
				if (t.getValutato() != null && !doppi.contains(t.getValutato().getId())) {
					doppi.add(t.getValutato().getId());
					out.add(Mapping.mapping(t.getValutato(), RisorsaUmanaSmartVM.class));
				}
			});
		}
		out.sort(new Comparator<RisorsaUmanaSmartVM>() {

			@Override
			public int compare(RisorsaUmanaSmartVM o1, RisorsaUmanaSmartVM o2) {
				int c = o1.getCognome().compareTo(o2.getCognome());
				if (c == 0) {
					c = o1.getNome().compareTo(o2.getNome());
				}
				return c;
			}
		});
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaSmartVM> getRisorse(Long idEnte, Integer anno, String cognome) throws BusinessException {
		RicercaRisorseUmane filter = new RicercaRisorseUmane();
		filter.setIdEnte(idEnte);
		filter.setAnno(anno);
		filter.setTestoRicerca(cognome);
		filter.setSoloAttiveAnno(true);		
		List<RisorsaUmana> items = risorsaUmanaRepository.cerca(filter);
		final List<RisorsaUmanaSmartVM> out = new ArrayList<>();
		final List<Long> doppi = new ArrayList<>();
		if (items != null) {
			items.stream().forEach(t -> {
				if (!doppi.contains(t.getId())) {
					doppi.add(t.getId());
					out.add(Mapping.mapping(t, RisorsaUmanaSmartVM.class));
				}
			});
		}
		out.sort(new Comparator<RisorsaUmanaSmartVM>() {

			@Override
			public int compare(RisorsaUmanaSmartVM o1, RisorsaUmanaSmartVM o2) {
				int c = o1.getCognome().compareTo(o2.getCognome());
				if (c == 0) {
					c = o1.getNome().compareTo(o2.getNome());
				}
				return c;
			}
		});
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaSmartVM> getValutatori(Long idEnte, Integer anno, String cognome, Long idValutato,Long idStruttura, Boolean interim,Boolean inattiva)
			throws BusinessException {
		ParametriRicercaRegistrazione filter = new ParametriRicercaRegistrazione();
		filter.setIdEnte(idEnte);
		filter.setAnno(anno);
		filter.setCognomeValutatore(cognome);
		filter.setIdValutato(idValutato);
		filter.setIdStruttura(idStruttura);		
		filter.setInterim(interim);		
		filter.setInattiva(inattiva);
		List<Registrazione> items = registrazioneRepository.cerca(filter);
		final List<RisorsaUmanaSmartVM> out = new ArrayList<>();
		final List<Long> doppi = new ArrayList<>();
		if (items != null) {
			items.stream().
			filter(t->!Boolean.TRUE.equals(t.getInterim()) || !Boolean.TRUE.equals(t.getResponsabile()))	
				.forEach(t -> {
				if (t.getValutatore() != null && !doppi.contains(t.getValutatore().getId())) {
					doppi.add(t.getValutatore().getId());
					out.add(Mapping.mapping(t.getValutatore(), RisorsaUmanaSmartVM.class));
				}
			});
		}
		out.sort(new Comparator<RisorsaUmanaSmartVM>() {

			@Override
			public int compare(RisorsaUmanaSmartVM o1, RisorsaUmanaSmartVM o2) {
				int c = o1.getCognome().compareTo(o2.getCognome());
				if (c == 0) {
					c = o1.getNome().compareTo(o2.getNome());
				}
				return c;
			}
		});
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public RisorsaUmanaSmartVM getRisorsa(Long idRisorsa) throws BusinessException {
		RisorsaUmana r = risorsaUmanaRepository.findById(idRisorsa).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Non trovato:" + idRisorsa));

		return Mapping.mapping(r, RisorsaUmanaSmartVM.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<RegistrazioneVM> search(ParametriRicercaRegistrazione filter, Pageable pageable) {
		Page<Registrazione> page = registrazioneRepository.search(filter, pageable);
		Page<RegistrazioneVM> out = page.map(new Function<Registrazione, RegistrazioneVM>() {
			@Override
			public RegistrazioneVM apply(Registrazione r) {
				return MappingRegistrazione.mapping(r);
			}
		});
		return out;
	}

	@Override
	public RegistrazioneVM crea(CreaRegistrazioneRequest request) throws BusinessException {
		Registrazione registrazione = Mapping.mapping(request, Registrazione.class);

		if (request.getIdOrganizzazione() != null) {
			Organizzazione o = organizzazioneRepository.findById(request.getIdOrganizzazione())
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
							"organizzazione inesistente:" + request.getIdOrganizzazione()));
			registrazione.setOrganizzazione(o);
		}
		registrazione.setQuestionario(questionarioRepository.findById(request.getIdQuestionario())
				.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
						"questionario inesistente:" + request.getIdQuestionario())));
		registrazione.setValutato(risorsaUmanaRepository.findById(request.getIdValutato())
				.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
						"valutato inesistente:" + request.getIdValutato())));
		registrazione.setValutatore(risorsaUmanaRepository.findById(request.getIdValutatore())
				.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
						"valutatore inesistente:" + request.getIdValutatore())));
		registrazione.setRegolamento(regolamentoRepository.findById(request.getIdRegolamento())
				.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
						"regolamento inesistente:" + request.getIdRegolamento())));

		registrazione.setNote(request.getNote());
		registrazione.setInizioValidita(request.getInizioValidita());
		registrazione.setFineValidita(request.getFineValidita());
		registrazione.setPo(request.getPo());
		registrazione.setResponsabile(request.getResponsabile());
		registrazione.setInterim(request.getInterim());

		registrazione.setForzaSchedaSeparata(request.getForzaSchedaSeparata());
		registrazione.setInattiva(request.getInattiva());
		registrazione.setForzaValutatore(request.getForzaValutatore());
		registrazione.setMancataAssegnazione(request.getMancataAssegnazione());
		registrazione.setMancatoColloquio(request.getMancatoColloquio());

		registrazione = registrazioneRepository.save(registrazione);
		return MappingRegistrazione.mapping(registrazione);
	}

	@Override
	public void aggiorna(Long id, AggiornaRegistrazioneRequest request) throws BusinessException {
		Registrazione registrazione = registrazioneRepository.findById(id)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Registrazione not trovata:" + id));
		if (request.getIdOrganizzazione() != null && (registrazione.getOrganizzazione() == null
				|| !request.getIdOrganizzazione().equals(registrazione.getOrganizzazione().getId()))) {
			Organizzazione o = organizzazioneRepository.findById(request.getIdOrganizzazione())
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
							"organizzazione inesistente:" + request.getIdOrganizzazione()));
			registrazione.setOrganizzazione(o);
		}
		if (request.getIdQuestionario() != null && (registrazione.getQuestionario() == null
				|| !request.getIdQuestionario().equals(registrazione.getQuestionario().getId()))) {
			registrazione.setQuestionario(questionarioRepository.findById(request.getIdQuestionario())
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
							"questionario inesistente:" + request.getIdQuestionario())));
		}
		if (request.getIdValutato() != null && (registrazione.getValutato() == null
				|| !request.getIdValutato().equals(registrazione.getValutato().getId()))) {
			registrazione.setValutato(risorsaUmanaRepository.findById(request.getIdValutato())
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
							"valutato inesistente:" + request.getIdValutato())));
		}
		if (request.getIdValutatore() != null && (registrazione.getValutatore() == null
				|| !request.getIdValutatore().equals(registrazione.getValutatore().getId()))) {
			registrazione.setValutatore(risorsaUmanaRepository.findById(request.getIdValutatore())
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
							"valutatore inesistente:" + request.getIdValutatore())));
		}

		if (request.getIdRegolamento() != null && (registrazione.getRegolamento() == null
				|| !request.getIdRegolamento().equals(registrazione.getRegolamento().getId()))) {
			registrazione.setRegolamento(regolamentoRepository.findById(request.getIdRegolamento())
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
							"regolamento inesistente:" + request.getIdRegolamento())));
		}
		registrazione.setNote(request.getNote());
		registrazione.setInizioValidita(request.getInizioValidita());
		registrazione.setFineValidita(request.getFineValidita());
		registrazione.setPo(request.getPo());
		registrazione.setResponsabile(request.getResponsabile());
		registrazione.setInterim(request.getInterim());
		
		//forzature admin		
		registrazione.setForzaSchedaSeparata(request.getForzaSchedaSeparata());
		registrazione.setInattiva(request.getInattiva());
		registrazione.setForzaValutatore(request.getForzaValutatore());
		registrazione.setMancataAssegnazione(request.getMancataAssegnazione());
		registrazione.setMancatoColloquio(request.getMancatoColloquio());
		
		registrazioneRepository.save(registrazione);


	}

	@Override
	public void elimina(Long id) throws BusinessException {
		Registrazione registrazione = registrazioneRepository.findById(id)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Registrazione not trovata:" + id));
		List<ValutazioneRegistrazione> val = valutazioneRegistrazioneRepository.findByRegistrazione(registrazione);
		if (val != null && !val.isEmpty()) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "registrazione valutata");
		}
		SchedaValutazione val1 = schedaValutazioneRepository.findByRegistrazione(registrazione);
		if (val1 != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "registrazione valutata");
		}

		registrazioneRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public RegistrazioneVM leggi(Long id) throws BusinessException {
		Registrazione registrazione = registrazioneRepository.findById(id)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Registrazione not trovata:" + id));
		return MappingRegistrazione.mapping(registrazione);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RegistrazioneVM> cerca(ParametriRicercaRegistrazione filter) throws BusinessException {
		List<Registrazione> items = registrazioneRepository.cerca(filter);
		return items.stream().map(t -> MappingRegistrazione.mapping(t)).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<DecodificaEasyVM> getQuestionari(Long idEnte) throws BusinessException {
		return questionarioRepository.findByIdEnte(idEnte).stream()
				.map(t -> new DecodificaEasyVM(t.getId(), t.getIntestazione())).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<DecodificaEasyVM> getRegolamenti(Long idEnte, Integer anno) throws BusinessException {
		return regolamentoRepository.findByIdEnteAndAnnoOrderByIntestazione(idEnte, anno).stream()
				.map(t -> new DecodificaEasyVM(t.getId(), t.getIntestazione())).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<DecodificaEasyVM> getStrutture(Long idEnte, Integer anno, String testo) throws BusinessException {
		if (StringUtils.isBlank(testo)) {
			return organizzazioneRepository.findByIdEnteAndAnnoOrderByCodiceCompleto(idEnte, anno).stream()
					.filter(t -> !Livello.ENTE.equals(t.getLivello()))
					.map(t -> new DecodificaEasyVM(t.getId(), OrganizzazioneHelper.getNomeCompleto(t)))
					.collect(Collectors.toList());

		}
		return organizzazioneRepository
				.findByIdEnteAndAnnoAndIntestazioneStartsWithIgnoreCaseOrderByCodiceCompleto(idEnte, anno, testo)
				.stream().filter(t -> !Livello.ENTE.equals(t.getLivello()))
				.map(t -> new DecodificaEasyVM(t.getId(), OrganizzazioneHelper.getNomeCompleto(t)))
				.collect(Collectors.toList());
	}

	public long countPerValutatore(Long idValutatore) throws BusinessException {
		return registrazioneRepository.countByValutatoreId(idValutatore);
	}

	public long countPerValutato(Long idValutato) throws BusinessException {
		return registrazioneRepository.countByValutatoId(idValutato);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaSmartVM> getValutatoriAll(Long idValutato) throws BusinessException {
		final RisorsaUmana valutato = risorsaUmanaRepository.findById(idValutato)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "valutato non trovato:" + idValutato));
		String codiceUnivoco = CalcoloValutazioneHelper.calcolaCodiceUnivoco(valutato);
		SchedaValutato sv=schedaValutatoRepository.findByIdEnteAndAnnoAndCodiceUnivoco(valutato.getIdEnte(),valutato.getAnno(),codiceUnivoco);
		List<SchedaValutazione> items = schedaValutazioneRepository.findBySchedaValutato(sv);
		if(items==null)
			return null;
		return items.stream()
				.filter(s -> !Boolean.TRUE.equals(s.getRegistrazione().getInattiva()) 
						&& (!Boolean.TRUE.equals(s.getRegistrazione().getInterim())
								|| !Boolean.TRUE.equals(s.getRegistrazione().getResponsabile()))
						)
				.filter(distinctByKey(s -> s.getRegistrazione().getValutatore().getId())).map(s->Mapping.mapping(s.getRegistrazione().getValutatore(), RisorsaUmanaSmartVM.class)).collect(Collectors.toList());
	}
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
		return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean verificaValutatore(@Valid Long idRisorsa) throws BusinessException {
		final RisorsaUmana risorsa = risorsaUmanaRepository.findById(idRisorsa)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "risorsa non trovata:" + idRisorsa));
		long count=registrazioneRepository.countByValutatoreAndInattivaIsFalseAndInterimIsFalse(risorsa);
		return count>0;
	}

	@Override
	public void undo(Long id) {
		Registrazione registrazione = registrazioneRepository.findById(id)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Registrazione not trovata:" + id));
		if(registrazione.getDataPubblicazioneValutazione()!=null) {
			registrazione.setNoteAccettazioneValutazione(null);		
			registrazione.setNotePubblicazioneValutazione(null);	
			registrazione.setDataAccettazioneValutazione(null);	
			registrazione.setDataPubblicazioneValutazione(null);	
			registrazioneRepository.save(registrazione);
		}else if(registrazione.getDataPubblicazioneScheda()!=null) {
			registrazione.setNoteAccettazioneScheda(null);		
			registrazione.setNotePubblicazioneScheda(null);	
			registrazione.setDataAccettazioneScheda(null);	
			registrazione.setDataPubblicazioneScheda(null);	
			registrazioneRepository.save(registrazione);
		}
	}
}
