package com.finconsgroup.performplus.rest.api.vm.v2;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class ElementoIntroduzioneVM extends EntityVM{
	private String gruppo;
	private String nome;
	private String contenuto;
	public ElementoIntroduzioneVM() {}
	public ElementoIntroduzioneVM(Long id,String gruppo, String nome, String contenuto) {
		super(id);
		this.gruppo = gruppo;
		this.nome = nome;
		this.contenuto = contenuto;
	}

}
