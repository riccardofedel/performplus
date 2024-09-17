package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.TipologiaRisorsaStrumentale;




/**
 * Spring Data  repository for the TipologiaRisorsaStr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipologiaRisorsaStrumentaleRepository extends JpaRepository<TipologiaRisorsaStrumentale, Long>, JpaSpecificationExecutor<TipologiaRisorsaStrumentale> {

	TipologiaRisorsaStrumentale findByCodice(String codice);


	@Query(value = "SELECT max(t.codice) FROM TipologiaRisorsaStrumentale t where t.idEnte=:idEnte")
	String maxByCodice(@Param("idEnte")Long idEnte);


	List<TipologiaRisorsaStrumentale> findByIdEnteOrderByCodice(Long idEnte);


	int countByIdEnte(Long idEnte);


	TipologiaRisorsaStrumentale findByIdEnteAndCodice(Long idEnte, String codice);

}
