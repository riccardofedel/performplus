package com.finconsgroup.performplus.rest.api.vm.v2.programmazione;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class CambiaResponsabileRequest extends ElencoAzioni{
	@NotNull
	Long idResponsabile;
}
