package com.finconsgroup.performplus.repository.advanced;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.enumeration.Fascia;
import com.finconsgroup.performplus.enumeration.TipologiaObiettiviOperativi;
import com.finconsgroup.performplus.rest.api.vm.v2.FiltroAlberoExtendedVM;
import com.finconsgroup.performplus.service.dto.Pesatura;

public interface NodoPianoRepositoryAdvanced {
	List<String> fontiFinanziamentoSql(Long idEnte, Integer anno, String codiceCompleto);

	int modificaPesoRamo(BigDecimal peso, Long idEnte, Integer anno, String codiceCompleto);

	int modificaPeso(Pesatura pesatura);

	int modificaPathFigli(Long idEnte, Integer anno, String vecchio, String nuovo);

	int modificaPeso(Long id, Fascia LivelloStrategicita, Fascia LivelloComplessita,
			TipologiaObiettiviOperativi tipologia, Double peso);
	
	Page<NodoPiano> searchByFilter(FiltroAlberoExtendedVM filter, Pageable pageable );
	
	List<NodoPiano> searchByFilter(FiltroAlberoExtendedVM filter);
	
	int modificaNoteInterne(Long id, String noteInterne);

	BigDecimal pesoTotaleFigli(Long idNodoPiano);
	
	int updateStatoAvanzamento(String statoAvanzamento, Integer period, Long idNodo);
	
	int updatePercentualiForzatura(Long idNodoPiano, Double percentualeForzatura, Double percentualeForzaturaResp);

}
