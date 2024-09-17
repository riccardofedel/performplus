package com.finconsgroup.performplus.rest.api.v2;

import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finconsgroup.performplus.rest.api.vm.KeyValues;
import com.finconsgroup.performplus.rest.api.vm.v2.AggiornaIntroduzioneRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.CreaIntroduzioneRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.IntroduzioneContenutoResponse;
import com.finconsgroup.performplus.rest.api.vm.v2.IntroduzioneVM;
import com.finconsgroup.performplus.service.business.IContestoBusiness;

@RestController
@RequestMapping("/api/programmazione/introduzione")
@Secured({"AMMINISTRATORE"})
public class ProgrammazioneIntroduzioneController {

	private static final Logger logger = LoggerFactory.getLogger(ProgrammazioneIntroduzioneController.class);

	@Autowired
	private IContestoBusiness contestoBusiness;
	

	@GetMapping(value = "/{idEnte}/{anno}")
	public ResponseEntity<IntroduzioneVM> getIntroduzione(
			@Valid @PathVariable(name = "idEnte", required = true) Long idEnte,
			@Valid @PathVariable(name = "anno", required = true) Integer anno
			) {
		logger.info("/api/programmazione/introduzione get {},{} - start",idEnte,anno);
		IntroduzioneVM out = contestoBusiness.leggiIntroduzione(idEnte,anno);
		return ResponseEntity.ok(out);
	}
	@GetMapping(value = "/{idEnte}/{anno}/contenuto")
	public ResponseEntity<IntroduzioneContenutoResponse> getContenuto(
			@Valid @PathVariable(name = "idEnte", required = true) Long idEnte,
			@Valid @PathVariable(name = "anno", required = true) Integer anno,
			@RequestParam(name="gruppo",required = true) String gruppo,
			@RequestParam(name="nome",required = true) String nome
			) {
		logger.info("/api/programmazione/introduzione get {},{} - start",idEnte,anno);
		IntroduzioneContenutoResponse out = contestoBusiness.leggiIntroduzioneContenuto(idEnte,anno,gruppo,nome);

		return ResponseEntity.ok(out);
	}
	@PostMapping(value = "")
	public ResponseEntity<IntroduzioneVM> creaIntroduzione(
			@Valid @RequestBody(required = true) CreaIntroduzioneRequest request) {
		logger.info("/api/programmazione/introduzione create - start");
		IntroduzioneVM out = contestoBusiness.creaIntroduzione(request);
		return ResponseEntity.ok(out);
	}
	@PutMapping(value = "/{idPiano}")
	public ResponseEntity<Void> aggiornaIntroduzione(
			@Valid @PathVariable(name = "idPiano", required = true) Long idPiano,
			@Valid @RequestBody(required = true) AggiornaIntroduzioneRequest request) {
		logger.info("/api/programmazione/introduzione update - start");
		contestoBusiness.aggiornaIntroduzione(idPiano,request);
		return ResponseEntity.ok(null);
	}
	@GetMapping(value = "/elementi")
	public ResponseEntity<List<KeyValues>> getIntroduzioneElementi() {
		logger.info("/api/programmazione/introduzione/elementi get  - start");
		List<KeyValues> out = contestoBusiness.getIntroduzioneElementi();
		return ResponseEntity.ok(out);
	}

}