package com.finconsgroup.performplus.service.business;

import com.finconsgroup.performplus.service.business.IBusinessEnte;
import com.finconsgroup.performplus.service.dto.ConfigurazioneDTO;

public interface IConfigurazioneBusiness extends IBusinessEnte<ConfigurazioneDTO> {
    public ConfigurazioneDTO leggiPerIdEnteAnno(Long idEnte,Integer anno) ;

	public int getOreMax(Long idEnte, Integer anno);

 }
