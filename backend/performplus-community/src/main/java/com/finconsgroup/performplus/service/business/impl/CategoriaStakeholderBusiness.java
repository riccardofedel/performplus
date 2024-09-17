package com.finconsgroup.performplus.service.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.CategoriaStakeholder;
import com.finconsgroup.performplus.repository.CategoriaStakeholderRepository;
import com.finconsgroup.performplus.service.business.ICategoriaStakeholderBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.dto.CategoriaStakeholderDTO;

@Service
@Transactional
public class CategoriaStakeholderBusiness implements ICategoriaStakeholderBusiness {
    @Autowired
    private CategoriaStakeholderRepository categoriaStakeholderManager;


    @Override
    
    public CategoriaStakeholderDTO crea(CategoriaStakeholderDTO dto)
	    throws BusinessException {
    	dto.setId(null);
	return Mapping.mapping(categoriaStakeholderManager.save(Mapping.mapping(dto,CategoriaStakeholder.class)),CategoriaStakeholderDTO.class);
    }

    @Override
    
    public CategoriaStakeholderDTO aggiorna(CategoriaStakeholderDTO dto
	    ) throws BusinessException {
	return Mapping.mapping(categoriaStakeholderManager.save(Mapping.mapping(dto,CategoriaStakeholder.class)),CategoriaStakeholderDTO.class);
    }

    @Override
    
    public void elimina(CategoriaStakeholderDTO dto)
	    throws BusinessException {
	 categoriaStakeholderManager.deleteById(dto.getId());
    }

    @Override
    
    public void elimina(Long id)
	    throws BusinessException {
	 categoriaStakeholderManager.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaStakeholderDTO leggi(Long id)
	    throws BusinessException {
	return Mapping.mapping(categoriaStakeholderManager.findById(id),CategoriaStakeholderDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaStakeholderDTO> cerca(CategoriaStakeholderDTO parametri
	    ) throws BusinessException {
		return Mapping.mapping(
				categoriaStakeholderManager.findAll(Example.of(Mapping.mapping(parametri, CategoriaStakeholder.class))),
				CategoriaStakeholderDTO.class);
   }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaStakeholderDTO> list(Long idEnte)
	    throws BusinessException {
	return Mapping.mapping(categoriaStakeholderManager.findByIdEnte(idEnte),CategoriaStakeholderDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public int count(Long idEnte) throws BusinessException {
	return categoriaStakeholderManager.countByIdEnte(idEnte);
    }

    @Override
    @Transactional(readOnly = true)
    public String maxCodice(Long idEnte)
	    throws BusinessException {
	return categoriaStakeholderManager.maxCodice(idEnte);
    }



}
