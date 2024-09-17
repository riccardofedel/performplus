package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.ContrattoRisorsaUmana;
import com.finconsgroup.performplus.domain.Incarico;


/**
 * Spring Data  repository for the ContrattoRisorsaUmana entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContrattoRisorsaUmanaRepository extends JpaRepository<ContrattoRisorsaUmana, Long>, JpaSpecificationExecutor<ContrattoRisorsaUmana> {

	ContrattoRisorsaUmana findByIdEnteAndCodice(Long idEnte, String codice);

	List<ContrattoRisorsaUmana> findByIdEnte(Long idEnte);

	List<ContrattoRisorsaUmana> findByIdEnteOrderByCodice(Long idEnte);

}
