package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.enumeration.TipoVariazione;
import com.finconsgroup.performplus.service.business.IBusinessEnteAnno;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.VariazioneAttributoDTO;

public interface IVariazioneAttributoBusiness extends
		IBusinessEnteAnno<VariazioneAttributoDTO> {

	public List<VariazioneAttributoDTO> modifiche(Long idEnte, Long idPiano,
			String oggetto, String attributo, String identificativo)
			throws BusinessException;

	public List<VariazioneAttributoDTO> modifiche(
			VariazioneAttributoDTO variazione) throws BusinessException;

	public List<VariazioneAttributoDTO> modifiche(Long idEnte, Long idPiano,
			Object dto) throws BusinessException;

	public void aggiorna(Long idEnte, Long idPiano, Object dto, Object old,
			TipoVariazione tipoVariazione)
			throws BusinessException;

	public boolean hasModifiche(Long idVariazione)
			throws BusinessException;

	public int countPerVariazione(Long idVariazione)
			throws BusinessException;

	public List<VariazioneAttributoDTO> list(Long idEnte, Long idPiano)
			throws BusinessException;

}
