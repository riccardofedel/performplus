package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.RisorsaStrumentaleOrganizzazione;


/**
 * Spring Data  repository for the RisorsaStrumentaleOrg entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RisorsaStrumentaleOrganizzazioneRepository extends JpaRepository<RisorsaStrumentaleOrganizzazione, Long>, JpaSpecificationExecutor<RisorsaStrumentaleOrganizzazione> {

	List<RisorsaStrumentaleOrganizzazione> findByOrganizzazioneIdEnteAndOrganizzazioneAnno(Long idEnte, Integer anno);

	List<RisorsaStrumentaleOrganizzazione> findByOrganizzazioneId(Long idOrganizzazione);

	List<RisorsaStrumentaleOrganizzazione> findByOrganizzazioneIdEnteAndOrganizzazioneAnnoAndOrganizzazioneCodiceCompletoStartsWith(long idEnte,int anno, String codiceCompleto);

	List<RisorsaStrumentaleOrganizzazione> findByRisorsaStrumentaleId(Long idRisorsaStrumentale);

	RisorsaStrumentaleOrganizzazione findByRisorsaStrumentaleIdAndOrganizzazioneId(Long idRisorsaStrumentale, Long idOrganizzazione);

	List<RisorsaStrumentaleOrganizzazione> findByOrganizzazioneIdIn(List<Long> items);

	@Query(nativeQuery = true,value="select r.* from risorsa_strumentale_org r join organizzazione o on o.id=r.organizzazione_id where o.id=:idOrganizzazione and upper(r.descrizione) like '%'||upper(:testo)||'%'"		)
	List<RisorsaStrumentaleOrganizzazione> findByOrganizzazioneIdAndRisorsaStrumentaleDescrizioneContainsIgnoreCase(@Param("idOrganizzazione")Long idOrganizzazione, @Param("testo")String testo);

	List<RisorsaStrumentaleOrganizzazione> findByOrganizzazioneIdInAndRisorsaStrumentaleAnno(List<Long> items, Integer anno);

	@Query(nativeQuery = true,value="select r.* from risorsa_strumentale_org r join organizzazione o on o.id=r.organizzazione_id where o.id=:idOrganizzazione and r.anno=:anno and upper(r.descrizione) like '%'||upper(:testo)||'%'"		)
	List<RisorsaStrumentaleOrganizzazione> findByOrganizzazioneIdInAndRisorsaStrumentaleAnnoAndRisorsaStrumentaleDescrizioneContainsIgnoreCase(
			@Param("idOrganizzazione")Long idOrganizzazione, @Param("anno") Integer anno, @Param("testo")String testo);

	List<RisorsaStrumentaleOrganizzazione> findByOrganizzazioneIdOrderByRisorsaStrumentaleCodice(Long idOrganizzazione);

	RisorsaStrumentaleOrganizzazione findByOrganizzazioneIdAndRisorsaStrumentaleCodice(Long idOrganizzazione, String codiceRisorsaStrumentale);

	@Query("SELECT SUM(m.disponibilita) FROM RisorsaStrumentaleOrganizzazione m where m.risorsaStrumentale.id=:idRisorsa")
	Integer utilizzo(@Param("idRisorsa") Long idRisorsa);

	@Query(nativeQuery = true,value="select r.* from risorsa_strumentale_org r join organizzazione o on o.id=r.organizzazione_id where o.id in :items and upper(r.descrizione) like '%'||upper(:testo)||'%'"		)
	List<RisorsaStrumentaleOrganizzazione> findByOrganizzazioneIdInAndRisorsaStrumentaleDescrizioneContainsIgnoreCase(@Param("items")List<Long> items, @Param("testo")String testo);

}
