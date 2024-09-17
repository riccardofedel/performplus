package com.finconsgroup.performplus.service.business;

import com.finconsgroup.performplus.service.business.IBusinessEnte;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.AllegatoDTO;

public interface IAllegatoBusiness extends IBusinessEnte<AllegatoDTO> {
	public AllegatoDTO save(AllegatoDTO allegato,
			byte[] imageData)
			throws BusinessException;


	public AllegatoDTO leggiPerNome(Long idEnte,String nome) throws BusinessException;
}
