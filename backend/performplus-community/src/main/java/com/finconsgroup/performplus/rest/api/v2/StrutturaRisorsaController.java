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
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa.AssociaRisorsaOrganizzazioneRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa.AssociaRisorseRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa.RisorsaUmanaOrganizzazioneListVM;
import com.finconsgroup.performplus.service.business.IOrganizzazioneBusiness;

@RestController
@RequestMapping("/api/struttura")
@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
public class StrutturaRisorsaController {

	private static final Logger logger = LoggerFactory.getLogger(StrutturaRisorsaController.class);

	@Autowired
	private IOrganizzazioneBusiness organizzazioneBusiness;


	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@DeleteMapping(value = "/rimuoviRisorsa/{idRisorsaUmanaOrganizzazione}")
	public ResponseEntity<Void> rimuoviRisorsaUmana(
			@Valid @PathVariable(name = "idRisorsaUmanaOrganizzazione", required = true) Long idRisorsaUmanaOrganizzazione) {
		organizzazioneBusiness.rimuoviRisorsaUmana(idRisorsaUmanaOrganizzazione);
		return ResponseEntity.ok(null);
	}

	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@PostMapping(value = "/associaRisorse")
	public ResponseEntity<Void> associaRisorse(@Valid @RequestBody(required = true) AssociaRisorseRequest request) {
		organizzazioneBusiness.associaRisorse(request.getId(), request.getSelezionati());
		return ResponseEntity.ok(null);
	}

	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@PostMapping(value = "/modificaDisponibilitaRisorsa")
	public ResponseEntity<Void> modificaDisponibilitaRisorsa(
			@Valid @RequestBody(required = true) ModificaDisponibilitaRisorsaRequest request) {
		organizzazioneBusiness.modificaDisponibilitaRisorsa(request);

		return ResponseEntity.ok(null);
	}

	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@PostMapping(value = "/associaRisorsa")
	public ResponseEntity<Void> associaRisorsa(@Valid @RequestBody(required = true) AssociaRisorsaOrganizzazioneRequest request) {
		organizzazioneBusiness.associaRisorsa(request);
		return ResponseEntity.ok(null);
	}

	@GetMapping(value = "/getRisorseAssociate")
	public ResponseEntity<List<RisorsaUmanaOrganizzazioneListVM>> getRisorseAssociate(
			@Valid @RequestParam(name = "idOrganizzazione", required = true) Long idOrganizzazione) {
		List<RisorsaUmanaOrganizzazioneListVM> out = organizzazioneBusiness.getRisorseAssociateAttuali(idOrganizzazione);
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/getRisorseAssociabili")
	public ResponseEntity<List<RisorsaUmanaListVM>> getRisorseAssociabili(
			@Valid @RequestParam(name = "idOrganizzazione", required = true) Long idOrganizzazione,
			@RequestParam(name = "testo", required = false) String testo) {
		List<RisorsaUmanaListVM> out = organizzazioneBusiness.getRisorseAssociabili(idOrganizzazione, testo);
		return ResponseEntity.ok(out);
	}

}