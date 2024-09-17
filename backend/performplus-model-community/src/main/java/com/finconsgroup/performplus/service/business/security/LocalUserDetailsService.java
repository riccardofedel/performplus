package com.finconsgroup.performplus.service.business.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.finconsgroup.performplus.domain.Utente;
import com.finconsgroup.performplus.repository.UtenteRepository;
import com.finconsgroup.performplus.rest.api.vm.UtenteVM;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.impl.UtenteBusiness;

@Service
@Primary
public class LocalUserDetailsService implements UserDetailsService,
		InitializingBean, MessageSourceAware {

	private static final Logger logger = LoggerFactory.getLogger(LocalUserDetailsService.class);
	
	
	@Autowired
	private IUtenteBusiness utenteBusiness;


	@Override
	public void setMessageSource(MessageSource messageSource) {

	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {

		UtenteVM utente = null;
		try {
			utente = utenteBusiness.leggiPerUserid(username);
		} catch (Exception e) {
			logger.error("Errore nel reperimento dei dati dell utente", e);
			return null;
		}

		return utente;
	}

}
