package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.SchedaValutato;


@Repository
public interface SchedaValutatoRepository extends JpaRepository<SchedaValutato, Long>, JpaSpecificationExecutor<SchedaValutato> {

	List<SchedaValutato> findByIdEnteAndAnnoOrderByCodiceUnivoco(Long idEnte, Integer anno);
	SchedaValutato findByIdEnteAndAnnoAndCodiceUnivoco(Long idEnte, Integer anno,String codiceUnivoco);
	

}
