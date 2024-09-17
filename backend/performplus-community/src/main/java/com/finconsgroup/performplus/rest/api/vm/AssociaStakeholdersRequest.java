package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class AssociaStakeholdersRequest implements Serializable {
	@NotNull
	private Long idNodo;
	@NotEmpty
	private List<Long> items;
}
