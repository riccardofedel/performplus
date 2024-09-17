package com.finconsgroup.performplus.rest.api.pi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finconsgroup.performplus.events.Publisher;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.CreaRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.ParametriRicercaRegistrazione;
import com.finconsgroup.performplus.rest.api.pi.vm.RegistrazioneVM;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEasyVM;
import com.finconsgroup.performplus.rest.api.vm.RisorsaUmanaSmartVM;
import com.finconsgroup.performplus.service.business.pi.IRegistrazioneBusiness;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/registrazione")
@Secured({"AMMINISTRATORE"})
public class RegistrazioneController {

	final IRegistrazioneBusiness registrazioneBusiness;



	private static final Logger logger = LoggerFactory.getLogger(RegistrazioneController.class);

	public RegistrazioneController(IRegistrazioneBusiness registrazioneBusiness,
			Publisher publisher) {
		this.registrazioneBusiness=registrazioneBusiness;
		
	}

	@PostMapping(value = "/search")
	public ResponseEntity<Page<RegistrazioneVM>> search(
			@Valid @RequestBody(required=true) ParametriRicercaRegistrazione filter, @PageableDefault(sort = {
					"cognome" }, direction = Sort.Direction.ASC, page = 0, size = 20) Pageable pageable) {

		Page<RegistrazioneVM> page = registrazioneBusiness.search(filter, pageable);
		return ResponseEntity.ok().body(page);
	}

	@GetMapping("/valutatori")
	public ResponseEntity<List<RisorsaUmanaSmartVM>> valutatori(@Valid @RequestParam(name="cognome", required=false) String cognome,
			@Valid @RequestParam(name="idEnte",required=true,defaultValue = "0") Long idEnte
			, @Valid @RequestParam(name="anno",required=true)Integer anno
			, @Valid @RequestParam(name="idValutato",required=false)Long idValutato
			, @Valid @RequestParam(name="idStruttura",required=false)Long idStruttura) {
		logger.info("valutatori {}, {}, {}, {}",idEnte,anno,cognome,idValutato);
		return ResponseEntity.ok(registrazioneBusiness.getValutatori(idEnte, anno, cognome,idValutato,idStruttura,null,null));
	}
	
	@GetMapping("/risorse")
	public ResponseEntity<List<RisorsaUmanaSmartVM>> risorse(@Valid @RequestParam(name="cognome", required=false) String cognome,
			@Valid @RequestParam(name="idEnte",required=true,defaultValue = "0") Long idEnte
			, @Valid @RequestParam(name="anno",required=true)Integer anno) {
		logger.info("risorse {}, {}, {}",idEnte,anno,cognome);
		return ResponseEntity.ok(registrazioneBusiness.getRisorse(idEnte, anno, cognome));
	}
	@GetMapping("/valutati")
	public ResponseEntity<List<RisorsaUmanaSmartVM>> valutati(@Valid @RequestParam(name="cognome", required=false) String cognome,
			@Valid @RequestParam(name="idEnte",required=true,defaultValue = "0") Long idEnte
			, @Valid @RequestParam(name="anno",required=true)Integer anno
			, @Valid @RequestParam(name="idValutatore",required=false)Long idValutatore
			, @Valid @RequestParam(name="idStruttura",required=false)Long idStruttura) {
		logger.info("valutatori {}, {}, {}, {}",idEnte,anno,cognome,idValutatore);
		return ResponseEntity.ok(registrazioneBusiness.getValutati(idEnte, anno, cognome,idValutatore,idStruttura,null,null));
	}
	
	@PostMapping
	public ResponseEntity<RegistrazioneVM> crea(@Valid @RequestBody(required = true) CreaRegistrazioneRequest request) {
		logger.info("creaRegistrazione ,{}", request);
		RegistrazioneVM out = registrazioneBusiness.crea(request);
		return ResponseEntity.ok(out);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> aggiorna(@Valid @PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody(required = true) AggiornaRegistrazioneRequest request) {
		logger.info("aggiornaRegistrazione {},{}",id,request);
		registrazioneBusiness.aggiorna(id,request);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RegistrazioneVM> leggi(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("leggiRegistrazione {}",id);
		return ResponseEntity.ok(registrazioneBusiness.leggi(id));
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> elimina(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("eliminaRegistrazione {}",id);
		registrazioneBusiness.elimina(id);
		return ResponseEntity.ok(null);
	}
	@PutMapping("/{id}/undo")
	public ResponseEntity<Void> undo(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("undoRegistrazione {}",id);
		registrazioneBusiness.undo(id);
		return ResponseEntity.ok(null);
	}
	@GetMapping("/questionari")
	public ResponseEntity<List<DecodificaEasyVM>> questionari(@Valid @RequestParam(name="idEnte",required=true,defaultValue = "0") Long idEnte
			) {
		logger.info("questionari {},{}",idEnte);
		return ResponseEntity.ok(registrazioneBusiness.getQuestionari(idEnte));
	}
	@GetMapping("/regolamenti")
	public ResponseEntity<List<DecodificaEasyVM>> regolamenti(@Valid @RequestParam(name="idEnte",required=true,defaultValue = "0") Long idEnte
			, @Valid @RequestParam(name="anno",required=true)Integer anno) {
		logger.info("regolamenti {},{}",idEnte,anno);
		return ResponseEntity.ok(registrazioneBusiness.getRegolamenti(idEnte,anno));
	}
	@GetMapping("/strutture")
	public ResponseEntity<List<DecodificaEasyVM>> strutture(@Valid @RequestParam(name="idEnte",required=true,defaultValue = "0") Long idEnte
			, @Valid @RequestParam(name="anno",required=true)Integer anno
			, @RequestParam(name="testo",required=false) String testo) {
		logger.info("strutture {},{},{}",idEnte,anno,testo);
		return ResponseEntity.ok(registrazioneBusiness.getStrutture(idEnte,anno,testo));
	}
	@PutMapping("/{idRegistrazione}/aggiornaScheda")
	public ResponseEntity<Void> aggiornaScheda(@Valid @PathVariable(name = "idRegistrazione", required = true) Long idRegistrazione) {
		logger.info("aggiornaScheda {}",idRegistrazione);
		return ResponseEntity.ok(null);
	}
	@GetMapping("/verificaValutatore")
	public ResponseEntity<Boolean> verificaValutatore(@Valid @RequestParam(name="idRisorsa",required=true) Long idRisorsa) {
		logger.info("verificaValutatore {}",idRisorsa);
		return ResponseEntity.ok(registrazioneBusiness.verificaValutatore(idRisorsa));
	}
}
