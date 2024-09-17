package com.finconsgroup.performplus.service.business.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.Configurazione;
import com.finconsgroup.performplus.repository.ConfigurazioneRepository;
import com.finconsgroup.performplus.service.business.IConfigurazioneBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.dto.ConfigurazioneDTO;

@Service
@Transactional
public class ConfigurazioneBusiness implements IConfigurazioneBusiness {
    @Autowired
    private ConfigurazioneRepository configurazioneManager;
 
    @Override
   
    public ConfigurazioneDTO crea(ConfigurazioneDTO dto
	    ) throws BusinessException {
    	dto.setId(null);
	return Mapping.mapping(configurazioneManager.save(Mapping.mapping(dto, Configurazione.class)),ConfigurazioneDTO.class);
    }
    @Override
   
    public ConfigurazioneDTO aggiorna(ConfigurazioneDTO dto
	    ) throws BusinessException {
	return Mapping.mapping(configurazioneManager.save(Mapping.mapping(dto, Configurazione.class)),ConfigurazioneDTO.class);
    }

      @Override
   
    public void elimina(ConfigurazioneDTO dto
	    ) throws BusinessException {
	 configurazioneManager.deleteById(dto.getId());
    }

    @Override
   
    public void elimina(Long id)
	    throws BusinessException {
	 configurazioneManager.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ConfigurazioneDTO leggi(Long id)
	    throws BusinessException {
	return Mapping.mapping(configurazioneManager.findById(id),ConfigurazioneDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurazioneDTO> cerca(ConfigurazioneDTO parametri
	    ) throws BusinessException {
		return Mapping.mapping(
				configurazioneManager.findAll(Example.of(Mapping.mapping(parametri, Configurazione.class))),
				ConfigurazioneDTO.class);
     }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigurazioneDTO> list(Long idEnte)
	    throws BusinessException {
	return Mapping.mapping(configurazioneManager.findByIdEnte(idEnte),ConfigurazioneDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public int count(Long idEnte)
	    throws BusinessException {
	return configurazioneManager.countByIdEnte(idEnte);
    }

    @Override
    @Transactional(readOnly = true)
    public ConfigurazioneDTO leggiPerIdEnteAnno(Long idEnte, Integer anno)
	     {
	return Mapping.mapping(configurazioneManager.findByIdEnteAndAnno(idEnte, anno),ConfigurazioneDTO.class);
	
    }
	@Override
	@Transactional(readOnly = true)
	public int getOreMax(Long idEnte, Integer anno) {
		Optional<Configurazione> optional = configurazioneManager.findByIdEnteAndAnno(idEnte, anno);
		return optional.isEmpty()||optional.get().getMaxOre()==null?30:optional.get().getMaxOre();
	}

 

 
}
