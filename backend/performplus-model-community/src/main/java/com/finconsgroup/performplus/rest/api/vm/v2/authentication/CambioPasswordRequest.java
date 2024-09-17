package com.finconsgroup.performplus.rest.api.vm.v2.authentication;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class CambioPasswordRequest implements Serializable{
	@NotBlank
	private String userid;
	@NotBlank
	private String password;
	@NotBlank
	private String nuovaPassword;
	@NotBlank
	private String passwordRepeat;
}