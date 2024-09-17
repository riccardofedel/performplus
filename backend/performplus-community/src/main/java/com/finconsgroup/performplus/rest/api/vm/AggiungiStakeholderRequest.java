package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class AggiungiStakeholderRequest implements Serializable{
	@NotNull
	 private Long idNodo;
	@NotNull
	 private Long  idStakeholder;
}
