package com.finconsgroup.performplus.service.business;

import com.finconsgroup.performplus.service.business.IBusinessEnteAnno;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.PrioritaOperativaDTO;

public interface IPrioritaOperativaBusiness extends
	IBusinessEnteAnno<PrioritaOperativaDTO> {

    public PrioritaOperativaDTO getPrioritaOperativa(Long idOrganizzazione,
	    Long idNodo)
	    throws BusinessException;

	void clona(Long idNodo, Long idClone) throws BusinessException;
    

}
