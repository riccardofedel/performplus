package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Incarico;


@Repository
public interface IncaricoRepository extends JpaRepository<Incarico, Long>, JpaSpecificationExecutor<Incarico> {

	int countByIdEnte(Long idEnte);

	List<Incarico> findByIdEnte(Long idEnte);
	
	List<Incarico> findByIdEnteOrderByCodice(Long idEnte);

	Incarico findByIdEnteAndCodice(Long idEnte, String codice);

}
