package com.finconsgroup.performplus.rest.api.v2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.finconsgroup.performplus.enumeration.ModalitaAttuative;
import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoObiettivo;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.enumeration.TipologiaObiettiviOperativi;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEnumVM;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.rest.api.vm.SpostaNodoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.FiltroAlberoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.NodoAlberoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.StrutturaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Abilitazione;
import com.finconsgroup.performplus.rest.api.vm.v2.cruscotto.CruscottoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.AggiornaNodoPianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.CreaNodoPianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoFiglioVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoSmartVM;
import com.finconsgroup.performplus.service.business.ICruscottoBusiness;
import com.finconsgroup.performplus.service.business.INodoPianoBusiness;
import com.finconsgroup.performplus.service.business.IOrganizzazioneBusiness;
import com.finconsgroup.performplus.service.business.IRisorsaUmanaBusiness;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;
import com.finconsgroup.performplus.service.business.utils.RuoloEStrutture;
import com.finconsgroup.performplus.service.business.utils.SecurityHelper;
import com.finconsgroup.performplus.service.dto.QuadraturaIndicatoriProg;
import com.finconsgroup.performplus.service.dto.QuadraturaRisorseProg;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/programmazione")
@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
public class ProgrammazioneController {

	private static final Logger logger = LoggerFactory.getLogger(ProgrammazioneController.class);

	@Autowired
	private INodoPianoBusiness nodoPianoBusiness;
	@Autowired
	private IOrganizzazioneBusiness organizzazioneBusiness;
	@Autowired
	private IRisorsaUmanaBusiness risorsaUmanaBusiness;
	@Autowired
	private IUtenteBusiness utenteBusiness;
	@Autowired
	private ICruscottoBusiness cruscottoBusiness;

	@GetMapping(value = "/{id}")
	public ResponseEntity<NodoPianoDetailVM> getNodoPiano(@Valid @PathVariable(name = "id", required = true) Long id) {

		NodoPianoDetailVM out = nodoPianoBusiness.read(id);
		final RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness, out.getAnno());
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());
		out.setEnabling(SecurityHelper.enabling(SecurityHelper.DUP_PROGRAMMAZIONE, out, rs, enablings));
		attivazioni(rs.getRuolo(),out);
		out.setIndicatori(detailIndicatori(out.getId()));
		return ResponseEntity.ok(out);
	}

	private void attivazioni(final Ruolo ruolo,final NodoPianoDetailVM out) {
		final CruscottoVM c = cruscottoBusiness.getCruscotto(out.getIdEnte(), out.getAnno());
		final LocalDate oggi=LocalDate.now();
		boolean abilitaObiettiviEIndicatori=false;
		if(!Ruolo.AMMINISTRATORE.equals(ruolo) && !Ruolo.SUPPORTO_SISTEMA.equals(ruolo)) {
			abilitaObiettiviEIndicatori=Boolean.TRUE.equals(c.getFlagAttivazioneInserimentoInterventi())
					&& !c.getDataAttivazioneInserimentoInterventiDa().isAfter(oggi)
					&& !c.getDataAttivazioneInserimentoInterventiA().isBefore(oggi);
		}else {
			abilitaObiettiviEIndicatori=Boolean.FALSE.equals(c.getFlagChiusuraPiano())
					|| c.getDataChiusuraPiano()==null
					|| !c.getDataChiusuraPiano().isBefore(oggi);
		}
		out.setAbilitaObiettiviEIndicatori(abilitaObiettiviEIndicatori);
		boolean abilitaAssociaRisorse=false;
		if(!Ruolo.AMMINISTRATORE.equals(ruolo) && !Ruolo.SUPPORTO_SISTEMA.equals(ruolo)) {
			abilitaAssociaRisorse=Boolean.TRUE.equals(c.getFlagConfermaScheda())
					&& !c.getDataConfermaSchedaDa().isAfter(oggi)
					&& !c.getDataConfermaSchedaA().isBefore(oggi);
		}else {
			abilitaAssociaRisorse=Boolean.FALSE.equals(c.getFlagChiusuraPI())
					|| c.getDataChiusuraPI()==null
					|| !c.getDataChiusuraPI().isBefore(oggi);
		}
		out.setAbilitaAssociaRisorse(abilitaAssociaRisorse);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@PostMapping(value = "")
	public ResponseEntity<Void> creaNodoPiano(@Valid @RequestBody(required = true) CreaNodoPianoRequest request) {
		logger.info("/api/programmazione/crea - start");
		nodoPianoBusiness.crea(request);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> aggiornaNodoPiano(@Valid @PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody(required = true) AggiornaNodoPianoRequest request) {
		logger.info("/api/programmazione/aggiorna - start");
		nodoPianoBusiness.aggiorna(id, request);
		return ResponseEntity.ok(null);
	}

	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE", "COLLABORATORE_REFERENTE", "PROGRAMMAZIONE_STRATEGICA" })
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> eliminaNodoPiano(@Valid @PathVariable(name = "id", required = true) Long id) {
		logger.info("/api/programmazione/elimina - start");
		nodoPianoBusiness.elimina(id);
		return ResponseEntity.ok(null);
	}

	@GetMapping(value = "/children/{idNodoPiano}")
	public ResponseEntity<List<NodoAlberoVM>> children(
			@Valid @PathVariable(name = "idNodoPiano", required = true) Long idNodoPiano) {
		logger.info("/api/programmazione/children - start");
		List<NodoPianoSmartVM> items = nodoPianoBusiness.ramo(idNodoPiano);
		if (items == null || items.isEmpty()) {
			return ResponseEntity.ok(new ArrayList<>());
		}
		final RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness,
				items.get(0).getAnno());
        if(!SecurityHelper.ADMINS.contains(rs.getRuolo())) {
            final OrganizzazioneSmartVM sp = rs.getStrutturaProf();
            final OrganizzazioneSmartVM sa = rs.getStrutturaProfAggiunta();
            final List<OrganizzazioneSmartVM> sr = rs.getStruttureResp();
            if(sp==null) {
            	throw new BusinessException(HttpStatus.NOT_ACCEPTABLE,"Utente senza struttura nel profilo");
            }
            if(!sp.getLivello().equals(Livello.ENTE)) {
            	final List<Long>filtroStrutture=filtroStruttureAlbero(sp,sa,sr);
            	items.removeIf(r->r.getIdStruttura()==null ||!filtroStrutture.contains(r.getIdStruttura()));
            }
        }
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());
		final List<NodoAlberoVM> albero = mappingChildren(items, rs, enablings);
		albero.sort(new Comparator<NodoAlberoVM>() {

			@Override
			public int compare(NodoAlberoVM o1, NodoAlberoVM o2) {
					return o1.getCodiceCompleto().compareTo(o2.getCodiceCompleto());
			}

		});
		return ResponseEntity.ok(albero);
	}

	@GetMapping(value = "/root")
	public ResponseEntity<NodoAlberoVM> root(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = true) Integer anno,
			@Valid @RequestParam(name = "filter", required = false) String testo) {
		logger.info("/api/programmazione/root - start");
		NodoPianoSmartVM root = nodoPianoBusiness.getRootOnly(idEnte, anno);
		FiltroAlberoVM filtro = new FiltroAlberoVM();
		filtro.setAnno(anno);
		filtro.setIdEnte(idEnte);
		filtro.setTesto(testo);
		RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness, anno);
        if(rs.getRuolo()==null) {
        	throw new BusinessException(HttpStatus.NOT_ACCEPTABLE,"Utente senza ruolo");
        }
        if(!SecurityHelper.ADMINS.contains(rs.getRuolo())) {
            final OrganizzazioneSmartVM sp = rs.getStrutturaProf();
            final OrganizzazioneSmartVM sa = rs.getStrutturaProfAggiunta();
            final List<OrganizzazioneSmartVM> sr = rs.getStruttureResp();
            if(sp==null) {
            	throw new BusinessException(HttpStatus.NOT_ACCEPTABLE,"Utente senza struttura nel profilo");
            }
            if(!sp.getLivello().equals(Livello.ENTE)) {
            	List<Long>filtroStrutture=filtroStruttureAlbero(sp,sa,sr);
            	filtro.setStrutture(filtroStrutture);
            }
        }
		List<NodoPianoSmartVM> items = nodoPianoBusiness.search(filtro);
		boolean vuoto = items == null || items.isEmpty();
		if (!vuoto) {
			if (!TipoNodo.PIANO.equals(items.get(0).getTipoNodo())) {
				items.add(0, root);
			}
		}
		NodoAlberoVM node = new NodoAlberoVM(root.getId(), root.getDenominazione(), root.getTipoNodo().ordinal() + 1,
				!vuoto, true).codiceCompleto(root.getCodiceCompleto());
		node.setTipoNodo(root.getTipoNodo().ordinal());
		if(vuoto)rs = null;
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());

		node.setTipoNodo(root.getTipoNodo().ordinal());
		node.setAnno(root.getAnno());
		node.setAnnoInizio(root.getAnnoInizio());
		node.setAnnoFine(root.getAnnoFine());
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
	@GetMapping(value = "/listSposta")
	public ResponseEntity<List<NodoPianoSmartVM>> listSposta(
			@Valid @RequestParam(name = "id", required = true) Long id){
		logger.info("/api/programmazione/listSposta - start");
		return ResponseEntity.ok(nodoPianoBusiness.listSposta(id));
	}
	private List<Long> filtroStrutture(OrganizzazioneSmartVM sp, List<OrganizzazioneSmartVM> sr) {
		final List<Long> out=organizzazioneBusiness.ramoId(sp.getId());
		if(sr!=null) {
			for (OrganizzazioneSmartVM o : sr) {
				if(out.contains(o.getId()))continue;
				List<Long>items=organizzazioneBusiness.ramoId(o.getId());
				if(items!=null) {
					for (Long id : items) {
						if(!out.contains(id)) {
							out.add(id);					}
					}
				}
			}
		}
		return out;
	}
	private List<Long> filtroStruttureAlbero(final OrganizzazioneSmartVM sp, final OrganizzazioneSmartVM sa,final List<OrganizzazioneSmartVM> sr) {
		final List<Long> out=organizzazioneBusiness.ramoCompletoId(sp.getId());
		if (sa != null && !sa.getCodiceCompleto().startsWith(sp.getCodiceCompleto())) {
			final List<Long> out1 = organizzazioneBusiness.ramoId(sp.getId());
			for (Long id : out1) {
				if (!out.contains(id))
					out.add(id);
			}
		}
		if(sr!=null) {
			for (OrganizzazioneSmartVM o : sr) {
				if(out.contains(o.getId()))continue;
				List<Long>items=organizzazioneBusiness.ramoId(o.getId());
				if(items!=null) {
					for (Long id : items) {
						if(!out.contains(id)) {
							out.add(id);					}
					}
				}
			}
		}
		return out;
	}
	@PostMapping(value = "/search")
	public ResponseEntity<NodoAlberoVM> search(@RequestBody(required = true) FiltroAlberoVM filter) {
		logger.info("/api/programmazione/root - start");
		NodoPianoSmartVM root = nodoPianoBusiness.getRootOnly(filter.getIdEnte(), filter.getAnno());
		RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness, filter.getAnno());
        if(rs.getRuolo()==null) {
        	throw new BusinessException(HttpStatus.NOT_ACCEPTABLE,"Utente senza ruolo");
        }
        if(!SecurityHelper.ADMINS.contains(rs.getRuolo())) {
            final OrganizzazioneSmartVM sp = rs.getStrutturaProf();
            final OrganizzazioneSmartVM sa = rs.getStrutturaProfAggiunta();
            final List<OrganizzazioneSmartVM> sr = rs.getStruttureResp();
            if(sp==null) {
            	throw new BusinessException(HttpStatus.NOT_ACCEPTABLE,"Utente senza struttura nel profilo");
            }
            if(!sp.getLivello().equals(Livello.ENTE)) {
            	List<Long>filtroStrutture=filtroStruttureAlbero(sp,sa,sr);
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
				!vuoto, true).codiceCompleto(root.getCodiceCompleto());
		node.setTipoNodo(root.getTipoNodo().ordinal());
		node.setStatoNodo(root.getStatoNodo());
		if(vuoto)rs = null;
		final List<Abilitazione> enablings = SecurityHelper.enablings(rs == null ? null : rs.getRuolo());
		node.setAnno(root.getAnno());
		node.setAnnoInizio(root.getAnnoInizio());
		node.setAnnoFine(root.getAnnoFine());
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

	@GetMapping(value = "/isCodiceDuplicato")
	public ResponseEntity<Boolean> isCodiceDuplicato(
			@Valid @RequestParam(name = "idPadre", required = true) Long idPadre,
			@Valid @RequestParam(name = "codice", required = true) String codice) {
		Boolean out = nodoPianoBusiness.isCodiceDuplicato(idPadre, codice);
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/preparaNuovoNodo")
	public ResponseEntity<CreaNodoPianoRequest> preparaNuovoNodo(
			@Valid @RequestParam(name = "idPadre", required = true) Long idPadre) {
		CreaNodoPianoRequest out = nodoPianoBusiness.prepareDescendant(idPadre);
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/preparaModificaNodo")
	public ResponseEntity<CreaNodoPianoRequest> preparaModificaNodo(
			@Valid @RequestParam(name = "idNodo", required = true) Long idNodo) {
		CreaNodoPianoRequest out = nodoPianoBusiness.prepareModifica(idNodo);
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/{id}/risorse")
	public ResponseEntity<List<QuadraturaRisorseProg>> detailRisorse(
			@Valid @PathVariable(name = "id", required = true) Long id) {

		List<QuadraturaRisorseProg> out = nodoPianoBusiness.detailRisorse(id);
		return ResponseEntity.ok(out);
	}


	@GetMapping(value = "/tipi")
	public ResponseEntity<List<DecodificaEnumVM<TipoObiettivo>>> tipi() {
		final List<DecodificaEnumVM<TipoObiettivo>> out = new ArrayList<>();
		for (TipoObiettivo t : TipoObiettivo.values()) {
			out.add(new DecodificaEnumVM<TipoObiettivo>(t));
		}
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/modalitaAttuative")
	public ResponseEntity<List<DecodificaEnumVM<ModalitaAttuative>>> modalitaAttuative() {
		final List<DecodificaEnumVM<ModalitaAttuative>> out = new ArrayList<>();
		for (ModalitaAttuative t : ModalitaAttuative.values()) {
			out.add(new DecodificaEnumVM<ModalitaAttuative>(t));
		}
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/tipologie")
	public ResponseEntity<List<DecodificaEnumVM<TipologiaObiettiviOperativi>>> tipologie() {
		final List<DecodificaEnumVM<TipologiaObiettiviOperativi>> out = new ArrayList<>();
		for (TipologiaObiettiviOperativi t : TipologiaObiettiviOperativi.values()) {
			out.add(new DecodificaEnumVM<TipologiaObiettiviOperativi>(t));
		}
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/organizzazioni")
	public ResponseEntity<List<StrutturaVM>> organizzazioni(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = true) Integer anno,
			@Valid @RequestParam(name = "testo", required = false) String testo) {
		List<OrganizzazioneSmartVM> items = organizzazioneBusiness.search(idEnte, anno, testo);
		final RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness,
				anno);
        if(!SecurityHelper.ADMINS.contains(rs.getRuolo())) {
            final OrganizzazioneSmartVM sp = rs.getStrutturaProf();
            final List<OrganizzazioneSmartVM> sr = rs.getStruttureResp();
            if(sp==null) {
            	throw new BusinessException(HttpStatus.NOT_ACCEPTABLE,"Utente senza struttura nel profilo");
            }
            if(!sp.getLivello().equals(Livello.ENTE)) {
            	final List<Long>filtroStrutture=filtroStrutture(sp,sr);
            	items.removeIf(r->r.getId()==null ||!filtroStrutture.contains(r.getId()));
            }
        }
		List<StrutturaVM> out = Mapping.mapping(items, StrutturaVM.class);
	
		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/direzioni")
	public ResponseEntity<List<StrutturaVM>> direzioni(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = true) Integer anno,
			@Valid @RequestParam(name = "testo", required = false) String testo) {
		List<StrutturaVM> out = Mapping.mapping(organizzazioneBusiness.direzioni(idEnte, anno, testo),
				StrutturaVM.class);

		return ResponseEntity.ok(out);
	}



	@GetMapping(value = "/responsabili")
	public ResponseEntity<List<DecodificaVM>> responsabili(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = true) Integer anno,
			@Valid @RequestParam(name = "cognome", required = false) String cognome) {
		List<DecodificaVM> out = nodoPianoBusiness.searchResponsabili(idEnte, anno, cognome);

		return ResponseEntity.ok(out);
	}

	@GetMapping(value = "/risorse")
	public ResponseEntity<List<DecodificaVM>> risorse(
			@Valid @RequestParam(name = "idEnte", required = true, defaultValue = "0") Long idEnte,
			@Valid @RequestParam(name = "anno", required = true) Integer anno,
			@Valid @RequestParam(name = "cognome", required = false) String cognome) {
		List<DecodificaVM> out = risorsaUmanaBusiness.searchRisorse(idEnte, anno, cognome);

		return ResponseEntity.ok(out);
	}


	@GetMapping(value = "/figli")
	public ResponseEntity<List<NodoFiglioVM>> figli(
			@Valid @RequestParam(name = "idNodo", required = true) Long idNodoPiano) {
		logger.info("/api/programmazione/figli - start");
		List<NodoFiglioVM> items = nodoPianoBusiness.figli(idNodoPiano);
		if (items == null || items.isEmpty()) {
			return ResponseEntity.ok(new ArrayList<>());
		}
		final RuoloEStrutture rs = SecurityHelper.ruoloEStrutture(utenteBusiness, risorsaUmanaBusiness,
				items.get(0).getAnno());
        if(!SecurityHelper.ADMINS.contains(rs.getRuolo())) {
            final OrganizzazioneSmartVM sp = rs.getStrutturaProf();
            final OrganizzazioneSmartVM sa = rs.getStrutturaProfAggiunta();
            final List<OrganizzazioneSmartVM> sr = rs.getStruttureResp();
            if(sp==null) {
            	throw new BusinessException(HttpStatus.NOT_ACCEPTABLE,"Utente senza struttura nel profilo");
            }
            if(!sp.getLivello().equals(Livello.ENTE)) {
            	final List<Long>filtroStrutture=filtroStruttureAlbero(sp,sa,sr);
            	items.removeIf(r->r.getIdStruttura()==null || !filtroStrutture.contains(r.getIdStruttura()));
            }
        }
		
		return ResponseEntity.ok(items);
	}
	private List<NodoAlberoVM> mappingChildren(final List<NodoPianoSmartVM> items, final RuoloEStrutture rs,
			final List<Abilitazione> enablings) {
		return mappingChildren(items, false, true, rs, enablings);
	}


	private List<NodoAlberoVM> mappingChildren(final List<NodoPianoSmartVM> items, boolean includi, boolean completo,
			final RuoloEStrutture rs, final List<Abilitazione> enablings) {
		final List<NodoAlberoVM> out = new ArrayList<>();
		if (items == null || items.isEmpty())
			return out;

		final TipoNodo zero = items.get(0).getTipoNodo();
		TipoNodo primo = null;
		Map<Long, NodoAlberoVM> map = new HashMap<>();
		for (NodoPianoSmartVM np : items) {
			if (primo == null && !zero.equals(np.getTipoNodo())) {
				primo = np.getTipoNodo();
			}
			NodoAlberoVM na = new NodoAlberoVM(np.getId(), nome(np), np.getTipoNodo().ordinal() + 1, false, false);
			na.setTipoNodo(np.getTipoNodo().ordinal());
			na.setAnno(np.getAnno());
			na.setAnnoInizio(np.getAnnoInizio());
			na.setAnnoFine(np.getAnnoFine());
			na.setOrdine(np.getOrdine());
			na.setStatoNodo(np.getStatoNodo());
			na.setCodiceCompleto(np.getCodiceCompleto());		
			na.setExpanded(np.getTipoNodo().equals(zero)
					|| np.getTipoNodo().equals(primo)
					|| np.getTipoNodo().ordinal()<=1);
			if ((zero.equals(np.getTipoNodo()) && includi) || np.getTipoNodo().equals(primo)) {
				na.enabling(SecurityHelper.enabling(SecurityHelper.DUP_PROGRAMMAZIONE, np.getCodiceCompletoStruttura(),
						rs, enablings));
				out.add(na);

			}
			map.put(np.getId(), na);
			NodoAlberoVM p = map.get(np.getIdPadre());
			if (p != null) {
				if (completo) {
					if (p.getChildren() == null)
						p.setChildren(new ArrayList<>());
					na.enabling(SecurityHelper.enabling(SecurityHelper.DUP_PROGRAMMAZIONE, np.getCodiceCompleto(), rs,
							enablings));
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

	private List<QuadraturaIndicatoriProg> detailIndicatori(final Long id) {
		return nodoPianoBusiness.detailIndicatori(id);
	}
	@Secured({ "AMMINISTRATORE", "REFERENTE_PROGRAMMAZIONE" })
	@PutMapping(value = "/spostaNodo")
	public ResponseEntity<Void> spostaNodo(
			@Valid @RequestBody(required = true) SpostaNodoRequest request) {
		logger.info("/api/programmazione/spostaNodo - start");
		nodoPianoBusiness.spostaNodo(request);
		return ResponseEntity.ok(null);
	}

}