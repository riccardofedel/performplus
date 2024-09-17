package com.finconsgroup.performplus.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class ValoreDTO extends AuditEntityDTO {
	private BigDecimal valoreNumerico;
	private BigDecimal valoreNumericoA;
	private BigDecimal valoreNumericoB;
	private Boolean valoreBooleano;
	private Date valoreTemporale;
	private LocalDate inizio;
	private LocalDate scadenza;

	public Integer getNumberInteger() {
		return getValoreNumerico() == null ? 0 : getValoreNumerico().intValue();
	}

	public void setNumberInteger(Integer number) {
		setValoreNumerico(number == null ? null : BigDecimal.valueOf(number));
	}

	public Double getNumberDouble() {
		return getValoreNumerico() == null ? 0d : getValoreNumerico().doubleValue();
	}

	public void setNumberDouble(Double number) {
		setValoreNumerico(number == null ? null : BigDecimal.valueOf(number));
	}

	public Integer getNumberAInteger() {
		return getValoreNumericoA() == null ? 0 : getValoreNumericoA().intValue();
	}

	public void setNumberAInteger(Integer number) {
		setValoreNumericoA(number == null ? null : BigDecimal.valueOf(number));
	}

	public Double getNumberADouble() {
		return getValoreNumericoA() == null ? 0d : getValoreNumericoA().doubleValue();
	}

	public void setNumberADouble(Double number) {
		setValoreNumericoA(number == null ? null : BigDecimal.valueOf(number));
	}

	public Integer getNumberBInteger() {
		return getValoreNumericoB() == null ? 0 : getValoreNumericoB().intValue();
	}

	public void setNumberBInteger(Integer number) {
		setValoreNumericoB(number == null ? null : BigDecimal.valueOf(number));
	}

	public Double getNumberBDouble() {
		return getValoreNumericoB() == null ? 0d : getValoreNumericoB().doubleValue();
	}

	public void setNumberBDouble(Double number) {
		setValoreNumericoB(number == null ? null : BigDecimal.valueOf(number));
	}
}
