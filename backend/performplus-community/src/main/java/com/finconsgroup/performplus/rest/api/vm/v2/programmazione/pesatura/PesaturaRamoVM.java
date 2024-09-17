package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura;

import java.io.Serializable;
import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.v2.EnablingInterface;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Enabling;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class PesaturaRamoVM implements Serializable, EnablingInterface{
	private PesaturaNodoListVM nodo;
	private List<PesaturaNodoListVM> figli;
	private Enabling enabling;

	public PesaturaRamoVM enabling(Enabling enabling) {
		this.enabling=enabling;
		return this;
	}

	
}
