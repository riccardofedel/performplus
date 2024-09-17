package com.finconsgroup.performplus.rest.api.pi;

import java.util.List;

import jakarta.validation.Valid;

import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaPesiRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaPesoIndicatorePianoRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaPesoNodoPianoRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.PesoIndicatoreRegistrazioneVM;
import com.finconsgroup.performplus.rest.api.pi.vm.PesoNodoPianoRegistrazioneVM;
import com.finconsgroup.performplus.rest.api.vm.RisorsaUmanaSmartVM;
import com.finconsgroup.performplus.service.business.pi.IPesoNodoPianoIndicatoreBusiness;
import com.finconsgroup.performplus.service.business.pi.IRegistrazioneBusiness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/peso-valutato")
public class PesoNodoPianoIndicatoreController {

	final IPesoNodoPianoIndicatoreBusiness pesoNodoPianoIndicatoreBusiness;
	final IRegistrazioneBusiness registrazioneBusiness;

	private static final Logger logger = LoggerFactory.getLogger(PesoNodoPianoIndicatoreController.class);

	public PesoNodoPianoIndicatoreController(IPesoNodoPianoIndicatoreBusiness pesoNodoPianoIndicatoreBusiness,
			IRegistrazioneBusiness registrazioneBusiness) {
		this.pesoNodoPianoIndicatoreBusiness=pesoNodoPianoIndicatoreBusiness;
		this.registrazioneBusiness =registrazioneBusiness;
		
	}


	@GetMapping("/elenco")
	public ResponseEntity<List<PesoNodoPianoRegistrazioneVM>> elenco(
			@Valid @RequestParam(name="idValutatore",required=true) Long idValutatore,
			@Valid @RequestParam(name="idValutato",required=true) Long idValutato,
			@Valid @RequestParam(name="idRegistrazione",required=true) Long idRegistrazione) {
		logger.info("elenco priorita {} {} {}",idValutatore, idValutato, idRegistrazione);
		return ResponseEntity.ok(pesoNodoPianoIndicatoreBusiness.elenco(idValutatore,idValutato,idRegistrazione));
	}
	@GetMapping("/prioritaNodo")
	public ResponseEntity<PesoNodoPianoRegistrazioneVM> prioritaNodo(
			@Valid @RequestParam(name="id",required=false) Long id,
			@Valid @RequestParam(name="idRegistrazione",required=true) Long idRegistrazione,
			@Valid @RequestParam(name="idNodo",required=true) Long idNodo) {
		logger.info("prioritaNodo {},{},{}",id,idRegistrazione,idNodo);
		return ResponseEntity.ok(pesoNodoPianoIndicatoreBusiness.prioritaNodo(id,idRegistrazione,idNodo));
	}
	@GetMapping("/prioritaIndicatore")
	public ResponseEntity<PesoIndicatoreRegistrazioneVM> prioritaIndicatore(
			@Valid @RequestParam(name="id",required=false) Long id,
			@Valid @RequestParam(name="idRegistrazione",required=true) Long idRegistrazione,
			@Valid @RequestParam(name="idIndicatorePiano",required=true) Long idIndicatorePiano) {
		logger.info("prioritaNodo {},{},{}",id,idRegistrazione,idIndicatorePiano);
		return ResponseEntity.ok(pesoNodoPianoIndicatoreBusiness.prioritaIndicatore(id,idRegistrazione,idIndicatorePiano));
	}
	@PostMapping("/aggiornaPesoNodo")
	public ResponseEntity<Void> aggiornaPesoNodo(@Valid @RequestBody(required=true) AggiornaPesoNodoPianoRegistrazioneRequest request) {
		logger.info("aggiornaPesoNodo {}",request);
		pesoNodoPianoIndicatoreBusiness.aggiornaPesoNodo(request);
		return ResponseEntity.ok(null);
	}
	@PostMapping("/aggiornaPesi")
	public ResponseEntity<Void> aggiornaPesi(@Valid @RequestBody(required=true) AggiornaPesiRegistrazioneRequest request) {
		logger.info("aggiornaPesi {}",request);
		pesoNodoPianoIndicatoreBusiness.aggiornaPesi(request);
		return ResponseEntity.ok(null);
	}
	@PostMapping("/aggiornaPesoIndicatore")
	public ResponseEntity<Void> aggiornaPesoIndicatore(@Valid @RequestBody(required=true) AggiornaPesoIndicatorePianoRegistrazioneRequest request) {
		logger.info("aggiornaPesoIndicatore {}",request);
		pesoNodoPianoIndicatoreBusiness.aggiornaPesoIndicatore(request);
		return ResponseEntity.ok(null);
	}
	@GetMapping("/valutatori")
	public ResponseEntity<List<RisorsaUmanaSmartVM>> valutatori(@Valid @RequestParam(name="cognome", required=false) String cognome,
			@Valid @RequestParam(name="idEnte",required=true,defaultValue = "0") Long idEnte
			, @Valid @RequestParam(name="anno",required=true)Integer anno
			, @Valid @RequestParam(name="idValutato",required=false)Long idValutato
			, @Valid @RequestParam(name="idStruttura",required=false)Long idStruttura
			) {
		logger.info("valutatori {},{}, {}",idEnte,anno,cognome);
		return ResponseEntity.ok(registrazioneBusiness.getValutatori(idEnte, anno, cognome, idValutato,idStruttura,false,false));
	}
	@GetMapping("/valutati")
	public ResponseEntity<List<RisorsaUmanaSmartVM>> valutati(@Valid @RequestParam(name="cognome", required=false) String cognome,
			@Valid @RequestParam(name="idEnte",required=true,defaultValue = "0") Long idEnte
			, @Valid @RequestParam(name="anno",required=true)Integer anno
			, @Valid @RequestParam(name="idValutatore",required=false)Long idValutatore
			, @Valid @RequestParam(name="idStruttura",required=false)Long idStruttura) {
		logger.info("valutatori {},{}, {}",idEnte,anno,cognome);
		return ResponseEntity.ok(registrazioneBusiness.getValutati(idEnte, anno, cognome,idValutatore,idStruttura,false,false));
	}
}
