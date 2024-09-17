package com.finconsgroup.performplus.rest.api.vm.v2.programmazione;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ElencoAzioni {
	@NotNull
	@NotEmpty
	List<Long> azioni;
}
