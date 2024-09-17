package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.KeyValues;
import com.finconsgroup.performplus.rest.api.vm.v2.AggiornaIntroduzioneRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.CreaIntroduzioneRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.IntroduzioneContenutoResponse;
import com.finconsgroup.performplus.rest.api.vm.v2.IntroduzioneVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.ContestoDTO;

public interface IContestoBusiness  {

	public ContestoDTO getContesto(Long idEnte,int anno,boolean isRelazione)
			throws BusinessException;

	public ContestoDTO leggiPerPiano(Long idPiano, boolean isRelazione)
			throws BusinessException;

//	void clona(Long idPiano, Long idPianoClone, boolean isRelazione) throws BusinessException;

	
	public IntroduzioneVM readIntroduzione(Long id)throws BusinessException;

	public IntroduzioneVM creaIntroduzione( CreaIntroduzioneRequest request)throws BusinessException;

	public void aggiornaIntroduzione(Long id, AggiornaIntroduzioneRequest request)throws BusinessException;

	public IntroduzioneVM leggiIntroduzione( Long idEnte,  Integer anno)throws BusinessException;

	public void clona(Long idPiano, Long idPianoClone, boolean isRelazione)throws BusinessException;

	public List<KeyValues> getIntroduzioneElementi()throws BusinessException;

	public IntroduzioneContenutoResponse leggiIntroduzioneContenuto( Long idEnte,  Integer anno,
			String gruppo, String nome)throws BusinessException;

}
