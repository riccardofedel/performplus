package com.finconsgroup.performplus.manager.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken{


	private static final long serialVersionUID = -8328784369076872609L;

	public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

}
