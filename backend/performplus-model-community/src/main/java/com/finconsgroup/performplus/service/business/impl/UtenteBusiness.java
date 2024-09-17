package com.finconsgroup.performplus.service.business.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import jakarta.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.Profilo;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.Utente;
import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.manager.security.IEncrypterManager;
import com.finconsgroup.performplus.repository.OrganizzazioneRepository;
import com.finconsgroup.performplus.repository.ProfiloRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.UtenteRepository;
import com.finconsgroup.performplus.rest.api.vm.ProfiloVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteFlatVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteSmartVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.AggiornaUtenteRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.CreaUtenteRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.CambioPasswordRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.ModificaPasswordRequest;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.MappingUtenteHelper;
import com.finconsgroup.performplus.service.dto.ProfiloDTO;
import com.finconsgroup.performplus.service.dto.UtenteDTO;

@Service
@Transactional
public class UtenteBusiness implements IUtenteBusiness {

	@Autowired(required=true)
	private UtenteRepository utenteRepository;
	@Autowired
	private ProfiloRepository profiloRepository;
	@Autowired
	private OrganizzazioneRepository organizzazioneRepository;
	@Autowired
	private RisorsaUmanaRepository risorsaUmanaRepository;

	@Autowired
	private IEncrypterManager shaManager;

	@Override
	@Transactional(readOnly = true)
	public UtenteVM getUser(String userid, String password) throws BusinessException {
		Utente u = utenteRepository.findByUserid(userid);
		if (u == null) {
			return null;
		}
		if (!shaManager.matches(password, u.getPasswd())) {
			return null;
		}
		return MappingUtenteHelper.mappingToUtente(u).codiceFiscale(u.getCodiceInterno());
	}

	@Override
	public UtenteDTO crea(UtenteDTO dto) throws BusinessException {
		dto.setPasswd(encrypte(dto.getPasswd()));

		Utente utente = MappingUtenteHelper.mappingFromDTO(dto);
		utente.setCodiceInterno(dto.getCodiceFiscale());

		utente = utenteRepository.save(utente);
		if (dto.isAdmin() && dto.getIdEnte() != null) {
			Profilo profilo;
			List<Organizzazione> items = organizzazioneRepository.findByIdEnteAndLivello(dto.getIdEnte(), Livello.ENTE);
			if (items != null) {
				for (Organizzazione o : items) {
					profilo = new Profilo();
					profilo.setOrganizzazione(o);
					profilo.setRuolo(Ruolo.AMMINISTRATORE);
					profilo.setAggiunto(false);
					profilo.setUtente(utente);
					if (dto.getRisorsaUmana() != null && dto.getRisorsaUmana().getId() != null)
						profilo.setRisorsaUmana(
								risorsaUmanaRepository.findById(dto.getRisorsaUmana().getId()).orElse(null));
					profiloRepository.save(profilo);
				}
			}
		}
		return completaUtente(MappingUtenteHelper.mappingToDTO(utente));
	}

	private String encrypte(String p) {
		return shaManager.encode(p);
	}

	private boolean chekPwd(String p, String p1) {
		return shaManager.matches(p, p1);
	}

	@Override
	public UtenteDTO aggiorna(UtenteDTO dto) throws BusinessException {
//		if (Boolean.TRUE.equals(dto.getSuperAdmin())) {
//			dto.setAdmin(false);
//			dto.setIdEnte(null);
//		}

		Utente old = utenteRepository.findById(dto.getId()).orElse(null);

		if (old == null) {
			throw new BusinessException("Non trovato user da modificare");
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		if (username != null && username.equalsIgnoreCase(dto.getUsername())) {
			throw new BusinessException("Per modificare la priopria password usare funzione 'cambio Password'");
		}

		if (StringUtils.isBlank(dto.getPasswd())) {
			old.setPasswd(old.getPasswd());
		} else {
			if (dto.getPasswd().trim().length() < 5)
				throw new BusinessException("Password deve avere almeno 5 caratteri");
			old.setPasswd(encrypte(dto.getPasswd()));
			if (dto.getPasswd().equals(old.getPasswd()))
				throw new BusinessException("Password nuova uguale alla vecchia");
		}
		old = utenteRepository.save(old);
		if (dto.isAdmin() && dto.getIdEnte() != null) {
			Profilo profilo;
			List<Organizzazione> items = organizzazioneRepository.findByIdEnteAndLivello(dto.getIdEnte(), Livello.ENTE);
			if (items != null) {
				for (Organizzazione o : items) {
					profilo = profiloRepository.findByUtenteIdAndOrganizzazioneIdAndRuolo(old.getId(), o.getId(),
							Ruolo.AMMINISTRATORE);
					if (profilo == null) {
						profilo = new Profilo();
						profilo.setOrganizzazione(o);
						profilo.setRuolo(Ruolo.AMMINISTRATORE);
						profilo.setUtente(old);
						profilo.setAggiunto(false);
						if (dto.getRisorsaUmana() != null && dto.getRisorsaUmana().getId() != null)
							profilo.setRisorsaUmana(
									risorsaUmanaRepository.findById(dto.getRisorsaUmana().getId()).orElse(null));
						profiloRepository.save(profilo);
					}
				}
			}
		}
		return completaUtente(MappingUtenteHelper.mappingToDTO(old));
	}

	@Override
	public void elimina(UtenteDTO dto) throws BusinessException {
		utenteRepository.findById(dto.getId()).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
				"utente_not_found", "Utente non trovato:" + dto.getId()));
		utenteRepository.deleteById(dto.getId());
	}

	@Override

	public void elimina(Long id) throws BusinessException {
		utenteRepository.findById(id).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "utente_not_found", "Utente non trovato:" + id));
		profiloRepository.deleteByUtenteId(id);
		utenteRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public UtenteDTO leggi(Long id) throws BusinessException {
		UtenteDTO utente = MappingUtenteHelper.mappingToDTO(utenteRepository.findById(id).orElse(null));
		return completaUtente(utente);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UtenteDTO> cerca(UtenteDTO parametri) throws BusinessException {
		List<UtenteDTO> list = MappingUtenteHelper
				.mappingItemsToDTO(utenteRepository.findAll(Example.of(MappingUtenteHelper.mappingFromDTO(parametri))));
		if (list != null)
			for (UtenteDTO u : list) {
				u.setPasswd(null);
			}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UtenteVM> list(Long idEnte) throws BusinessException {
		return MappingUtenteHelper.mappingItemsToUtente(utenteRepository.findByIdEnteOrderByNome(idEnte));
	}

	@Override
	@Transactional(readOnly = true)
	public int count(Long idEnte) throws BusinessException {
		return (int) utenteRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public UtenteVM leggiPerUserid(String userid) throws BusinessException {
		UtenteVM out = MappingUtenteHelper.mappingToUtente(utenteRepository.findByUserid(userid));
		if (out == null)
			return null;
		List<Profilo> profili = profiloRepository.findByUtenteId(out.getId());
		if (profili != null) {
			List<ProfiloVM> items = new ArrayList<>();
			for (Profilo p : profili) {
				ProfiloVM vm = Mapping.mapping(p, ProfiloVM.class);
				vm.setIdRisorsa(p.getRisorsaUmana() == null ? null : p.getRisorsaUmana().getId());
				items.add(vm);
			}
			out.setProfili(items);
		}
		return out;
	}

	private UtenteDTO completaUtente(UtenteDTO utente) {
		if (utente != null) {
			utente.setProfili(Mapping.mapping(profiloRepository.findByUtenteId(utente.getId()), ProfiloDTO.class));
		}
		return utente;
	}

	@Override
	public ProfiloDTO aggiungiRuoloUtente(String userid, Long idOrganizzazione, Ruolo ruolo) throws BusinessException {
		Profilo profilo = new Profilo();
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione)
				.orElseThrow(() -> new BusinessException("organizzazione_not_found",
						"organizzazione non trovata:" + idOrganizzazione));
		profilo.setAnno(o.getAnno());
		profilo.setOrganizzazione(o);
		profilo.setRuolo(ruolo);
		profilo.setAggiunto(false);
		profilo.setUtente(utenteRepository.findByUserid(userid));
		Profilo p = profiloRepository.save(profilo);
		final ProfiloDTO out = Mapping.mapping(p, ProfiloDTO.class);
		if (out != null && out.getUtente() != null) {
			out.getUtente().setPasswd(null);
		}
		return out;

	}

	@Override

	public void rimuoviRuoloUtente(String userid, Long idOrganizzazione, Ruolo ruolo) throws BusinessException {
		Utente utente = utenteRepository.findByUserid(userid);
		Profilo profilo = profiloRepository.findByUtenteIdAndOrganizzazioneIdAndRuolo(utente.getId(), idOrganizzazione,
				ruolo);
		if (profilo == null)
			return;
		profiloRepository.deleteById(profilo.getId());

	}

	@Override
	@Transactional(readOnly = true)
	public List<ProfiloDTO> profili(Long idEnte) throws BusinessException {
		return Mapping.mapping(profiloRepository.findByIdEnte(idEnte), ProfiloDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProfiloDTO> profiliUtente(Long idUtente) throws BusinessException {
		return Mapping.mapping(profiloRepository.findByUtenteId(idUtente), ProfiloDTO.class);
	}

	@Override

	public void eliminaProfilo(Long idProfilo) throws BusinessException {
		profiloRepository.deleteById(idProfilo);

	}

	@Override

	public void aggiornaProfilo(ProfiloDTO profilo) throws BusinessException {
		profiloRepository.save(Mapping.mapping(profilo, Profilo.class));

	}

	@Override

	public void creaProfilo(ProfiloDTO profilo) throws BusinessException {
		profilo.setId(null);
		profiloRepository.save(Mapping.mapping(profilo, Profilo.class));

	}

	@Override
	public UtenteDTO cambiaPassword(CambioPasswordRequest request) throws BusinessException {
		Utente user = utenteRepository.findByUserid(request.getUserid());
		if (user == null)
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Userid e/o password errati");
		if (!chekPwd(request.getPassword(), user.getPasswd()))
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Userid e/o password errati");
		if (!request.getNuovaPassword().equals(request.getPasswordRepeat()))
			throw new BusinessException(HttpStatus.BAD_REQUEST, "password_not_valid", "Password non valida");
		user.setPasswd(encrypte(request.getNuovaPassword()));
		user = utenteRepository.save(user);
		return MappingUtenteHelper.mappingToDTO(user);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UtenteSmartVM> search(Long idEnte, Integer anno, Ruolo ruolo, Long idDirezione, String nome,
			Pageable pageable) throws BusinessException {
		Page<Utente> page = null;
		Integer year = anno == null ? LocalDate.now().getYear() : anno;
		page = utenteRepository.search(idEnte, anno, ruolo, idDirezione, nome, pageable);
		Page<UtenteSmartVM> out = page.map(new Function<Utente, UtenteSmartVM>() {
			@Override
			public UtenteSmartVM apply(Utente u) {
				UtenteSmartVM vm = new UtenteSmartVM();
				vm.setAdmin(u.isAdmin());
				vm.setId(u.getId());
				vm.setIdEnte(u.getIdEnte());
				vm.setNome(u.getNome());
				vm.setUsername(u.getUserid());
				
				vm.setCodiceFiscale(u.getCodiceInterno());
				List<Profilo> profili = profiloRepository.findByUtenteId(u.getId());
				if (profili != null) {
					for (Profilo p : profili) {
						if (year.equals(p.getAnno())) {
							if (Boolean.TRUE.equals(p.getAggiunto())) {
								vm.setRuoloAggiunto(p.getRuolo());
								vm.setRuoloAggiuntoLabel(p.getRuolo().label);
								vm.setStrutturaAggiunta(
										p.getOrganizzazione() == null ? "" : p.getOrganizzazione().getCodiceInterno());
							} else {
								vm.setRuolo(p.getRuolo());
								vm.setRuoloLabel(p.getRuolo().label);
								vm.setStruttura(
										p.getOrganizzazione() == null ? "" : p.getOrganizzazione().getCodiceInterno());
								if(p.getRisorsaUmana()!=null) {
									vm.setMatricola(p.getRisorsaUmana().getCodiceInterno());
									vm.setCognomeNome(p.getRisorsaUmana().getCognome()+" "+p.getRisorsaUmana().getNome());
								}
							}
						}
					}
				}
				return vm;
			}
		});
		return out;

	}

	@Override
	public UtenteFlatVM creaUtente(CreaUtenteRequest request) throws BusinessException {
		Integer anno = request.getAnno() == null ? LocalDate.now().getYear() : request.getAnno();
		Utente u = utenteRepository.findByUserid(request.getUsername());
		if (u != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "username_exists", "Username esistente");
		}
		String codiceFiscale = request.getCodiceFiscale() == null ? null
				: request.getCodiceFiscale().trim().toUpperCase();
		if (StringUtils.isNotBlank(codiceFiscale)) {
			Utente tmp = utenteRepository.findByIdEnteAndCodiceInternoIgnoreCase(request.getIdEnte(), codiceFiscale);
			if (tmp != null) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, "internal_code_exists",
						"Codice fiscale esistente:" + codiceFiscale);
			}
		}
		RisorsaUmana r = risorsaUmanaRepository
				.findTopByIdEnteAndAnnoAndCodiceFiscaleOrderByInizioValiditaDescFineValiditaDesc(request.getIdEnte(),
						request.getAnno(), codiceFiscale);
		if (r == null && Ruolo.POSIZIONE_ORGANIZZATIVA.equals(request.getRuolo())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "risorsa_not_found",
					"Per " + Ruolo.POSIZIONE_ORGANIZZATIVA.label
							+ " deve esistere in anagrafica responsabile un elemento con lo stesso codice fiscale");
		}
		u = new Utente();

		u.setAdmin(Ruolo.AMMINISTRATORE.equals(request.getRuolo()));
		u.setIdEnte(request.getIdEnte());
		u.setNome(request.getNome());
		u.setUserid(request.getUsername().trim());
		String pwd = pwd(request.getUsername(),request.getNome(),u.isAdmin());
		u.setPasswd(encrypte(pwd));
		u.setCodiceInterno(codiceFiscale);
		u = utenteRepository.save(u);
		Profilo p = new Profilo();
		Organizzazione o = null;
		if (request.getStruttura() == null) {
			List<Organizzazione> items = organizzazioneRepository.findByIdEnteAndLivello(u.getIdEnte(), Livello.ENTE);
			o = items.get(0);
		} else {
			o = organizzazioneRepository.findById(request.getStruttura())
					.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "struttura_not_found",
							"Struttura non trovata:" + request.getStruttura()));
		}

		p.setAnno(anno);
		p.setOrganizzazione(o);
		p.setRuolo(request.getRuolo());
		p.setUtente(u);
		p.setRisorsaUmana(r);
		p.setAggiunto(false);
		profiloRepository.save(p);

		if (request.getRuoloAggiunto() != null) {
			p = new Profilo();
			o = null;
			if (request.getStrutturaAggiunta() == null) {
				List<Organizzazione> items = organizzazioneRepository.findByIdEnteAndLivello(u.getIdEnte(),
						Livello.ENTE);
				o = items.get(0);
			} else {
				o = organizzazioneRepository.findById(request.getStrutturaAggiunta())
						.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "struttura_not_found",
								"Struttura non trovata:" + request.getStrutturaAggiunta()));
			}

			p.setAnno(anno);
			p.setOrganizzazione(o);
			p.setRuolo(request.getRuoloAggiunto());
			p.setAggiunto(true);
			p.setUtente(u);
			p.setRisorsaUmana(r);
			profiloRepository.save(p);

		}

		final UtenteFlatVM out = new UtenteFlatVM();
		out.setId(u.getId());
		out.setIdEnte(u.getIdEnte());
		out.setNome(u.getNome());
		out.setRisorsa(p.getRisorsaUmana() == null ? null : p.getRisorsaUmana().getId());
		List<Profilo> profili = profiloRepository.findByUtenteId(u.getId());
		if (profili != null) {
			for (Profilo f : profili) {
				if (anno.equals(f.getAnno())) {
					if (Boolean.TRUE.equals(f.getAggiunto())) {
						out.setRuoloAggiunto(f.getRuolo());
						out.setStrutturaAggiunta(f.getOrganizzazione() == null ? null : f.getOrganizzazione().getId());
					} else {
						out.setRuolo(f.getRuolo());
						out.setStruttura(f.getOrganizzazione() == null ? null : f.getOrganizzazione().getId());
					}
				}
			}
		}
		out.setUsername(u.getUserid());
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public UtenteFlatVM leggiUtente(Long id) throws BusinessException {
		Integer anno = LocalDate.now().getYear();
		Utente u = utenteRepository.findById(id).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "utente_not_found", "Utente non trovato:" + id));

		final UtenteFlatVM out = new UtenteFlatVM();
		out.setId(u.getId());
		out.setIdEnte(u.getIdEnte());
		out.setNome(u.getNome());
		out.setCodiceFiscale(u.getCodiceInterno());
		List<Profilo> profili = profiloRepository.findByUtenteId(u.getId());
		if (profili != null) {
			for (Profilo f : profili) {
				if (anno.equals(f.getAnno())) {
					if (Boolean.TRUE.equals(f.getAggiunto())) {
						out.setRuoloAggiunto(f.getRuolo());
						out.setStrutturaAggiunta(f.getOrganizzazione() == null ? null : f.getOrganizzazione().getId());
						out.setRisorsa(f.getRisorsaUmana() == null ? null : f.getRisorsaUmana().getId());
					} else {
						out.setRuolo(f.getRuolo());
						out.setStruttura(f.getOrganizzazione() == null ? null : f.getOrganizzazione().getId());
						out.setRisorsa(f.getRisorsaUmana() == null ? null : f.getRisorsaUmana().getId());
						if(f.getRisorsaUmana()!=null) {
							out.setCategoria(f.getRisorsaUmana().getCategoria().getDescrizione());
							out.setCognomeNome(f.getRisorsaUmana().getCognome()+" "+f.getRisorsaUmana().getNome());
							out.setMatricola(f.getRisorsaUmana().getCodiceInterno());
						}
					}
				}
			}
		}
		out.setUsername(u.getUserid());
		return out;
	}

	@Override
	public void aggiornaUtente(Long id, AggiornaUtenteRequest request) throws BusinessException {
		Integer anno = request.getAnno() == null ? LocalDate.now().getYear() : request.getAnno();
		Utente u = utenteRepository.findById(id).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "user_not_found", "Utente non trovato id:" + id));
		Utente tmp = utenteRepository.findByUserid(request.getUsername());
		if (tmp != null && !tmp.getId().equals(u.getId())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "username_exists", "Username esistente");
		}
		u.setUserid(request.getUsername());
		u.setAdmin(Ruolo.AMMINISTRATORE.equals(request.getRuolo()));
		u.setNome(request.getNome());
		String codiceFiscale = request.getCodiceFiscale() == null ? null
				: request.getCodiceFiscale().trim().toUpperCase();
		if (StringUtils.isNotBlank(codiceFiscale)) {
			tmp= utenteRepository.findByIdEnteAndCodiceInternoIgnoreCase(u.getIdEnte(), codiceFiscale);
			if (tmp != null && !tmp.getId().equals(u.getId())) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, "internal_code_exists",
						"Codice fiscale esistente:" + codiceFiscale);
			}
		}
		RisorsaUmana r = risorsaUmanaRepository
				.findTopByIdEnteAndAnnoAndCodiceFiscaleOrderByInizioValiditaDescFineValiditaDesc(u.getIdEnte(),
						request.getAnno(), codiceFiscale);
		if (r == null && Ruolo.POSIZIONE_ORGANIZZATIVA.equals(request.getRuolo())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "risorsa_not_found",
					"Per " + Ruolo.POSIZIONE_ORGANIZZATIVA.label
							+ " deve esistere in anagrafica responsabile un elemento con lo stesso codice fiscale");
		}
		List<Profilo> profili = profiloRepository.findByUtenteId(u.getId());
		Profilo p = null;
		Profilo pagg = null;
		if (profili != null) {
			for (Profilo f : profili) {
				if (anno.equals(f.getAnno())) {
					if (Boolean.TRUE.equals(f.getAggiunto())) {
						pagg = f;
					} else {
						p = f;
					}
				}
			}
		}
		u.setCodiceInterno(codiceFiscale);
		u = utenteRepository.save(u);
		if (p==null) {
			p = new Profilo();
		}

		Organizzazione o = null;
		if (request.getStruttura() == null) {
			o = organizzazioneRepository.findTopByIdEnteAndAnnoAndLivello(u.getIdEnte(), anno, Livello.ENTE);
		} else {
			o = organizzazioneRepository.findById(request.getStruttura())
					.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "struttura_not_found",
							"Struttura non trovata:" + request.getStruttura()));
		}
		p.setAnno(anno);
		p.setOrganizzazione(o);
		p.setRuolo(request.getRuolo());
		p.setUtente(u);
		p.setRisorsaUmana(r);
		p.setAggiunto(false);
		profiloRepository.save(p);


		if(request.getRuoloAggiunto()==null) {
			if(pagg!=null) {
				profiloRepository.delete(pagg);
			}
			return;
		}
        if(pagg==null) {
        	pagg=new Profilo();
        }
		Organizzazione oagg = null;
		if (request.getStrutturaAggiunta() == null) {
			oagg = organizzazioneRepository.findTopByIdEnteAndAnnoAndLivello(u.getIdEnte(), anno, Livello.ENTE);
		} else {
			oagg = organizzazioneRepository.findById(request.getStrutturaAggiunta())
					.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "struttura_not_found",
							"Struttura aggiunta non trovata:" + request.getStrutturaAggiunta()));
		}
		pagg.setAnno(anno);
		pagg.setOrganizzazione(oagg);
		pagg.setRuolo(request.getRuoloAggiunto());
		pagg.setUtente(u);
		pagg.setRisorsaUmana(r);
		pagg.setAggiunto(true);
		profiloRepository.save(pagg);
	}

	@Override
	public void modificaPassword(@Valid ModificaPasswordRequest request) throws BusinessException {
		Utente u = utenteRepository.findById(request.getIdUtente())
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "user_not_found",
						"Utente non trovato id:" + request.getIdUtente()));

		if (!request.getPassword().equals(request.getPasswordRepeat()))
			throw new BusinessException(HttpStatus.BAD_REQUEST, "password_not_valid", "Password non valida");
		utenteRepository.modificaPassword(request.getIdUtente(), encrypte(request.getPassword()));
	}

	@Override
	@Transactional(readOnly = true)
	public UtenteVM leggiPerCodiceFiscale(String codiceFiscale) throws BusinessException {
		Utente u = utenteRepository.findTopByIdEnteAndCodiceInterno(0l, codiceFiscale);
		if (u == null)
			return null;
		UtenteVM out = MappingUtenteHelper.mappingToUtente(u);
		List<Profilo> profili = profiloRepository.findByUtenteId(out.getId());
		if (profili != null) {
			List<ProfiloVM> items = new ArrayList<>();
			for (Profilo p : profili) {
				ProfiloVM vm = Mapping.mapping(p, ProfiloVM.class);
				vm.setIdRisorsa(p.getRisorsaUmana() == null ? null : p.getRisorsaUmana().getId());
				items.add(vm);
			}
			out.setProfili(items);
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UtenteSmartVM> search(Long idEnte, Integer anno, String nome, Pageable pageable)
			throws BusinessException {
		return search(idEnte, anno, null, null, nome, pageable);
	}
	
	public static String pwd(String username, String nome, boolean admin) {
		if (admin || !isMatricola(username) || StringUtils.isBlank(nome)) {
			return username;
		}
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(nome)) {
			sb.append(nome.substring(0, 1).toUpperCase());
		}
		if (StringUtils.isNotBlank(username)) {
			sb.append(username.trim());
		}
		sb.append('!');
		return sb.toString().toUpperCase();
	}

	public static boolean isMatricola(String s) {
		if (s == null || s.isBlank() || s.length() != 6)
			return false;
		try {
			int n = Integer.parseInt(s);
			return n > 0;
		} catch (Exception e) {
			return false;
		}
	}
}
