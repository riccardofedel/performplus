package com.finconsgroup.performplus.service.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.Ente;
import com.finconsgroup.performplus.repository.EnteRepository;
import com.finconsgroup.performplus.service.business.IEnteBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.dto.EnteDTO;

@Service
@Transactional
public class EnteBusiness implements IEnteBusiness {
	@Autowired
	private EnteRepository enteManager;

	@Override
	public EnteDTO crea(EnteDTO dto) throws BusinessException {
		dto.setId(null);
		return Mapping.mapping(enteManager.save(Mapping.mapping(dto, Ente.class)), EnteDTO.class);
	}

	@Override
	public EnteDTO aggiorna(EnteDTO dto) throws BusinessException {
		return Mapping.mapping(enteManager.save(Mapping.mapping(dto, Ente.class)), EnteDTO.class);
	}

	@Override
	public void elimina(EnteDTO dto) throws BusinessException {
		enteManager.deleteById(dto.getId());
	}

	@Override
	public void elimina(Long id) throws BusinessException {
		enteManager.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public EnteDTO leggi(Long id) throws BusinessException {
		return Mapping.mapping(enteManager.findById(id), EnteDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EnteDTO> cerca(EnteDTO parametri) throws BusinessException {
		return Mapping.mapping(enteManager.findAll(Example.of(Mapping.mapping(parametri, Ente.class))), EnteDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EnteDTO> list() throws BusinessException {
		return Mapping.mapping(enteManager.findAll(Sort.by("nome")), EnteDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public int count() throws BusinessException {
		return (int) enteManager.count();
	}

}
