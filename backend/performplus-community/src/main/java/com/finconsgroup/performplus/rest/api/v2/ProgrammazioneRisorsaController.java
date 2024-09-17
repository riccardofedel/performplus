package com.finconsgroup.performplus.rest.api.v2;

import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finconsgroup.performplus.rest.api.vm.v2.ModificaDisponibilitaRisorsaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.risorsa.AssociaRisorsaNodoPianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.risorsa.RisorsaUmanaNodoPianoListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa.AssociaRisorseRequest;
import com.finconsgroup.performplus.service.business.INodoPianoBusiness;

@RestController
@RequestMapping("/api/programmazione")
@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
public class ProgrammazioneRisorsaController {

	private static final Logger logger = LoggerFactory.getLogger(ProgrammazioneRisorsaController.class);

	@Autowired
	private INodoPianoBusiness nodoPianoBusiness;


	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@DeleteMapping(value = "/rimuoviRisorsa/{idRisorsaUmanaNodoPiano}")
	public ResponseEntity<Void> rimuoviRisorsaUmana(
			@Valid @PathVariable(name = "idRisorsaUmanaNodoPiano", required = true) Long idRisorsaUmanaNodoPiano) {
		nodoPianoBusiness.rimuoviRisorsaUmana(idRisorsaUmanaNodoPiano);
		return ResponseEntity.ok(null);
	}
	
	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@PostMapping(value = "/associaRisorse")
	public ResponseEntity<Void> associaRisorse(@Valid @RequestBody(required = true) AssociaRisorseRequest request) {
		nodoPianoBusiness.associaRisorse(request.getId(), request.getSelezionati());
		return ResponseEntity.ok(null);
	}

	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@PostMapping(value = "/modificaDisponibilitaRisorsa")
	public ResponseEntity<Void> modificaDisponibilitaRisorsa(
			@Valid @RequestBody(required = true) ModificaDisponibilitaRisorsaRequest request) {
		nodoPianoBusiness.modificaDisponibilitaRisorsa(request);

		return ResponseEntity.ok(null);
	}

	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@PostMapping(value = "/associaRisorsa")
	public ResponseEntity<Void> associaRisorsa(@Valid @RequestBody(required = true) AssociaRisorsaNodoPianoRequest request) {
		nodoPianoBusiness.associaRisorsa(request);
		return ResponseEntity.ok(null);
	}

	@GetMapping(value = "/getRisorseAssociate")
	public ResponseEntity<List<RisorsaUmanaNodoPianoListVM>> getRisorseAssociate(
			@Valid @RequestParam(name = "idNodoPiano", required = true) Long idNodoPiano) {
		List<RisorsaUmanaNodoPianoListVM> out = nodoPianoBusiness.getRisorseAssociate(idNodoPiano);
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/getRisorseAssociabili")
	public ResponseEntity<List<RisorsaUmanaListVM>> getRisorseAssociabili(
			@Valid @RequestParam(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @RequestParam(name = "esterna", required = false) Boolean esterna,
			@Valid @RequestParam(name = "soloStruttura", required = false, defaultValue = "true") Boolean soloStruttura,
			@RequestParam(name = "testo", required = false) String testo) {
		List<RisorsaUmanaListVM> out = nodoPianoBusiness.getRisorseAssociabili(idNodoPiano, testo,esterna,soloStruttura);
		return ResponseEntity.ok(out);
	}

}