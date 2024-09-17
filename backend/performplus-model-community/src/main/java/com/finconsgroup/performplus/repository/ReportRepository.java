package com.finconsgroup.performplus.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Report;


/**
 * Spring Data  repository for the Report entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {


	List<Report> findByIdEnteAndAnnoOrderByNome(Long idEnte, Integer anno);

	int countByIdEnteAndAnno(Long idEnte, Integer anno);

	@Query(nativeQuery = true,value="select * from report where id_ente=:idEnte and anno=:anno and  upper(nome) like '%'||upper(:testo)||'%' order by nome"		)
	List<Report> findByIdEnteAndAnnoAndNomeContainsIgnoreCaseOrderByNome(@Param("idEnte")Long idEnte, @Param("anno")Integer anno
			, @Param("testo")String testo);

	List<Report> findByIdEnteAndAnnoAndTipoOrderByNome(Long idEnte, Integer anno, String tipo);


	Report findByIdEnteAndAnnoAndNome(Long idEnte, Integer anno, String nome);

	void deleteByIdEnteAndAnnoAndNome(Long idEnte, Integer anno, String nome);


}
