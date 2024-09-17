package com.finconsgroup.performplus.service.business;

import com.finconsgroup.performplus.service.business.IBusinessEnte;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.ImageEntryDTO;

public interface IImageBusiness extends IBusinessEnte<ImageEntryDTO> {

	public ImageEntryDTO save(ImageEntryDTO imageEntry,
			byte[] imageData)
			throws BusinessException;

	public ImageEntryDTO leggiPerRisorsa(Long idRisorsaUmana)
			throws BusinessException;


	public ImageEntryDTO leggiPerNome(Long idEnte,String nome) throws BusinessException;
}
