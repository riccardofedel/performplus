package com.finconsgroup.performplus.repository.advanced;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finconsgroup.performplus.domain.RisorsaUmanaOrganizzazione;
import com.finconsgroup.performplus.service.dto.RicercaRisorseUmaneOrg;


public interface RisorsaUmanaOrganizzazioneRepositoryAdvanced{

	int modificaDisponibilita(Integer disponibilita, Long idRisorsaUmanaOrganizzazione);
	
	Page<RisorsaUmanaOrganizzazione> search(RicercaRisorseUmaneOrg parametri, Pageable pageable);

	List<RisorsaUmanaOrganizzazione> list(RicercaRisorseUmaneOrg parametri);

}
