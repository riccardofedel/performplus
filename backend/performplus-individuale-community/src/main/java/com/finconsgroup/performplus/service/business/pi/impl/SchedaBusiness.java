package com.finconsgroup.performplus.service.business.pi.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.AmbitoValutazione;
import com.finconsgroup.performplus.domain.Cruscotto;
import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.Profilo;
import com.finconsgroup.performplus.domain.Questionario;
import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.domain.Regolamento;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.SchedaValutato;
import com.finconsgroup.performplus.domain.SchedaValutazione;
import com.finconsgroup.performplus.domain.SchedaValutazioneIndicatore;
import com.finconsgroup.performplus.domain.SchedaValutazioneNodo;
import com.finconsgroup.performplus.domain.Utente;
import com.finconsgroup.performplus.domain.ValoreAmbito;
import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.domain.ValutazioneRegistrazione;
import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.enumeration.TipoRegolamento;
import com.finconsgroup.performplus.enumeration.TipoValutazione;
import com.finconsgroup.performplus.pi.utils.CalcoloValutazioneHelper;
import com.finconsgroup.performplus.repository.AmbitoValutazioneRepository;
import com.finconsgroup.performplus.repository.CruscottoRepository;
import com.finconsgroup.performplus.repository.ProfiloRepository;
import com.finconsgroup.performplus.repository.QuestionarioRepository;
import com.finconsgroup.performplus.repository.RegistrazioneRepository;
import com.finconsgroup.performplus.repository.RegolamentoRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.SchedaValutatoRepository;
import com.finconsgroup.performplus.repository.SchedaValutazioneIndicatoreRepository;
import com.finconsgroup.performplus.repository.SchedaValutazioneNodoRepository;
import com.finconsgroup.performplus.repository.SchedaValutazioneRepository;
import com.finconsgroup.performplus.repository.UtenteRepository;
import com.finconsgroup.performplus.repository.ValoreAmbitoRepository;
import com.finconsgroup.performplus.repository.ValutazioneRegistrazioneRepository;
import com.finconsgroup.performplus.repository.ValutazioneRepository;
import com.finconsgroup.performplus.rest.api.pi.vm.ElementoQuestionario;
import com.finconsgroup.performplus.rest.api.pi.vm.NoteValutazioneVM;
import com.finconsgroup.performplus.rest.api.pi.vm.SchedaVM;
import com.finconsgroup.performplus.rest.api.pi.vm.SchedaValutatoVM;
import com.finconsgroup.performplus.rest.api.pi.vm.TotaliVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneIndicatoreVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneObiettiviVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneObiettivoVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneQuestionarioVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneVM;

import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;

import com.finconsgroup.performplus.service.business.pi.ISchedaBusiness;
import com.finconsgroup.performplus.service.business.security.SecurityUtils;
import com.finconsgroup.performplus.service.business.utils.FVGEmailService;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.ValutazioneHelper;

import jakarta.mail.MessagingException;

@Service
@Transactional
public class SchedaBusiness implements ISchedaBusiness {

	public static final String VALUTAZIONE = "Valutazione";
	public static final String QUESTIONARIO = "Questionario";
	public static final String PERFORMANCE_ORGANIZZATIVA = "Performance organizzativa";
	public static final String OBIETTIVI_DI_STRUTTURA = "Obiettivi di Struttura";
	public static final String OBIETTIVI_INDIVIDUALI = "Obiettivi Individuali";
	public static final String NNNNN = "NNNNN";

	@Autowired
	private RisorsaUmanaRepository risorsaUmanaRepository;
	@Autowired
	private RegistrazioneRepository registrazioneRepository;

	@Autowired
	private SchedaValutazioneRepository schedaValutazioneRepository;
	@Autowired
	private SchedaValutazioneNodoRepository schedaValutazioneNodoRepository;
	@Autowired
	private SchedaValutazioneIndicatoreRepository schedaValutazioneIndicatoreRepository;
	@Autowired
	private QuestionarioRepository questionarioRepository;
	@Autowired
	private AmbitoValutazioneRepository ambitoValutazioneRepository;
	@Autowired
	private ValoreAmbitoRepository valoreAmbitoRepository;
	@Autowired
	private ValutazioneRegistrazioneRepository valutazioneRegistrazioneRepository;
	@Autowired
	private ValutazioneRepository valutazioneRepository;
	@Autowired
	private SchedaValutatoRepository schedaValutatoRepository;
	@Autowired
	private UtenteRepository utenteRepository;
	@Autowired
	private ProfiloRepository profiloRepository;
	@Autowired
	private RegolamentoRepository regolamentoRepository;

	@Autowired
	private CruscottoRepository cruscottoRepository;

	@Autowired
	private IUtenteBusiness utenteBusiness;


	@Autowired
	private FVGEmailService fvgEmailService;

	@Value("${tovalutato.uno.mail.title}")
	String invioSchedaSubject;
	@Value("${tovalutato.uno.mail.body}")
	String invioSchedaBody;
	@Value("${tovalutato.due.mail.title}")
	String invioValutazioneSubject;
	@Value("${tovalutato.due.mail.body}")
	String invioValutazioneBody;

	@Value("${tovalutatore.uno.mail.title}")
	String presaVisioneSchedaSubject;
	@Value("${tovalutatore.uno.mail.body}")
	String presaVisioneSchedaBody;
	@Value("${tovalutatore.due.mail.title}")
	String presaVisioneValutazioneSubject;
	@Value("${tovalutatore.due.mail.body}")
	String presaVisioneValutazioneBody;



	@Override
	public SchedaVM leggi(Long idRegistrazione) throws BusinessException {
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		final SchedaValutazione sv = schedaValutazioneRepository.findByRegistrazione(registrazione);
		if (sv == null)
			throw new BusinessException(HttpStatus.NOT_FOUND, "scheda valutazione non ancora calcolata");

		final SchedaVM out = mapping(sv);
		return out;
	}

	@Override
	public TotaliVM totali(Long idRegistrazione) throws BusinessException {
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		final SchedaValutazione sv = schedaValutazioneRepository.findByRegistrazione(registrazione);
		if (sv == null)
			throw new BusinessException(HttpStatus.NOT_FOUND, "scheda valutazione non ancora calcolata");

		final TotaliVM out = new TotaliVM();
		final List<ValutazioneVM> items = new ArrayList<>();

		ValutazioneVM v = null;

		if (sv.getPesoObiettiviIndividuali() != null) {
			v = new ValutazioneVM();
			v.setDenominazione(OBIETTIVI_INDIVIDUALI);
			v.setLivello(1);
			v.setPeso(sv.getPesoObiettiviIndividuali() == null ? 0f : sv.getPesoObiettiviIndividuali().floatValue());
			v.setValutazione(sv.getRaggiungimentoObiettiviIndividuali());
			items.add(v);
		}
		if (sv.getPesoObiettiviStruttura() != null) {
			v = new ValutazioneVM();
			v.setDenominazione(OBIETTIVI_DI_STRUTTURA);
			v.setLivello(1);
			v.setPeso(sv.getPesoObiettiviStruttura() == null ? 0f : sv.getPesoObiettiviStruttura().floatValue());
			v.setValutazione(sv.getRaggiungimentoObiettiviStruttura());
			items.add(v);
		}
		if (sv.getPesoObiettiviPerformance() != null) {
			v = new ValutazioneVM();
			v.setDenominazione(PERFORMANCE_ORGANIZZATIVA);
			v.setLivello(1);
			v.setPeso(sv.getPesoObiettiviPerformance() == null ? 0f : sv.getPesoObiettiviPerformance().floatValue());
			v.setValutazione(sv.getRaggiungimentoObiettiviPerformance());
			items.add(v);
		}
		if (sv.getPesoQuestionario() != null) {
			v = new ValutazioneVM();
			v.setDenominazione(QUESTIONARIO);
			v.setLivello(1);
			v.setPeso(sv.getPesoQuestionario() == null ? 0f : sv.getPesoQuestionario().floatValue());
			v.setValutazione(sv.getRaggiungimentoQuestionario());
			items.add(v);
		}

		v = new ValutazioneVM();
		v.setDenominazione(VALUTAZIONE);
		v.setLivello(0);
		v.setValutazione(sv.getValutazione());
		items.add(v);

		out.setItems(items);
		return out;
	}

	@Override
	public void aggiornaQuestionario(ValutazioneQuestionarioRequest request) throws BusinessException {
		final Registrazione registrazione = registrazioneRepository.findById(request.getIdRegistrazione())
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
						"registrazione non trovata:" + request.getIdRegistrazione()));
		verifyAdminOrValutatore(registrazione);
		final List<ValoreAmbito> valutazioni = new ArrayList<>();
		final List<Long> ids = new ArrayList<>();
		final List<Long> olds = new ArrayList<>();
		for (Long id : request.getRisposte()) {
			ValoreAmbito va = valoreAmbitoRepository.findById(id)
					.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Valore non trovata:" + id));
			valutazioni.add(va);
			ids.add(va.getId());
		}
		final List<ValutazioneRegistrazione> items = valutazioneRegistrazioneRepository
				.findByRegistrazione(registrazione);
		if (items != null && !items.isEmpty()) {
			items.forEach(o -> olds.add(o.getValoreAmbito().getId()));
			items.removeIf(v -> ids.contains(v.getValoreAmbito().getId()));
			valutazioni.removeIf(v -> olds.contains(v.getId()));
		}
		if (items != null && !items.isEmpty()) {
			olds.clear();
			items.forEach(o -> olds.add(o.getId()));
			valutazioneRegistrazioneRepository.deleteAllById(olds);
		}
		if (valutazioni != null && !valutazioni.isEmpty()) {
			ids.clear();
			for (ValoreAmbito va : valutazioni) {
				if (!ids.contains(va.getId())) {
					ValutazioneRegistrazione vr = new ValutazioneRegistrazione(va, registrazione);
					vr = valutazioneRegistrazioneRepository.save(vr);
					ids.add(va.getId());
				}
			}
		}
	}

	@Override
	public void forzaValutazione(ValutazioneObiettiviVM valutazione) throws BusinessException {
		if (valutazione == null || valutazione.getObiettivi() == null || valutazione.getObiettivi().isEmpty()) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca valutazionecon obiettivi");
		}

		Long idSchedavalutazioneObittivo = valutazione.getObiettivi().get(0).getIdSchedaValutazioneObiettivo();
		if (idSchedavalutazioneObittivo != null) {
			SchedaValutazioneNodo scheda = schedaValutazioneNodoRepository.findById(idSchedavalutazioneObittivo)
					.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
							"scheda valutazione nodo non trovata:" + idSchedavalutazioneObittivo));
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<SchedaVM> elenco(Long idValutatore, Long idValutato) throws BusinessException {
		final List<SchedaVM> out = new ArrayList<>();
		final RisorsaUmana valutatore = risorsaUmanaRepository.findById(idValutatore).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "valutatore non trovato:" + idValutatore));
		final RisorsaUmana valutato = risorsaUmanaRepository.findById(idValutato)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "valutato non trovato:" + idValutato));
		final List<Registrazione> registrazioni = registrazioneRepository
				.findByValutatoreIdAndValutatoIdOrderByValutatoCognomeAsc(idValutatore, idValutato);
		if (registrazioni == null || registrazioni.isEmpty()) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, valutato.getCognome() + " " + valutato.getNome()
					+ " non è valutato da " + valutatore.getCognome() + " " + valutatore.getNome());
		}
		if (registrazioni != null) {
			registrazioni.stream()
					.filter(r -> !Boolean.TRUE.equals(r.getInattiva()) && !Boolean.TRUE.equals(r.getInterim()))
					.forEach(r -> {
						SchedaValutazione sv = schedaValutazioneRepository.findByRegistrazione(r);
						if (sv != null)
							out.add(mapping(sv));
					});
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public ValutazioneQuestionarioVM getQuestionario(Long idRegistrazione) throws BusinessException {
		return _questionario(idRegistrazione, true);
	}

	@Override
	@Transactional(readOnly = true)
	public ValutazioneQuestionarioVM risultatoQuestionario(Long idRegistrazione) throws BusinessException {
		return _questionario(idRegistrazione, false);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ValutazioneObiettivoVM> getObiettivi(Long idRegistrazione) throws BusinessException {
		final List<ValutazioneObiettivoVM> out = new ArrayList<>();
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		final SchedaValutazione sv = schedaValutazioneRepository.findByRegistrazione(registrazione);
		if (sv == null)
			throw new BusinessException(HttpStatus.NOT_FOUND, "scheda valutazione non ancora calcolata");
		boolean performance = sv.getPesoObiettiviPerformance() != null
				&& sv.getPesoObiettiviPerformance().doubleValue() > 0;
		if (performance) {
			Integer anno = registrazione.getAnno();
			ValutazioneObiettivoVM vo = new ValutazioneObiettivoVM();
			vo.setTipoRegolamento(TipoRegolamento.PERFORMANCE.label);
			vo.setInizio(registrazione.getInizioValidita());
			vo.setScadenza(registrazione.getFineValidita());
			vo.setIdSchedaValutazioneObiettivo(-1l);
			vo.setIdObiettivo(-1l);
			vo.setObiettivo("-");
			vo.setRaggiungimentoObiettivo(sv.getRaggiungimentoObiettiviPerformance());
			vo.setPesoObiettivo(sv.getPesoObiettiviPerformance());
			BigDecimal bd4 = BigDecimal.valueOf(sv.getRaggiungimentoObiettiviPerformance())
					.multiply(BigDecimal.valueOf(sv.getPesoObiettiviPerformance()))
					.divide(BigDecimal.valueOf(100f), 2, RoundingMode.HALF_DOWN);

			vo.setValutazioneObiettivo(bd4.floatValue());
			vo.setTipoObiettivo("");
			out.add(vo);
		} else {
			List<SchedaValutazioneNodo> nodi = schedaValutazioneNodoRepository
					.findBySchedaValutazioneOrderByNodoPianoCodiceCompleto(sv);
			if (nodi == null || nodi.isEmpty())
				return out;
			nodi.sort(
					(n1, n2) -> n1.getNodoPiano().getCodiceCompleto().compareTo(n2.getNodoPiano().getCodiceCompleto()));

			nodi.forEach(svn -> mapping(svn, out));
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public SchedaValutatoVM schedaValutatoByValutato(Long idValutato) throws BusinessException {
		final RisorsaUmana valutato = risorsaUmanaRepository.findById(idValutato)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "valutato non trovato:" + idValutato));
		String codiceUnivoco = CalcoloValutazioneHelper.calcolaCodiceUnivoco(valutato);
		SchedaValutato sv = schedaValutatoRepository.findByIdEnteAndAnnoAndCodiceUnivoco(valutato.getIdEnte(),
				valutato.getAnno(), codiceUnivoco);
		if (sv == null)
			throw new BusinessException(HttpStatus.NOT_FOUND, "scheda valutazione non ancora calcolata");
		List<SchedaValutazione> items = schedaValutazioneRepository.findBySchedaValutato(sv);
		final SchedaValutatoVM out = new SchedaValutatoVM();
		out.setAnno(valutato.getAnno());
		out.setCodiceUnivoco(codiceUnivoco);
		out.setCognome(valutato.getCognome());
		out.setDataOraCalcolo(sv.getDataOraCalcolo());
		out.setId(sv.getId());
		out.setIdEnte(valutato.getIdEnte());
		out.setNome(valutato.getNome());
		out.setValutazione(sv.getValutazione());
		if (items != null && !items.isEmpty()) {
			out.setValutazioni(items.stream()
					.filter(sc -> !Boolean.TRUE.equals(sc.getRegistrazione().getInattiva())
							&& !Boolean.TRUE.equals(sc.getRegistrazione().getInterim()))
					.map(sc -> mapping(sc)).collect(Collectors.toList()));
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public SchedaValutatoVM schedaValutatoByRegistrazione(Long idRegistrazione) throws BusinessException {
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		final RisorsaUmana valutato = registrazione.getValutato();
		String codiceUnivoco = CalcoloValutazioneHelper.calcolaCodiceUnivoco(valutato);
		SchedaValutato sv = schedaValutatoRepository.findByIdEnteAndAnnoAndCodiceUnivoco(valutato.getIdEnte(),
				valutato.getAnno(), codiceUnivoco);
		if (sv == null)
			throw new BusinessException(HttpStatus.NOT_FOUND, "scheda valutazione non ancora calcolata");
		List<SchedaValutazione> items = schedaValutazioneRepository.findBySchedaValutato(sv);
		final SchedaValutatoVM out = new SchedaValutatoVM();
		out.setAnno(valutato.getAnno());
		out.setCodiceUnivoco(codiceUnivoco);
		out.setCognome(valutato.getCognome());
		out.setDataOraCalcolo(sv.getDataOraCalcolo());
		out.setId(sv.getId());
		out.setIdEnte(valutato.getIdEnte());
		out.setNome(valutato.getNome());
		out.setValutazione(sv.getValutazione());
		if (items != null && !items.isEmpty()) {
			out.setValutazioni(items.stream()
					.filter(sc -> !Boolean.TRUE.equals(sc.getRegistrazione().getInattiva())
							&& !Boolean.TRUE.equals(sc.getRegistrazione().getInterim()))
					.map(sc -> mapping(sc)).collect(Collectors.toList()));
		}
		return out;
	}

	@Override
	public void pubblicazioneScheda(Long idRegistrazione, String note) throws BusinessException {
		Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		verifyValutatore(registrazione);

		if (registrazione.getDataPubblicazioneScheda() != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Scheda già pubblicata");
		}
		registrazione.setDataPubblicazioneScheda(LocalDate.now());
		registrazione.setNotePubblicazioneScheda(note);
		registrazione = registrazioneRepository.save(registrazione);
		String msg = invioSchedaBody.replace(NNNNN, StringUtils.capitalize(registrazione.getValutatore().getCognome())
				+ " " + StringUtils.capitalize(registrazione.getValutatore().getNome()));
		if (StringUtils.isNotBlank(note)) {
			msg = msg + "\n\nNote valutatore:\n" + note;
		}

		try {
			fvgEmailService.sendMail(registrazione.getValutato().getEmail(), registrazione.getValutatore().getEmail(),
					invioSchedaSubject, msg);
		} catch (MessagingException e) {
			new BusinessException(HttpStatus.BAD_REQUEST, "Email non inviata:" + e.getMessage());
		}
	}

	@Override
	public void accettazioneScheda(Long idRegistrazione, Boolean flagAccettazione, String note)
			throws BusinessException {
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		if (flagAccettazione == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Accettazione scheda non confermata o rifiutata");
		}
		verifyValutato(registrazione);

		if (registrazione.getDataPubblicazioneScheda() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Scheda non ancora pubblicata");
		}
		if (registrazione.getDataAccettazioneScheda() != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Scheda già accettata");
		}
		registrazione.setDataAccettazioneScheda(LocalDate.now());
		registrazione.setNoteAccettazioneScheda(note);
		registrazione.setFlagAccettazioneScheda(flagAccettazione);
		registrazioneRepository.save(registrazione);

		String msg = presaVisioneSchedaBody.replace(NNNNN,
				StringUtils.capitalize(registrazione.getValutato().getCognome()) + " "
						+ StringUtils.capitalize(registrazione.getValutato().getNome()));
		if (StringUtils.isNotBlank(note)) {
			msg = msg + "\n\nNote valutato:\n" + note;
		}
		try {
			fvgEmailService.sendMail(registrazione.getValutatore().getEmail(), registrazione.getValutato().getEmail(),
					presaVisioneSchedaSubject, msg);
		} catch (MessagingException e) {
			new BusinessException(HttpStatus.BAD_REQUEST, "Email non inviata:" + e.getMessage());
		}
	}

	@Override
	public void approvazioneScheda(Long idRegistrazione, String note) throws BusinessException {
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		verifyOIV(registrazione);
		if (registrazione.getDataPubblicazioneScheda() != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Scheda già pubblicata");
		}
		if (registrazione.getDataApprovazioneSchedaOiv() != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Scheda già approvata da OIV");
		}
		registrazione.setDataApprovazioneSchedaOiv(LocalDate.now());
		registrazione.setNoteApprovazioneSchedaOiv(note);
		registrazioneRepository.save(registrazione);
	}

	@Override
	public void pubblicazioneValutazione(Long idRegistrazione, String note) throws BusinessException {
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		verifyValutatore(registrazione);

		SchedaValutazione sv = schedaValutazioneRepository.findByRegistrazione(registrazione);
		if (sv == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Scheda valutazione non calcolata");
		}
		if (registrazione.getDataAccettazioneScheda() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Scheda mai accettata");
		}
		if (registrazione.getDataPubblicazioneValutazione() != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Valutazione già pubblicata");
		}
		registrazione.setDataPubblicazioneValutazione(LocalDate.now());
		registrazione.setNotePubblicazioneValutazione(note);
		registrazioneRepository.save(registrazione);
		String msg = invioValutazioneBody.replace(NNNNN,
				StringUtils.capitalize(registrazione.getValutatore().getCognome()) + " "
						+ StringUtils.capitalize(registrazione.getValutatore().getNome()));
		if (StringUtils.isNotBlank(note)) {
			msg = msg + "\n\nNote valutatore:\n" + note;
		}

		try {
			fvgEmailService.sendMail(registrazione.getValutato().getEmail(), registrazione.getValutatore().getEmail(),
					invioValutazioneSubject, msg);
		} catch (MessagingException e) {
			new BusinessException(HttpStatus.BAD_REQUEST, "Email non inviata:" + e.getMessage());
		}

	}

	@Override
	public void accettazioneValutazione(Long idRegistrazione, Boolean flagAccettazione, String note)
			throws BusinessException {
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));

		verifyValutato(registrazione);
		SchedaValutazione sv = schedaValutazioneRepository.findByRegistrazione(registrazione);
		if (sv == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Scheda valutazione non calcolata");
		}
		if (flagAccettazione == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Accettazione scheda non confermata o rifiutata");
		}
		if (registrazione.getDataPubblicazioneValutazione() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Valutazione non ancora pubblicata");
		}
		if (registrazione.getDataAccettazioneValutazione() != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Valutazione già accettata");
		}
		registrazione.setDataAccettazioneValutazione(LocalDate.now());
		registrazione.setNoteAccettazioneValutazione(note);
		registrazione.setFlagAccettazioneValutazione(flagAccettazione);
		registrazioneRepository.save(registrazione);
		String msg = presaVisioneValutazioneBody.replace(NNNNN,
				StringUtils.capitalize(registrazione.getValutatore().getCognome()) + " "
						+ StringUtils.capitalize(registrazione.getValutatore().getNome()));
		if (StringUtils.isNotBlank(note)) {
			msg = msg + "\n\nNote valutatore:\n" + note;
		}

		try {
			fvgEmailService.sendMail(registrazione.getValutato().getEmail(), registrazione.getValutatore().getEmail(),
					presaVisioneValutazioneSubject, msg);
		} catch (MessagingException e) {
			new BusinessException(HttpStatus.BAD_REQUEST, "Email non inviata:" + e.getMessage());
		}
	}

	@Override
	public void approvazioneValutazione(Long idRegistrazione, String note) throws BusinessException {
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		verifyOIV(registrazione);
		SchedaValutazione sv = schedaValutazioneRepository.findByRegistrazione(registrazione);
		if (sv == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Scheda valutazione non calcolata");
		}
		if (registrazione.getDataPubblicazioneValutazione() != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Valutazione già pubblicata");
		}
		if (registrazione.getDataApprovazioneValutazioneOiv() != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Valutazione già approvata da OIV");
		}
		registrazione.setDataApprovazioneValutazioneOiv(LocalDate.now());
		registrazione.setNoteApprovazioneValutazioneOiv(note);
		registrazioneRepository.save(registrazione);

	}

	@Override
	@Transactional(readOnly = true)
	public NoteValutazioneVM getNote(Long idRegistrazione) throws BusinessException {
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		verifyAdminOrOIVOrValutatoOrValutatore(registrazione);
		NoteValutazioneVM out = new NoteValutazioneVM();
		Mapping.mapping(registrazione, out);
		return out;
	}

	@Override
	public List<SchedaVM> elencoAll(Long idValutato, Long idValutatore) throws BusinessException {
		final RisorsaUmana valutato = risorsaUmanaRepository.findById(idValutato)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "valutato non trovato:" + idValutato));
		String codiceUnivoco = CalcoloValutazioneHelper.calcolaCodiceUnivoco(valutato);
		SchedaValutato sv = schedaValutatoRepository.findByIdEnteAndAnnoAndCodiceUnivoco(valutato.getIdEnte(),
				valutato.getAnno(), codiceUnivoco);
		List<SchedaValutazione> items = schedaValutazioneRepository.findBySchedaValutato(sv);
		if (items == null)
			return new ArrayList<>();
		if (idValutatore == null) {
			return items.stream()
					.filter(s -> !Boolean.TRUE.equals(s.getRegistrazione().getInattiva())
							&& !Boolean.TRUE.equals(s.getRegistrazione().getInterim()))
					.map(s -> mapping(s)).collect(Collectors.toList());
		} else {
			return items.stream().filter(s -> idValutatore.equals(s.getRegistrazione().getValutatore().getId()))
					.filter(s -> !Boolean.TRUE.equals(s.getRegistrazione().getInattiva())
							&& !Boolean.TRUE.equals(s.getRegistrazione().getInterim()))
					.map(s -> mapping(s)).collect(Collectors.toList());

		}
	}

	private SchedaVM mapping(SchedaValutazione sv) {
		final SchedaVM out = new SchedaVM();
		mapping(out, sv);
		return out;
	}

	private void mapping(SchedaVM out, SchedaValutazione sv) {
		final Registrazione r = sv.getRegistrazione();
		out.setIdSchedaValutazione(sv.getId());
		out.setFine(r.getFineValidita());
		out.setIdRegistrazione(r.getId());
		out.setInizio(r.getInizioValidita());
		out.setFine(r.getFineValidita());
		out.setResponsabile(Boolean.TRUE.equals(r.getResponsabile()));
		if (r.getQuestionario() != null) {
			out.setIdQuestionario(r.getQuestionario().getId());
			out.setQuestionario(r.getQuestionario().getIntestazione());
		}
		if (r.getRegolamento() != null) {
			out.setIdRegolamento(r.getRegolamento().getId());
			out.setRegolamento(r.getRegolamento().getIntestazione());
		}
		if (r.getValutato() != null) {
			out.setIdValutato(r.getValutato().getId());
			out.setValutato(r.getValutato().getCognome() + " " + r.getValutato().getNome());
			String codiceUnivoco = CalcoloValutazioneHelper.calcolaCodiceUnivoco(r.getValutato());
			out.setCodiceUnivoco(codiceUnivoco);
		}
		if (r.getValutatore() != null) {
			out.setIdValutatore(r.getValutatore().getId());
			out.setValutatore(r.getValutatore().getCognome() + " " + r.getValutatore().getNome());
		}
		if (r.getOrganizzazione() != null) {
			out.setIdStruttura(r.getOrganizzazione().getId());
			out.setStruttura(r.getOrganizzazione().getIntestazione());
		}
		out.setValutazione(sv.getValutazione());
		out.setPo(Boolean.TRUE.equals(r.getPo()));
		out.setMancataAssegnazione(Boolean.TRUE.equals(r.getMancataAssegnazione()));
		out.setMancatoColloquio(Boolean.TRUE.equals(r.getMancatoColloquio()));
		out.setPerformanceOrganizzativa(r.getRegolamento().getPesoObiettiviDiPerformance() != null
				&& r.getRegolamento().getPesoObiettiviDiPerformance().doubleValue() > 0);
		out.setPesoPerformanceOrganizzativa(r.getRegolamento().getPesoObiettiviDiPerformance() != null
				&& r.getRegolamento().getPesoObiettiviDiPerformance().doubleValue() > 0
						? r.getRegolamento().getPesoObiettiviDiPerformance().floatValue()
						: null);
		out.setInterim(Boolean.TRUE.equals(r.getInterim()));
		final Ruolo ruolo = SecurityUtils.ruolo(utenteBusiness, r.getAnno());

		attivazioni(ruolo, r, out);
	}

	private void attivazioni(final Ruolo ruolo, final Registrazione r, final SchedaVM out) {
		final Cruscotto c = cruscottoRepository.findByIdEnteAndAnno(r.getIdEnte(), r.getAnno()).orElse(null);
		if (c == null || ruolo == null)
			return;
		final LocalDate oggi = LocalDate.now();

		out.setAbilitaInvioScheda(
				abilita(c.getFlagConfermaScheda(), c.getDataConfermaSchedaDa(), c.getDataConfermaSchedaA(), oggi));

		out.setAbilitaInvioValutazione(abilita(c.getFlagCompletamentoScheda(), c.getDataCompletamentoSchedaDa(),
				c.getDataCompletamentoSchedaA(), oggi));

		if (Ruolo.AMMINISTRATORE.equals(ruolo) || Ruolo.SUPPORTO_SISTEMA.equals(ruolo)) {
			if (c.getDataPerformanceOrgDa() == null || c.getDataPerformanceOrgDa().isAfter(oggi)
					|| (c.getFlagValidazioneScheda() != null && c.getDataValidazioneSchedaA() != null
							&& c.getDataValidazioneSchedaA().isBefore(oggi))) {
				out.setAbilitaPerformance(false);
			} else {
				out.setAbilitaPerformance(true);
			}
			return;
		}

		out.setAbilitaPerformance(
				abilita(c.getFlagPerformanceOrg(), c.getDataPerformanceOrgDa(), c.getDataPerformanceOrgA(), oggi));
	}

	private boolean abilita(Boolean flag, LocalDate da, LocalDate a, LocalDate oggi) {
		if (!Boolean.TRUE.equals(flag) || da == null || a == null || da.isAfter(oggi) || a.isBefore(oggi)) {
			return false;
		} else {
			return true;
		}
	}

	private void verifyAdminOrOIVOrValutatoOrValutatore(Registrazione registrazione) {
		if (registrazione.getValutatore() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca valutatore");
		}
		if (StringUtils.isBlank(registrazione.getValutatore().getEmail())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca mail valutatore");
		}
		if (registrazione.getValutato() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca valutato");
		}
		if (StringUtils.isBlank(registrazione.getValutato().getEmail())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca mail valutato");
		}
		Profilo p = getProfilo(registrazione.getAnno());
		Long idRisorsa = p.getRisorsaUmana() == null ? null : p.getRisorsaUmana().getId();
		if (idRisorsa != null && idRisorsa.equals(registrazione.getValutatore().getId())) {
			return;
		}
		if (idRisorsa != null && idRisorsa.equals(registrazione.getValutato().getId())) {
			return;
		}
		if (Ruolo.OIV.equals(p.getRuolo())) {
			return;
		}
		if (Ruolo.AMMINISTRATORE.equals(p.getRuolo()) || Ruolo.SUPPORTO_SISTEMA.equals(p.getRuolo())) {
			return;
		}
		throw new BusinessException(HttpStatus.BAD_REQUEST,
				"Il richiedetente deve essere valutatore o valutato o OIV o admin");
	}

	private void verifyOIV(Registrazione registrazione) {
		if (registrazione.getValutatore() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca valutatore");
		}
		if (StringUtils.isBlank(registrazione.getValutatore().getEmail())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca mail valutatore");
		}
		if (registrazione.getValutato() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca valutato");
		}
		if (StringUtils.isBlank(registrazione.getValutato().getEmail())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca mail valutato");
		}
		Profilo p = getProfilo(registrazione.getAnno());
		if (p == null || !Ruolo.OIV.equals(p.getRuolo())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Il richiedente non è un OIV");
		}
	}

	private void verifyValutatore(Registrazione registrazione) {
		if (registrazione.getValutatore() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca valutatore");
		}
		if (StringUtils.isBlank(registrazione.getValutatore().getEmail())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca mail valutatore");
		}
		if (registrazione.getValutato() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca valutato");
		}
		if (StringUtils.isBlank(registrazione.getValutato().getEmail())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca mail valutato");
		}
		Long idRisorsa = getRisorsa(registrazione.getAnno());
		if (idRisorsa == null || !idRisorsa.equals(registrazione.getValutatore().getId())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Il richiedente non è il valutatore");
		}
	}

	private void verifyAdminOrValutatore(Registrazione registrazione) {
		if (registrazione.getValutatore() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca valutatore");
		}
		if (StringUtils.isBlank(registrazione.getValutatore().getEmail())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca mail valutatore");
		}
		if (registrazione.getValutato() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca valutato");
		}
		if (StringUtils.isBlank(registrazione.getValutato().getEmail())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca mail valutato");
		}
		Profilo p = getProfilo(registrazione.getAnno());
		Long idRisorsa = p.getRisorsaUmana() == null ? null : p.getRisorsaUmana().getId();
		if (idRisorsa != null && idRisorsa.equals(registrazione.getValutatore().getId())) {
			return;
		}
		if (Ruolo.AMMINISTRATORE.equals(p.getRuolo()) || Ruolo.SUPPORTO_SISTEMA.equals(p.getRuolo())) {
			return;
		}
		throw new BusinessException(HttpStatus.BAD_REQUEST, "Il richiedetente deve essere valutatore o admin");
	}

	private Long getRisorsa(Integer anno) {
		Profilo profilo = getProfilo(anno);
		return profilo.getRisorsaUmana() == null ? null : profilo.getRisorsaUmana().getId();
	}

	private Profilo getProfilo(Integer anno) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Utente utente = utenteRepository.findByUserid(username);
		if (utente == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Utente non trovato:" + username);
		}
		List<Profilo> profili = profiloRepository.findByUtenteId(utente.getId());
		Profilo profilo = null;
		if (profili != null && !profili.isEmpty()) {
			for (Profilo p : profili) {
				if (anno.equals(p.getAnno()) && !Boolean.TRUE.equals(p.getAggiunto())) {
					profilo = p;
					break;
				}
			}
		}
		if (profilo == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Profilo non trovato nell'anno:" + anno);
		}

		return profilo;
	}

	private void verifyValutato(Registrazione registrazione) {
		if (registrazione.getValutatore() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca valutatore");
		}
		if (StringUtils.isBlank(registrazione.getValutatore().getEmail())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca mail valutatore");
		}
		if (registrazione.getValutato() == null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca valutato");
		}
		if (StringUtils.isBlank(registrazione.getValutato().getEmail())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca mail valutato");
		}
		Long idRisorsa = getRisorsa(registrazione.getAnno());
		if (idRisorsa == null || !idRisorsa.equals(registrazione.getValutato().getId())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Il richiedente non è il valutato");
		}

	}

	private ValutazioneQuestionarioVM _questionario(Long idRegistrazione, boolean tutti) {
		final Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));
		final Regolamento regolamento = registrazione.getRegolamento();
		if (regolamento == null)
			throw new BusinessException(HttpStatus.NOT_FOUND, "registrazione senza regolamento:" + idRegistrazione);
		final Questionario questionario = registrazione.getQuestionario();
		if (questionario == null)
			return null;
		final Ruolo ruolo = SecurityUtils.ruolo(utenteBusiness, registrazione.getAnno());
		final boolean admin = Ruolo.AMMINISTRATORE.equals(ruolo) || Ruolo.SUPPORTO_SISTEMA.equals(ruolo);

		final ValutazioneQuestionarioVM out = new ValutazioneQuestionarioVM();
		out.setIdRegistrazione(idRegistrazione);

		out.setIdQuestionario(questionario.getId());
		out.setQuestionario(questionario.getIntestazione());
		final AmbitoValutazione mancataAssegnazione = ambitoValutazioneRepository
				.findTopByQuestionarioAndFogliaIsTrueAndPesoMancataAssegnazioneIsNotNull(questionario);
		final AmbitoValutazione mancatoColloquio = ambitoValutazioneRepository
				.findTopByQuestionarioAndFogliaIsTrueAndPesoMancatoColloquioIsNotNull(questionario);
		BigDecimal tot = BigDecimal.ZERO;
		final List<ElementoQuestionario> items = new ArrayList<>();
		out.setItems(items);

		List<ValoreAmbito> domande = valoreAmbitoRepository
				.findByAmbitoValutazioneQuestionarioIdOrderByAmbitoValutazioneCodiceCompletoAscCodiceAsc(
						questionario.getId());
		verificaMancanze(domande, registrazione, mancataAssegnazione, mancatoColloquio, admin);
		List<ValutazioneRegistrazione> valutazioni = valutazioneRegistrazioneRepository
				.findByRegistrazioneOrderByValoreAmbitoAmbitoValutazioneCodiceCompletoAsc(registrazione);
		Map<Long, Boolean> selected = new HashMap<>();
		List<Long> padri = new ArrayList<>();
		if (valutazioni != null) {
			valutazioni.stream().forEach(v -> {
				selected.put(v.getValoreAmbito().getId(), Boolean.TRUE);
			});
		}
		for (ValoreAmbito d : domande) {
			caricaPadri(d.getAmbitoValutazione(), items, padri);
			ElementoQuestionario ele = new ElementoQuestionario(d);
			ele.setGruppo(d.getAmbitoValutazione().getId());
			Boolean sel = selected.get(d.getId());
			ele.setSelected(sel == null ? false : sel);
			if (ele.isSelected() || tutti)
				items.add(ele);
			if (ele.isSelected()) {
				tot = tot.add(BigDecimal.valueOf(ele.getPeso() == null ? 0f : ele.getPeso()));
			}
		}
		out.setRaggiungimentoQuestionario(tot.floatValue());
		return out;
	}

	private void verificaMancanze(final List<ValoreAmbito> domande, final Registrazione registrazione,
			final AmbitoValutazione mancataAssegnazione, final AmbitoValutazione mancatoColloquio,
			final boolean admin) {
		final List<Long> rimuovi = new ArrayList<>();
		for (ValoreAmbito d : domande) {
			if (Boolean.TRUE.equals(d.getAmbitoValutazione().getFlagSoloAdmin()) && !admin) {
				if (!rimuovi.contains(d.getId())) {
					rimuovi.add(d.getId());
				}
			} 
			if ( Boolean.TRUE.equals(registrazione.getMancataAssegnazione())
					&& d.getAmbitoValutazione().getId().equals(mancataAssegnazione.getId())
					&& d.getPeso().floatValue() > mancataAssegnazione.getPesoMancataAssegnazione().floatValue()

				) {
				if (!rimuovi.contains(d.getId())) {
					rimuovi.add(d.getId());
				}
			}
			if (Boolean.TRUE.equals(registrazione.getMancatoColloquio())
					&& d.getAmbitoValutazione().getId().equals(mancatoColloquio.getId())
							&& d.getPeso().floatValue() > mancatoColloquio.getPesoMancatoColloquio().floatValue()) {
				if (!rimuovi.contains(d.getId())) {
					rimuovi.add(d.getId());
				}
			}
		}
		if(!rimuovi.isEmpty()) {
			domande.removeIf(d->rimuovi.contains(d.getId()));
		}
		
//		if (Boolean.TRUE.equals(registrazione.getMancataAssegnazione()) && mancataAssegnazione != null
//				&& mancataAssegnazione.getPesoMancataAssegnazione().floatValue() > 0) {
//			domande.removeIf(d -> d.getAmbitoValutazione().getId().equals(mancataAssegnazione.getId())
//					&& d.getPeso().floatValue() > mancataAssegnazione.getPesoMancataAssegnazione().floatValue());
//		}
//		if (Boolean.TRUE.equals(registrazione.getMancatoColloquio()) && mancatoColloquio != null
//				&& mancatoColloquio.getPesoMancatoColloquio().floatValue() > 0) {
//			domande.removeIf(d -> d.getAmbitoValutazione().getId().equals(mancatoColloquio.getId())
//					&& d.getPeso().floatValue() > mancatoColloquio.getPesoMancatoColloquio().floatValue());
//		}
	}

	private void mapping(final SchedaValutazioneNodo svn, final List<ValutazioneObiettivoVM> items) {
		if(svn.getPeso()==null || svn.getPeso()<=0)return;
		Integer anno = svn.getNodoPiano().getAnno();
		items.add(new ValutazioneObiettivoVM(svn));
		List<SchedaValutazioneIndicatore> list = schedaValutazioneIndicatoreRepository
				.findBySchedaValutazioneNodoOrderByIndicatorePianoSpecifica(svn);
		if (list != null) {
			for (SchedaValutazioneIndicatore svi : list) {
				ValutazioneIndicatoreVM vi = new ValutazioneIndicatoreVM(svi);
				vi.setTarget(trovaTarget(svi.getIndicatorePiano(), anno));
				items.add(vi);
			}
		}

	}

	private String trovaTarget(IndicatorePiano indicatorePiano, Integer anno) {
		Valutazione p = valutazioneRepository.findTopByIndicatorePianoAndAnnoAndTipoValutazioneOrderByPeriodoDesc(
				indicatorePiano, anno, TipoValutazione.PREVENTIVO);
		return ValutazioneHelper.getValToString(p);

	}

	private void caricaPadri(final AmbitoValutazione a, final List<ElementoQuestionario> items, List<Long> padri) {
		if (a.getPadre() != null)
			caricaPadri(a.getPadre(), items, padri);
		if (!padri.contains(a.getId())) {
			items.add(new ElementoQuestionario(a));
			padri.add(a.getId());
		}
	}

	@Override
	public void performanceOrganizzativa(Long idRegistrazione, Long idScheda, Boolean attiva) throws BusinessException {
		SchedaValutazione sv = null;
		Registrazione registrazione = registrazioneRepository.findById(idRegistrazione).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "registrazione non trovata:" + idRegistrazione));

		if (idScheda != null) {
			sv = schedaValutazioneRepository.findById(idScheda).orElse(null);
			if (sv == null)
				throw new BusinessException(HttpStatus.NOT_FOUND, "scheda valutazione non trovata:" + idScheda);
			if (!sv.getRegistrazione().getId().equals(idRegistrazione)) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, "Scheda con registrazione diversa");
			}
		}

		final BigDecimal pp = registrazione.getRegolamento().getPesoObiettiviDiPerformance();
		final RisorsaUmana valutato = registrazione.getValutato();
		final List<Regolamento> regolamenti = regolamentoRepository.findByIdEnteAndAnno(valutato.getIdEnte(),
				valutato.getAnno());
		final List<Questionario> questionari = questionarioRepository.findByIdEnte(valutato.getIdEnte());
		boolean modificato = false;
		if (Boolean.TRUE.equals(attiva)) {
			if (pp == null || pp.doubleValue() == 0) {
				modificato = true;
				// cerca regolamento con performance
				Regolamento reg = trovaRegolamento(valutato, regolamenti, true);
				if (reg == null) {
					throw new BusinessException(HttpStatus.BAD_REQUEST,
							"Non trovato regolamento con performance organizzativa per:" + valutato.getCognome() + " "
									+ valutato.getNome() + " " + (valutato.getCategoria() == null ? "(null)"
											: valutato.getCategoria().getCodice()));

				}
				registrazione.setRegolamento(reg);
				registrazione.setQuestionario(null);
				if (reg.getPesoComportamentiOrganizzativi() != null
						&& reg.getPesoComportamentiOrganizzativi().doubleValue() > 0) {
					Questionario q = trovaQuestionario(valutato, questionari, reg);
					if (q == null) {
						throw new BusinessException(HttpStatus.BAD_REQUEST, "Non trovato questionario per:"
								+ valutato.getCognome() + " " + valutato.getNome() + " "
								+ (valutato.getCategoria() == null ? "(null)" : valutato.getCategoria().getCodice()));

					}
					registrazione.setQuestionario(q);
				}
			}
		} else {
			if (pp != null && pp.doubleValue() > 0) {
				modificato = true;
				// cerca regolamento senza performance
				Regolamento reg = trovaRegolamento(valutato, regolamenti, false);
				if (reg == null) {
					throw new BusinessException(HttpStatus.BAD_REQUEST, "Non trovato regolamento per:"
							+ valutato.getCognome() + " " + valutato.getNome() + " "
							+ (valutato.getCategoria() == null ? "(null)" : valutato.getCategoria().getCodice()));

				}
				registrazione.setRegolamento(reg);
				registrazione.setQuestionario(null);
				if (reg.getPesoComportamentiOrganizzativi() != null
						&& reg.getPesoComportamentiOrganizzativi().doubleValue() > 0) {
					Questionario q = trovaQuestionario(valutato, questionari, reg);
					if (q == null) {
						throw new BusinessException(HttpStatus.BAD_REQUEST, "Non trovato questionario per:"
								+ valutato.getCognome() + " " + valutato.getNome() + " "
								+ (valutato.getCategoria() == null ? "(null)" : valutato.getCategoria().getCodice()));

					}
					registrazione.setQuestionario(q);
				}
			}
		}
		if (modificato) {
			registrazione = registrazioneRepository.save(registrazione);
			if (sv != null) {
				calcola(sv,registrazione);
				schedaValutazioneRepository.save(sv);
			}
		}

	}

	private Questionario trovaQuestionario(RisorsaUmana valutato, List<Questionario> questionari, Regolamento reg) {
		// TODO return AggiornaRegistrazionePI.trovaQuestionario(valutato, questionari, reg);
		return null;
	}

	private void calcola(SchedaValutazione sv, Registrazione registrazione) {
		// aggiornaSchedePIService.calcola(sv,registrazione);
	}

	private Regolamento trovaRegolamento(RisorsaUmana valutato, List<Regolamento> regolamenti, boolean b) {
		//AggiornaRegistrazionePI.trovaRegolamento(valutato, regolamenti, false);
		return null;
	}

}
