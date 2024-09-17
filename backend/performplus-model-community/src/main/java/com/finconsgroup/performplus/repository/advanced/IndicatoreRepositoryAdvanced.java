package com.finconsgroup.performplus.repository.advanced;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finconsgroup.performplus.domain.Indicatore;
import com.finconsgroup.performplus.service.dto.RicercaIndicatori;

public interface IndicatoreRepositoryAdvanced {
	List<Indicatore> cerca(RicercaIndicatori parametri);

	Page<Indicatore> search(RicercaIndicatori parametri, Pageable page);

}
