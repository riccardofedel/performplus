package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.service.business.IBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.EnteDTO;

public interface IEnteBusiness extends IBusiness<EnteDTO> {
 
    public List<EnteDTO> list() throws BusinessException;

    public int count() throws BusinessException;

}
