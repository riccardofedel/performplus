package com.finconsgroup.performplus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finconsgroup.performplus.domain.Utente;
import com.finconsgroup.performplus.enumeration.Ruolo;

public interface UtenteRepositoryAdvanced {
	int modificaPassword(Long idUtente, String pwd);
	Page<Utente> search(Long idEnte, Integer anno, Ruolo ruolo, Long idDirezione, String name, Pageable pageable);

}
