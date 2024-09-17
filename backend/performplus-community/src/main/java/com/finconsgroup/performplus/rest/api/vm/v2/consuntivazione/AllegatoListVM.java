package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import com.finconsgroup.performplus.domain.AllegatoIndicatorePiano;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class AllegatoListVM extends EntityVM{
	private String fileName;
	private String descrizione;
	private String nome;
	private String contentType;
	
	public AllegatoListVM() {}
	public AllegatoListVM(AllegatoIndicatorePiano a) {
		setId(a.getId());
		this.fileName=a.getFileName();
		this.descrizione=a.getDescrizione();
		this.nome=a.getNome();
		this.contentType=a.getContentType();
	}

}
