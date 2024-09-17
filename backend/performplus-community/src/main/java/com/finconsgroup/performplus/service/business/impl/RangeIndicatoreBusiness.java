package com.finconsgroup.performplus.service.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.RangeIndicatore;
import com.finconsgroup.performplus.repository.RangeIndicatoreRepository;
import com.finconsgroup.performplus.service.business.IRangeIndicatoreBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.dto.RangeIndicatoreDTO;

@Service
@Transactional
public class RangeIndicatoreBusiness implements IRangeIndicatoreBusiness {
	@Autowired
	private RangeIndicatoreRepository rangeIndicatoreManager;

	@Override
	public RangeIndicatoreDTO crea(RangeIndicatoreDTO dto) throws BusinessException {
		dto.setId(null);
		return Mapping.mapping(rangeIndicatoreManager.save(Mapping.mapping(dto,RangeIndicatore.class)),RangeIndicatoreDTO.class);
	}

	@Override
	public RangeIndicatoreDTO aggiorna(RangeIndicatoreDTO dto) throws BusinessException {
		return Mapping.mapping(rangeIndicatoreManager.save(Mapping.mapping(dto,RangeIndicatore.class)),RangeIndicatoreDTO.class);
	}

	@Override
	public void elimina(RangeIndicatoreDTO dto) throws BusinessException {
		 rangeIndicatoreManager.deleteById(dto.getId());
	}

	@Override
	public void elimina(Long id) throws BusinessException {
		 rangeIndicatoreManager.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public RangeIndicatoreDTO leggi(Long id) throws BusinessException {
		return Mapping.mapping(rangeIndicatoreManager.findById(id),RangeIndicatoreDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RangeIndicatoreDTO> cerca(RangeIndicatoreDTO parametri)
			throws BusinessException {
		return Mapping.mapping(rangeIndicatoreManager.findAll(Example.of(Mapping.mapping(parametri,RangeIndicatore.class))),RangeIndicatoreDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RangeIndicatoreDTO> list(Long idEnte,Integer anno) throws BusinessException {
		return Mapping.mapping(rangeIndicatoreManager.findByIndicatorePianoNodoPianoIdEnteAndIndicatorePianoNodoPianoAnnoAndIndicatorePianoNodoPianoDateDeleteIsNull(idEnte,anno),RangeIndicatoreDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public int count(Long idEnte,Integer anno) throws BusinessException {
		return rangeIndicatoreManager.countByIndicatorePianoNodoPianoIdEnteAndIndicatorePianoNodoPianoAnnoAndIndicatorePianoNodoPianoDateDeleteIsNull(idEnte,anno);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RangeIndicatoreDTO> leggiPerIndicatorePiano(Long idIndicatorePiano)
			throws BusinessException {
		return Mapping.mapping(rangeIndicatoreManager.findByIndicatorePianoId(idIndicatorePiano),RangeIndicatoreDTO.class);
	}

	@Override
	public void deletePerIndicatorePiano(Long idIndicatorePiano) throws BusinessException {
		rangeIndicatoreManager.deleteByIndicatorePianoId(idIndicatorePiano);
	}
}
