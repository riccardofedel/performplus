package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.CategoriaStakeholder;


/**
 * Spring Data  repository for the CategoriaStakeholder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaStakeholderRepository extends JpaRepository<CategoriaStakeholder, Long>, JpaSpecificationExecutor<CategoriaStakeholder> {

	@Query(value = "SELECT max(c.codice) FROM CategoriaStakeholder c where c.idEnte=:idEnte")
	String maxCodice(@Param("idEnte") Long idEnte);

	int countByIdEnte(Long idEnte);
	
	List<CategoriaStakeholder> findByIdEnte(Long idEnte);

}
