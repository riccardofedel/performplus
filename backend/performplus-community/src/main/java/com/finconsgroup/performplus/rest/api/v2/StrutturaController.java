package com.finconsgroup.performplus.rest.api.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.enumeration.TipoStruttura;
import com.finconsgroup.performplus.enumeration.TipologiaStruttura;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEnumVM;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.rest.api.vm.v2.DipendenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.NodoAlberoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Abilitazione;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.AggiornaStrutturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.CreaStrutturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.OrganizzazioneDetailVM;
import com.finconsgroup.performplus.service.business.IOrganizzazioneBusiness;
import com.finconsgroup.performplus.service.business.IRisorsaUmanaBusiness;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.OrganizzazioneHelper;
import com.finconsgroup.performplus.service.business.utils.RuoloEStrutture;
import com.finconsgroup.performplus.service.business.utils.SecurityHelper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/struttura")
@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
public class StrutturaController {

	private static final Logger logger = LoggerFactory.getLogger(StrutturaController.class);

	@Autowired
	private IOrganizzazioneBusiness organizzazioneBusiness;
	@Autowired
	private IUtenteBusiness utenteBusiness;
	@Autowired
	private IRisorsaUmanaBusiness risorsaUmanaBusiness;

	@GetMapping(value = "/{id}")
	public ResponseEntity<OrganizzazioneDetailVM> getStruttura(
			@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("/api/struttura/get - start");
		OrganizzazioneDetailVM out = organizzazioneBusiness.read(id);
		final RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness, out.getAnno());
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());
		out.setEnabling(
				SecurityHelper.enabling(SecurityHelper.STRUTTURA_ORGANIGRAMMA, out.getCodiceCompleto(), rs, enablings));

//		out.setDipendenti(detail(out.getId()));
//		out.setTotaleDisponibili(0);
//		out.setTotalePartTime(0);
//		out.setTotaleRisorse(0);
//		out.setTotaleRisorseEffettive(0);
//		for (DipendenteVM d : out.getDipendenti()) {
//			if (Disponibile.DISPONIBILE.equals(d.getDisponibile())) {
//				out.setTotaleDisponibili(out.getTotaleDisponibili() + 1);
//			}
//			if (Boolean.TRUE.equals(d.getPartTime())) {
//				out.setTotalePartTime(out.getTotalePartTime() + 1);
//			}
//			out.setTotaleRisorse(out.getTotaleRisorse() + 1);
//			out.setTotaleRisorseEffettive(out.getTotaleRisorseEffettive() + d.getContributoEffettivo());
//
//		}

		return ResponseEntity.ok(out);
	}

	@Secured({ "AMMINISTRATORE" })
	@PostMapping(value = "")
	public ResponseEntity<Void> creaStruttura(@Valid @RequestBody(required = true) CreaStrutturaRequest request) {
		logger.info("/api/struttura/crea - start");
		organizzazioneBusiness.crea(request);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE" })
	@PutMapping(value = "")
	public ResponseEntity<Void> aggiornaStruttura(
			@Valid @RequestBody(required = true) AggiornaStrutturaRequest request) {
		logger.info("/api/struttura/aggiorna - start");
		organizzazioneBusiness.aggiorna(request);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE" })
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> eliminaStruttura(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("/api/struttura/elimina - start");
		organizzazioneBusiness.elimina(id);
		return ResponseEntity.ok(null);
	}

	@GetMapping(value = "/children/{idOrganizzazione}")
	public ResponseEntity<List<NodoAlberoVM>> children(
			@Valid @PathVariable(name = "idOrganizzazione", required = true) Long idOrganizzazione) {
		logger.info("/api/organizzazione/children - start");
		List<OrganizzazioneSmartVM> items = organizzazioneBusiness.ramo(idOrganizzazione);

		if (items == null || items.isEmpty()) {
			return ResponseEntity.ok(new ArrayList<>());
		}
		final RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness,
				items.get(0).getAnno());
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());
		if (!SecurityHelper.ADMINS.contains(rs.getRuolo())) {
			final OrganizzazioneSmartVM sp = rs.getStrutturaProf();
			final OrganizzazioneSmartVM sa = rs.getStrutturaProfAggiunta();
			final List<OrganizzazioneSmartVM> sr = rs.getStruttureResp();
			if (sp == null) {
				throw new BusinessException(HttpStatus.NOT_ACCEPTABLE, "Utente senza struttura nel profilo");
			}
			if (!sp.getLivello().equals(Livello.ENTE)) {
				final List<Long> filtroStrutture = filtroRami(sp, sa, sr);
				items.removeIf(r -> r.getId() == null || !filtroStrutture.contains(r.getId()));
			}
		}
		final List<NodoAlberoVM> albero = mappingChildren(items, rs, enablings);
		return ResponseEntity.ok(albero);
	}

	@GetMapping(value = "/root")
	public ResponseEntity<NodoAlberoVM> root(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = false) Integer anno,
			@Valid @RequestParam(name = "filter", required = false) String filter,
			@Valid @RequestParam(name = "responsabile", required = false) String responsabile) {
		logger.info("/api/organizzazione/root - start");
		OrganizzazioneSmartVM root = organizzazioneBusiness.getRootOnly(idEnte, anno);
		final RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness, anno);
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());
		List<OrganizzazioneSmartVM> items = organizzazioneBusiness.ramo(root.getId());

		filtra(items, filter, responsabile);

		if (!SecurityHelper.ADMINS.contains(rs.getRuolo())) {
			final OrganizzazioneSmartVM sp = rs.getStrutturaProf();
			final OrganizzazioneSmartVM sa = rs.getStrutturaProfAggiunta();
			final List<OrganizzazioneSmartVM> sr = rs.getStruttureResp();
			if (sp == null) {
				throw new BusinessException(HttpStatus.NOT_ACCEPTABLE, "Utente senza struttura nel profilo");
			}
			if (!sp.getLivello().equals(Livello.ENTE)) {
				final List<Long> filtroStrutture = filtroRami(sp, sa, sr);
				items.removeIf(r -> r.getId() == null || !filtroStrutture.contains(r.getId()));
			}
		}
		NodoAlberoVM node = new NodoAlberoVM(root.getId(), root.getIntestazione(), root.getLivello().ordinal() + 1,
				items != null && items.size() > 1, true);
		node.setTipoNodo(root.getLivello().ordinal());
		final List<NodoAlberoVM> children = mappingChildren(items, false, true, rs, enablings);
		node.setChildren(children);
		return ResponseEntity.ok(node);
	}

	@GetMapping(value = "/isCodiceDuplicato")
	public ResponseEntity<Boolean> isCodiceDuplicato(
			@Valid @RequestParam(name = "idPadre", required = true) Long idPadre,
			@Valid @RequestParam(name = "codice", required = true) String codice) {
		Boolean out = organizzazioneBusiness.isCodiceDuplicato(idPadre, codice);
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/prepareNuovaStruttura")
	public ResponseEntity<CreaStrutturaRequest> prepareNuovaStruttura(
			@Valid @RequestParam(name = "idPadre", required = true) Long idPadre) {
		CreaStrutturaRequest out = organizzazioneBusiness.prepareDescendant(idPadre);
		return ResponseEntity.ok(out);
	}

	private void filtra(List<OrganizzazioneSmartVM> items, String testo, String responsabile) {
		if (StringUtils.isBlank(testo) && StringUtils.isBlank(responsabile))
			return;
		final List<Long> eliminati = new ArrayList<>();
		final List<Long> rimanenti = new ArrayList<>();
		for (int i = Livello.values().length - 1; i >= 0; i--) {
			Livello l = Livello.values()[i];
			items.forEach(o -> {
				if (l.equals(o.getLivello())) {
					if (!rimanenti.contains(o.getId()) && ((StringUtils.isNotBlank(testo)
							&& !o.getIntestazione().toLowerCase().contains(testo.toLowerCase()))
							|| ((StringUtils.isNotBlank(responsabile)
									&& !o.getNomeResponsabile().toLowerCase().toLowerCase().contains(responsabile))))) {
						if (!eliminati.contains(o.getId()))
							eliminati.add(o.getId());
					} else {
						if (!rimanenti.contains(o.getIdPadre()))
							rimanenti.add(o.getIdPadre());
					}
				}
			});
		}
		items.removeIf(o -> eliminati.contains(o.getId()));
	}

	private List<NodoAlberoVM> mappingChildren(final List<OrganizzazioneSmartVM> items, final RuoloEStrutture rs,
			final List<Abilitazione> enablings) {
		return mappingChildren(items, false, true, rs, enablings);
	}

	private List<NodoAlberoVM> mappingChildren(final List<OrganizzazioneSmartVM> items, final boolean includi,
			final boolean completo, final RuoloEStrutture rs, List<Abilitazione> enablings) {
		final List<NodoAlberoVM> out = new ArrayList<>();
		if (items == null || items.isEmpty())
			return out;

		final Livello zero = items.get(0).getLivello();
		Livello primo = null;
		Map<Long, NodoAlberoVM> map = new HashMap<>();
		for (OrganizzazioneSmartVM o : items) {
			if (primo == null && !zero.equals(o.getLivello())) {
				primo = o.getLivello();
			}
			NodoAlberoVM na = new NodoAlberoVM(o.getId(), nome(o), o.getLivello().ordinal() + 1, false, false);
			na.setTipoNodo(o.getLivello().ordinal());
			if ((zero.equals(o.getLivello()) && includi) || o.getLivello().equals(primo)) {
				na.enabling(SecurityHelper.enabling(SecurityHelper.STRUTTURA_ORGANIGRAMMA, o.getCodiceCompleto(), rs,
						enablings));
				out.add(na);
			}
			map.put(o.getId(), na);
			NodoAlberoVM p = map.get(o.getIdPadre());
			if (p != null) {
				if (completo) {
					if (p.getChildren() == null)
						p.setChildren(new ArrayList<>());
					na.enabling(SecurityHelper.enabling(SecurityHelper.STRUTTURA_ORGANIGRAMMA, o.getCodiceCompleto(),
							rs, enablings));
					p.getChildren().add(na);
				}
				p.setExpandable(true);
			}

		}

		return out;
	}

	private String nome(final OrganizzazioneSmartVM o) {
		return OrganizzazioneHelper.ridotto(o.getCodiceCompleto()) + " " + o.getIntestazione();
	}

//	private List<DipendenteVM> detail(final Long id) {
//		return organizzazioneBusiness.quadraturaRisorseUmaneRamo(id);
//	}
	@GetMapping(value = "/{id}/risorse")
	public ResponseEntity<Page<DipendenteVM>> getRisorse(@Valid @PathVariable(name = "id", required = true) Long id,
			@RequestParam(required = true, name = "anno") Integer anno,
			@RequestParam(required = false, name = "cognome") String cognome, @PageableDefault(sort = {
					"cognome" }, direction = Sort.Direction.ASC, page = 0, size = 20) Pageable pageable) {
		logger.info("/api/{id}/risorse&cognome={} - start", id, cognome);
		List<Long> filtroStrutture = null;
		final RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness, anno);

		if (!SecurityHelper.ADMINS.contains(rs.getRuolo())) {
			final OrganizzazioneSmartVM sp = rs.getStrutturaProf();
			final List<OrganizzazioneSmartVM> sr = rs.getStruttureResp();
			if (sp == null) {
				throw new BusinessException(HttpStatus.NOT_ACCEPTABLE, "Utente senza struttura nel profilo");
			}
			if (!sp.getLivello().equals(Livello.ENTE)) {
				filtroStrutture = filtroStrutture(sp, sr);
			}
		}
		Page<DipendenteVM> out = organizzazioneBusiness.getDipendenti(id, cognome, filtroStrutture, pageable);

		return ResponseEntity.ok(out);
	}

	private List<Long> filtroStrutture(OrganizzazioneSmartVM sp, List<OrganizzazioneSmartVM> sr) {
		final List<Long> out = organizzazioneBusiness.ramoId(sp.getId());
		if (sr != null) {
			for (OrganizzazioneSmartVM o : sr) {
				if (out.contains(o.getId()))
					continue;
				List<Long> items = organizzazioneBusiness.ramoId(o.getId());
				if (items != null) {
					for (Long id : items) {
						if (!out.contains(id)) {
							out.add(id);
						}
					}
				}
			}
		}
		return out;
	}

	private List<Long> filtroRami(final OrganizzazioneSmartVM sp, final OrganizzazioneSmartVM sa,
			final List<OrganizzazioneSmartVM> sr) {
		final List<Long> out = organizzazioneBusiness.ramoCompletoId(sp.getId());
		if (sa != null && !sa.getCodiceCompleto().startsWith(sp.getCodiceCompleto())) {
			final List<Long> out1 = organizzazioneBusiness.ramoId(sp.getId());
			for (Long id : out1) {
				if (!out.contains(id))
					out.add(id);
			}
		}
		if (sr != null) {
			for (OrganizzazioneSmartVM o : sr) {
				if (out.contains(o.getId()))
					continue;
				List<Long> items = organizzazioneBusiness.ramoCompletoId(o.getId());
				if (items != null) {
					for (Long id : items) {
						if (!out.contains(id)) {
							out.add(id);
						}
					}
				}
			}
		}
		return out;
	}

	@GetMapping(value = "/tipologie")
	public ResponseEntity<List<DecodificaEnumVM<TipologiaStruttura>>> tipologie() {
		final List<DecodificaEnumVM<TipologiaStruttura>> out = new ArrayList<>();
		out.add(new DecodificaEnumVM<TipologiaStruttura>(TipologiaStruttura.STRUTT_COMP));
		out.add(new DecodificaEnumVM<TipologiaStruttura>(TipologiaStruttura.UOSVD));
		out.add(new DecodificaEnumVM<TipologiaStruttura>(TipologiaStruttura.UOS));
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/tipi")
	public ResponseEntity<List<DecodificaEnumVM<TipoStruttura>>> tipi() {
		final List<DecodificaEnumVM<TipoStruttura>> out = new ArrayList<>();
		for (TipoStruttura t : TipoStruttura.values()) {
			out.add(new DecodificaEnumVM<TipoStruttura>(t));
		}
		return ResponseEntity.ok(out);
	}
}