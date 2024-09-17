package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Indicatore;
import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.repository.advanced.IndicatoreRepositoryAdvanced;


/**
 * Spring Data  repository for the Indicatore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndicatoreRepository extends JpaRepository<Indicatore, Long>, JpaSpecificationExecutor<Indicatore> , IndicatoreRepositoryAdvanced{


//	List<Indicatore> findByRaggruppamentoOrderByDenominazione(RaggruppamentoIndicatori raggrupopamento);


	List<Indicatore> findByIdEnteAndRaggruppamentoInOrderByDenominazione(long idEnte, List<RaggruppamentoIndicatori> raggruppamenti);


	List<Indicatore> findByIdEnteAndRaggruppamentoOrderByDenominazione(long idEnte, RaggruppamentoIndicatori raggruppamento);


	int countByIdEnte(long idEnte);


	Indicatore findByIdEnteAndDenominazione(long idEnte, String nome);


	List<Indicatore> findByIdEnte(long idEnte);


	List<Indicatore> findByIdEnteOrderByDenominazione(Long idEnte);


}
