package com.finconsgroup.performplus.service.business.pi;

import java.util.List;

import com.finconsgroup.performplus.rest.api.pi.vm.StatoValutazioneVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;

public interface IValutazioneRegistrazioneBusiness {
	public void eliminaValutazioniRegistrazione(long idRegistrazione) throws BusinessException, Exception;
	public void salvaNuovaValutazione(List<Long> idsValoreAmbito,Long idRegistrazione) throws Exception;
	public StatoValutazioneVM statoValutazione(Long idRegistrazione) throws Exception;
}
