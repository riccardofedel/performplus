package com.finconsgroup.performplus.service.business.pi;

import java.util.List;

import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.CreaRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.ParametriRicercaRegistrazione;
import com.finconsgroup.performplus.rest.api.pi.vm.RegistrazioneVM;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEasyVM;
import com.finconsgroup.performplus.rest.api.vm.RisorsaUmanaSmartVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRegistrazioneBusiness {
	public List<RegistrazioneVM> listPerQuestionario(Long idQuestionario, Integer anno) throws BusinessException;

	public List<RegistrazioneVM> listPerValutatore(Long idValutatore)
			throws BusinessException;

	public List<RegistrazioneVM> listPerValutato(Long idValutato) throws BusinessException;

	public List<RegistrazioneVM> leggi(Long idValutatore, Long idValutato) throws BusinessException;

	public List<RisorsaUmanaSmartVM> getValutati(Long idEnte, Integer anno, String cognome,Long idValutatore, Long idStruttura, Boolean interim, Boolean inattiva)
			throws BusinessException;
	public List<RisorsaUmanaSmartVM> getValutatori(Long idEnte, Integer anno, String cognome,Long idValutato, Long idStruttura, Boolean interim, Boolean inattiva)
			throws BusinessException;
	public List<RisorsaUmanaSmartVM> getValutatoriAll(Long idValutato)
			throws BusinessException;

	public Page<RegistrazioneVM> search(ParametriRicercaRegistrazione filter, Pageable pageable);

	public RegistrazioneVM crea(CreaRegistrazioneRequest request) throws BusinessException;

	public void aggiorna(Long id, AggiornaRegistrazioneRequest request) throws BusinessException;

	public void elimina(Long id) throws BusinessException;

	public RegistrazioneVM leggi(Long id) throws BusinessException;

	public List<RegistrazioneVM> cerca(ParametriRicercaRegistrazione filter) throws BusinessException;
	
	List<DecodificaEasyVM> getQuestionari( Long idEnte) throws BusinessException;
	
	List<DecodificaEasyVM> getRegolamenti( Long idEnte,  Integer anno) throws BusinessException;

	List<DecodificaEasyVM> getStrutture( Long idEnte,  Integer anno, String testo) throws BusinessException;

	public List<RisorsaUmanaSmartVM> getRisorse( Long idEnte,  Integer anno,  String cognome)throws BusinessException;

	public RisorsaUmanaSmartVM getRisorsa(Long idRisorsa)throws BusinessException;

	public boolean verificaValutatore(@Valid Long idRisorsa)throws BusinessException;

	public void undo(Long idRegistrazione);

}
