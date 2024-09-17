package com.finconsgroup.performplus.service.business.pi;

import java.util.List;

import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaPesiRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaPesoIndicatorePianoRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaPesoNodoPianoRegistrazioneRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.PesoIndicatoreRegistrazioneVM;
import com.finconsgroup.performplus.rest.api.pi.vm.PesoNodoPianoRegistrazioneVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;

public interface IPesoNodoPianoIndicatoreBusiness {


	public void aggiornaPesoNodo(AggiornaPesoNodoPianoRegistrazioneRequest request) throws BusinessException;

	public void aggiornaPesoIndicatore(AggiornaPesoIndicatorePianoRegistrazioneRequest request) throws BusinessException;

	public List<PesoNodoPianoRegistrazioneVM> elenco(Long idValutatore, Long idValutato, Long idRegistrazione)throws BusinessException;

	public void aggiornaPesi(AggiornaPesiRegistrazioneRequest request)throws BusinessException;

	public PesoNodoPianoRegistrazioneVM prioritaNodo( Long id,  Long idRegistrazione,  Long idNodo)throws BusinessException;

	public PesoIndicatoreRegistrazioneVM prioritaIndicatore( Long id,  Long idRegistrazione,  Long idIndicatorePiano)throws BusinessException;


}
