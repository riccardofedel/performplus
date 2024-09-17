package com.finconsgroup.performplus.repository.advanced;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.service.dto.RicercaRisorseUmane;

public interface RisorsaUmanaRepositoryAdvanced {
	List<RisorsaUmana> cerca(RicercaRisorseUmane parametri);

	Page<RisorsaUmana> search(RicercaRisorseUmane parametri, Pageable page);

}
