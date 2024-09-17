package com.finconsgroup.performplus.rest.api.vm;

import com.finconsgroup.performplus.service.dto.ImageEntryDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class ImageEntrySaveRequest extends ImageEntryDTO{
	private byte[] imageData;

}
