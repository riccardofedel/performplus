package com.finconsgroup.performplus.rest.api.v2;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;

import com.finconsgroup.performplus.enumeration.AltraProgrammazione;
import com.finconsgroup.performplus.enumeration.Dimensione;
import com.finconsgroup.performplus.enumeration.TipoIndicatore;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEnumVM;
import com.finconsgroup.performplus.rest.api.vm.v2.IndicatoreTemplateVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.AggiornaIndicatorePianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.CreaIndicatorePianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatorePianoDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatorePianoListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatoreRangeVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatoreTargetVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.SaveIndicatoreRangeRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.SaveIndicatoreTargetRequest;
import com.finconsgroup.performplus.service.business.IIndicatoreBusiness;
import com.finconsgroup.performplus.service.business.IIndicatorePianoBusiness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/programmazione")
@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
public class ProgrammazioneIndicatoreController {

	private static final Logger logger = LoggerFactory.getLogger(ProgrammazioneIndicatoreController.class);

	@Autowired
	private IIndicatorePianoBusiness indicatorePianoBusiness;

	@Autowired
	private IIndicatoreBusiness indicatoreBusiness;

	@GetMapping(value = "/{idNodoPiano}/indicatore")
	public ResponseEntity<List<IndicatorePianoListVM>> get(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano) {
		logger.info("/api/programmazione/{}/indicatore list - start", idNodoPiano);
		List<IndicatorePianoListVM> out = indicatorePianoBusiness.indicatori(idNodoPiano);
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/{idNodoPiano}/indicatore/{id}")
	public ResponseEntity<IndicatorePianoDetailVM> get(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("/api/programmazione/{}/indicatore/{} get - start", idNodoPiano, id);
		IndicatorePianoDetailVM out = indicatorePianoBusiness.read(id);
		return ResponseEntity.ok(out);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@PostMapping(value = "/{idNodoPiano}/indicatore")
	public ResponseEntity<Long> crea(@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @RequestBody(required = true) CreaIndicatorePianoRequest request) {
		logger.info("/api/programmazione/{}/indicatore create - start", idNodoPiano);
		Long idIndicatorePiano = indicatorePianoBusiness.create(idNodoPiano, request);
		return ResponseEntity.ok(idIndicatorePiano);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@PutMapping(value = "/{idNodoPiano}/indicatore/{id}")
	public ResponseEntity<Void> update(@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody(required = true) AggiornaIndicatorePianoRequest request) {
		logger.info("/api/programmazione/{}/indicatore/{} update - start", idNodoPiano, id);
		indicatorePianoBusiness.update(id, request);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@DeleteMapping(value = "/{idNodoPiano}/indicatore/{id}")
	public ResponseEntity<Void> delete(@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("/api/programmazione/{}/indicatore/{} delete - start", idNodoPiano, id);
		indicatorePianoBusiness.elimina(id);
		return ResponseEntity.ok(null);
	}

	@GetMapping(value = "/{idNodoPiano}/indicatore/{id}/target")
	public ResponseEntity<IndicatoreTargetVM> readTarget(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("/api/programmazione/{}/indicatore/{}/target read - start", idNodoPiano, id);
		IndicatoreTargetVM out = indicatorePianoBusiness.readTarget(id);
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/{idNodoPiano}/indicatore/{id}/range")
	public ResponseEntity<List<IndicatoreRangeVM>> readRange(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("/api/programmazione/{}/indicatore/{}/range read - start", idNodoPiano, id);
		List<IndicatoreRangeVM> out = indicatorePianoBusiness.readRange(id);
		return ResponseEntity.ok(out);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@PostMapping(value = "/{idNodoPiano}/indicatore/{id}/target")
	public ResponseEntity<Void> saveTarget(@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody(required = true) SaveIndicatoreTargetRequest request) {
		logger.info("/api/programmazione/{}/indicatore/{}/target save - start", idNodoPiano, id);
		indicatorePianoBusiness.saveTarget(id, request);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@PostMapping(value = "/{idNodoPiano}/indicatore/{id}/range")
	public ResponseEntity<Void> saverRange(@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody(required = true) SaveIndicatoreRangeRequest request) {
		logger.info("/api/programmazione/{}/indicatore/{}/range save - start", idNodoPiano, id);
		indicatorePianoBusiness.saveRange(id, request);
		return ResponseEntity.ok(null);
	}

	@GetMapping(value = "/indicatore/templates")
	public ResponseEntity<List<IndicatoreTemplateVM>> templates(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte) {
		logger.info("/api/programmazione/indicatore/templates({}) read - start", idEnte);
		List<IndicatoreTemplateVM> out = indicatoreBusiness.templates(idEnte);
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/indicatore/dimensioni")
	public ResponseEntity<List<DecodificaEnumVM<Dimensione>>> dimensioni() {
		final List<DecodificaEnumVM<Dimensione>> out = new ArrayList<>();
		for (Dimensione t : Dimensione.values()) {
			out.add(new DecodificaEnumVM<Dimensione>(t));
		}
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/indicatore/altreProgrammazioni")
	public ResponseEntity<List<DecodificaEnumVM<AltraProgrammazione>>> altreProgrammazioni() {
		final List<DecodificaEnumVM<AltraProgrammazione>> out = new ArrayList<>();
		for (AltraProgrammazione t : AltraProgrammazione.values()) {
			out.add(new DecodificaEnumVM<AltraProgrammazione>(t));
		}
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/indicatore/tipiIndicatore")
	public ResponseEntity<List<DecodificaEnumVM<TipoIndicatore>>> tipiIndicatore(
			@RequestParam(name = "tipoNodo", required = false) TipoNodo tipoNodo) {
		final List<DecodificaEnumVM<TipoIndicatore>> out = new ArrayList<>();
		for (TipoIndicatore t : TipoIndicatore.values()) {
			if (TipoNodo.AREA.equals(tipoNodo) || TipoIndicatore.OUTPUT.equals(t)){
				out.add(new DecodificaEnumVM<TipoIndicatore>(t));
			}
		}
		return ResponseEntity.ok(out);
	}
}