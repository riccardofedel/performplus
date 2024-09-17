package com.finconsgroup.performplus.rest.api.vm.v2;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class IntroduzioneContenutoResponse extends ElementoIntroduzioneVM{

    private Long   idPiano;
    
	public IntroduzioneContenutoResponse idPiano(Long idPiano) {
		setIdPiano(idPiano);
		return this;
	}
	public IntroduzioneContenutoResponse() {
		super();
	}
	public IntroduzioneContenutoResponse(Long id, String gruppo, String nome, String contenuto) {
		super(id, gruppo, nome, contenuto);
	}
}
