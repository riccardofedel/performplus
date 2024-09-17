package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.v2.attivitagestionali.AggiornaAttivitaGestionaleRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.attivitagestionali.AttivitaGestionaleDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.attivitagestionali.AttivitaGestionaleVM;
import com.finconsgroup.performplus.rest.api.vm.v2.attivitagestionali.CreaAttivitaGestionaleRequest;
import com.finconsgroup.performplus.service.business.errors.BusinessException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAttivitaGestionaleBusiness {
	List<AttivitaGestionaleVM> search(Long idEnte, Integer anno, String testo)throws BusinessException;

	Page<AttivitaGestionaleVM> search( Long idEnte, Integer anno, String testo, Pageable pageable)throws BusinessException;

	AttivitaGestionaleDetailVM leggi( Long id)throws BusinessException;

	AttivitaGestionaleDetailVM crea( CreaAttivitaGestionaleRequest request)throws BusinessException;

	void aggiorna(Long id, AggiornaAttivitaGestionaleRequest request)throws BusinessException;

	void elimina( Long id)throws BusinessException;

}
