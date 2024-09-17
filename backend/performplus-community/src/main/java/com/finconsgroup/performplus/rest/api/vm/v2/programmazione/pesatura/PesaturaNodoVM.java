package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura;

import com.finconsgroup.performplus.enumeration.Fascia;
import com.finconsgroup.performplus.enumeration.TipologiaObiettiviOperativi;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.rest.api.vm.v2.EnablingInterface;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Enabling;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class PesaturaNodoVM extends EntityVM implements EnablingInterface{
	private Double peso;
	private Fascia LivelloStrategicita;
	private Fascia LivelloComplessita;
	private TipologiaObiettiviOperativi tipologia;
	private Double pesoLivelloStrategicita;
	private Double pesoLivelloComplessita;
	
	private Enabling enabling;

	public PesaturaNodoVM enabling(Enabling enabling) {
		this.enabling=enabling;
		return this;
	}

}
