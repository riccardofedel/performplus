package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.service.business.errors.BusinessException;

public interface IBusiness<T> {

    public T crea(T dto) throws BusinessException;

    public T aggiorna(T dto)
	    throws BusinessException;

    public void elimina(T dto)
	    throws BusinessException;

    public void elimina(Long id)
	    throws BusinessException;

    public T leggi(Long id)
	    throws BusinessException;

    public List<T> cerca(T dto)
	    throws BusinessException;
    
}
