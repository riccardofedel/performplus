package com.finconsgroup.performplus.service.business;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.rest.api.vm.v2.IndicatoreTemplateVM;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.AggiornaIndicatoreRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.CreaIndicatoreRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.IndicatoreDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.IndicatoreListVM;
import com.finconsgroup.performplus.service.business.IBusinessEnte;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.IndicatoreDTO;
import com.finconsgroup.performplus.service.dto.RicercaIndicatori;

public interface IIndicatoreBusiness extends IBusinessEnte<IndicatoreDTO> {

	public List<IndicatoreDTO> cercaPerRaggruppamento(Long idEnte, RaggruppamentoIndicatori raggruppamento)
			throws BusinessException;

	public List<IndicatoreDTO> cerca(RicercaIndicatori parametriRicercaIndicatori) throws BusinessException;

	public IndicatoreDTO leggiPerNome(Long idEnte, String indicatore) throws BusinessException;

	public List<IndicatoreDTO> cercaStrategico() throws BusinessException;

	public List<IndicatoreDTO> cercaSviluppoSostenibile() throws BusinessException;

	public List<IndicatoreDTO> cercaPersonalizzabili() throws BusinessException;

	public Page<IndicatoreListVM> searchIndicatore(Long idEnte, String denominazione, TipoFormula formula,
			RaggruppamentoIndicatori raggruppamento, Pageable pageable) throws BusinessException;

	public IndicatoreDetailVM creaIndicatore(CreaIndicatoreRequest request)throws BusinessException;

	public void aggiornaIndicatore(Long id, AggiornaIndicatoreRequest request)throws BusinessException;

	public IndicatoreDetailVM leggiIndicatore(Long id)throws BusinessException;

	public List<IndicatoreTemplateVM> templates( Long idEnte)throws BusinessException;

}
