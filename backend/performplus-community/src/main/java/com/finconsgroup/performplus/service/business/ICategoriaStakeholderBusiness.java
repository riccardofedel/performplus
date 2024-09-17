package com.finconsgroup.performplus.service.business;

import com.finconsgroup.performplus.service.business.IBusinessEnte;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.CategoriaStakeholderDTO;

public interface ICategoriaStakeholderBusiness extends IBusinessEnte<CategoriaStakeholderDTO> {
    public String maxCodice(Long idEnte) throws BusinessException;

}
