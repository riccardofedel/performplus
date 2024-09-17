package com.finconsgroup.performplus.rest.api.pi.vm;

import java.math.BigDecimal;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ValoreAmbitoVM extends EntityVM{
	private BigDecimal peso;
	private String codice;
	private String intestazione;
	private String descrizione;

}
