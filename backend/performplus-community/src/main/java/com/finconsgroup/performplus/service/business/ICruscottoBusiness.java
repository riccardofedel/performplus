package com.finconsgroup.performplus.service.business;

import com.finconsgroup.performplus.rest.api.vm.v2.cruscotto.CruscottoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.cruscotto.StatisticheCruscottoVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;

public interface ICruscottoBusiness  {

	public CruscottoVM getCruscotto(Long idEnte,Integer anno)
			throws BusinessException;

	public StatisticheCruscottoVM statistiche(Long idEnte,Integer anno) throws BusinessException;

	CruscottoVM crea(CruscottoVM dto) throws BusinessException;

	void elimina(Long id) throws BusinessException;

	CruscottoVM leggi(Long id) throws BusinessException;

	CruscottoVM aggiorna(CruscottoVM dto) throws BusinessException;

}
