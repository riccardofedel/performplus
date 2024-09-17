package com.finconsgroup.performplus.rest.api.vm.v2.authentication;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class ModificaPasswordRequest implements Serializable{
	@NotNull
	private Long idUtente;
	@NotBlank
	private String password;
	@NotBlank
	private String passwordRepeat;
}