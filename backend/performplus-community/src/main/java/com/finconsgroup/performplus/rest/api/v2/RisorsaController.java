package com.finconsgroup.performplus.rest.api.v2;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.AggiornaRisorsaUmanaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.CreaRisorsaUmanaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaListVM;
import com.finconsgroup.performplus.service.business.IOrganizzazioneBusiness;
import com.finconsgroup.performplus.service.business.IRisorsaUmanaBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/risorsa_umana")
@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
public class RisorsaController {

	final IRisorsaUmanaBusiness risorsaUmanaBusiness;
	final IOrganizzazioneBusiness organizzazioneBusiness;


	private static final Logger logger = LoggerFactory.getLogger(RisorsaController.class);

	protected RisorsaController(IRisorsaUmanaBusiness risorsaUmanaBusiness, IOrganizzazioneBusiness organizzazioneBusiness) {
		this.risorsaUmanaBusiness = risorsaUmanaBusiness;
		this.organizzazioneBusiness = organizzazioneBusiness;
			}

	@GetMapping(value = "/search")
	public ResponseEntity<Page<RisorsaUmanaListVM>> search(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@RequestParam(required = false, name = "anno") Integer anno,
			@Valid @RequestParam(name = "interno", required = true) Boolean interno,
			@Valid @RequestParam(name = "soloAttiveAnno", required = false) Boolean soloAttiveAnno,
			@RequestParam(required = false, name = "cognome") String cognome, @PageableDefault(sort = {
					"cognome" }, direction = Sort.Direction.ASC, page = 0, size = 20) Pageable pageable) {

		Page<RisorsaUmanaListVM> page = risorsaUmanaBusiness.search(idEnte, anno, interno, cognome, soloAttiveAnno==null?true:soloAttiveAnno,pageable);
		return ResponseEntity.ok().body(page);
	}



	@Secured({"AMMINISTRATORE"})
	@PostMapping
	public ResponseEntity<RisorsaUmanaDetailVM> creaRisorsaUmana(@Valid @RequestBody(required = true) CreaRisorsaUmanaRequest request) {
		logger.info("creaRisorsaUmana ,{}", request);

		return ResponseEntity.ok(risorsaUmanaBusiness.creaRisorsaUmana(request));
	}

	@Secured({"AMMINISTRATORE"})
	@PutMapping("/{id}")
	public ResponseEntity<Void> aggiornaRisorsaUmana(@Valid @PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody(required = true) AggiornaRisorsaUmanaRequest request) {
		logger.info("aggiornaRisorsaUmana {},{}",id,request);
		risorsaUmanaBusiness.aggiornaRisorsaUmana(id,request);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RisorsaUmanaDetailVM> leggiRisorsaUmana(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("leggiRisorsaUmana {}",id);
		return ResponseEntity.ok(risorsaUmanaBusiness.leggiRisorsaUmana(id));
	}

	@Secured({"AMMINISTRATORE"})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminaRisorsaUmana(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("eliminaRisorsaUmana {}",id);
		try {
		risorsaUmanaBusiness.elimina(id);
		}catch(Throwable t) {
			throw new BusinessException(HttpStatus.BAD_REQUEST,"risorsa_associata","Risorsa utilizzata: non eliminabile");
		}
		return ResponseEntity.ok(null);
	}
	@GetMapping("/incarichi")
	public ResponseEntity<List<DecodificaVM>> incarichi(@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte) {
		logger.info("incarichi");
		return ResponseEntity.ok(risorsaUmanaBusiness.incarichi(idEnte));
	}
	@GetMapping("/categorie")
	public ResponseEntity<List<DecodificaVM>> categorie(@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte) {
		logger.info("incarichi");
		return ResponseEntity.ok(risorsaUmanaBusiness.categorie(idEnte));
	}
	@GetMapping("/contratti")
	public ResponseEntity<List<DecodificaVM>> contratti(@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte) {
		logger.info("incarichi");
		return ResponseEntity.ok(risorsaUmanaBusiness.contratti(idEnte));
	}
	@GetMapping("/profili")
	public ResponseEntity<List<DecodificaVM>> profili(@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte) {
		logger.info("incarichi");
		return ResponseEntity.ok(risorsaUmanaBusiness.profili(idEnte));
	}
	
}
