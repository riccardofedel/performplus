package com.finconsgroup.performplus.service.business.pi;

import java.util.List;

import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaRegolamentoRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.CreaRegolamentoRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.RegolamentoVM;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;

public interface IRegolamentoBusiness {

	public RegolamentoVM leggi(Long idRegolamento) throws BusinessException;

	public List<RegolamentoVM> list(Long idEnte, Integer anno)
			throws BusinessException;
	public List<DecodificaVM> getCategorie(Long idEnte)
			throws BusinessException;

	public List<DecodificaVM> getIncarichi(Long idEnte);

	public RegolamentoVM crea(CreaRegolamentoRequest request) throws BusinessException;

	public void aggiorna(Long id, AggiornaRegolamentoRequest request) throws BusinessException;

	public void elimina(Long id) throws BusinessException;


}
