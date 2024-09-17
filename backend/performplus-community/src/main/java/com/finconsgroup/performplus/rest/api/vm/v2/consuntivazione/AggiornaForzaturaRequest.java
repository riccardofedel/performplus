package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class AggiornaForzaturaRequest implements Serializable{
	private Double forzatura;
	private String note;
	private Boolean nonValutabile;
}
