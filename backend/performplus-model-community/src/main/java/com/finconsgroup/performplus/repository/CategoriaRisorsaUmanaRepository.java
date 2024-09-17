package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.CategoriaRisorsaUmana;
import com.finconsgroup.performplus.domain.Incarico;


/**
 * Spring Data  repository for the CategoriaRisorsaUmana entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaRisorsaUmanaRepository extends JpaRepository<CategoriaRisorsaUmana, Long>, JpaSpecificationExecutor<CategoriaRisorsaUmana> {

	CategoriaRisorsaUmana findByCodice(String codice);

	CategoriaRisorsaUmana findByIdEnteAndCodice(Long idEnte, String codice);

	List<CategoriaRisorsaUmana> findByIdEnte(Long idEnte);

	List<CategoriaRisorsaUmana> findByIdEnteOrderByCodice(Long idEnte);

}
