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

import com.finconsgroup.performplus.enumeration.CalcoloConsuntivazione;
import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.rest.api.vm.DecodificaCodeVM;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.AggiornaIndicatoreRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.CreaIndicatoreRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.IndicatoreDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.IndicatoreListVM;
import com.finconsgroup.performplus.service.business.IIndicatoreBusiness;

@RestController
@RequestMapping("/api/indicatore")
@Secured({"AMMINISTRATORE", "READ_ALL","REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
public class IndicatoreController {

	final IIndicatoreBusiness indicatoreBusiness;

	private static final Logger logger = LoggerFactory.getLogger(IndicatoreController.class);

	protected IndicatoreController(IIndicatoreBusiness indicatoreBusiness) {
		this.indicatoreBusiness = indicatoreBusiness;
			}

	@GetMapping(value = "/search")
	public ResponseEntity<Page<IndicatoreListVM>> search(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@RequestParam(required = false, name = "formula") TipoFormula formula,
			@RequestParam(required = false, name = "raggruppamento") RaggruppamentoIndicatori raggruppamento,
			@RequestParam(required = false, name = "denominazione") String denominazione, @PageableDefault(sort = {
					"denominazione" }, direction = Sort.Direction.ASC, page = 0, size = 20) Pageable pageable) {

		Page<IndicatoreListVM> page = indicatoreBusiness.searchIndicatore(idEnte, denominazione, formula, raggruppamento,pageable);
		return ResponseEntity.ok().body(page);
	}

	@GetMapping(value = "/formule")
	public ResponseEntity<List<DecodificaCodeVM>> formule() {
		final List<DecodificaCodeVM> out = new ArrayList<>();
		for (TipoFormula t : TipoFormula.values()) {
			out.add(new DecodificaCodeVM(t));
		}

		return ResponseEntity.ok(out);
	}
	
	@GetMapping(value = "/raggruppamenti")
	public ResponseEntity<List<DecodificaCodeVM>> raggruppamenti() {
		final List<DecodificaCodeVM> out = new ArrayList<>();
		for (RaggruppamentoIndicatori t : RaggruppamentoIndicatori.values()) {
			out.add(new DecodificaCodeVM(t));
		}

		return ResponseEntity.ok(out);
	}
	@GetMapping(value = "/calcoliConsuntivazione")
	public ResponseEntity<List<DecodificaCodeVM>> calcoliConsuntivazione() {
		final List<DecodificaCodeVM> out = new ArrayList<>();
		for (CalcoloConsuntivazione t : CalcoloConsuntivazione.values()) {
			out.add(new DecodificaCodeVM(t));
		}
		return ResponseEntity.ok(out);
	}
		
	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@PostMapping
	public ResponseEntity<IndicatoreDetailVM> creaIndicatore(@Valid @RequestBody(required = true) CreaIndicatoreRequest request) {
		logger.info("creaIndicatore ,{}", request);

		return ResponseEntity.ok(indicatoreBusiness.creaIndicatore(request));
	}

	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@PutMapping("/{id}")
	public ResponseEntity<Void> aggiornaIndicatore(@Valid @PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody(required = true) AggiornaIndicatoreRequest request) {
		logger.info("aggiornaIndicatore {},{}",id,request);
		indicatoreBusiness.aggiornaIndicatore(id,request);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<IndicatoreDetailVM> leggiIndicatore(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("leggiIndicatore {}",id);
		return ResponseEntity.ok(indicatoreBusiness.leggiIndicatore(id));
	}

	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminaIndicatore(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("eliminaIndicatore {}",id);
		indicatoreBusiness.elimina(id);
		return ResponseEntity.ok(null);
	}

}
