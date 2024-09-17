package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Regolamento;


@Repository
public interface RegolamentoRepository extends JpaRepository<Regolamento, Long>, JpaSpecificationExecutor<Regolamento> {

	List<Regolamento> findByIdEnteAndAnno(Long idEnte, Integer anno);

	Regolamento findByIdEnteAndAnnoAndIntestazione(Long idEnte, Integer anno, String intestazione);

	List<Regolamento> findByIdEnteAndAnnoOrderByIntestazione(Long idEnte, Integer anno);

	long countByAnno(Integer anno);


}
