package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Incarico;
import com.finconsgroup.performplus.domain.ProfiloRisorsaUmana;


/**
 * Spring Data  repository for the ProfiloRisorsaUmana entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfiloRisorsaUmanaRepository extends JpaRepository<ProfiloRisorsaUmana, Long>, JpaSpecificationExecutor<ProfiloRisorsaUmana> {


	ProfiloRisorsaUmana findByIdEnteAndCodice(Long idEnte, String codice);

	List<ProfiloRisorsaUmana> findByIdEnte(Long idEnte);

	List<ProfiloRisorsaUmana> findByIdEnteOrderByCodice(Long idEnte);


}
