package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@SuppressWarnings("serial")
@Data

public class UtilizzoRisorsaDTO implements Serializable{
	private NodoPianoDTO nodoPiano;
	private RisorsaUmanaDTO risorsaUmana;
	private BigDecimal priorita;

}
