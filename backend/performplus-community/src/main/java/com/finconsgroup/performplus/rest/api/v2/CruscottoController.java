package com.finconsgroup.performplus.rest.api.v2;

import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finconsgroup.performplus.rest.api.vm.v2.cruscotto.AggiornaCruscottoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.cruscotto.CruscottoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.cruscotto.StatisticheCruscottoVM;
import com.finconsgroup.performplus.service.business.ICruscottoBusiness;
import com.finconsgroup.performplus.service.business.IPianoBusiness;
import com.finconsgroup.performplus.service.business.utils.Mapping;

@RestController
@RequestMapping("/api/cruscotto")
public class CruscottoController {

	final ICruscottoBusiness cruscottoBusiness;

	final IPianoBusiness pianoBusiness;

	private static final Logger logger = LoggerFactory.getLogger(CruscottoController.class);

	protected CruscottoController(ICruscottoBusiness cruscottoBusiness, IPianoBusiness pianoBusiness) {
		this.cruscottoBusiness = cruscottoBusiness;
		this.pianoBusiness = pianoBusiness;
	}

	@GetMapping(value = "/{idEnte}/anni")
	public ResponseEntity<List<Integer>> anni(
			@Valid @PathVariable(name = "idEnte", required = true) Long idEnte) {
		List<Integer> items = pianoBusiness.getAnni(idEnte);
		return ResponseEntity.ok(items);
	}

	@Secured({"AMMINISTRATORE"})
	@PutMapping("/{idEnte}/{anno}")
	public ResponseEntity<CruscottoVM> aggiornaCruscotto(
			@Valid @PathVariable(name = "idEnte", required = true) Long idEnte,
			@Valid @PathVariable(name = "anno", required = true) Integer anno,
			@Valid @RequestBody(required = true) AggiornaCruscottoRequest request) {
		logger.info("aggiornaCruscotto {}, {}, {}", idEnte, anno, request);
		CruscottoVM tmp = cruscottoBusiness.getCruscotto(idEnte, anno);
		if (tmp == null || tmp.getId()==null) {
			tmp = Mapping.mapping(request, CruscottoVM.class);
			tmp.setIdEnte(idEnte);
			tmp.setAnno(anno);
			tmp = cruscottoBusiness.crea(tmp);
		} else {
			Mapping.mapping(request, tmp);
			tmp = cruscottoBusiness.aggiorna(tmp);
		}
		CruscottoVM out = Mapping.mapping(tmp,CruscottoVM.class);
		addStatistiche(out);
		return ResponseEntity.ok(out);
	}

	@Secured({"AMMINISTRATORE"})
	@GetMapping("/{idEnte}/{anno}")
	public ResponseEntity<CruscottoVM> leggiCruscotto(
			@Valid @PathVariable(name = "idEnte", required = true) Long idEnte,
			@Valid @PathVariable(name = "anno", required = true) Integer anno) {
		logger.info("leggiCruscotto {} {}", idEnte, anno);
		CruscottoVM out = Mapping.mapping(cruscottoBusiness.getCruscotto(idEnte, anno),CruscottoVM.class);
//s		addStatistiche(out);
		return ResponseEntity.ok(out);
	}	

	private void addStatistiche(CruscottoVM out) {
		StatisticheCruscottoVM statistiche=cruscottoBusiness.statistiche(out.getIdEnte(),out.getAnno());
		out.setStatoAvanzamentoPiano(statistiche.getStatoAvanzamentoPiano()==null?null:statistiche.getStatoAvanzamentoPiano().intValue());
		out.setStatoAvanzamentoConsuntivazione(statistiche.getStatoAvanzamentoConsuntivazione()==null?null:statistiche.getStatoAvanzamentoConsuntivazione().intValue());
		
	}

//	@GetMapping("/{id}")
//	public ResponseEntity<CruscottoDTO> leggiCruscotto(@Valid @PathVariable(name = "id", required = true) Long id) {
//		logger.info("leggiCruscotto {}", id);
//		return ResponseEntity.ok(cruscottoBusiness.leggi(id));
//	}

}
