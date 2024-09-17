package com.finconsgroup.performplus.rest.api.pi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finconsgroup.performplus.rest.api.pi.vm.AccettazioneSchedaRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.AccettazioneValutazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.NoteValutazioneVM;
import com.finconsgroup.performplus.rest.api.pi.vm.PerformanceOrganizzativaRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.PubblicazioneSchedaRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.PubblicazioneValutazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.SchedaVM;
import com.finconsgroup.performplus.rest.api.pi.vm.SchedaValutatoVM;
import com.finconsgroup.performplus.rest.api.pi.vm.TotaliVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneObiettiviVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneObiettivoVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneQuestionarioVM;
import com.finconsgroup.performplus.rest.api.vm.RisorsaUmanaSmartVM;
import com.finconsgroup.performplus.service.business.pi.IRegistrazioneBusiness;
import com.finconsgroup.performplus.service.business.pi.ISchedaBusiness;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/scheda-valutato")
public class SchedaController {

	final ISchedaBusiness schedaBusiness;
	final IRegistrazioneBusiness registrazioneBusiness;

	private static final Logger logger = LoggerFactory.getLogger(SchedaController.class);

	public SchedaController(ISchedaBusiness schedaBusiness, IRegistrazioneBusiness registrazioneBusiness) {
		this.schedaBusiness = schedaBusiness;
		this.registrazioneBusiness = registrazioneBusiness;

	}

	@GetMapping("/elenco")
	public ResponseEntity<List<SchedaVM>> elenco(
			@Valid @RequestParam(name = "idValutatore", required = true) Long idValutatore,
			@Valid @RequestParam(name = "idValutato", required = true) Long idValutato) {
		logger.info("elenco schede {} {}", idValutatore, idValutato);
		return ResponseEntity.ok(schedaBusiness.elenco(idValutatore, idValutato));
	}
	@GetMapping("/elencoAll")
	public ResponseEntity<List<SchedaVM>> elencoAll(
			@Valid @RequestParam(name = "idValutatore", required = false) Long idValutatore,
			@Valid @RequestParam(name = "idValutato", required = true) Long idValutato) {
		logger.info("elenco all {}", idValutato);
		return ResponseEntity.ok(schedaBusiness.elencoAll(idValutato,idValutatore));
	}

	@GetMapping("/leggi")
	public ResponseEntity<SchedaVM> leggi(
			@Valid @RequestParam(name = "idRegistrazione", required = true) Long idRegistrazione) {
		logger.info("leggi scheda {}", idRegistrazione);
		return ResponseEntity.ok(schedaBusiness.leggi(idRegistrazione));
	}

	@GetMapping("/obiettivi")
	public ResponseEntity<List<ValutazioneObiettivoVM>> getObiettivi(
			@Valid @RequestParam(name = "idRegistrazione", required = true) Long idRegistrazione) {
		logger.info("getObiettivi {}", idRegistrazione);
		return ResponseEntity.ok(schedaBusiness.getObiettivi(idRegistrazione));
	}

	@PostMapping("/aggiornaQuestionario")
	public ResponseEntity<Void> aggiornaQuestionario(
			@Valid @RequestBody(required = true) ValutazioneQuestionarioRequest request) {
		logger.info("aggiornaQuestionario {}", request);
		schedaBusiness.aggiornaQuestionario(request);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/questionario")
	public ResponseEntity<ValutazioneQuestionarioVM> getQuestionario(
			@Valid @RequestParam(name = "idRegistrazione", required = true) Long idRegistrazione) {
		logger.info("getQuestionario {}", idRegistrazione);
		ValutazioneQuestionarioVM out = schedaBusiness.getQuestionario(idRegistrazione);
		return ResponseEntity.ok(out);
	}

	@GetMapping("/risultatoQuestionario")
	public ResponseEntity<ValutazioneQuestionarioVM> risultatoQuestionario(
			@Valid @RequestParam(name = "idRegistrazione", required = true) Long idRegistrazione) {
		logger.info("risultatoQuestionario {}", idRegistrazione);
		ValutazioneQuestionarioVM out = schedaBusiness.risultatoQuestionario(idRegistrazione);
		return ResponseEntity.ok(out);
	}

	@PostMapping("/forzaValutazione")
	public ResponseEntity<Void> forzaValutazione(@Valid @RequestBody(required = true) ValutazioneObiettiviVM request) {
		logger.info("forzaValutazione {}", request);
		schedaBusiness.forzaValutazione(request);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/valutatori")
	public ResponseEntity<List<RisorsaUmanaSmartVM>> valutatori(
			@Valid @RequestParam(name = "cognome", required = false) String cognome,
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = true) Integer anno,
			@Valid @RequestParam(name = "idValutato", required = false) Long idValutato,
			@Valid @RequestParam(name = "idValutatore", required = false) Long idValutatore,
			@Valid @RequestParam(name = "idStruttura", required = false) Long idStruttura) {
		logger.info("valutatori {},{}, {}", idEnte, anno, cognome);
		return ResponseEntity.ok(registrazioneBusiness.getValutatori(idEnte, anno, cognome, idValutato,idStruttura,false,false));
	}
	@GetMapping("/valutatoriAll")
	public ResponseEntity<List<RisorsaUmanaSmartVM>> valutatoriAll(
			@Valid @RequestParam(name = "idValutato", required = false) Long idValutato) {
		logger.info("valutatori all {}", idValutato);
		return ResponseEntity.ok(registrazioneBusiness.getValutatoriAll(idValutato));
	}
	@GetMapping("/valutati")
	public ResponseEntity<List<RisorsaUmanaSmartVM>> valutati(
			@Valid @RequestParam(name = "cognome", required = false) String cognome,
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = true) Integer anno,
			@Valid @RequestParam(name = "idValutatore", required = false) Long idValutatore,
			@Valid @RequestParam(name = "idStruttura", required = false) Long idStruttura) {
		logger.info("valutatori {},{}, {}", idEnte, anno, cognome);
		
		return ResponseEntity.ok(registrazioneBusiness.getValutati(idEnte, anno, cognome, idValutatore,idStruttura,false,false));
	}

	@GetMapping("/risorsa")
	public ResponseEntity<RisorsaUmanaSmartVM> risorsa(
			@Valid @RequestParam(name = "idRisorsa", required = true) Long idRisorsa) {
		logger.info("risoresa {}", idRisorsa);
		return ResponseEntity.ok(registrazioneBusiness.getRisorsa(idRisorsa));
	}

	@GetMapping("/totali")
	public ResponseEntity<TotaliVM> totali(
			@Valid @RequestParam(name = "idRegistrazione", required = true) Long idRegistrazione) {
		logger.info("totali {}", idRegistrazione);
		return ResponseEntity.ok(schedaBusiness.totali(idRegistrazione));
	}

	@GetMapping("/schedaValutato")
	public ResponseEntity<SchedaValutatoVM> schedaValutato(
			@Valid @RequestParam(name = "idValutato", required = true) Long idValutato) {
		logger.info("schedaValutato {}", idValutato);
		return ResponseEntity.ok(schedaBusiness.schedaValutatoByValutato(idValutato));
	}

	@GetMapping("/statoScheda")
	public ResponseEntity<NoteValutazioneVM> statoScheda(
			@Valid @RequestParam(name = "idRegistrazione", required = true) Long idRegistrazione) {
		logger.info("statoScheda {}", idRegistrazione);
		return ResponseEntity.ok(schedaBusiness.getNote(idRegistrazione));
	}

	@PutMapping("/pubblicazioneScheda")
	public ResponseEntity<Void> pubblicazionScheda(
			@Valid @RequestBody(required = true) PubblicazioneSchedaRequest request) {
		logger.info("pubblicazionScheda {}", request);
		schedaBusiness.pubblicazioneScheda(request.getIdRegistrazione(), request.getNote());
		return ResponseEntity.ok(null);
	}

	@PutMapping("/pubblicazioneValutazione")
	public ResponseEntity<Void> pubblicazioneValutazione(
			@Valid @RequestBody(required = true) PubblicazioneValutazioneRequest request) {
		logger.info("pubblicazioneValutazione {}", request);
		schedaBusiness.pubblicazioneValutazione(request.getIdRegistrazione(), request.getNote());
		return ResponseEntity.ok(null);
	}

	@PutMapping("/accettaScheda")
	public ResponseEntity<Void> accettaScheda(@Valid @RequestBody(required = true) AccettazioneSchedaRequest request) {
		logger.info("accettaScheda {}", request);
		schedaBusiness.accettazioneScheda(request.getIdRegistrazione(), request.getFlagAccettazione(),
				request.getNote());
		return ResponseEntity.ok(null);
	}

	@PutMapping("/accettaValutazione")
	public ResponseEntity<Void> accettaValutazione(
			@Valid @RequestBody(required = true) AccettazioneValutazioneRequest request) {
		logger.info("accettaValutazione {}", request);
		schedaBusiness.accettazioneValutazione(request.getIdRegistrazione(), request.getFlagAccettazione(),
				request.getNote());
		return ResponseEntity.ok(null);
	}
	@PutMapping("/performanceOrganizzativa")
	public ResponseEntity<Void> performanceOrganizzativa(
			@Valid @RequestBody(required = true) PerformanceOrganizzativaRequest request) {
		logger.info("accettaValutazione {}", request);
		schedaBusiness.performanceOrganizzativa(request.getIdRegistrazione(),request.getIdScheda(), request.getAttiva());
		return ResponseEntity.ok(null);
	}
}
