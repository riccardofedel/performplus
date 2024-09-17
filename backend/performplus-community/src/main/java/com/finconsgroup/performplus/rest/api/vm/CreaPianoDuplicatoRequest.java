package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class CreaPianoDuplicatoRequest implements Serializable {
	private Long idEnte = 0l;
	@NotBlank
	private String codicePiano;

}
