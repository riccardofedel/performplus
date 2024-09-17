package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura;

import java.io.Serializable;

import com.finconsgroup.performplus.enumeration.Fascia;
import com.finconsgroup.performplus.enumeration.TipologiaObiettiviOperativi;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class CalcoloPesaturaObiettivoRequest implements Serializable{
	private Fascia LivelloStrategicita;
	private Fascia LivelloComplessita;
	private TipologiaObiettiviOperativi tipologia;

}
