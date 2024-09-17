package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.RisorsaStrumentale;


/**
 * Spring Data  repository for the RisorsaStrumentale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RisorsaStrumentaleRepository extends JpaRepository<RisorsaStrumentale, Long>, JpaSpecificationExecutor<RisorsaStrumentale> {

	List<RisorsaStrumentale> findByIdEnteAndAnno(Long idEnte, int anno);

	int countByIdEnteAndAnno(Long idEnte, int anno);

	RisorsaStrumentale findByIdEnteAndAnnoAndCodice(Long idEnte,int anno, String codice);

	@Query(value = "SELECT max(r.codice) FROM RisorsaStrumentale r where r.idEnte=:idEnte and r.anno=:anno")
	String maxCodice(@Param("idEnte") Long idEnte, @Param("anno")int anno);

	@Query(nativeQuery = true,value="select * from risorsa_strumentale where id_ente=:idEnte and anno=:anno and  upper(descrizione) like '%'||upper(:testo)||'%'"		)
	List<RisorsaStrumentale> findByIdEnteAndAnnoAndDescrizioneContainsIgnoreCase(@Param("idEnte")Long idEnte, @Param("anno")int anno, @Param("testo")String testo);

	List<RisorsaStrumentale> findByIdEnte(Long idEnte);

	int countByIdEnte(Long idEnte);

	@Query(value = "SELECT t FROM RisorsaStrumentale t where t.idEnte=:idEnte and t.anno=:anno"
	+" and not exists (select p.id from RisorsaStrumentaleOrganizzazione p where p.organizzazione.id=:idOrganizzazione and p.risorsaStrumentale.id=t.id)")
	List<RisorsaStrumentale> cercaNonAssociate(@Param("idEnte")Long idEnte,@Param("anno")Integer anno, @Param("idOrganizzazione")Long idOrganizzazione);

}
