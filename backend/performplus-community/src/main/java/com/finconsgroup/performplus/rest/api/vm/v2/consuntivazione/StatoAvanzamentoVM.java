package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class StatoAvanzamentoVM implements Serializable{
	Integer periodo;
	String descPeriodo;
	Long idNodoPiano;
	String statoAvanzamento;
}
