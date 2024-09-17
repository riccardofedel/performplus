package com.finconsgroup.performplus.rest.api.v2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.finconsgroup.performplus.enumeration.Periodicita;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEnumVM;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.rest.api.vm.v2.FiltroAlberoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.NodoAlberoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Abilitazione;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AddAllegatoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AggiornaConsuntivoNodoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AggiornaConsuntivoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AggiornaForzaturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AllegatoListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AllegatoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AvanzamentoFineAnnoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AvanzamentoFineAnnoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ConsuntivoIndicatoreVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ConsuntivoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ForzaturaNodoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ForzaturaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.IndicatoreViewVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.RichiestaForzaturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.StatoAvanzamentoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.StatoAvanzamentoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ValutazioneVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoSmartVM;
import com.finconsgroup.performplus.service.business.IConsuntivoBusiness;
import com.finconsgroup.performplus.service.business.INodoPianoBusiness;
import com.finconsgroup.performplus.service.business.IOrganizzazioneBusiness;
import com.finconsgroup.performplus.service.business.IRisorsaUmanaBusiness;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;
import com.finconsgroup.performplus.service.business.utils.RuoloEStrutture;
import com.finconsgroup.performplus.service.business.utils.SecurityHelper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/consuntivazione")
@Secured({ "AMMINISTRATORE", "READ_ALL", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE",
		"PROGRAMMAZIONE_STRATEGICA" })
public class ConsuntivazioneController {

	private static final Logger logger = LoggerFactory.getLogger(ConsuntivazioneController.class);

	@Autowired
	private INodoPianoBusiness nodoPianoBusiness;
	@Autowired
	private IConsuntivoBusiness consuntivoBusiness;
	@Autowired
	private IRisorsaUmanaBusiness risorsaUmanaBusiness;
	@Autowired
	private IUtenteBusiness utenteBusiness;
	@Autowired
	private IOrganizzazioneBusiness organizzazioneBusiness;

	@GetMapping(value = "/{idNodoPiano}")
	public ResponseEntity<ConsuntivoVM> getNodoPiano(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano) {
		ConsuntivoVM out = consuntivoBusiness.read(idNodoPiano);
		final RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness, out.getAnno());
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());
		out.setEnabling(SecurityHelper.enabling(SecurityHelper.DUP_CONSUNTIVAZIONE, out, rs, enablings));

		out.setConsuntivoIndicatori(detail(out.getId()));
		if (out.getConsuntivoIndicatori() != null && !out.getConsuntivoIndicatori().isEmpty()) {
			out.setPeriodicitaTarget(out.getConsuntivoIndicatori().get(0).getPeriodicitaTarget());
			out.setPeriodicitaRend(out.getConsuntivoIndicatori().get(0).getPeriodicitaRend());
		} else {
			if (out.getIndicatorePiano() != null) {
				Periodicita[] pf = consuntivoBusiness
						.getPeridicitaFase(out.getIndicatorePiano().getNodoPiano().getTipoNodo());
				out.setPeriodicitaTarget(pf[0]);
				out.setPeriodicitaRend(pf[1]);
			}
		}

		out.setEnabledConsuntivo(consuntivoBusiness.enabledConsuntivo(idNodoPiano));

		return ResponseEntity.ok(out);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@PutMapping(value = "/{idNodoPiano}")
	public ResponseEntity<Void> aggiornaConsuntivoNodo(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano,
			@Valid @RequestBody(required = true) AggiornaConsuntivoNodoRequest request) {
		logger.info("/api/consuntivazione/aggiorna - start");
		consuntivoBusiness.aggiorna(idNodoPiano, request);
		return ResponseEntity.ok(null);
	}

	@GetMapping(value = "/children/{idNodoPiano}")
	public ResponseEntity<List<NodoAlberoVM>> children(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano) {
		logger.info("/api/consuntivazione/children - start");
		List<NodoPianoSmartVM> items = nodoPianoBusiness.ramo(idNodoPiano);
		if (items == null || items.isEmpty()) {
			return ResponseEntity.ok(new ArrayList<>());
		}
		final RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness,
				items.get(0).getAnno());
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());
		final List<NodoAlberoVM> albero = mappingChildren(items, rs, enablings);
		return ResponseEntity.ok(albero);
	}

	@GetMapping(value = "/root")
	public ResponseEntity<NodoAlberoVM> root(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = false) Integer anno,
			@Valid @RequestParam(name = "filter", required = false) String testo) {
		logger.info("/api/consuntivazione/root - start");
		NodoPianoSmartVM root = nodoPianoBusiness.getRootOnly(idEnte, anno);
		FiltroAlberoVM filter = new FiltroAlberoVM();
		filter.setAnno(anno);
		filter.setIdEnte(idEnte);
		filter.setTesto(testo);
		RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness, anno);
		if (rs.getRuolo() == null) {
			throw new BusinessException(HttpStatus.NOT_ACCEPTABLE, "Utente senza ruolo");
		}
		if (!SecurityHelper.ADMINS.contains(rs.getRuolo())) {
			final OrganizzazioneSmartVM sp = rs.getStrutturaProf();
			final OrganizzazioneSmartVM sa = rs.getStrutturaProfAggiunta();
			final List<OrganizzazioneSmartVM> sr = rs.getStruttureResp();
			if (sp == null) {
				throw new BusinessException(HttpStatus.NOT_ACCEPTABLE, "Utente senza struttura nel profilo");
			}
			if (!sp.getLivello().equals(Livello.ENTE)) {
				List<Long> filtroStrutture = filtroStrutture(sp, sa, sr);
				filter.setStrutture(filtroStrutture);
			}
		}
		List<NodoPianoSmartVM> items = nodoPianoBusiness.search(filter);
		boolean vuoto = items == null || items.isEmpty();
		if (!vuoto) {
			if (!TipoNodo.PIANO.equals(items.get(0).getTipoNodo())) {
				items.add(0, root);
			}
		}
		NodoAlberoVM node = new NodoAlberoVM(root.getId(), root.getDenominazione(), root.getTipoNodo().ordinal() + 1,
				!vuoto, true);
		node.setTipoNodo(root.getTipoNodo().ordinal());
		if (vuoto)
			rs = null;
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());
		final List<NodoAlberoVM> children = mappingChildren(items, false, true, rs, enablings);
		children.sort(new Comparator<NodoAlberoVM>() {

			@Override
			public int compare(NodoAlberoVM o1, NodoAlberoVM o2) {

					return o1.getCodiceCompleto().compareTo(o2.getCodiceCompleto());

			}

		});
		node.setChildren(children);
		return ResponseEntity.ok(node);
	}

	@PostMapping(value = "/search")
	public ResponseEntity<NodoAlberoVM> search(@RequestBody(required = true) FiltroAlberoVM filter) {
		logger.info("/api/programmazione/root - start");
		NodoPianoSmartVM root = nodoPianoBusiness.getRootOnly(filter.getIdEnte(), filter.getAnno());
		RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness, filter.getAnno());
		if (rs.getRuolo() == null) {
			throw new BusinessException(HttpStatus.NOT_ACCEPTABLE, "Utente senza ruolo");
		}
		if (!SecurityHelper.ADMINS.contains(rs.getRuolo())) {
			final OrganizzazioneSmartVM sp = rs.getStrutturaProf();
			final OrganizzazioneSmartVM sa = rs.getStrutturaProfAggiunta();
			final List<OrganizzazioneSmartVM> sr = rs.getStruttureResp();
			if (sp == null) {
				throw new BusinessException(HttpStatus.NOT_ACCEPTABLE, "Utente senza struttura nel profilo");
			}
			if (!sp.getLivello().equals(Livello.ENTE)) {
				List<Long> filtroStrutture = filtroStrutture(sp, sa, sr);
				filter.setStrutture(filtroStrutture);
			}
		}
		List<NodoPianoSmartVM> items = nodoPianoBusiness.search(filter);
		boolean vuoto = items == null || items.isEmpty();
		if (!vuoto) {
			if (!TipoNodo.PIANO.equals(items.get(0).getTipoNodo())) {
				items.add(0, root);
			}
		}
		NodoAlberoVM node = new NodoAlberoVM(root.getId(), root.getDenominazione(), root.getTipoNodo().ordinal() + 1,
				!vuoto, true);
		node.setTipoNodo(root.getTipoNodo().ordinal());
		if (vuoto)
			rs = null;
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());
		if (!vuoto) {
			final List<NodoAlberoVM> children = mappingChildren(items, false, true, rs, enablings);
			children.sort(new Comparator<NodoAlberoVM>() {

				@Override
				public int compare(NodoAlberoVM o1, NodoAlberoVM o2) {

						return o1.getCodiceCompleto().compareTo(o2.getCodiceCompleto());
	
				}

			});
			node.setChildren(children);
		}
		return ResponseEntity.ok(node);
	}

	@GetMapping(value = "/view/{idIndicatore}")
	public ResponseEntity<IndicatoreViewVM> view(
			@Valid @PathVariable(name = "idIndicatore", required = true) Long idIndicatore) {

		IndicatoreViewVM out = consuntivoBusiness.view(idIndicatore);
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/valutazione/{idIndicatore}")
	public ResponseEntity<ValutazioneVM> getValutazione(
			@Valid @PathVariable(name = "idIndicatore", required = true) Long idIndicatore,
			@Valid @RequestParam(name = "idValutazione", required = false) Long idValutazione,
			@Valid @RequestParam(name = "dataRilevazione", required = false) LocalDate dataRilevazione) {

		ValutazioneVM out = consuntivoBusiness.getValutazione(idIndicatore, idValutazione, dataRilevazione);
		return ResponseEntity.ok(out);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA",
			"RESPONSABILE_AZIONI" })
	@PutMapping(value = "/consuntiva/{idIndicatore}")
	public ResponseEntity<Void> consuntiva(
			@Valid @PathVariable(name = "idIndicatore", required = true) Long idIndicatore,
			@Valid @RequestBody(required = true) AggiornaConsuntivoRequest request) {

		consuntivoBusiness.consuntiva(idIndicatore, request);

		return ResponseEntity.ok(null);
	}

	@GetMapping(value = "/forzatura/{idIndicatore}")
	public ResponseEntity<ForzaturaVM> getForzatura(
			@Valid @PathVariable(name = "idIndicatore", required = true) Long idIndicatore) {

		ForzaturaVM out = consuntivoBusiness.getForzatura(idIndicatore);
		return ResponseEntity.ok(out);
	}

	@Secured({ "AMMINISTRATORE" })
	@PutMapping(value = "/forzatura/{idIndicatore}")
	public ResponseEntity<Void> aggiornaForzatura(
			@Valid @PathVariable(name = "idIndicatore", required = true) Long idIndicatore,
			@Valid @RequestBody(required = true) AggiornaForzaturaRequest request) {

		consuntivoBusiness.aggiornaForzatura(idIndicatore, request);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@PutMapping(value = "/richiestaForzatura/{idIndicatore}")
	public ResponseEntity<Void> richiestaForzatura(
			@Valid @PathVariable(name = "idIndicatore", required = true) Long idIndicatore,
			@Valid @RequestBody(required = true) RichiestaForzaturaRequest request) {

		consuntivoBusiness.richiestaForzatura(idIndicatore, request);
		return ResponseEntity.ok(null);
	}

	@GetMapping(value = "/allegati/{idIndicatore}")
	public ResponseEntity<List<AllegatoListVM>> getAllegati(
			@Valid @PathVariable(name = "idIndicatore", required = true) Long idIndicatore) {

		List<AllegatoListVM> out = consuntivoBusiness.getAllegati(idIndicatore);
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/allegato/{idAllegato}")
	public ResponseEntity<AllegatoVM> getAllegato(
			@Valid @PathVariable(name = "idAllegato", required = true) Long idAllegato) {

		AllegatoVM out = consuntivoBusiness.getAllegato(idAllegato);
		return ResponseEntity.ok(out);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@DeleteMapping(value = "/allegato/{idAllegato}")
	public ResponseEntity<Void> deleteAllegato(
			@Valid @PathVariable(name = "idAllegato", required = true) Long idAllegato) {

		consuntivoBusiness.deleteAllegato(idAllegato);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@PostMapping(value = "/allegato/{idIndicatore}")
	public ResponseEntity<Void> addAllegato(
			@Valid @PathVariable(name = "idIndicatore", required = true) Long idIndicatore,
			@Valid @RequestBody(required = true) AddAllegatoRequest request) {

		consuntivoBusiness.addAllegato(idIndicatore, request);
		return ResponseEntity.ok(null);
	}

	@GetMapping(value = "/allegato/contentTypes")
	public ResponseEntity<List<String>> getContentTypes() {

		return ResponseEntity.ok(List.of("application/pdf", "application/msword", "application/vnd.ms-excel",
				"application/vnd.ms-powerpoint",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
				"application/vnd.openxmlformats-officedocument.presentationml.presentation", "image/gif", "image/jpeg",
				"image/png", "image/tiff"));
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA",
			"RESPONSABILE_AZIONI" })
	@PutMapping(value = "/statoAvanzamento")
	public ResponseEntity<Void> setStatoAvanzamento(
			@Valid @RequestBody(required = true) StatoAvanzamentoRequest request) {
		consuntivoBusiness.statoAvanzamento(request);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA",
			"RESPONSABILE_AZIONI" })
	@GetMapping(value = "/statoAvanzamento/{idNodoPiano}")
	public ResponseEntity<StatoAvanzamentoVM> getStatoAvanzamento(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano) {
		StatoAvanzamentoVM out = consuntivoBusiness.getStatoAvanzamento(idNodoPiano);
		return ResponseEntity.ok(out);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE" })
	@PutMapping(value = "/forzatureNodo")
	public ResponseEntity<Void> setForzatureNodo(@Valid @RequestBody(required = true) ForzaturaNodoVM request) {
		consuntivoBusiness.forzatureNodo(request);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE" })
	@GetMapping(value = "/forzatureNodo/{idNodoPiano}")
	public ResponseEntity<ForzaturaNodoVM> getForzatureNodo(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano) {
		ForzaturaNodoVM out = consuntivoBusiness.getForzatureNodo(idNodoPiano);
		return ResponseEntity.ok(out);
	}

	private void filtra(List<NodoPianoSmartVM> items, String testo) {
		final List<Long> eliminati = new ArrayList<>();
		final List<Long> rimanenti = new ArrayList<>();
		for (int i = TipoNodo.values().length - 1; i >= 0; i--) {
			TipoNodo l = TipoNodo.values()[i];
			items.forEach(o -> {
				if (l.equals(o.getTipoNodo())) {
					if (!rimanenti.contains(o.getId())
							&& !o.getDenominazione().toLowerCase().contains(testo.toLowerCase())) {
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

	private List<NodoAlberoVM> mappingChildren(final List<NodoPianoSmartVM> items, final RuoloEStrutture rs,
			final List<Abilitazione> enablings) {
		return mappingChildren(items, false, true, rs, enablings);
	}

	private List<NodoAlberoVM> mappingChildren(final List<NodoPianoSmartVM> items, boolean includi,
			final RuoloEStrutture rs, final List<Abilitazione> enablings) {
		final List<NodoAlberoVM> out = new ArrayList<>();
		if (items == null || items.isEmpty())
			return out;

		final TipoNodo zero = items.get(0).getTipoNodo();
		TipoNodo primo = null;
		Map<String, NodoAlberoVM> map = new HashMap<>();
		for (NodoPianoSmartVM o : items) {
			if (primo == null && !zero.equals(o.getTipoNodo())) {
				primo = o.getTipoNodo();
			}
			NodoAlberoVM na = new NodoAlberoVM(o.getId(), nome(o), o.getTipoNodo().ordinal() + 1, false, false);
			na.setTipoNodo(o.getTipoNodo().ordinal());
			if ((zero.equals(o.getTipoNodo()) && includi) || o.getTipoNodo().equals(primo)) {
				na.enabling(SecurityHelper.enabling(SecurityHelper.DUP_CONSUNTIVAZIONE, o.getCodiceCompletoStruttura(),
						rs, enablings));
				out.add(na);
			}
			map.put(o.getCodiceCompleto(), na);
			NodoAlberoVM p = map.get(parent(o.getCodiceCompleto()));
			if (p != null) {
				p.setExpandable(true);
			}

		}

		return out;
	}

	private List<NodoAlberoVM> mappingChildren(final List<NodoPianoSmartVM> items, boolean includi, boolean completo,
			final RuoloEStrutture rs, final List<Abilitazione> enablings) {
		final List<NodoAlberoVM> out = new ArrayList<>();
		if (items == null || items.isEmpty())
			return out;

		final TipoNodo zero = items.get(0).getTipoNodo();
		TipoNodo primo = null;
		Map<Long, NodoAlberoVM> map = new HashMap<>();
		for (NodoPianoSmartVM item : items) {
			if (primo == null && !zero.equals(item.getTipoNodo())) {
				primo = item.getTipoNodo();
			}
			NodoAlberoVM na = new NodoAlberoVM(item.getId(), nome(item), item.getTipoNodo().ordinal() + 1, false,
					false);
			na.setTipoNodo(item.getTipoNodo().ordinal());
			na.setStatoNodo(item.getStatoNodo());
			na.setCodiceCompleto(item.getCodiceCompleto());
			na.setOrdine(item.getOrdine());
			if ((zero.equals(item.getTipoNodo()) && includi) || item.getTipoNodo().equals(primo)) {
				na.enabling(SecurityHelper.enabling(SecurityHelper.DUP_CONSUNTIVAZIONE,
						item.getCodiceCompletoStruttura(), rs, enablings));
				out.add(na);

			}
			map.put(item.getId(), na);
			NodoAlberoVM p = map.get(item.getIdPadre());
			if (p != null) {
				if (completo) {
					if (p.getChildren() == null)
						p.setChildren(new ArrayList<>());
					na.enabling(SecurityHelper.enabling(SecurityHelper.DUP_CONSUNTIVAZIONE,
							item.getCodiceCompletoStruttura(), rs, enablings));
					p.getChildren().add(na);
				}
				p.setExpandable(true);
			}

		}

		return out;
	}

	private String nome(final NodoPianoSmartVM o) {
		return NodoPianoHelper.ridotto(o.getCodiceCompleto()) + " " + o.getDenominazione();
	}

	private String parent(String codiceCompleto) {
		if (StringUtils.isBlank(codiceCompleto)) {
			return codiceCompleto;
		}
		int k = codiceCompleto.lastIndexOf('.');
		if (k < 0) {
			return "";
		}
		return codiceCompleto.substring(0, k);
	}

	private List<ConsuntivoIndicatoreVM> detail(final Long idNodoPiano) {
		return consuntivoBusiness.detailConsuntivoIndicatori(idNodoPiano);
	}

	private List<Long> filtroStrutture(OrganizzazioneSmartVM sp, OrganizzazioneSmartVM sa,
			List<OrganizzazioneSmartVM> sr) {
		final List<Long> out = organizzazioneBusiness.ramoId(sp.getId());
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
}