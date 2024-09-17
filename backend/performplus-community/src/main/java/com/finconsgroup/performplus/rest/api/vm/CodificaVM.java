package com.finconsgroup.performplus.rest.api.vm;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class CodificaVM extends EntityVM{
    private String descrizione;
    
    public CodificaVM() {}
	public CodificaVM(Long id, String descrizione) {
		super();
		setId(id);
		this.descrizione = descrizione;
	}
    
}
