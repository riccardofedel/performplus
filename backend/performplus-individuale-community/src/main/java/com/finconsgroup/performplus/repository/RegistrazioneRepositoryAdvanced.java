package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.rest.api.pi.vm.ParametriRicercaRegistrazione;

public interface RegistrazioneRepositoryAdvanced {
	public List<Registrazione> cerca(ParametriRicercaRegistrazione filter);
	public Page<Registrazione> search(ParametriRicercaRegistrazione parametri, Pageable page);

}
