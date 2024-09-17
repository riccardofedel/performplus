package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.service.business.errors.BusinessException;

public interface IBusinessEnte<T> extends IBusiness<T> {

	public List<T> list(Long idEnte) throws BusinessException;

	public int count(Long idEnte) throws BusinessException;

}
