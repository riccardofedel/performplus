package com.finconsgroup.performplus.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Ente;


/**
 * Spring Data  repository for the Contesto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuboRepository extends JpaRepository<Ente, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM VIEW_REGISTRAZIONI where ANNO=:anno")
	List<Map<String,Object>> getViewRegistrazioni(@Param("anno") Integer anno);
	
	@Query(nativeQuery = true, value = "SELECT * FROM VIEW_PIAO where ANNO=:anno")
	List<Map<String,Object>> getViewPiao(@Param("anno") Integer anno);
	
	@Query(nativeQuery = true, value = "SELECT * FROM VIEW_INDICATORI where ANNO=:anno")
	List<Map<String,Object>> getViewIndicatori(@Param("anno") Integer anno);

	@Query(nativeQuery = true, value = "SELECT * FROM VIEW_ORGANIGRAMMA where ANNO=:anno")
	List<Map<String,Object>> getViewOrganigramma(@Param("anno") Integer anno);

	@Query(nativeQuery = true, value = "SELECT * FROM VIEW_SCHEDA where ANNO=:anno")
	List<Map<String,Object>> getViewSchede(@Param("anno") Integer anno);

	@Query(nativeQuery = true, value = "SELECT * FROM VIEW_PESATURA where ANNO=:anno")
	List<Map<String,Object>> getViewPesature(@Param("anno") Integer anno);

	@Query(nativeQuery = true, value = "SELECT * FROM VIEW_FVG_STRUTTURE where ANNO=:anno")
	List<Map<String,Object>> getViewFvgStrutture(@Param("anno") Integer anno);

	@Query(nativeQuery = true, value = "SELECT * FROM VIEW_FVG_RESPONSABILI where ANNO=:anno")
	List<Map<String,Object>> getViewFvgResponsabili(@Param("anno") Integer anno);
	
	@Query(nativeQuery = true, value = "SELECT * FROM VIEW_FVG_RISORSE where ANNO=:anno")
	List<Map<String,Object>> getViewFvgRisorse(@Param("anno") Integer anno);

	@Query(nativeQuery = true, value = "SELECT * FROM VIEW_RISORSE where ANNO=:anno")
	List<Map<String, Object>> getViewRisorse(@Param("anno") Integer anno);

	@Query(nativeQuery = true, value = "SELECT * FROM VIEW_UTENTI where ANNO=:anno")
	List<Map<String, Object>> getViewUtenti(@Param("anno") Integer anno);


}
