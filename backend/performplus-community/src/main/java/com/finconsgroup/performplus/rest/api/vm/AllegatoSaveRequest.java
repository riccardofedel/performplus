package com.finconsgroup.performplus.rest.api.vm;

import com.finconsgroup.performplus.service.dto.AllegatoDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class AllegatoSaveRequest extends AllegatoDTO{
	private byte[] imageData;

}
