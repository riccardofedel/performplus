package com.finconsgroup.performplus.rest.api.vm.v2.authentication;

import java.io.Serializable;

import lombok.Data;

@SuppressWarnings("serial")
@Data


public class JwtAuthenticationRequest implements Serializable{

	private String username;
	private String password;


}
