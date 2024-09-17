package com.finconsgroup.performplus.rest.api.vm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class AllegatoIndicatorePianoSaveRequest extends AllegatoIndicatorePianoVM{
	private byte[] imageData;
}
