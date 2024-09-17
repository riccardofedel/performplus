package com.finconsgroup.performplus.rest.api.vm.v2.authentication;

import java.io.Serializable;
import java.util.Collection;

import lombok.Data;

@SuppressWarnings("serial")
@Data


public class JwtAuthenticationResponse implements Serializable{

	private String username;
	private Collection<?> authorities;
	private String token;
	private Long idEnte;
	
	public JwtAuthenticationResponse() {}
	public JwtAuthenticationResponse(String username, Collection<?> authorities) {
		super();
		this.username = username;
		this.authorities = authorities;
	}
	
	public JwtAuthenticationResponse token(String token) {
		setToken(token);
		return this;
	}
	public JwtAuthenticationResponse idEnte(Long idEnte) {
		setIdEnte(idEnte);
		return this;
	}
}
