package com.finconsgroup.performplus.rest.api.vm.v2;

import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class IntroduzioneVM extends EntityVM{

    private List<ElementoIntroduzioneVM> elementi;
    private Long   idPiano;
    
	public IntroduzioneVM idPiano(Long idPiano) {
		setIdPiano(idPiano);
		return this;
	}
}
