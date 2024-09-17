package com.finconsgroup.performplus.service.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.TipologiaRisorsaStrumentale;
import com.finconsgroup.performplus.repository.TipologiaRisorsaStrumentaleRepository;
import com.finconsgroup.performplus.service.business.ITipologiaRisorsaStrumentaleBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.dto.TipologiaRisorsaStrumentaleDTO;

@Service
@Transactional
public class TipologiaRisorsaStrumentaleBusiness implements ITipologiaRisorsaStrumentaleBusiness {
	@Autowired
	private TipologiaRisorsaStrumentaleRepository tipologiaRisorsaStrumentaleManager;

	@Override

	public TipologiaRisorsaStrumentaleDTO crea(TipologiaRisorsaStrumentaleDTO dto) throws BusinessException {
		dto.setId(null);
		return Mapping.mapping(tipologiaRisorsaStrumentaleManager.save(Mapping.mapping(dto, TipologiaRisorsaStrumentale.class)), TipologiaRisorsaStrumentaleDTO.class);
	}

	@Override

	public TipologiaRisorsaStrumentaleDTO aggiorna(TipologiaRisorsaStrumentaleDTO dto) throws BusinessException {
		return Mapping.mapping(tipologiaRisorsaStrumentaleManager.save(Mapping.mapping(dto, TipologiaRisorsaStrumentale.class)), TipologiaRisorsaStrumentaleDTO.class);
	}

	@Override

	public void elimina(TipologiaRisorsaStrumentaleDTO dto) throws BusinessException {
		tipologiaRisorsaStrumentaleManager.deleteById(dto.getId());
	}

	@Override

	public void elimina(Long id) throws BusinessException {
		tipologiaRisorsaStrumentaleManager.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public TipologiaRisorsaStrumentaleDTO leggi(Long id) throws BusinessException {
		return Mapping.mapping(tipologiaRisorsaStrumentaleManager.findById(id), TipologiaRisorsaStrumentaleDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TipologiaRisorsaStrumentaleDTO> cerca(TipologiaRisorsaStrumentaleDTO parametri) throws BusinessException {
		return Mapping.mapping(tipologiaRisorsaStrumentaleManager.findAll(Example.of(Mapping.mapping(parametri, TipologiaRisorsaStrumentale.class))),
				TipologiaRisorsaStrumentaleDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TipologiaRisorsaStrumentaleDTO> list(Long idEnte) throws BusinessException {
		return Mapping.mapping(tipologiaRisorsaStrumentaleManager.findByIdEnteOrderByCodice(idEnte), TipologiaRisorsaStrumentaleDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public int count(Long idEnte) throws BusinessException {
		return tipologiaRisorsaStrumentaleManager.countByIdEnte(idEnte);
	}

	@Override
	@Transactional(readOnly = true)
	public TipologiaRisorsaStrumentaleDTO leggiPerCodice(Long idEnte,String codice) throws BusinessException {
		return Mapping.mapping(tipologiaRisorsaStrumentaleManager.findByIdEnteAndCodice(idEnte,codice), TipologiaRisorsaStrumentaleDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public String maxCodice(Long idEnte) throws BusinessException {
		return tipologiaRisorsaStrumentaleManager.maxByCodice(idEnte);
	}

}