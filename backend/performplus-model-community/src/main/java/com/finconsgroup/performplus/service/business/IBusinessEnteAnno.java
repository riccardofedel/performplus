package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.service.business.errors.BusinessException;

public interface IBusinessEnteAnno<T> extends IBusiness<T>{

    public List<T> list(Long idEnte,Integer anno) throws BusinessException;

    public int count(Long idEnte, Integer anno) throws BusinessException;

}
