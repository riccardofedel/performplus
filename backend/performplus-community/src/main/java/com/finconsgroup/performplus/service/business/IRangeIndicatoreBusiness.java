package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.service.business.IBusinessEnteAnno;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.RangeIndicatoreDTO;

public interface IRangeIndicatoreBusiness extends IBusinessEnteAnno<RangeIndicatoreDTO> {

    public List<RangeIndicatoreDTO> leggiPerIndicatorePiano(Long idIndicatorePiano) throws BusinessException;
    public void deletePerIndicatorePiano(Long idIndicatorePiano) throws BusinessException;
}
