package com.finconsgroup.performplus.manager.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.finconsgroup.performplus.rest.api.vm.UtenteVM;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private IUtenteBusiness utenteBusiness;

   @Override
    public Authentication authenticate(Authentication auth) 
      throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials()
            .toString();
 
        UtenteVM u = utenteBusiness.getUser(username, password);


        if (u==null) {
             throw new BadCredentialsException("External system authentication failed");
        }else {
        	return new CustomUsernamePasswordAuthenticationToken
                    (username, username, new ArrayList<>());
        }
    }
 
    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(CustomUsernamePasswordAuthenticationToken.class);
    }
}
