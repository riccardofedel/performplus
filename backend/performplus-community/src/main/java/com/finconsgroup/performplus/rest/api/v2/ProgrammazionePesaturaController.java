package com.finconsgroup.performplus.rest.api.v2;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finconsgroup.performplus.enumeration.Fascia;
import com.finconsgroup.performplus.enumeration.TipologiaObiettiviOperativi;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEnumVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Abilitazione;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.CalcoloPesaturaObiettivoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.CalcoloPesaturaObiettivoResponse;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.PesaturaNodoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.PesaturaRamoVM;
import com.finconsgroup.performplus.service.business.INodoPianoBusiness;
import com.finconsgroup.performplus.service.business.IRisorsaUmanaBusiness;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.utils.RuoloEStrutture;
import com.finconsgroup.performplus.service.business.utils.SecurityHelper;

@RestController
@RequestMapping("/api/programmazione")
@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
public class ProgrammazionePesaturaController {

	private static final Logger logger = LoggerFactory.getLogger(ProgrammazionePesaturaController.class);

	@Autowired
	private INodoPianoBusiness nodoPianoBusiness;
	
	@Autowired
	private IRisorsaUmanaBusiness risorsaUmanaBusiness;
	@Autowired
	private IUtenteBusiness utenteBusiness;

	@GetMapping(value = "/{idNodoPiano}/pesatura/ramo")
	public ResponseEntity<PesaturaRamoVM> getRamo(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano
			) {
		logger.info("/api/programmazione/{}/pesatura/ramo - start",idNodoPiano);
		NodoPianoDetailVM np = nodoPianoBusiness.read(idNodoPiano);
		PesaturaRamoVM out = nodoPianoBusiness.pesaturaRamo(idNodoPiano);
		final RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness, np.getAnno());
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());
		out.setEnabling(SecurityHelper.enabling(SecurityHelper.DUP_PROGRAMMAZIONE, 
				np.getOrganizzazione()==null?null:np.getOrganizzazione().getCodiceCompleto(), rs, enablings));
		return ResponseEntity.ok(out);
	}
	@GetMapping(value = "/{idNodoPiano}/pesatura")
	public ResponseEntity<PesaturaNodoVM> get(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano
			) {
		logger.info("/api/programmazione/{}/pesatura get - start",idNodoPiano);
		PesaturaNodoVM out = nodoPianoBusiness.pesatura(idNodoPiano);
		return ResponseEntity.ok(out);
	}
	@Secured({"AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE","COLLABORATORE_REFERENTE","PROGRAMMAZIONE_STRATEGICA"})
	@PostMapping(value = "/{idNodoPiano}/pesatura")
	public ResponseEntity<Void> save(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @RequestBody(required = true) CalcoloPesaturaObiettivoRequest request){
		logger.info("/api/programmazione/{}/pesatura sav - start",idNodoPiano);
		nodoPianoBusiness.salvaPesatura(idNodoPiano,request);
		return ResponseEntity.ok(null);
	}
	@GetMapping(value = "/pesatura/livelliStrategicita")
	public ResponseEntity<List<DecodificaEnumVM<Fascia>>> getLivelliStrategicita() {
		final List<DecodificaEnumVM<Fascia>> out = new ArrayList<>();
		out.add(new DecodificaEnumVM<>(Fascia.BASSA, "Basso"));
		out.add(new DecodificaEnumVM<>(Fascia.MEDIA, "Medio"));
		out.add(new DecodificaEnumVM<>(Fascia.ALTA, "Alto"));
		return ResponseEntity.ok(out);
	}
	@GetMapping(value = "/pesatura/livelliComplessita")
	public ResponseEntity<List<DecodificaEnumVM<Fascia>>> getLivelliComplessita() {
		final List<DecodificaEnumVM<Fascia>> out = new ArrayList<>();
		out.add(new DecodificaEnumVM<>(Fascia.BASSA, "Bassa"));
		out.add(new DecodificaEnumVM<>(Fascia.MEDIA, "Media"));
		out.add(new DecodificaEnumVM<>(Fascia.ALTA, "Alta"));
		return ResponseEntity.ok(out);
	}
	@GetMapping(value = "/pesatura/tipologia")
	public ResponseEntity<List<DecodificaEnumVM<TipologiaObiettiviOperativi>>> getTipologie() {
		final List<DecodificaEnumVM<TipologiaObiettiviOperativi>> out = new ArrayList<>();
		out.add(new DecodificaEnumVM<>(TipologiaObiettiviOperativi.IMPATTO, TipologiaObiettiviOperativi.IMPATTO.label));
		out.add(new DecodificaEnumVM<>(TipologiaObiettiviOperativi.MANTENIMENTO, TipologiaObiettiviOperativi.MANTENIMENTO.label));
		out.add(new DecodificaEnumVM<>(TipologiaObiettiviOperativi.SVILUPPO, TipologiaObiettiviOperativi.MANTENIMENTO.label));
		return ResponseEntity.ok(out);
	}
	@PostMapping(value = "/{idNodoPiano}/pesatura/calcolo")
	public ResponseEntity<CalcoloPesaturaObiettivoResponse> calcolo(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @RequestBody(required = true) CalcoloPesaturaObiettivoRequest request) {
		final CalcoloPesaturaObiettivoResponse out = nodoPianoBusiness.calcolo(idNodoPiano,request);
		return ResponseEntity.ok(out);
	}
}