package com.finconsgroup.performplus.service.business;

import com.finconsgroup.performplus.service.business.IBusinessEnte;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.TipologiaRisorsaStrumentaleDTO;

public interface ITipologiaRisorsaStrumentaleBusiness extends IBusinessEnte<TipologiaRisorsaStrumentaleDTO> {
    public String maxCodice(Long idEnte) throws BusinessException;

    public TipologiaRisorsaStrumentaleDTO leggiPerCodice(Long idEnte,
	    String codice)throws BusinessException;

}
