package com.finconsgroup.performplus.rest.api.v2;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;

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

import com.finconsgroup.performplus.enumeration.TipoFunzione;
import com.finconsgroup.performplus.rest.api.vm.DecodificaCodeVM;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AggiornaAmministratoreRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AmministratoreDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AmministratoreListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.CreaAmministratoreRequest;
import com.finconsgroup.performplus.service.business.IOrganizzazioneBusiness;
import com.finconsgroup.performplus.service.business.IRisorsaUmanaBusiness;

@RestController
@RequestMapping("/api/amministratore")
@Secured({"AMMINISTRATORE"})
public class AmministratoreController {

	final IRisorsaUmanaBusiness risorsaUmanaBusiness;
	final IOrganizzazioneBusiness organizzazioneBusiness;

	private static final Logger logger = LoggerFactory.getLogger(AmministratoreController.class);

	protected AmministratoreController(IRisorsaUmanaBusiness risorsaUmanaBusiness, IOrganizzazioneBusiness organizzazioneBusiness) {
		this.risorsaUmanaBusiness = risorsaUmanaBusiness;
		this.organizzazioneBusiness = organizzazioneBusiness;
			}

	@GetMapping(value = "/search")
	public ResponseEntity<Page<AmministratoreListVM>> search(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@RequestParam(required = false, name = "anno") Integer anno,
			@RequestParam(required = false, name = "cognome") String cognome, @PageableDefault(sort = {
					"cognome" }, direction = Sort.Direction.ASC, page = 0, size = 20) Pageable pageable) {

		Page<AmministratoreListVM> page = risorsaUmanaBusiness.searchAmministratore(idEnte, anno, cognome, pageable);
		return ResponseEntity.ok().body(page);
	}

	@GetMapping(value = "/funzioni")
	public ResponseEntity<List<DecodificaCodeVM>> funzioni() {
		final List<DecodificaCodeVM> out = new ArrayList<>();
		for (TipoFunzione t : TipoFunzione.values()) {
			out.add(new DecodificaCodeVM(t));
		}

		return ResponseEntity.ok(out);
	}
	

	@PostMapping
	public ResponseEntity<AmministratoreDetailVM> creaAmministratore(@Valid @RequestBody(required = true) CreaAmministratoreRequest request) {
		logger.info("creaAmministratore ,{}", request);

		return ResponseEntity.ok(risorsaUmanaBusiness.creaAmministratore(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> aggiornaAmministratore(@Valid @PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody(required = true) AggiornaAmministratoreRequest request) {
		logger.info("aggiornaAmministratore {},{}",id,request);
		risorsaUmanaBusiness.aggiornaAmministratore(id,request);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AmministratoreDetailVM> leggiAmministratore(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("leggiAmministratore {}",id);
		return ResponseEntity.ok(risorsaUmanaBusiness.leggiAmministratore(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminaAmministratore(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("eliminaAmministratore {}",id);
		risorsaUmanaBusiness.elimina(id);
		return ResponseEntity.ok(null);
	}

}
