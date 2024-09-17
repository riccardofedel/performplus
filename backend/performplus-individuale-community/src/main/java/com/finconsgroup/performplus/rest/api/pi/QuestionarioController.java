package com.finconsgroup.performplus.rest.api.pi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.manager.security.BaseSecurityHelper;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaNodoPesoRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaNodoQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.CreaNodoQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.CreaQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.DetailNodoQuestionarioVM;
import com.finconsgroup.performplus.rest.api.pi.vm.NodoQuestionarioVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ParametriRiceraQuestionario;
import com.finconsgroup.performplus.rest.api.pi.vm.PreparaNodoQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.QuestionarioVM;
import com.finconsgroup.performplus.rest.api.pi.vm.TipoNodoQuestionario;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.NodoAlberoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Abilitazione;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Enabling;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.pi.IQuestionarioBusiness;
import com.finconsgroup.performplus.service.business.pi.IRegolamentoBusiness;

import org.apache.commons.lang3.StringUtils;
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

@RestController
@RequestMapping("/api/questionario")
@Secured({ "AMMINISTRATORE" })
public class QuestionarioController {

	final IQuestionarioBusiness questionarioBusiness;
	final IRegolamentoBusiness regolamentoBusiness;
	final IUtenteBusiness utenteBusiness;

	private static final Logger logger = LoggerFactory.getLogger(QuestionarioController.class);

	public QuestionarioController(IQuestionarioBusiness questionarioBusiness, IRegolamentoBusiness regolamentoBusiness,
			IUtenteBusiness utenteBusiness) {
		this.questionarioBusiness = questionarioBusiness;
		this.regolamentoBusiness = regolamentoBusiness;
		this.utenteBusiness = utenteBusiness;
	}

	@PostMapping(value = "/search")
	public ResponseEntity<List<NodoQuestionarioVM>> search(@Valid @RequestParam(name = "testo") String testo,
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte) {

		ParametriRiceraQuestionario parametri = new ParametriRiceraQuestionario();
		parametri.setIdEnte(idEnte);
		parametri.setTesto(testo);
		List<NodoQuestionarioVM> items = questionarioBusiness.search(parametri);
		return ResponseEntity.ok().body(items);
	}

	@GetMapping(value = "/nodo/{tipo}/{idNodo}")
	public ResponseEntity<DetailNodoQuestionarioVM> nodo(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @PathVariable(name = "tipo", required = true) TipoNodoQuestionario tipo,
			@Valid @PathVariable(name = "idNodo", required = true) Long idNodo) {
		logger.info("/api/questionario/nodo - start");
		DetailNodoQuestionarioVM nodo = questionarioBusiness.leggiNodo(idEnte, tipo, idNodo);
		return ResponseEntity.ok(nodo);
	}

	@Secured({ "AMMINISTRATORE" })
	@GetMapping(value = "/nodo/prepara")
	public ResponseEntity<PreparaNodoQuestionarioRequest> preparaNodo(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "tipoPadre", required = true) TipoNodoQuestionario tipoPadre,
			@Valid @RequestParam(name = "idNodoPadre", required = false) Long idNodoPadre) {
		logger.info("preparaNodo - start");
		PreparaNodoQuestionarioRequest out = questionarioBusiness.preparaNodo(idEnte,tipoPadre, idNodoPadre);
		return ResponseEntity.ok(out);
	}

	@Secured({ "AMMINISTRATORE" })
	@PostMapping(value = "/nodo")
	public ResponseEntity<NodoQuestionarioVM> creaNodo(
			@Valid @RequestBody(required = true) CreaNodoQuestionarioRequest request) {
		logger.info("creaQuestionario - start");
		NodoQuestionarioVM out = questionarioBusiness.creaNodo(request);
		return ResponseEntity.ok(out);
	}

	@Secured({ "AMMINISTRATORE" })
	@PutMapping(value = "/nodo/{tipoNodo}/{idNodo}")
	public ResponseEntity<Void> aggiornaNodo(
			@Valid @PathVariable(name = "tipoNodo", required = true) TipoNodoQuestionario tipoNodo,
			@Valid @PathVariable(name = "idNodo", required = true) Long idNodo,
			@Valid @RequestBody(required = true) AggiornaNodoQuestionarioRequest request) {
		logger.info("aggiornaNodo - start");
		questionarioBusiness.aggiornaNodo(tipoNodo, idNodo, request);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE" })
	@DeleteMapping(value = "/nodo/{tipoNodo}/{idNodo}")
	public ResponseEntity<Void> eliminaNodo(
			@Valid @PathVariable(name = "tipoNodo", required = true) TipoNodoQuestionario tipoNodo,
			@Valid @PathVariable(name = "idNodo", required = true) Long idNodo) {
		logger.info("eliminaQuestionario - start");
		questionarioBusiness.eliminaNodo(tipoNodo, idNodo);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE" })
	@PutMapping(value = "/nodo/{tipoNodo}/{idNodo}/peso")
	public ResponseEntity<Void> updatePeso(
			@Valid @PathVariable(name = "tipoNodo", required = true) TipoNodoQuestionario tipoNodo,
			@Valid @PathVariable(name = "idNodo", required = true) Long idNodo,
			@Valid @RequestBody(required = true) AggiornaNodoPesoRequest request) {
		logger.info("updatePeso - start");
		questionarioBusiness.updatePeso(tipoNodo, idNodo, request.getPeso());
		return ResponseEntity.ok(null);
	}

	@GetMapping("/categorie")
	public ResponseEntity<List<DecodificaVM>> categorie(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte) {
		logger.info("risorse {}", idEnte);
		return ResponseEntity.ok(regolamentoBusiness.getCategorie(idEnte));
	}

	@GetMapping("/incarichi")
	public ResponseEntity<List<DecodificaVM>> incarichi(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte) {
		logger.info("incarichi {}", idEnte);
		return ResponseEntity.ok(regolamentoBusiness.getIncarichi(idEnte));
	}

	private Integer Livello(NodoQuestionarioVM av) {
		if (av == null)
			return 0;
		if (TipoNodoQuestionario.questionario.equals(av.getTipo()))
			return 1;
		return Livello(av.getCodiceCompleto());
	}

	private Integer Livello(String codiceCompleto) {
		if (StringUtils.isBlank(codiceCompleto))
			return 0;
		String[] a = codiceCompleto.split("\\.");
		if (a == null || a.length == 0)
			return 0;
		return a.length + 1;
	}

	private String nome(final NodoQuestionarioVM o) {
		return o.getIntestazione();
	}

	@GetMapping(value = "/children/{tipoNodo}/{idNodo}")
	public ResponseEntity<List<NodoAlberoVM>> children(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = true) Integer anno,
			@Valid @PathVariable(name = "tipoNodo", required = true) TipoNodoQuestionario tipoNodo,
			@Valid @PathVariable(name = "idNodo", required = true) Long idNodo) {
		logger.info("/api/questionario/children - start");
		List<NodoQuestionarioVM> items = null;
		if (TipoNodoQuestionario.root.equals(tipoNodo)) {
			ParametriRiceraQuestionario params = new ParametriRiceraQuestionario();
			params.setIdEnte(idEnte);
			items = questionarioBusiness.search(params);
		} else {
			items = questionarioBusiness.ramo(tipoNodo, idNodo);
		}
		if (items == null || items.isEmpty()) {
			return ResponseEntity.ok(new ArrayList<>());
		}
		UtenteVM u = BaseSecurityHelper.utente(utenteBusiness);
		final Ruolo ruolo = BaseSecurityHelper.ruolo(u, anno);
		final List<NodoAlberoVM> albero = mappingChildren(items, false, true,ruolo);
		return ResponseEntity.ok(albero);
	}

	@GetMapping(value = "/root")
	public ResponseEntity<NodoAlberoVM> root(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = true) Integer anno,
			@Valid @RequestParam(name = "filter", required = false) String filter) {
		logger.info("/api/questionario/root - start");
		ParametriRiceraQuestionario params = new ParametriRiceraQuestionario();
		params.setIdEnte(idEnte);
		params.setTesto(filter);
		List<NodoQuestionarioVM> items = questionarioBusiness.search(params);
		UtenteVM u = BaseSecurityHelper.utente(utenteBusiness);
		final Ruolo ruolo = BaseSecurityHelper.ruolo(u, anno);
		final List<NodoAlberoVM> nodi = mappingChildren(items, ruolo);
		return ResponseEntity.ok(nodi.get(0));
	}

	private List<NodoAlberoVM> mappingChildren(final List<NodoQuestionarioVM> items, final Ruolo ruolo) {
		return mappingChildren(items, true, true, ruolo);
	}

	private List<NodoAlberoVM> mappingChildren(final List<NodoQuestionarioVM> items, final boolean includi,
			final boolean completo, final Ruolo ruolo) {
		final List<NodoAlberoVM> out = new ArrayList<>();
		List<Abilitazione> abilitazioni = BaseSecurityHelper.enablings(ruolo);
		final Enabling enabling = BaseSecurityHelper.enabling(BaseSecurityHelper.QUESTIONARI, abilitazioni);
		if (items == null || items.isEmpty())
			return out;
		final Integer zero = Livello(items.get(0));
		Integer primo = null;
		Map<Long, NodoAlberoVM> map = new HashMap<>();
		for (NodoQuestionarioVM o : items) {
			if (primo == null && !zero.equals(o.getLivello())) {
				primo = o.getLivello();
			}
			NodoAlberoVM na = new NodoAlberoVM(o.getId(), nome(o), o.getLivello() + 1, false, false);
			na.setTipoNodo(o.getTipo().ordinal());
			na.setFoglia(Boolean.TRUE.equals(o.getFoglia()));
			if ((zero.equals(o.getLivello()) && includi) || (o.getLivello().equals(primo) && !includi)) {
				na.enabling(enabling);
				out.add(na);
			}
			map.put(o.getId(), na);
			NodoAlberoVM p = TipoNodoQuestionario.root.equals(o.getTipo()) ? null
					: map.get(o.getIdPadre() == null ? -1l : o.getIdPadre());
			if (p != null) {
				if (completo) {
					if (p.getChildren() == null)
						p.setChildren(new ArrayList<>());
					na.enabling(enabling);
					p.getChildren().add(na);
				}
				p.setExpandable(true);
			}

		}

		return out;
	}
}
