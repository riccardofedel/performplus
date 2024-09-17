package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.service.business.IBusinessEnte;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.AllegatoIndicatorePianoDTO;
import com.finconsgroup.performplus.service.dto.IndicatorePianoDTO;

public interface IAllegatoIndicatorePianoBusiness extends IBusinessEnte<AllegatoIndicatorePianoDTO> {
	public AllegatoIndicatorePianoDTO save(AllegatoIndicatorePianoDTO allegato,
			byte[] imageData)
			throws BusinessException;


	public List<AllegatoIndicatorePianoDTO> leggiPerIndicatorePiano(IndicatorePianoDTO indicatorePiano) throws BusinessException;
}
