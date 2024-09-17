package com.finconsgroup.performplus.rest.api.v2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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

import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEnumVM;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.rest.api.vm.RisorsaUmanaSmartVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteFlatVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteSmartVM;
import com.finconsgroup.performplus.rest.api.vm.v2.AggiornaUtenteRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.CreaUtenteRequest;
import com.finconsgroup.performplus.service.business.IOrganizzazioneBusiness;
import com.finconsgroup.performplus.service.business.IRisorsaUmanaBusiness;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;

@RestController
@RequestMapping("/api/utente")
@Secured({"AMMINISTRATORE"})
public class UtenteController {

	final IUtenteBusiness utenteBusiness;
	final IOrganizzazioneBusiness organizzazioneBusiness;
	final IRisorsaUmanaBusiness risorsaUmanaBusiness;

	private static final Logger logger = LoggerFactory.getLogger(UtenteController.class);

	protected UtenteController(IUtenteBusiness utenteBusiness, IOrganizzazioneBusiness organizzazioneBusiness,
			IRisorsaUmanaBusiness risorsaUmanaBusiness) {
		this.utenteBusiness = utenteBusiness;
		this.organizzazioneBusiness = organizzazioneBusiness;
		this.risorsaUmanaBusiness = risorsaUmanaBusiness;
	}

	@GetMapping(value = "/search")
	public ResponseEntity<Page<UtenteSmartVM>> search(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(required = true, name = "anno") Integer anno,
			@RequestParam(required = false, name = "nome") String nome, 
			@RequestParam(required = false, name = "ruolo") Ruolo ruolo, 
			@RequestParam(required = false, name = "idDirezione") Long idDirezione, 
			@PageableDefault(sort = {
					"nome" }, direction = Sort.Direction.ASC, page = 0, size = 20) Pageable pageable) {

		Page<UtenteSmartVM> page = utenteBusiness.search(idEnte, anno, ruolo, idDirezione, nome, pageable);
		return ResponseEntity.ok().body(page);
	}

	@GetMapping(value = "/ruoli")
	public ResponseEntity<List<DecodificaEnumVM<Ruolo>>> ruoli(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte) {
		final List<DecodificaEnumVM<Ruolo>> out = new ArrayList<>();
		out.add(new DecodificaEnumVM<>(Ruolo.AMMINISTRATORE, Ruolo.AMMINISTRATORE.label));
		out.add(new DecodificaEnumVM<>(Ruolo.SUPPORTO_SISTEMA, Ruolo.SUPPORTO_SISTEMA.label));
		out.add(new DecodificaEnumVM<>(Ruolo.POSIZIONE_ORGANIZZATIVA, Ruolo.POSIZIONE_ORGANIZZATIVA.label));
		out.add(new DecodificaEnumVM<>(Ruolo.REFERENTE, Ruolo.REFERENTE.label));
		out.add(new DecodificaEnumVM<>(Ruolo.RISORSA, Ruolo.RISORSA.label));
		out.add(new DecodificaEnumVM<>(Ruolo.OIV, Ruolo.OIV.label));
		out.add(new DecodificaEnumVM<>(Ruolo.DIRETTORE_GENERALE, Ruolo.DIRETTORE_GENERALE.label));

		return ResponseEntity.ok(out);

	}

	@GetMapping(value = "/strutture")
	public ResponseEntity<List<DecodificaVM>> strutture(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = false) Integer anno,
			@Valid @RequestParam(name = "testo", required = false) String testo) {
		Integer year = anno == null ? LocalDate.now().getYear() : anno;
		List<OrganizzazioneSmartVM> items = organizzazioneBusiness.search(idEnte, year, testo);
		final List<DecodificaVM> out = new ArrayList<>();
		items.forEach(o -> out.add(new DecodificaVM(o.getId(), o.getCodiceCompleto(), o.getCodiceInterno()+" "+o.getIntestazione())));
		out.sort(new Comparator<DecodificaVM>() {
			@Override
			public int compare(DecodificaVM o1, DecodificaVM o2) {
				return o1.getDescrizione().compareTo(o2.getDescrizione());
			}
		});
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/risorse")
	public ResponseEntity<List<DecodificaVM>> risorse(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = false) Integer anno,
			@Valid @RequestParam(name = "testo", required = false) String testo) {
		Integer year = anno == null ? LocalDate.now().getYear() : anno;
		List<RisorsaUmanaSmartVM> items = risorsaUmanaBusiness.search(idEnte, year, false, testo);
		final List<DecodificaVM> out = new ArrayList<>();
		items.forEach(
				r -> out.add(new DecodificaVM(r.getId(), r.getCodiceInterno(), r.getCognome() + " " + r.getNome())));
		return ResponseEntity.ok(out);
	}

	@PostMapping
	public ResponseEntity<UtenteFlatVM> creaUtente(@Valid @RequestBody(required = true) CreaUtenteRequest request) {
		logger.info("creaUtente ,{}", request);

		return ResponseEntity.ok(utenteBusiness.creaUtente(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> aggiornaUtente(@Valid @PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody(required = true) AggiornaUtenteRequest request) {
		logger.info("aggiornaUtente {},{}",id,request);
		utenteBusiness.aggiornaUtente(id,request);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UtenteFlatVM> leggiUtente(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("leggiUtente {}",id);
		return ResponseEntity.ok(utenteBusiness.leggiUtente(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminaUtente(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("eliminaUtente {}",id);
		utenteBusiness.elimina(id);
		return ResponseEntity.ok(null);
	}

}
