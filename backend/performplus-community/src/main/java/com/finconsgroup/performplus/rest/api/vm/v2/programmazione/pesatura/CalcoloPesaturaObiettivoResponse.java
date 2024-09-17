package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class CalcoloPesaturaObiettivoResponse implements Serializable{
	private Double pesoLivelloStrategicita;
	private Double pesoLivelloComplessita;
	private Double peso;

}
