package com.finconsgroup.performplus.service.business.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.AllegatoIndicatorePiano;
import com.finconsgroup.performplus.domain.Configurazione;
import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.RangeIndicatore;
import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.enumeration.CalcoloConsuntivazione;
import com.finconsgroup.performplus.enumeration.Periodicita;
import com.finconsgroup.performplus.enumeration.Periodo;
import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.enumeration.StatoPiano;
import com.finconsgroup.performplus.enumeration.TipoConsuntivazione;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoValutazione;
import com.finconsgroup.performplus.repository.AllegatoIndicatorePianoRepository;
import com.finconsgroup.performplus.repository.ConfigurazioneRepository;
import com.finconsgroup.performplus.repository.IndicatorePianoRepository;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.RangeIndicatoreRepository;
import com.finconsgroup.performplus.repository.ValutazioneRepository;
import com.finconsgroup.performplus.rest.api.vm.ProfiloVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.IndicatorePianoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AddAllegatoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AggiornaConsuntivoNodoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AggiornaConsuntivoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AggiornaForzaturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AllegatoListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AllegatoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ConsuntivoIndicatoreVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ConsuntivoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ForzaturaNodoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ForzaturaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.IndicatoreViewVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.RichiestaForzaturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.StatoAvanzamentoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.StatoAvanzamentoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ValutazioneConsuntivoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ValutazioneVM;
import com.finconsgroup.performplus.rest.api.vm.v2.cruscotto.CruscottoVM;

import com.finconsgroup.performplus.service.business.IConsuntivoBusiness;
import com.finconsgroup.performplus.service.business.ICruscottoBusiness;
import com.finconsgroup.performplus.service.business.ITemplateDataBusiness;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.AttachHelper;
import com.finconsgroup.performplus.service.business.utils.ConsuntivoHelper;
import com.finconsgroup.performplus.service.business.utils.IndicatorePianoHelper;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.MappingIndicatorePianoHelper;
import com.finconsgroup.performplus.service.business.utils.MappingNodoPianoHelper;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;
import com.finconsgroup.performplus.service.business.utils.MyDateHelper;
import com.finconsgroup.performplus.service.business.utils.SecurityHelper;
import com.finconsgroup.performplus.service.dto.AzioneDTO;
import com.finconsgroup.performplus.service.dto.ConfigurazioneDTO;
import com.finconsgroup.performplus.service.dto.ConsuntivoDTO;
import com.finconsgroup.performplus.service.dto.ConsuntivoIndicatoreDTO;
import com.finconsgroup.performplus.service.dto.IndicatorePianoDTO;
import com.finconsgroup.performplus.service.dto.LineaStrategicaDTO;
import com.finconsgroup.performplus.service.dto.NodoPianoDTO;
import com.finconsgroup.performplus.service.dto.ObiettivoStrategicoDTO;
import com.finconsgroup.performplus.service.dto.PianoDTO;
import com.finconsgroup.performplus.service.dto.RelazioneIndicatoreDTO;
import com.finconsgroup.performplus.service.dto.ValutazioneDTO;

@Service
@Transactional
public class ConsuntivoBusiness implements IConsuntivoBusiness {
	private static final String _31_12 = "31/12";
//	private static final String _30_09 = "30/09";
	private static final String _30_06 = "30/06";
//	private static final String _31_03 = "31/03";
	private static final String NODO_PRS_NON_TROVATO = "nodo piano non trovato:";
	private static final String INDICATORE_PRS_NON_TROVATO = "indicatore non trovato:";
	private static final String VALUTAZIONE_NON_TROVATA = "valutazione non trovata:";
	private static final String ALLEGATO_NON_TROVATO = "allegato non trovato:";
	@Autowired
	private NodoPianoRepository nodoPianoRepository;
	@Autowired
	private IndicatorePianoRepository indicatorePianoRepository;
	@Autowired
	private ValutazioneRepository valutazioneRepository;

	@Autowired
	private ConfigurazioneRepository configurazioneRepository;

	@Autowired
	private ICruscottoBusiness cruscottoBusiness;
	@Autowired
	private IUtenteBusiness utenteBusiness;

	@Autowired
	private RangeIndicatoreRepository rangeIndicatoreRepository;
	@Autowired
	private AllegatoIndicatorePianoRepository allegatoIndicatorePianoRepository;

	@Autowired
	AttachHelper AttachHelper;

	@Autowired
	private ITemplateDataBusiness templateDataBusiness;

	@Value("${sistema.tipiNodo}")
	private TipoNodo[] tipiNodo;
	@Value("${sistema.target}")
	private Periodicita[] periodiTarget;
	@Value("${sistema.rendicontazione}")
	private Periodicita[] periodiRend;
	private static final Logger logger = LoggerFactory.getLogger(SecurityHelper.class);

	@Override
	@Transactional(readOnly = true)
	public List<NodoPianoDTO> contiene(final Long idNodo) {
		final List<NodoPianoDTO> out = new ArrayList<>();
		final NodoPiano nodo = nodoPianoRepository.findById(idNodo).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, NODO_PRS_NON_TROVATO + idNodo);
		});
		contiene(Mapping.mapping(nodo, NodoPianoDTO.class), out);
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ConsuntivoDTO> getRelazioneIndicatoriStampa(Long idEnte, int anno) throws BusinessException {
		List<ConsuntivoDTO> out = new ArrayList<>();
		ConfigurazioneDTO configurazione = null;
		Optional<Configurazione> conf = configurazioneRepository.findByIdEnteAndAnno(idEnte, anno);
		if (conf.isEmpty()) {
			configurazione = new ConfigurazioneDTO(idEnte, anno);
		} else {
			configurazione = Mapping.mapping(conf.get(), ConfigurazioneDTO.class);
		}
		NodoPiano piano = nodoPianoRepository.findTopByIdEnteAndAnnoAndTipoNodoAndStatoPianoAndDateDeleteIsNull(idEnte,
				anno, TipoNodo.PIANO, StatoPiano.ATTIVO);
		List<NodoPiano> items = nodoPianoRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithAndDateDeleteIsNullOrderByCodiceCompleto(idEnte, anno,
						piano.getCodiceCompleto() + ".");
		for (NodoPiano np : items) {
			List<ConsuntivoIndicatoreDTO> list = consuntivoIndicatori(np.getId(), Periodo.PERIODO_4, configurazione);
			boolean primo = true;
			for (ConsuntivoIndicatoreDTO c : list) {
				if (primo) {
					out.add(new ConsuntivoDTO(c).primo(primo));
					primo = false;
				}
				out.add(new ConsuntivoDTO(c).primo(primo));

			}
		}
		return out;
	}

	@Override
	public void aggiorna(ValutazioneDTO valutazione) throws BusinessException {
		try {
			Valutazione v = Mapping.mapping(valutazione, Valutazione.class);
			v.setIndicatorePiano(
					indicatorePianoRepository.findById(valutazione.getIndicatorePiano().getId()).orElse(null));
			valutazioneRepository.save(v);
		} catch (Exception ex) {
			throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@Override
	public void aggiorna(LineaStrategicaDTO obiettivoGestionale) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(obiettivoGestionale.getId()).orElseThrow();
		np.setInizioEffettivo(obiettivoGestionale.getInizioEffettivo());
		np.setScadenzaEffettiva(obiettivoGestionale.getScadenzaEffettiva());
		np.setNoteConsuntivo(obiettivoGestionale.getNoteConsuntivo());
		nodoPianoRepository.save(np);
	}

	@Override
	public void aggiorna(AzioneDTO obiettivoOperativo) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(obiettivoOperativo.getId()).orElseThrow();
		np.setInizioEffettivo(obiettivoOperativo.getInizioEffettivo());
		np.setScadenzaEffettiva(obiettivoOperativo.getScadenzaEffettiva());
		np.setNoteConsuntivo(obiettivoOperativo.getNoteConsuntivo());
		nodoPianoRepository.save(np);
	}

	@Override
	public void aggiorna(ObiettivoStrategicoDTO obiettivoStrategico) throws BusinessException {
		NodoPiano np = nodoPianoRepository.findById(obiettivoStrategico.getId()).orElseThrow();
		np.setNoteConsuntivo(obiettivoStrategico.getNoteConsuntivo());
		nodoPianoRepository.save(np);

	}

	@Override
	public void aggiornaNote(IndicatorePianoDTO indicatorePiano) throws BusinessException {
		indicatorePianoRepository.updateNote(indicatorePiano.getNote(), indicatorePiano.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ConsuntivoIndicatoreDTO> getConsuntivoIndicatori(Long idNodo, Long idEnte, Integer anno,
			Periodo periodo) {
		ConfigurazioneDTO configurazione = Mapping.mapping(configurazioneRepository.findByIdEnteAndAnno(idEnte, anno),
				ConfigurazioneDTO.class);
		return consuntivoIndicatori(idNodo, periodo, configurazione);
	}

	protected List<ConsuntivoIndicatoreDTO> consuntivoIndicatori(Long idNodo, Periodo periodo,
			ConfigurazioneDTO configurazione) {
		final NodoPiano nodo = nodoPianoRepository.findById(idNodo).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, NODO_PRS_NON_TROVATO + idNodo);
		});
		final List<ConsuntivoIndicatoreDTO> output = new ArrayList<>();
		final List<IndicatorePiano> collegati = indicatorePianoRepository.findByNodoPianoId(idNodo);
		final NodoPiano piano = nodo.getPiano();
		final int annoCorrente = MyDateHelper.getAnno();
		final int annoInizio = piano.getAnnoInizio();
		final int annoFine = piano.getAnnoFine();
		ConsuntivoIndicatoreDTO consuntivo = null;
		Double pesoTot = 0d;
		if (collegati == null) {
			return output;
		}
		for (IndicatorePiano ind : collegati) {
			if (ind.getPeso() != null && !Boolean.TRUE.equals(ind.getNonValutabile())) {
				pesoTot += ind.getPeso();
			}
		}
		for (IndicatorePiano ind : collegati) {
			consuntivo = consuntivo(ind, nodo, pesoTot, piano, configurazione.getAnno(), annoCorrente, annoInizio,
					annoFine, periodo, configurazione);
			if (consuntivo != null)
				output.add(consuntivo);
		}

		return output;
	}

	@Override
	@Transactional(readOnly = true)
	public Double raggiungimentoFigli(Long idNodo, Integer anno, Periodo periodo) {
		List<NodoPiano> figli = nodoPianoRepository.findByPadreIdAndDateDeleteIsNull(idNodo);
		if (figli == null || figli.isEmpty()) {
			return 0d;
		}
		Double totale = 0d;
		Double pesoTot = 0d;
		for (NodoPiano np : figli) {
			if (np.getPeso() != null) {
				pesoTot += np.getPeso().doubleValue();
			}
		}
		for (NodoPiano np : figli) {
			Double pr = np.getPeso() == null ? 0d : ConsuntivoHelper.pesoRelativo(np.getPeso().doubleValue(), pesoTot);
			Double ragg = raggiungimento(np, anno, periodo);
			if (ragg != null) {
				totale += ConsuntivoHelper.pesatoPerc(ragg, pr);
			}
		}
		return totale;
	}

	protected Double raggiungimento(NodoPiano nodo, Integer anno, Periodo periodo) {
		ConfigurazioneDTO configurazione = Mapping
				.mapping(configurazioneRepository.findByIdEnteAndAnno(nodo.getIdEnte(), anno), ConfigurazioneDTO.class);
		if (configurazione.getAnno() == null) {
			configurazione = new ConfigurazioneDTO(nodo.getIdEnte(), anno);
		}
		List<IndicatorePiano> collegati = indicatorePianoRepository.findByNodoPianoId(nodo.getId());
		if (collegati == null) {
			return null;
		}
		Double out = 0d;
		NodoPiano piano = Mapping.mapping(nodo.getPiano(), NodoPiano.class);
		int annoCorrente = MyDateHelper.getAnno();
		int annoInizio = piano.getAnnoInizio();
		int annoFine = piano.getAnnoFine();
		ConsuntivoIndicatoreDTO consuntivo = null;
		Double pesoTot = 0d;
		for (IndicatorePiano ind : collegati) {
			if (ind.getPeso() != null) {
				pesoTot += ind.getPeso();
			}
		}
		for (IndicatorePiano ind : collegati) {
			consuntivo = consuntivo(ind, nodo, pesoTot, piano, anno, annoCorrente, annoInizio, annoFine, periodo,
					configurazione);
			if (consuntivo != null && consuntivo.getRaggiungimentoPesato() != null)
				out += consuntivo.getRaggiungimentoPesato();
		}

		return out;
	}

	private ConsuntivoIndicatoreDTO consuntivo(final IndicatorePiano indicatorePiano, final NodoPiano nodoPiano,
			final double pesoTot, final NodoPiano piano, final int anno, final int annoCorrente, final int annoInizio,
			final int annoFine, final Periodo periodo, final ConfigurazioneDTO configurazione) {
		ConsuntivoIndicatoreDTO consuntivo = null;
		final IndicatorePianoDTO ind = Mapping.mapping(indicatorePiano, IndicatorePianoDTO.class);

		final List<Valutazione> vals = valutazioneRepository
				.findByIndicatorePianoIdOrderByAnnoAscPeriodoAsc(indicatorePiano.getId());
		for (Valutazione v : vals) {
			ValutazioneDTO dto = Mapping.mapping(v, ValutazioneDTO.class);
			boolean percentuale = indicatorePiano.getSpecificaPercentuale() != null
					? indicatorePiano.getSpecificaPercentuale()
					: (Boolean.TRUE.equals(indicatorePiano.getIndicatore().getPercentuale()));
			if (percentuale && dto.getValoreNumerico() != null && dto.getValoreNumerico().doubleValue() < 1)
				dto.setValoreNumerico(dto.getValoreNumerico().multiply(BigDecimal.valueOf(100)));
			ind.addValutazione(dto);
		}
		final NodoPianoDTO nodo = MappingNodoPianoHelper.toDto(nodoPiano);
		final PianoDTO p = Mapping.mapping(piano, PianoDTO.class);
		ind.setNodoPiano(nodo);
		if (pesoTot > 0 && ind.getPeso() != null) {
			ind.setPesoRelativo(ConsuntivoHelper.pesoRelativo(ind.getPeso(), pesoTot));
		}

		RelazioneIndicatoreDTO relazione = new RelazioneIndicatoreDTO(configurazione);
		relazione.setIndicatorePiano(ind);
		relazione.setNodoPiano(nodo);
		NodoPianoDTO np = nodo;
		if (relazione.getPreventivi().isEmpty())
			return null;
		LocalDate inizio = ConsuntivoHelper.getInizio(anno, annoInizio, annoFine, np, p);
		LocalDate fine = ConsuntivoHelper.getFine(anno, annoInizio, annoFine, np, p);
		if (!(np.getTipoNodo().ordinal() >= TipoNodo.AREA.ordinal() || (Periodo.PERIODO_4.equals(periodo))
				|| piano.getAnno() < annoCorrente))
			return null;

		ValutazioneDTO cons = getConsuntivoTrimestre(relazione.getConsuntivi(), anno, periodo,
				configurazione.getTipoConsuntivazione());
		if (cons == null && (ind.getConsuntivi() == null || piano.getAnno() >= annoCorrente)) {
			cons = new ValutazioneDTO();
			cons.setIndicatorePiano(ind);
			cons.setTipoValutazione(TipoValutazione.CONSUNTIVO);
			cons.setInizio(inizio);
			cons.setScadenza(fine);
			cons.setDataRilevazione(ConsuntivoHelper.finePeriodo(p, configurazione.getTipoConsuntivazione()));
			relazione.addConsuntivo(cons);
		}
		consuntivo = new ConsuntivoIndicatoreDTO(relazione, configurazione);

		if (ind.getPesoRelativo() != null) {
			consuntivo.setRaggiungimentoPesato(
					ConsuntivoHelper.pesatoPerc(consuntivo.getRaggiungimentoPerc(), ind.getPesoRelativo()));
		}
		consuntivo.setPesoRelativo(ind.getPesoRelativo());

		return consuntivo;
	}

	@Override
	public void aggiornaForzatura(IndicatorePianoDTO ip) throws BusinessException {
		indicatorePianoRepository.updateRaggiungimentoForzato(ip.getRaggiungimentoForzato(), ip.getId());
	}

	@Override
	public void aggiornaNonValutabile(IndicatorePianoDTO ip) throws BusinessException {
		indicatorePianoRepository.updateNonValutabile(ip.getNonValutabile(), ip.getId());
	}
	@Override
	public void aggiornaPercentualiForzatura(Long idNodoPiano , Double percentualeForzatura, Double percentualeForzaturaResp) throws BusinessException {
		nodoPianoRepository.updatePercentualiForzatura(idNodoPiano, percentualeForzatura,percentualeForzaturaResp);
	}
	
	@Override
	@Transactional(readOnly = true)
	public ConsuntivoVM read(Long idNodo) throws BusinessException {
		final NodoPiano nodo = nodoPianoRepository.findById(idNodo).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, NODO_PRS_NON_TROVATO + idNodo);
		});
		final Periodicita[] periodicitaFase = getPeridicitaFase(nodo.getTipoNodo());
		ImmutablePair<Integer, Map<Integer, Valutazione>> pair = getRendicontazione(nodo, false);
		ConsuntivoVM output = MappingNodoPianoHelper
				.mappingToConsuntivo(nodo, pair, nodoPianoRepository, indicatorePianoRepository)
				.fields(templateDataBusiness
						.findByContainerAndType(nodo.getIdEnte(), "PROGRAMMAZIONE", nodo.getTipoNodo().name())
						.getFields());
		output.setPeriodicitaTarget(periodicitaFase[0]);
		output.setPeriodicitaRend(periodicitaFase[1]);
		IndicatorePiano ind = indicatorePianoRepository.findTopByNodoPianoId(nodo.getId());
		output.setIndicatorePiano(Mapping.mapping(ind, IndicatorePianoVM.class));
		MappingNodoPianoHelper.completa(output, nodo);

		if (Periodicita.NESSUNO.equals(periodicitaFase[0]) || Periodicita.STORICO.equals(periodicitaFase[0])) {

		} else if (Periodicita.FINALE.equals(periodicitaFase[0])) {
			output.setAnnoInizioTarget(output.getAnnoFine());
			output.setAnnoFineTarget(output.getAnnoFine());
		} else {
			output.setAnnoInizioTarget(output.getAnnoInizio());
			output.setAnnoFineTarget(output.getAnnoFine());
		}
		
		output.setPeriodo(pair.left);

		output.setNoteConsuntivo(nodo.getNoteConsuntivo());
		output.setPercentualeRaggiungimentoForzata(nodo.getPercentualeRaggiungimentoForzata());
		output.setPercentualeRaggiungimentoForzataResp(nodo.getPercentualeRaggiungimentoForzataResp());	
		return output;

	}

	@Override
	@Transactional(readOnly = true)
	public List<ConsuntivoIndicatoreVM> detailConsuntivoIndicatori(Long idNodo) throws BusinessException {
		final NodoPiano nodo = nodoPianoRepository.findById(idNodo).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, NODO_PRS_NON_TROVATO + idNodo);
		});
		final Periodicita[] periodicitaFase = getPeridicitaFase(nodo.getTipoNodo());
		Configurazione configurazione = configurazioneRepository.findByIdEnteAndAnno(nodo.getIdEnte(), nodo.getAnno())
				.orElse(null);
		final List<ConsuntivoIndicatoreVM> items = new ArrayList<>();
		final List<IndicatorePiano> indicatori = indicatorePianoRepository.findByNodoPianoId(idNodo);
		final List<Valutazione> valutazioni = valutazioneRepository
				.findByIndicatorePianoNodoPianoIdOrderByIndicatorePianoIdAscAnnoAscPeriodoAsc(idNodo);

		final NodoPiano piano = TipoNodo.PIANO.equals(nodo.getTipoNodo()) ? nodo : nodo.getPiano();
		final int annoCorrente = MyDateHelper.getAnno();
		final int annoInizio = piano == null ? annoCorrente : piano.getAnnoInizio();
		final int annoFine = piano == null ? annoCorrente : piano.getAnnoFine();

		ConsuntivoIndicatoreVM consuntivo = null;
		Double pesoTot = 0d;
		if (indicatori == null) {
			return items;
		}
		for (IndicatorePiano ind : indicatori) {
			if (ind.getPeso() != null && !Boolean.TRUE.equals(ind.getNonValutabile())) {
				pesoTot += ind.getPeso();
			}
		}
		TipoConsuntivazione tipoConsuntivazione = configurazione == null ? TipoConsuntivazione.SEMESTRE
				: configurazione.getTipoConsuntivazione();
		Periodo periodo = periodoAttivo(nodo.getIdEnte(), nodo.getAnno());
		for (IndicatorePiano ind : indicatori) {
			consuntivo = getConsuntivo(ind, nodo, pesoTot, piano, nodo.getAnno(), annoCorrente, annoInizio, annoFine,
					periodo, configurazione, valutazioni, periodicitaFase[0], periodicitaFase[1]);
			if (consuntivo != null)
				items.add(consuntivo);
		}
		for (ConsuntivoIndicatoreVM c : items) {
			if (c.getPesoRelativo() != null) {
				c.setRaggiungimentoPesato(
						ConsuntivoHelper.pesatoPerc(c.getRaggiungimentoPerc(), c.getPesoRelativo().doubleValue()));
			}

		}

		return items;
	}

	@Override
	@Transactional(readOnly = true)
	public IndicatoreViewVM view(Long idIndicatore) throws BusinessException {
		final IndicatorePiano indicatorePiano = indicatorePianoRepository.findById(idIndicatore).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, INDICATORE_PRS_NON_TROVATO + idIndicatore);
		});
		return null;
	}

	@Override
	public void consuntiva(Long idIndicatore, AggiornaConsuntivoRequest request) throws BusinessException {
		final IndicatorePiano indicatorePiano = indicatorePianoRepository.findById(idIndicatore).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, INDICATORE_PRS_NON_TROVATO + idIndicatore);
		});
		final NodoPiano nodo = indicatorePiano.getNodoPiano();
		Integer anno = nodo.getAnno();
		Long idNodoPiano = nodo.getId();
		LocalDate scadenzaEffettiva = nodo.getScadenzaEffettiva();
		boolean percentuale = indicatorePiano.getSpecificaPercentuale() == null
				? Boolean.TRUE.equals(indicatorePiano.getIndicatore().getPercentuale())
				: indicatorePiano.getSpecificaPercentuale();
		final boolean desc = Boolean.TRUE.equals(indicatorePiano.getDecrescente());
		Long idEnte = nodo.getIdEnte();
		TipoFormula tipoFormula = indicatorePiano.getIndicatore().getTipoFormula();
		CalcoloConsuntivazione calcoloConsuntivazione = indicatorePiano.getIndicatore().getCalcoloConsuntivazione();

		Periodo p = periodo(idEnte, anno);
		Integer period = ConsuntivoHelper.periodo(p);
		if (request.getPeriodo() != null && !period.equals(request.getPeriodo())) {
			throw new BusinessException(HttpStatus.NOT_ACCEPTABLE,
					"Ora non si può consuntivare per l'anno " + anno + " nel " + request.getPeriodo() + "^ periodo");
		}
		Valutazione valutazione;
		BigDecimal sottrai = BigDecimal.ZERO;
		if (request.getId() != null) {
			valutazione = valutazioneRepository.findById(request.getId()).orElseThrow(() -> {
				throw new BusinessException(HttpStatus.NOT_FOUND, VALUTAZIONE_NON_TROVATA + request.getId());
			});
		} else {
			valutazione = valutazioneRepository.findByIndicatorePianoIdAndTipoValutazioneAndAnnoAndPeriodo(idIndicatore,
					TipoValutazione.CONSUNTIVO, anno, period);
		}
		if (valutazione == null) {
			valutazione = new Valutazione();
			valutazione.setAnno(anno);
			valutazione.setTipoValutazione(TipoValutazione.CONSUNTIVO);
			valutazione.setPeriodo(period);
			valutazione.setIndicatorePiano(indicatorePiano);
			valutazione.setInizio(ConsuntivoHelper.inizioPeriodo(anno, p, TipoConsuntivazione.SEMESTRE));
			valutazione.setScadenza(ConsuntivoHelper.finePeriodo(anno, p, TipoConsuntivazione.SEMESTRE));
		} else {
			sottrai = valutazione.getValoreNumerico();
		}
		LocalDate dr = request.getDataRilevazione();
		if (dr == null) {
			dr = ConsuntivoHelper.finePeriodo(anno, p, TipoConsuntivazione.SEMESTRE);
		}
		if (dr.isBefore(valutazione.getInizio()) || dr.isAfter(valutazione.getScadenza())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Data rilevazione non valida");
		}
		valutazione.setDataRilevazione(dr);
		valutazione.setValoreBooleano(request.getValoreBooleano());
		valutazione.setValoreTemporale(request.getValoreTemporale());
		if (percentuale && request.getValoreNumerico() != null && request.getValoreNumerico() > 1) {
			valutazione.setValoreNumerico(BigDecimal.valueOf(request.getValoreNumerico() / 100d));
		} else {
			valutazione.setValoreNumerico(
					request.getValoreNumerico() == null ? null : BigDecimal.valueOf(request.getValoreNumerico()));
		}
		if (percentuale && request.getValoreNumericoA() != null && request.getValoreNumericoB() == null
				&& request.getValoreNumericoA() > 1) {
			valutazione.setValoreNumericoA(BigDecimal.valueOf(request.getValoreNumericoA() / 100d));
		} else {
			valutazione.setValoreNumericoA(
					request.getValoreNumericoA() == null ? null : BigDecimal.valueOf(request.getValoreNumericoA()));
		}
		valutazione.setValoreNumericoB(
				request.getValoreNumericoB() == null ? null : BigDecimal.valueOf(request.getValoreNumericoB()));
		ConsuntivoHelper.calcolaValoreNumerico(valutazione, tipoFormula, calcoloConsuntivazione,
				MappingIndicatorePianoHelper.getDecimali(valutazione));
		valutazione = valutazioneRepository.save(valutazione);
		if (percentuale && valutazione.getValoreNumerico() != null && valutazione.getValoreNumerico().doubleValue() >= 1
				&& scadenzaEffettiva == null) {
			nodoPianoRepository.updateDataScadenzaEffettiva(idNodoPiano, LocalDate.now());
		}

	}

	@Override
	@Transactional(readOnly = true)
	public ForzaturaVM getForzatura(Long idIndicatore) throws BusinessException {
		final IndicatorePiano indicatorePiano = indicatorePianoRepository.findById(idIndicatore).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, INDICATORE_PRS_NON_TROVATO + idIndicatore));
		final ForzaturaVM out = new ForzaturaVM();
		out.setForzatura(indicatorePiano.getRaggiungimentoForzato());
		out.setId(idIndicatore);
		out.setNonValutabile(Boolean.TRUE.equals(indicatorePiano.getNonValutabile()));
		out.setNote(indicatorePiano.getNoteRaggiungimentoForzato());
		out.setRichiestaForzatura(indicatorePiano.getRichiestaForzatura());
		out.setRichiestaNote(indicatorePiano.getNoteRichiestaForzatura());
		return out;
	}

	@Override
	public void aggiornaForzatura(Long idIndicatore, AggiornaForzaturaRequest request) throws BusinessException {
		if (!indicatorePianoRepository.existsById(idIndicatore)) {
			throw new BusinessException(HttpStatus.NOT_FOUND, INDICATORE_PRS_NON_TROVATO + idIndicatore);
		}
		indicatorePianoRepository.modificaForzatura(idIndicatore, request.getForzatura(), request.getNonValutabile(),
				request.getNote());
	}

	@Override
	public void richiestaForzatura(Long idIndicatore, RichiestaForzaturaRequest request) throws BusinessException {
		if (!indicatorePianoRepository.existsById(idIndicatore)) {
			throw new BusinessException(HttpStatus.NOT_FOUND, INDICATORE_PRS_NON_TROVATO + idIndicatore);
		}
		indicatorePianoRepository.modificaRichiestaForzatura(idIndicatore, request.getForzatura(), request.getNote());

	}

	@Override
	@Transactional(readOnly = true)
	public List<AllegatoListVM> getAllegati(Long idIndicatore) throws BusinessException {
		List<AllegatoListVM> out = new ArrayList<>();
		List<AllegatoIndicatorePiano> items = allegatoIndicatorePianoRepository.findByIndicatorePianoId(idIndicatore);
		if (items != null) {
			out = items.stream().map(AllegatoListVM::new).collect(Collectors.toList());
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public AllegatoVM getAllegato(Long idAllegato) throws BusinessException {
		final AllegatoIndicatorePiano allegato = allegatoIndicatorePianoRepository.findById(idAllegato)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, ALLEGATO_NON_TROVATO + idAllegato));
		final AllegatoVM out = new AllegatoVM(allegato);
		byte[] b = AttachHelper.getAttachFile(out.getFileName(), out.getContentType());
		String base64 = Base64.encodeBase64String(b);
		out.setBase64(base64);
		return out;
	}

	@Override
	public void addAllegato(Long idIndicatore, AddAllegatoRequest request) throws BusinessException {
		final IndicatorePiano indicatorePiano = indicatorePianoRepository.findById(idIndicatore).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, INDICATORE_PRS_NON_TROVATO + idIndicatore));
		AllegatoIndicatorePiano allegato = allegatoIndicatorePianoRepository
				.findByIndicatorePianoIdAndNome(idIndicatore, request.getNome());
		String base64 = request.getBase64();
		int k = base64.indexOf(";base64,");
		if (k > 0) {
			base64 = base64.substring(k + ";base64,".length());
		}
		final byte[] content = Base64.decodeBase64(base64);
		if (allegato == null) {
			allegato = new AllegatoIndicatorePiano();
			allegato.setIndicatorePiano(indicatorePiano);
			allegato.setNome(request.getNome());
		}
		allegato.setContentType(request.getContentType());
		allegato.setDescrizione(request.getDescrizione());
		allegato.setFileName(request.getFileName());
		File file = null;
		try {
			file = AttachHelper.toFileAttach(allegato.getFileName());
			OutputStream outStream = new FileOutputStream(file);
			outStream.write(content);
			outStream.close();

		} catch (Exception e) {
			throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		allegato = allegatoIndicatorePianoRepository.save(allegato);
	}

	@Override
	public void aggiorna(Long idNodoPiano, AggiornaConsuntivoNodoRequest request) throws BusinessException {
		final NodoPiano nodo = nodoPianoRepository.findById(idNodoPiano).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, NODO_PRS_NON_TROVATO + idNodoPiano);
		});

		nodo.setInizioEffettivo(request.getDataInizio());
		nodo.setScadenzaEffettiva(request.getDataScadenza());
		nodo.setNoteConsuntivo(request.getRendicontazioneDescrittiva());
		nodoPianoRepository.save(nodo);
	}

	@Override
	@Transactional(readOnly = true)
	public ValutazioneVM getValutazione(Long idIndicatore, Long idValutazione, LocalDate dataRilevazione)
			throws BusinessException {
		final IndicatorePiano indicatorePiano = indicatorePianoRepository.findById(idIndicatore).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, INDICATORE_PRS_NON_TROVATO + idIndicatore));
		Integer anno = indicatorePiano.getNodoPiano().getAnno();
		Long idEnte = indicatorePiano.getNodoPiano().getIdEnte();
		TipoFormula tipoFormula = indicatorePiano.getIndicatore().getTipoFormula();
		CalcoloConsuntivazione calcoloConsuntivazione = indicatorePiano.getIndicatore().getCalcoloConsuntivazione();
		boolean percentuale = indicatorePiano.getSpecificaPercentuale() == null
				? Boolean.TRUE.equals(indicatorePiano.getIndicatore().getPercentuale())
				: Boolean.TRUE.equals(indicatorePiano.getSpecificaPercentuale());
		Periodo p = periodo(idEnte, anno);
		Valutazione valutazione;
		ValutazioneVM out = new ValutazioneVM();
		out.setIdIndicatore(idIndicatore);
		final Periodicita[] periodicitaFase = getPeridicitaFase(indicatorePiano.getNodoPiano().getTipoNodo());

		Integer[] dec = MappingIndicatorePianoHelper.getDecimaliAllZero(indicatorePiano,
				indicatorePiano.getIndicatore());
		out.setDecimali(dec[0]);
		out.setDecimaliA(dec[1]);
		out.setDecimaliB(dec[2]);
		out.setDecrescente(indicatorePiano.getDecrescente());
		out.setDescrizione(
				StringUtils.isBlank(indicatorePiano.getSpecifica()) ? indicatorePiano.getIndicatore().getDenominazione()
						: indicatorePiano.getSpecifica());
		out.setDenominatore(StringUtils.isBlank(indicatorePiano.getSpecificaDenominatore())
				? indicatorePiano.getIndicatore().getNomeValoreB()
				: indicatorePiano.getSpecificaDenominatore());
		out.setNumeratore(StringUtils.isBlank(indicatorePiano.getSpecificaNumeratore())
				? indicatorePiano.getIndicatore().getNomeValoreA()
				: indicatorePiano.getSpecificaNumeratore());
		out.setCalcoloConsuntivazione(indicatorePiano.getIndicatore().getCalcoloConsuntivazione());
		out.setTipoFormula(indicatorePiano.getIndicatore().getTipoFormula());
		out.setPercentuale(
				indicatorePiano.getSpecificaPercentuale() == null ? indicatorePiano.getIndicatore().getPercentuale()
						: indicatorePiano.getSpecificaPercentuale());
		out.setPeso(indicatorePiano.getPeso());
		out.setStrategico(Boolean.TRUE.equals(indicatorePiano.getStrategico()));
		out.setSviluppoSostenibile(indicatorePiano.getSviluppoSostenibile());
		out.setUnitaMisura(indicatorePiano.getUnitaMisura());
		out.setPeriodicitaTarget(periodicitaFase[0]);
		out.setPeriodicitaRend(periodicitaFase[1]);
		if (idValutazione != null) {
			valutazione = valutazioneRepository.findById(idValutazione).orElseThrow(() -> {
				throw new BusinessException(HttpStatus.NOT_FOUND, VALUTAZIONE_NON_TROVATA + idValutazione);
			});
			out.setValoreBooleano(valutazione.getValoreBooleano());
			if (percentuale && valutazione.getValoreNumerico() != null
					&& valutazione.getValoreNumerico().doubleValue() < 1) {
				out.setValoreNumerico(valutazione.getValoreNumerico().multiply(BigDecimal.valueOf(100)).doubleValue());
			} else {

				out.setValoreNumerico(
						valutazione.getValoreNumerico() == null ? null : valutazione.getValoreNumerico().doubleValue());
			}
			if (percentuale && valutazione.getValoreNumericoA() != null && valutazione.getValoreNumericoB() == null
					&& valutazione.getValoreNumericoA().doubleValue() > 1) {
				out.setValoreNumericoA(
						valutazione.getValoreNumericoA().multiply(BigDecimal.valueOf(100)).doubleValue());
			} else {
				out.setValoreNumericoA(valutazione.getValoreNumericoA() == null ? null
						: valutazione.getValoreNumericoA().doubleValue());
			}
			out.setValoreNumericoB(
					valutazione.getValoreNumericoB() == null ? null : valutazione.getValoreNumericoB().doubleValue());
			out.setValoreTemporale(valutazione.getValoreTemporale());
			out.setDataRilevazione(valutazione.getDataRilevazione());
		} else {
			LocalDate inizio = ConsuntivoHelper.inizioPeriodo(anno, p, TipoConsuntivazione.SEMESTRE);
			LocalDate scadenza = ConsuntivoHelper.finePeriodo(anno, p, TipoConsuntivazione.SEMESTRE);

			LocalDate dr = dataRilevazione;
			if (dr == null) {
				dr = ConsuntivoHelper.finePeriodo(anno, p, TipoConsuntivazione.SEMESTRE);
			}
			out.setDataRilevazione(dr);
		}
		if (Periodicita.STORICO.equals(out.getPeriodicitaTarget())) {
			out.setStorico(storico(indicatorePiano, anno));
		} else {
			out.setTarget(target(indicatorePiano, anno, out.getPeriodicitaRend()));
		}

		return out;
	}

	private String target(final IndicatorePiano indicatorePiano, final Integer anno,
			final Periodicita periodicitaRend) {
		final List<Valutazione> items = valutazioneRepository
				.findByIndicatorePianoAndTipoValutazioneOrderByAnnoAscPeriodoAsc(indicatorePiano,
						TipoValutazione.PREVENTIVO);
		if (items == null || items.isEmpty() || periodicitaRend == null)
			return "";
		Valutazione target = null;
		for (Valutazione v : items) {
			if (v.getAnno() >= anno && (target == null || target.getAnno().equals(v.getAnno())
					|| (target.getAnno() < v.getAnno() && Periodicita.FINALE.equals(periodicitaRend)))) {
				target = v;
			}
		}
		if (target == null)
			return "";
		String a = IndicatorePianoHelper.getValoreStringaA(target);
		String b = IndicatorePianoHelper.getValoreStringaB(target);
		return IndicatorePianoHelper.getValToString(target)
				+ ((a != null && b != null) ? " (" + a + "/" + b + ")" : "");
	}

	private String storico(IndicatorePiano indicatorePiano, Integer anno) {
		final List<Valutazione> items = valutazioneRepository
				.findByIndicatorePianoAndTipoValutazioneOrderByAnnoAscPeriodoAsc(indicatorePiano,
						TipoValutazione.STORICO);
		if (items == null || items.isEmpty())
			return "";
		Valutazione storico = null;
		for (Valutazione v : items) {
			if (v.getAnno() <= anno && (storico == null || storico.getAnno() < v.getAnno()
					|| (storico.getAnno().equals(v.getAnno()) && v.getPeriodo() > storico.getPeriodo()))) {
				storico = v;
			}
		}
		if (storico == null)
			return "";
		String a = IndicatorePianoHelper.getValoreStringaA(storico);
		String b = IndicatorePianoHelper.getValoreStringaB(storico);
		return IndicatorePianoHelper.getValToString(storico)
				+ ((a != null && b != null) ? " (" + a + "/" + b + ")" : "");
	}

	@Override
	public void deleteAllegato(Long idAllegato) throws BusinessException {
		allegatoIndicatorePianoRepository.deleteById(idAllegato);
	}

	@Transactional(readOnly = true)
	private ConsuntivoIndicatoreVM getConsuntivo(final IndicatorePiano indicatorePiano, final NodoPiano nodoPiano,
			final double pesoTot, final NodoPiano piano, final int anno, final int annoCorrente, final int annoInizio,
			final int annoFine, final Periodo periodo, final Configurazione configurazione,
			final List<Valutazione> valutazioni, final Periodicita periodicitaTarget,
			final Periodicita periodicitaRend) {
		UtenteVM u = SecurityHelper.utente(utenteBusiness);
		Ruolo ruolo = SecurityHelper.ruolo(u, piano.getAnno());
		ProfiloVM profilo = SecurityHelper.profilo(u, piano.getAnno());
		Long idResp = profilo == null ? null : profilo.getIdRisorsa();
		Long idDirezione = profilo == null || profilo.getOrganizzazione() == null ? null
				: profilo.getOrganizzazione().getId();
		final boolean referente = nodoPiano.getOrganizzazione() != null && idDirezione != null
				&& idDirezione.equals(nodoPiano.getOrganizzazione().getId())
				&& Ruolo.POSIZIONE_ORGANIZZATIVA.equals(ruolo);
		final boolean collaboratore = nodoPiano.getOrganizzazione() != null && idDirezione != null
				&& idDirezione.equals(nodoPiano.getOrganizzazione().getId()) && Ruolo.REFERENTE.equals(ruolo);
		final List<Ruolo> ruoliModificaSempre = List.of(Ruolo.AMMINISTRATORE, Ruolo.SUPPORTO_SISTEMA);

		boolean validatore = SecurityHelper.ALTRI_VALIDATORI.contains(ruolo) || referente;
		boolean proponente = SecurityHelper.ALTRI_PROPONENTI.contains(ruolo) || referente
				|| collaboratore;

		final boolean strategico = Boolean.TRUE.equals(nodoPiano.getStrategico());
		ConsuntivoIndicatoreVM consuntivo = new ConsuntivoIndicatoreVM();
		consuntivo.setTipoIndicatore(indicatorePiano.getTipoIndicatore());
		consuntivo.setScadenzaIndicatore(indicatorePiano.getScadenzaIndicatore());
		consuntivo.setPeriodicitaTarget(periodicitaTarget);
		consuntivo.setPeriodicitaRend(periodicitaRend);
		final TipoFormula tipoFormula = indicatorePiano.getIndicatore().getTipoFormula();
		final CalcoloConsuntivazione calcoloConsuntivazione = indicatorePiano.getIndicatore()
				.getCalcoloConsuntivazione();
		boolean automatico = CalcoloConsuntivazione.AUTOMATICO.equals(calcoloConsuntivazione);
		final boolean desc = Boolean.TRUE.equals(indicatorePiano.getDecrescente());
		consuntivo.setDenominazione(
				indicatorePiano.getSpecifica() == null ? indicatorePiano.getIndicatore().getDenominazione()
						: indicatorePiano.getSpecifica());
		consuntivo.setPeso(indicatorePiano.getPeso() == null ? null : indicatorePiano.getPeso().floatValue());
		consuntivo.setStrategico(Boolean.TRUE.equals(indicatorePiano.getStrategico()));
		consuntivo.setSviluppoSostenibile(indicatorePiano.getSviluppoSostenibile());
		if (pesoTot > 0 && indicatorePiano.getPeso() != null) {
			Double pesoRelativo = ConsuntivoHelper.pesoRelativo(indicatorePiano.getPeso(), pesoTot);
			consuntivo.setPesoRelativo(pesoRelativo.floatValue());
		}
		
		consuntivo.setId(indicatorePiano.getId());
		final List<Valutazione> preventivi = new ArrayList<>();
		final List<Valutazione> consuntivi = new ArrayList<>();
		final List<Valutazione> storici = new ArrayList<>();
		Valutazione storico = null;

		Double prev1 = null;
		Double prev2 = null;
		Double prev3 = null;
		Double prev4 = null;
		Double prev = null;
		int annoScadenza = indicatorePiano.getNodoPiano().getScadenza().getYear();
		int annoDa = indicatorePiano.getNodoPiano().getInizio().getYear();
		int annoMeta = (annoScadenza - annoDa / 2) + annoDa;
		int periodoMeta = 4;
		int periodoIniziale = ConsuntivoHelper.getPeriodo(indicatorePiano.getNodoPiano().getInizio().getMonthValue(),
				TipoConsuntivazione.SEMESTRE);
		int periodoFinale = ConsuntivoHelper.getPeriodo(indicatorePiano.getNodoPiano().getScadenza().getMonthValue(),
				TipoConsuntivazione.SEMESTRE);

		valutazioni.forEach(t -> {
			if (t.getIndicatorePiano().getId().equals(indicatorePiano.getId())) {
				switch (t.getTipoValutazione()) {
				case PREVENTIVO:
					preventivi.add(t);
					break;
				case CONSUNTIVO:
					consuntivi.add(t);
					break;
				case STORICO:
					storici.add(t);
					break;
				}
			}
		});

		Double target = null;

		int maxAnno = 0;
		for (Valutazione s : storici) {
			int year = s.getAnno();
			if (year > maxAnno) {
				maxAnno = year;
				storico = s;
			}
		}
		for (Valutazione p : preventivi) {

			int year = p.getAnno();
			if (year == anno) {
				if ((Periodicita.ANNUALE.equals(periodicitaTarget)
						&& (year < annoScadenza || (anno == annoScadenza && p.getPeriodo() >= periodoFinale)))
						|| (Periodicita.FINALE.equals(periodicitaTarget)
								&& (year == annoScadenza && p.getPeriodo() >= periodoFinale))
						|| (!Periodicita.ANNUALE.equals(periodicitaTarget)
								&& !Periodicita.FINALE.equals(periodicitaTarget))) {
					consuntivo.setTarget1(IndicatorePianoHelper.getValToString(p));
					if (Periodicita.ANNUALE.equals(periodicitaTarget)) {
						target = ConsuntivoHelper.getValore(p, tipoFormula);
						if (target != null) {
							prev = target;
							consuntivo.setTarget(IndicatorePianoHelper.getValToString(p));
							consuntivo.setPreventivo(
									new ValutazioneConsuntivoVM(p.getId(), "", IndicatorePianoHelper.getValToString(p),
											p.getDataRilevazione()).anno(p.getAnno()).period(p.getPeriodo()));
						}
					}

				}
				switch (p.getPeriodo()) {
				case 1:
				case 2:
					consuntivo.setPreventivo2(
							new ValutazioneConsuntivoVM(p.getId(), _30_06, IndicatorePianoHelper.getValToString(p),
									p.getDataRilevazione()).anno(p.getAnno()).period(p.getPeriodo()));
					prev2 = ConsuntivoHelper.getValore(p, tipoFormula);
					if (prev2 != null)
						prev = prev2;
					break;
				case 3:
				case 4:
					consuntivo.setPreventivo4(
							new ValutazioneConsuntivoVM(p.getId(), _31_12, IndicatorePianoHelper.getValToString(p),
									p.getDataRilevazione()).anno(p.getAnno()).period(p.getPeriodo()));
					prev4 = ConsuntivoHelper.getValore(p, tipoFormula);
					if (prev4 != null)
						prev = prev4;
					break;
				}
			} else if ((Periodicita.ANNUALE.equals(periodicitaTarget)
					&& (year < annoScadenza || (anno == annoScadenza && p.getPeriodo() >= periodoFinale)))
					|| (Periodicita.FINALE.equals(periodicitaTarget)
							&& (year == annoScadenza && p.getPeriodo() >= periodoFinale))
					|| (!Periodicita.ANNUALE.equals(periodicitaTarget)
							&& !Periodicita.FINALE.equals(periodicitaTarget))) {
				if (!Periodicita.ANNUALE.equals(periodicitaTarget)) {
					target = ConsuntivoHelper.getValore(p, tipoFormula);
					if (target != null) {
						prev = target;
						consuntivo.setTarget(IndicatorePianoHelper.getValToString(p));
						consuntivo.setPreventivo(
								new ValutazioneConsuntivoVM(p.getId(), "", IndicatorePianoHelper.getValToString(p),
										p.getDataRilevazione()).anno(p.getAnno()).period(p.getPeriodo()));
					}
				}
				if (year == (anno + 1)) {
					consuntivo.setTarget2(IndicatorePianoHelper.getValToString(p));
				} else if (year == (anno + 2)) {
					consuntivo.setTarget3(IndicatorePianoHelper.getValToString(p));
				} else if (year == (anno + 3)) {
					consuntivo.setTarget4(IndicatorePianoHelper.getValToString(p));
				} else if (year == (anno + 4)) {
					consuntivo.setTarget5(IndicatorePianoHelper.getValToString(p));
				} else if (year == (anno + 5)) {
					consuntivo.setTarget6(IndicatorePianoHelper.getValToString(p));
				}
			}
		}

		if (storico != null) {
			consuntivo.setStorico(
					new ValutazioneConsuntivoVM(storico.getId(), "", IndicatorePianoHelper.getValToString(storico),
							storico.getDataRilevazione()).anno(storico.getAnno()).period(storico.getPeriodo()));
		}

		Double cons = null;
		Double[] arr = null;

		if (TipoNodo.AZIONE.equals(nodoPiano.getTipoNodo())) {
			arr = consuntiviAzioni(consuntivi, consuntivo, anno, strategico, annoInizio, annoFine, u, ruolo, profilo,
					idResp, idDirezione, referente, collaboratore, ruoliModificaSempre, validatore,
					proponente, automatico, desc, periodo, indicatorePiano, tipoFormula,
					calcoloConsuntivazione);
		} else {
			arr = consuntivi(consuntivi, consuntivo, anno, strategico, annoInizio, annoFine, u, ruolo, profilo, idResp,
					idDirezione, referente, collaboratore, ruoliModificaSempre, automatico, desc,
					periodo, indicatorePiano, tipoFormula, calcoloConsuntivazione, periodicitaRend);
		}
		if (Periodicita.NESSUNO.equals(periodicitaRend))
			return consuntivo;

		cons = arr[0];
//		Double cons1 = arr[1];
		Double cons2 = arr[1];
//		Double cons3 = arr[3];
		Double cons4 = arr[2];

		consuntiviEmpty(consuntivo, annoCorrente, strategico, annoInizio, annoFine, proponente, automatico,
				periodo);

		Float pesoRelativo = consuntivo.getPeso() == null ? 0f
				: ConsuntivoHelper.pesoRelativo(consuntivo.getPeso(), pesoTot).floatValue();
		consuntivo.setPesoRelativo(pesoRelativo);
		List<RangeIndicatore> ranges = new ArrayList<>();
//				ranges=rangeIndicatoreRepository
//				.findByIndicatorePianoIdOrderByMinimoAsc(indicatorePiano.getId());
		Double ragg = 0d;
		ragg = ConsuntivoHelper.raggiungimentoRange(desc, prev, cons, tipoFormula, ranges);

		consuntivo.setRaggiungimentoPerc(ragg);
		if (consuntivo.getPesoRelativo() != null) {
			consuntivo.setRaggiungimentoPesato(ConsuntivoHelper.pesatoPerc(consuntivo.getRaggiungimentoPerc(),
					consuntivo.getPesoRelativo().doubleValue()));
		}

		return consuntivo;
	}



	private void consuntiviEmpty(ConsuntivoIndicatoreVM consuntivo, int anno, boolean strategico, int annoInizio,
			int annoFine, boolean proponente, boolean automatico, Periodo periodo) {
		if (consuntivo.getConsuntivo2() == null && Periodo.PERIODO_2.equals(periodo)) {
			consuntivo.setConsuntivo2(new ValutazioneConsuntivoVM(null, _30_06, null,
					ConsuntivoHelper.finePeriodo(anno, Periodo.PERIODO_2, TipoConsuntivazione.SEMESTRE)).anno(anno)
					.period(2).enabled(Periodo.PERIODO_2.equals(periodo) && !automatico  && proponente));
		}
		if (consuntivo.getConsuntivo4() == null && Periodo.PERIODO_4.equals(periodo)) {
			consuntivo.setConsuntivo4(new ValutazioneConsuntivoVM(null, _31_12, null,
					ConsuntivoHelper.finePeriodo(anno, Periodo.PERIODO_4, TipoConsuntivazione.SEMESTRE)).anno(anno)
					.period(4).enabled(Periodo.PERIODO_4.equals(periodo) && !automatico &&  proponente));
		}
	}

	private Double[] consuntiviAzioni(final List<Valutazione> consuntivi, final ConsuntivoIndicatoreVM consuntivo,
			final Integer anno, final boolean strategico, final Integer annoInizio, final Integer annoFine,
			final UtenteVM u, final Ruolo ruolo, final ProfiloVM profilo, final Long idResp, final Long idDirezione,
			final boolean referente, final boolean collaboratore,
			final List<Ruolo> ruoliModificaSempre, final boolean validatore, final boolean proponente,
			final boolean automatico, final boolean desc, final Periodo periodo,
			final IndicatorePiano indicatorePiano, final TipoFormula tipoFormula,
			final CalcoloConsuntivazione calcoloConsuntivazione) {

		Integer[] dec = IndicatorePianoHelper.getDecimaliAll(indicatorePiano, indicatorePiano.getIndicatore());
		if (dec[0] == null)
			dec[0] = 0;
		if (dec[1] == null)
			dec[1] = 0;
		if (dec[2] == null)
			dec[2] = 0;

//		Double cons1 = null;
		Double cons2 = null;
//		Double cons3 = null;
		Double cons4 = null;
		Double cons = null;
		if (automatico && TipoNodo.AZIONE.equals(indicatorePiano.getNodoPiano().getTipoNodo())) {
			/*
			 * estrai scadenzaEffettiva fasi se precedente o uguale fine periodo 100%
			 * altrimenti 0% risultato uguale somma percentuali fratto numero fasi
			 * 
			 */
			List<NodoPiano> fasi = nodoPianoRepository.findByPadreAndDateDeleteIsNull(indicatorePiano.getNodoPiano());

			if (fasi != null) {
//				cons1 = 0d;
				cons2 = 0d;
//				cons3 = 0d;
				cons4 = 0d;
				cons = 0d;
//				LocalDate date1 = ConsuntivoHelper.finePeriodo(anno, 1, TipoConsuntivazione.TRIMESTRE);
				LocalDate date2 = ConsuntivoHelper.finePeriodo(anno, 2, TipoConsuntivazione.SEMESTRE);
//				LocalDate date3 = ConsuntivoHelper.finePeriodo(anno, 3, TipoConsuntivazione.TRIMESTRE);
				LocalDate date4 = ConsuntivoHelper.finePeriodo(anno, 4, TipoConsuntivazione.SEMESTRE);
				int n = fasi.size();
				for (NodoPiano fase : fasi) {
					if (fase.getScadenzaEffettiva() != null) {
//						if (!fase.getScadenzaEffettiva().isAfter(date1)) {
//							cons1 = cons1 + 1;
//						}
						if (!fase.getScadenzaEffettiva().isAfter(date2)) {
							cons2 = cons2 + 1;
						}
//						if (!fase.getScadenzaEffettiva().isAfter(date3)) {
//							cons3 = cons3 + 1;
//						}
						if (!fase.getScadenzaEffettiva().isAfter(date4)) {
							cons4 = cons4 + 1;
						}
					}
				}
				if (n > 1) {
//					cons1 = BigDecimal.valueOf(cons1).divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_DOWN)
//							.doubleValue();
					cons2 = BigDecimal.valueOf(cons2).divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_DOWN)
							.doubleValue();
//					cons3 = BigDecimal.valueOf(cons3).divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_DOWN)
//							.doubleValue();
					cons4 = BigDecimal.valueOf(cons4).divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_DOWN)
							.doubleValue();
				}
				switch (periodo) {
				case PERIODO_1:
				case PERIODO_2:
					cons = cons2;
					break;
				case PERIODO_3:
				case PERIODO_4:
					cons = cons4;
					break;

				}

//				consuntivo.setConsuntivo1(
//						new ValutazioneConsuntivoVM(null, _31_03, ModelHelper.toStringDec(cons1 * 100d, 2), date1)
//								.anno(anno).period(1).enabled(false).enabledRipristina(false));
				consuntivo.setConsuntivo2(
						new ValutazioneConsuntivoVM(null, _30_06, ModelHelper.toStringDec(cons2 * 100d, dec[0]), date2)
								.anno(anno).period(2).enabled(false));
//				consuntivo.setConsuntivo3(
//						new ValutazioneConsuntivoVM(null, _30_09, ModelHelper.toStringDec(cons3 * 100d, 2), date3)
//								.anno(anno).period(3).enabled(false).enabledRipristina(false));
				consuntivo.setConsuntivo4(
						new ValutazioneConsuntivoVM(null, _31_12, ModelHelper.toStringDec(cons4 * 100d, dec[0]), date4)
								.anno(anno).period(4).enabled(false));

			}
			if (cons != null)
				consuntivo.setConsuntivo(
						new ValutazioneConsuntivoVM(null, null, ModelHelper.toStringDec(cons * 100d, dec[0]), null)
								.anno(anno).period(4).enabled(false));
			return new Double[] { cons
//					, cons1
					, cons2
//					, cons3
					, cons4 };
		}

		if (consuntivi != null && !consuntivi.isEmpty()) {
			for (Valutazione p : consuntivi) {
				if (p.getAnno().equals(anno - 1)) {
					Double tmp = ConsuntivoHelper.getValore(p, tipoFormula);
					if (tmp != null) {
						cons = tmp;
					}
				}

				if (p.getAnno().equals(anno)) {
					switch (p.getPeriodo()) {
					case 1:
					case 2:
						consuntivo.setConsuntivo2(new ValutazioneConsuntivoVM(p.getId(), _30_06,
								IndicatorePianoHelper.getValToString(p), p.getDataRilevazione()).anno(anno)
								.period(p.getPeriodo())
								.enabled(Periodo.PERIODO_2.equals(periodo) && !automatico 
										&& (ruoliModificaSempre.contains(ruolo)
												|| (referente || collaboratore))));
						cons2 = ConsuntivoHelper.getValore(p, tipoFormula);
						if (cons2 != null)
							cons = cons2;
						break;
					case 3:
					case 4:
						consuntivo.setConsuntivo4(new ValutazioneConsuntivoVM(p.getId(), _31_12,
								IndicatorePianoHelper.getValToString(p), p.getDataRilevazione()).anno(anno)
								.period(p.getPeriodo())
								.enabled(Periodo.PERIODO_4.equals(periodo) && !automatico 
										&& (ruoliModificaSempre.contains(ruolo)
												|| (referente || collaboratore)))
								);
						cons4 = ConsuntivoHelper.getValore(p, tipoFormula);
						if (cons4 != null)
							cons = cons4;
						break;
					}
				}
			}
		}
		if (cons != null)
			consuntivo.setConsuntivo(new ValutazioneConsuntivoVM(null, null, ModelHelper.toStringDec(cons, 2), null)
					.anno(anno).period(4).enabled(false));

		return new Double[] { cons
//				, cons1
				, cons2
//				, cons3
				, cons4 };
	}

	private Double[] consuntivi(final List<Valutazione> consuntivi, final ConsuntivoIndicatoreVM consuntivo,
			final Integer anno, final boolean strategico, final Integer annoInizio, final Integer annoFine,
			final UtenteVM u, final Ruolo ruolo, final ProfiloVM profilo, final Long idResp, final Long idDirezione,
			final boolean referente, final boolean collaboratore,
			final List<Ruolo> ruoliModificaSempre, final boolean automatico, final boolean desc,
			final Periodo periodo, final IndicatorePiano indicatorePiano, final TipoFormula tipoFormula,
			final CalcoloConsuntivazione calcoloConsuntivazione, final Periodicita periodicitaRend) {
		int annoScadenza = indicatorePiano.getNodoPiano().getScadenza().getYear();
		int annoDa = indicatorePiano.getNodoPiano().getInizio().getYear();
		int annoMeta = (annoScadenza - annoDa / 2) + annoDa;
		int periodoMeta = 4;
		int periodoIniziale = ConsuntivoHelper.getPeriodo(indicatorePiano.getNodoPiano().getInizio().getMonthValue(),
				TipoConsuntivazione.SEMESTRE);
		int periodoFinale = ConsuntivoHelper.getPeriodo(indicatorePiano.getNodoPiano().getScadenza().getMonthValue(),
				TipoConsuntivazione.SEMESTRE);

//		Double cons1 = null;
		Double cons2 = null;
//		Double cons3 = null;
		Double cons4 = null;
		Double cons = null;

		if (consuntivi != null && !consuntivi.isEmpty()) {
			for (Valutazione p : consuntivi) {
				if (p.getAnno().equals(anno - 1)) {
					Double tmp = ConsuntivoHelper.getValore(p, tipoFormula);
					if (tmp != null) {
						cons = tmp;
					}
				}
				if (p.getAnno().equals(anno) && ((anno >= annoDa && anno <= annoScadenza))
						|| (anno == annoDa && p.getPeriodo() >= periodoIniziale)
						|| (anno == annoScadenza && p.getPeriodo() <= periodoFinale)) {
					switch (p.getPeriodo()) {
					case 1:
					case 2:
						consuntivo.setConsuntivo2(new ValutazioneConsuntivoVM(p.getId(), _30_06,
								IndicatorePianoHelper.getValToString(p), p.getDataRilevazione()).anno(anno)
								.period(p.getPeriodo())
								.enabled(Periodo.PERIODO_2.equals(periodo) && !automatico 
										&& (!Periodicita.FINALE.equals(periodicitaRend)
												|| (anno == annoMeta && p.getPeriodo() == periodoMeta
														&& Periodicita.FINALE.equals(periodicitaRend)))
										&& (ruoliModificaSempre.contains(ruolo) || ((referente || collaboratore))))
				
						);
						cons2 = ConsuntivoHelper.getValore(p, tipoFormula);
						if (cons2 != null)
							cons = cons2;
						break;
					case 3:
					case 4:
						consuntivo.setConsuntivo4(new ValutazioneConsuntivoVM(p.getId(), _31_12,
								IndicatorePianoHelper.getValToString(p), p.getDataRilevazione()).anno(anno)
								.period(p.getPeriodo())
								.enabled(Periodo.PERIODO_4.equals(periodo) && !automatico 
										&& (!Periodicita.FINALE.equals(periodicitaRend)
												|| (anno == annoMeta && p.getPeriodo() == periodoMeta
														&& Periodicita.FINALE.equals(periodicitaRend)))
										&& (ruoliModificaSempre.contains(ruolo) || (referente || collaboratore)))
								);
						cons4 = ConsuntivoHelper.getValore(p, tipoFormula);
						if (cons4 != null)
							cons = cons4;
						break;
					}
				}
			}
		}
		if (cons != null)
			consuntivo.setConsuntivo(new ValutazioneConsuntivoVM(null, null,
					ModelHelper.toStringDec(cons,
							IndicatorePianoHelper.getDecimali(indicatorePiano, indicatorePiano.getIndicatore())),
					null).anno(anno).period(4).enabled(false));

		return new Double[] { cons
//				, cons1
				, cons2
//				, cons3
				, cons4 };
	}





	@Override
	public void statoAvanzamento(StatoAvanzamentoRequest request) throws BusinessException {
		final NodoPiano nodo = nodoPianoRepository.findById(request.getIdNodoPiano()).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, NODO_PRS_NON_TROVATO + request.getIdNodoPiano());
		});
		Integer anno = nodo.getAnno();
		Long idEnte = nodo.getIdEnte();
		Periodo p = periodo(idEnte, anno);
		Integer period = ConsuntivoHelper.periodo(p);
		if (request.getPeriodo() != null && !period.equals(request.getPeriodo())) {
			throw new BusinessException(HttpStatus.NOT_ACCEPTABLE,
					"Ora non si può consuntivare per l'anno " + anno + " nel " + request.getPeriodo() + "^ periodo");
		}
		nodoPianoRepository.updateStatoAvanzamento(request.getStatoAvanzamento(), period, request.getIdNodoPiano());

	}

	@Override
	@Transactional(readOnly = true)
	public StatoAvanzamentoVM getStatoAvanzamento(Long idNodoPiano) {
		final StatoAvanzamentoVM out = new StatoAvanzamentoVM();
		final NodoPiano nodo = nodoPianoRepository.findById(idNodoPiano).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, NODO_PRS_NON_TROVATO + idNodoPiano);
		});
		Integer anno = nodo.getAnno();
		Long idEnte = nodo.getIdEnte();
		Periodo p = periodo(idEnte, anno);
		Integer period = ConsuntivoHelper.periodo(p);
		out.setIdNodoPiano(idNodoPiano);
		out.setPeriodo(period);
		switch (period) {
		case 1:
		case 2:
			out.setStatoAvanzamento(nodo.getStatoAvanzamentoS1());
			break;
		case 3:
		case 4:
			out.setStatoAvanzamento(nodo.getStatoAvanzamentoS2());
			break;
		}

		out.setDescPeriodo(ConsuntivoHelper.descPeriodoSemestre(period));
		return out;
	}


	private ImmutablePair<Integer, Map<Integer, Valutazione>> getRendicontazione(final NodoPiano nodo,
			final boolean exists) {
		Periodo p = null;
		IndicatorePiano ip = null;
		Integer period = null;
		int year = LocalDate.now().getYear();
		int mese = LocalDate.now().getMonthValue();
		final Map<Integer, Valutazione> map = new HashMap<>();
		List<IndicatorePiano> ips = indicatorePianoRepository.findByNodoPianoId(nodo.getId());
		if (exists) {
			p = periodo(nodo.getIdEnte(), nodo.getAnno());
		} else {
			try {
				p = periodo(nodo.getIdEnte(), nodo.getAnno());
			} catch (Throwable t) {
			}
			if (p == null) {
				if (nodo.getAnno() < year) {
					p = Periodo.PERIODO_4;
				} else {
					if (mese <= 6)
						p = Periodo.PERIODO_2;
					else
						p = Periodo.PERIODO_4;
				}

			}

		}

		period = ConsuntivoHelper.periodo(p);

		ip = ips == null || ips.isEmpty() ? null : ips.get(0);
		if (ip != null) {
			List<Valutazione> items = valutazioneRepository
					.findByIndicatorePianoIdAndAnnoAndTipoValutazioneOrderByPeriodo(ip.getId(), nodo.getAnno(),
							TipoValutazione.CONSUNTIVO);
			if (items != null) {
				items.forEach(c -> map.put(c.getPeriodo(), c));
			}
		}
		return new ImmutablePair<>(period, map);
	}

	private ImmutablePair<Integer, Map<Integer, Valutazione>> getRendicontazione(IndicatorePiano ip, boolean exists) {
		Periodo p = null;
		Integer period = null;
		NodoPiano nodo = ip.getNodoPiano();
		final Map<Integer, Valutazione> map = new HashMap<>();
		if (exists) {
			p = periodo(nodo.getIdEnte(), nodo.getAnno());
		} else {
			try {
				p = periodo(nodo.getIdEnte(), nodo.getAnno());
			} catch (Throwable t) {
			}
		}

		period = ConsuntivoHelper.periodo(p);

		List<Valutazione> items = valutazioneRepository.findByIndicatorePianoIdAndAnnoAndTipoValutazioneOrderByPeriodo(
				ip.getId(), nodo.getAnno(), TipoValutazione.CONSUNTIVO);
		if (items != null) {
			items.forEach(c -> map.put(c.getPeriodo(), c));
		}

		return new ImmutablePair<>(period, map);
	}

	private Periodo periodoAttivo(Long idEnte, Integer anno) {
		CruscottoVM cruscotto = cruscottoBusiness.getCruscotto(idEnte, anno);
		if (cruscotto == null)
			return null;
		LocalDate now = LocalDate.now();
		if (cruscotto.getDataConsuntivazioneS1Da() != null && cruscotto.getDataConsuntivazioneS1A() != null
				&& !cruscotto.getDataConsuntivazioneS1Da().isAfter(now)
				&& !cruscotto.getDataConsuntivazioneS1A().isBefore(now)
				&& Boolean.TRUE.equals(cruscotto.getFlagConsuntivazioneS1())) {
			return Periodo.PERIODO_2;
		}
		if (cruscotto.getDataConsuntivazioneS2Da() != null && cruscotto.getDataConsuntivazioneS2A() != null
				&& !cruscotto.getDataConsuntivazioneS2Da().isAfter(now)
				&& !cruscotto.getDataConsuntivazioneS2A().isBefore(now)
				&& Boolean.TRUE.equals(cruscotto.getFlagConsuntivazioneS2())) {
			return Periodo.PERIODO_4;
		}
		return null;

	}


	private Periodo periodo(final Long idEnte, final Integer anno) throws BusinessException {
		CruscottoVM cruscotto = cruscottoBusiness.getCruscotto(idEnte, anno);
		LocalDate now = LocalDate.now();
		if (cruscotto.getDataConsuntivazioneS1Da() != null && cruscotto.getDataConsuntivazioneS1A() != null
				&& !cruscotto.getDataConsuntivazioneS1Da().isAfter(now)
				&& !cruscotto.getDataConsuntivazioneS1A().isBefore(now)
				&& Boolean.TRUE.equals(cruscotto.getFlagConsuntivazioneS1())) {
			return Periodo.PERIODO_2;
		}
		if (cruscotto.getDataConsuntivazioneS2Da() != null && cruscotto.getDataConsuntivazioneS2A() != null
				&& !cruscotto.getDataConsuntivazioneS2Da().isAfter(now)
				&& !cruscotto.getDataConsuntivazioneS2A().isBefore(now)
				&& Boolean.TRUE.equals(cruscotto.getFlagConsuntivazioneS2())) {
			return Periodo.PERIODO_4;
		}
		throw new BusinessException(HttpStatus.NOT_ACCEPTABLE, "Ora non si può consuntivare per l'anno:" + anno);
	}


	private Valutazione maxConsuntivazione(Long idIndicatorPiano) {
		return valutazioneRepository.findTopByIndicatorePianoIdAndTipoValutazioneOrderByAnnoDescPeriodoDesc(
				idIndicatorPiano, TipoValutazione.CONSUNTIVO);

	}

	private void contiene(final NodoPianoDTO np, final List<NodoPianoDTO> list) {
		if (np == null)
			return;
		list.add(np);
		List<NodoPiano> figli = nodoPianoRepository.findByPadreIdAndDateDeleteIsNullOrderByCodice(np.getId());
		for (NodoPiano f : figli) {
			contiene(MappingNodoPianoHelper.toDto(f), list);
		}
	}

	private boolean nelPeriodo(int anno, ValutazioneDTO v, Periodo tipoPiano, TipoConsuntivazione tipoConsuntivazione) {
		LocalDate scadenza = ConsuntivoHelper.fineAnno(anno);
		int m1 = ConsuntivoHelper.meseInizio(tipoPiano, tipoConsuntivazione);
		int m2 = ConsuntivoHelper.meseFine(tipoPiano, tipoConsuntivazione);
		int mm = -1;
		int aa = -1;
		if (v.getDataRilevazione() != null) {
			mm = ConsuntivoHelper.getMese(v.getDataRilevazione());
			aa = ConsuntivoHelper.getAnno(v.getDataRilevazione());
			return (aa == anno && mm >= m1 && mm <= m2);
		}
		aa = ConsuntivoHelper.getAnno(v.getInizio());
		if (v.getScadenza() == null)
			v.setScadenza(scadenza);
		mm = ConsuntivoHelper.getMese(v.getInizio());
		return (aa == anno && mm >= m1 && mm <= m2);
	}

	private ValutazioneDTO getConsuntivoTrimestre(List<ValutazioneDTO> consuntivi, int anno, Periodo periodo,
			TipoConsuntivazione tipoConsuntivazione) {
		if (consuntivi == null)
			return null;
		for (ValutazioneDTO v : consuntivi) {
			if (nelPeriodo(anno, v, periodo, tipoConsuntivazione))
				return v;
		}
		return null;
	}




	@Override
	@Transactional(readOnly = true)
	public Periodicita[] getPeridicitaFase(final TipoNodo tipoNodo) {
		int tipoNodoIndex = 0;
		for (int i = 0; i < tipiNodo.length; i++) {
			if (tipiNodo[i].equals(tipoNodo)) {
				tipoNodoIndex = i;
				break;
			}
		}

		return new Periodicita[] { periodiTarget[tipoNodoIndex], periodiRend[tipoNodoIndex] };
	}

	private void calcolaRaggioungimento(ConsuntivoVM out) {
		Double r = 0d;
		if (out.getConsuntivoIndicatori() != null)
			r = out.getConsuntivoIndicatori().stream()
					.mapToDouble(t -> t.getRaggiungimentoPesato() == null ? 0f : t.getRaggiungimentoPesato()).sum();
		out.setRaggiungimento((r > 100) ? 100f : r.floatValue());

	}

	@Override
	@Transactional(readOnly = true)
	public boolean enabledConsuntivo(Long idNodo) throws BusinessException {
		final NodoPiano nodo = nodoPianoRepository.findById(idNodo).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, NODO_PRS_NON_TROVATO + idNodo);
		});

		if (!TipoNodo.AZIONE.equals(nodo.getTipoNodo())) {
			return false;
		}
		NodoPiano intervento = null;
		if (TipoNodo.AZIONE.equals(nodo.getTipoNodo())) {
			intervento = nodo;
		} else {
			intervento = nodo.getPadre();
		}
		Periodo periodo = periodoAttivo(nodo.getIdEnte(), nodo.getAnno());

		UtenteVM u = SecurityHelper.utente(utenteBusiness);
		Ruolo ruolo = SecurityHelper.ruolo(u, nodo.getAnno());
		ProfiloVM profilo = SecurityHelper.profilo(u, nodo.getAnno());
		Long idResp = profilo == null ? null : profilo.getIdRisorsa();
		Long idDirezione = profilo == null || profilo.getOrganizzazione() == null ? null
				: profilo.getOrganizzazione().getId();
		final boolean referente = nodo.getOrganizzazione() != null && idDirezione != null
				&& idDirezione.equals(nodo.getOrganizzazione().getId()) && Ruolo.POSIZIONE_ORGANIZZATIVA.equals(ruolo);
		final boolean collaboratore = nodo.getOrganizzazione() != null && idDirezione != null
				&& idDirezione.equals(nodo.getOrganizzazione().getId()) && Ruolo.REFERENTE.equals(ruolo);
		final List<Ruolo> ruoliModificaSempre = List.of(Ruolo.AMMINISTRATORE, Ruolo.SUPPORTO_SISTEMA);

		boolean proponente = SecurityHelper.ALTRI_PROPONENTI.contains(ruolo) || referente
				|| collaboratore;
		

		return periodo != null &&  (ruoliModificaSempre.contains(ruolo) || proponente);

	}
	
	@Override
	public void forzatureNodo(ForzaturaNodoVM request) throws BusinessException {
		final NodoPiano nodo = nodoPianoRepository.findById(request.getIdNodoPiano()).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, NODO_PRS_NON_TROVATO + request.getIdNodoPiano());
		});
		Integer anno = nodo.getAnno();
		Long idEnte = nodo.getIdEnte();
		Periodo p = periodo(idEnte, anno);
		nodoPianoRepository.updatePercentualiForzatura(request.getIdNodoPiano(),request.getForzaturaRisorse(), request.getForzaturaResponsabili());

	}

	@Override
	@Transactional(readOnly = true)
	public ForzaturaNodoVM getForzatureNodo(Long idNodoPiano) {
		final ForzaturaNodoVM out = new ForzaturaNodoVM();
		final NodoPiano nodo = nodoPianoRepository.findById(idNodoPiano).orElseThrow(() -> {
			throw new BusinessException(HttpStatus.NOT_FOUND, NODO_PRS_NON_TROVATO + idNodoPiano);
		});
		out.setForzaturaResponsabili(nodo.getPercentualeRaggiungimentoForzataResp());
		out.setForzaturaRisorse(nodo.getPercentualeRaggiungimentoForzata());
		out.setIdNodoPiano(idNodoPiano);
		return out;
	}

}
