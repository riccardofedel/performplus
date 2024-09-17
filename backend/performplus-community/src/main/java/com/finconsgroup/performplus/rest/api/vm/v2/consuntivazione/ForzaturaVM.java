package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class ForzaturaVM extends EntityVM{
	
	private Double richiestaForzatura;
	private String richiestaNote;
	private Double forzatura;
	private String note;
	private Boolean nonValutabile; 

}
