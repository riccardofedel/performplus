package com.finconsgroup.performplus.service.business;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.rest.api.vm.UtenteFlatVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteSmartVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.AggiornaUtenteRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.CreaUtenteRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.CambioPasswordRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.ModificaPasswordRequest;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.ProfiloDTO;
import com.finconsgroup.performplus.service.dto.UtenteDTO;

import jakarta.validation.Valid;

public interface IUtenteBusiness extends IBusiness<UtenteDTO> {

	public UtenteVM getUser(String userid, String password) throws BusinessException;

	public UtenteVM leggiPerUserid(String userid) throws BusinessException;

	public ProfiloDTO aggiungiRuoloUtente(String userId, Long idOrganizzazione, Ruolo ruolo) throws BusinessException;

	public void rimuoviRuoloUtente(String userId, Long idOrganizzazione, Ruolo ruolo) throws BusinessException;

	public List<ProfiloDTO> profili(Long idEnte) throws BusinessException;

	public List<ProfiloDTO> profiliUtente(Long idUtente) throws BusinessException;

	public void eliminaProfilo(Long idProfilo) throws BusinessException;

	public void aggiornaProfilo(ProfiloDTO profilo) throws BusinessException;

	public void creaProfilo(ProfiloDTO profilo) throws BusinessException;

	public UtenteDTO cambiaPassword(CambioPasswordRequest request) throws BusinessException;

	public UtenteDTO crea(UtenteDTO dto) throws BusinessException;

	public UtenteDTO aggiorna(UtenteDTO dto) throws BusinessException;

	int count(Long idEnte) throws BusinessException;

	List<UtenteVM> list(Long idEnte) throws BusinessException;

	public Page<UtenteSmartVM> search(Long idEnte, Integer anno, String nome, Pageable pageable)
			throws BusinessException;

	public UtenteFlatVM creaUtente(CreaUtenteRequest utente) throws BusinessException;

	UtenteFlatVM leggiUtente(Long id) throws BusinessException;

	public void aggiornaUtente(Long id, AggiornaUtenteRequest request) throws BusinessException;

	public void modificaPassword(@Valid ModificaPasswordRequest request) throws BusinessException;

	public UtenteVM leggiPerCodiceFiscale(String name);

	Page<UtenteSmartVM> search(Long idEnte, Integer anno, Ruolo ruolo, Long idDirezione, String nome, Pageable pageable)
			throws BusinessException;

}
