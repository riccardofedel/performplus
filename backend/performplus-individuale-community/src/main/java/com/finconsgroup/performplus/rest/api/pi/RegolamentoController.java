package com.finconsgroup.performplus.rest.api.pi;

import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaRegolamentoRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.CreaRegolamentoRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.RegolamentoVM;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEasyVM;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.service.business.pi.IRegolamentoBusiness;

@RestController
@RequestMapping("/api/regolamento")
@Secured({"AMMINISTRATORE"})
public class RegolamentoController {

	final IRegolamentoBusiness regolamentoBusiness;

	private static final Logger logger = LoggerFactory.getLogger(RegolamentoController.class);

	public RegolamentoController(IRegolamentoBusiness regolamentoBusiness) {
		this.regolamentoBusiness=regolamentoBusiness;
		
	}


	@GetMapping("/categorie")
	public ResponseEntity<List<DecodificaVM>> categorie(
			@Valid @RequestParam(name="idEnte",required=true,defaultValue = "0") Long idEnte) {
		logger.info("risorse {}",idEnte);
		return ResponseEntity.ok(regolamentoBusiness.getCategorie(idEnte));
	}
	@GetMapping("/incarichi")
	public ResponseEntity<List<DecodificaVM>> incarichi(@Valid @RequestParam(name="idEnte",required=true,defaultValue = "0") Long idEnte) {
		logger.info("incarichi {}",idEnte);
		return ResponseEntity.ok(regolamentoBusiness.getIncarichi(idEnte));
	}
	
	@PostMapping
	public ResponseEntity<RegolamentoVM> crea(@Valid @RequestBody(required = true) CreaRegolamentoRequest request) {
		logger.info("creaRegolamento ,{}", request);

		return ResponseEntity.ok(regolamentoBusiness.crea(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> aggiorna(@Valid @PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody(required = true) AggiornaRegolamentoRequest request) {
		logger.info("aggiornaRegolamento {},{}",id,request);
		regolamentoBusiness.aggiorna(id,request);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RegolamentoVM> leggi(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("leggiRegolamento {}",id);
		return ResponseEntity.ok(regolamentoBusiness.leggi(id));
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> elimina(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("eliminaRegolamento {}",id);
		regolamentoBusiness.elimina(id);
		return ResponseEntity.ok(null);
	}
	@GetMapping("/list")
	public ResponseEntity<List<RegolamentoVM>> list(
			@Valid @RequestParam(name="idEnte",required=true,defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name="anno",required=true) Integer anno) {
		logger.info("listRegolamento {}, {}",idEnte, anno);
		return ResponseEntity.ok(regolamentoBusiness.list(idEnte, anno));
	}

}
