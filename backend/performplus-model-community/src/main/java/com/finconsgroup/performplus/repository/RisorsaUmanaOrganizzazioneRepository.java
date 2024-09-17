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

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaOrganizzazione;
import com.finconsgroup.performplus.repository.advanced.RisorsaUmanaOrganizzazioneRepositoryAdvanced;
import com.finconsgroup.performplus.service.dto.RicercaRisorseUmaneOrg;


/**
 * Spring Data  repository for the RisorsaUmanaOrganizzazione entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RisorsaUmanaOrganizzazioneRepository extends JpaRepository<RisorsaUmanaOrganizzazione, Long>, JpaSpecificationExecutor<RisorsaUmanaOrganizzazione>, RisorsaUmanaOrganizzazioneRepositoryAdvanced {

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmana(RisorsaUmana risorsaUmana);

	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdAndRisorsaUmanaPolitico(long idOrganizzazione,boolean politico);

	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdAndRisorsaUmanaEsternaAndRisorsaUmanaPoliticoIsFalse(long idOrganizzazione, boolean esterna);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaId(long idRisorsaUmana);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaIdAndOrganizzazioneId(long idRisorsaUmana, long idOrganizzazione);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaIdEnteAndRisorsaUmanaAnnoAndRisorsaUmanaPolitico(long idEnte, int anno, boolean politico);

	@Query(nativeQuery = true,value="select ro.* from risorsa_umana_org r join risorsa_umana o on r.id=ro.risorsa_umana_id "+
	" where r.id_ente=:idEnte and r.anno=:anno and upper(r.cognome) like '%'||upper(:testo)||'%' and r.politico=:politico"		)
	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaIdEnteAndRisorsaUmanaAnnoAndRisorsaUmanaCognomeContainsIgnoreCaseAndRisorsaUmanaPolitico(@Param("idEnte")long idEnte,@Param("anno")int anno, @Param("testo")String testo,@Param("politico")Integer politico);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaIdEnteAndRisorsaUmanaAnno(long idEnte,int anno);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaAnnoAndOrganizzazioneIdInAndRisorsaUmanaPolitico(int anno, List<Long> items,boolean politico);

	@Query(nativeQuery = true,value="select ro.* from risorsa_umana_org r join organizzazione o on o.id=ro.organizzazione_id join risorsa_umana r on r.id=ro.risorsa:umana_id"+
	" where r.anno=:anno and o.id in :items and r.esterna=:esterna and upper(r.cognome) like '%'||upper(:testo)||'%' and r.politico=:politico"	)
	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaAnnoAndOrganizzazioneIdInAndRisorsaUmanaCognomeContainsIgnoreCaseAndRisorsaUmanaPolitico(
		@Param("anno")int anno, @Param("items")List<Long> items, @Param("testo")String testo,@Param("politico")Integer politico);

	@Query(nativeQuery = true,value="select ro.* from risorsa_umana_org r join organizzazione o on o.id=ro.organizzazione_id join risorsa_umana r on r.id=ro.risorsa:umana_id"+
	" where o.id = :idOrganizzazione and r.esterna=:esterna and upper(r.cognome) like '%'||upper(:testo)||'%' and r.politico='0'"	)
	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdAndRisorsaUmanaEsternaAndRisorsaUmanaCognomeContainsIgnoreCaseAndRisorsaUmanaPoliticoIsFalse(
			 @Param("idOrganizzazione")long idOrganizzazione,  @Param("esterna")Integer esterna,  @Param("testo")String testo);

	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdAndRisorsaUmanaAnnoAndRisorsaUmanaPolitico(long idOrganizzazione, int anno,boolean politico);

	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdInAndRisorsaUmanaPolitico(List<Long> items,boolean politico);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaIdAndOrganizzazioneAnno(long id, int anno);

	@Query(nativeQuery = true,value="select ro.* from risorsa_umana_org r join organizzazione o on o.id=ro.organizzazione_id join risorsa_umana r on r.id=ro.risorsa:umana_id"+
	" where  o.id=:items and upper(r.cognome) like '%'||upper(:testo)||'%' and r.politico=:politico")
	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdInAndRisorsaUmanaCognomeContainsIgnoreCaseAndRisorsaUmanaPolitico(
			 @Param("items")List<Long> items, @Param("testo")String testo,@Param("politico")Integer politico);

	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdAndRisorsaUmanaPoliticoIsFalse(long id);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaIdEnteAndRisorsaUmanaAnnoAndOrganizzazioneIdEnteAndOrganizzazioneAnno(
			long idEnte1, int anno1, Long idEnte2, int anno2);

	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdInAndRisorsaUmanaEsternaAndRisorsaUmanaPoliticoIsFalse(
			List<Long> items, boolean esterne);

	@Query(nativeQuery = true,value="select ro.* from risorsa_umana_org r join organizzazione o on o.id=r.organizzazione_id join risorsa_umana r on r.id=ro.risorsa:umana_id"+
	" where o.id=:items and r.esterna=:esterna and upper(r.cognome) like '%'||upper(:testo)||'%' and r.politico='0'"	)
	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdInAndRisorsaUmanaEsternaAndRisorsaUmanaCognomeContainsIgnoreCaseAndRisorsaUmanaPoliticoIsFalse(
			@Param("items")List<Long> items, @Param("esterna")Integer esterna,@Param("testo") String testo);

	void deleteByRisorsaUmanaId(Long id);

	@Query("SELECT SUM(m.disponibilita) FROM RisorsaUmanaOrganizzazione m where m.risorsaUmana.id=:idRisorsa and m.inizioValidita<=:fineValidita and m.fineValidita>=:inizioValidita")
	Integer utilizzo(@Param("idRisorsa") Long idRisorsa, @Param("inizioValidita") LocalDate inizioValidita, @Param("fineValidita") LocalDate fineValidita);

	@Query("SELECT SUM(m.disponibilita) FROM RisorsaUmanaOrganizzazione m where m.risorsaUmana.id=:idRisorsa")
	Integer utilizzo(@Param("idRisorsa") Long idRisorsa);

	@Query("SELECT m FROM RisorsaUmanaOrganizzazione m where m.risorsaUmana.id=:idRisorsa and m.organizzazione.id=:idOrganizzazione"
			+ " and m.inizioValidita<=:fineValidita and m.fineValidita>=:inizioValidita")
	RisorsaUmanaOrganizzazione leggi(@Param("idRisorsa") Long idRisorsa,@Param("idOrganizzazione") Long idOrganizzazione, @Param("inizioValidita") LocalDate inizioValidita, @Param("fineValidita") LocalDate fineValidita);

	@Query("SELECT m FROM RisorsaUmanaOrganizzazione m where  m.organizzazione.id in (:list)"
			+ " and m.inizioValidita<=:fineValidita and m.fineValidita>=:inizioValidita")
	List<RisorsaUmanaOrganizzazione> listaPerOrganizzazioni(@Param("list") List<Long> list, @Param("inizioValidita") LocalDate inizioValidita, @Param("fineValidita")LocalDate fineValidita);

	@Query("SELECT m FROM RisorsaUmanaOrganizzazione m where  m.risorsaUmana.id in (:list)"
			+ " and m.inizioValidita<=:fineValidita and m.fineValidita>=:inizioValidita")
	List<RisorsaUmanaOrganizzazione> listaPerRisorse(@Param("list") List<Long> list, @Param("inizioValidita") LocalDate inizioValidita, @Param("fineValidita")LocalDate fineValidita);
	
	@Query("SELECT m FROM RisorsaUmanaOrganizzazione m where  m.risorsaUmana.id =:idRisorsa"
			+ " and m.inizioValidita<=:fineValidita and m.fineValidita>=:inizioValidita")
	List<RisorsaUmanaOrganizzazione> listaPerRisorsa(@Param("idRisorsa") Long idRisorsa, @Param("inizioValidita") LocalDate inizioValidita, @Param("fineValidita")LocalDate fineValidita);

	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdInAndRisorsaUmanaPoliticoIsFalse(List<Long> ids);

	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdInAndRisorsaUmanaPoliticoOrderByRisorsaUmanaCognomeAscRisorsaUmanaNomeAsc(List<Long> ids,
			boolean b);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaIdIn(List<Long> ids);

	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdIn(List<Long> ids);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaIdEnteAndRisorsaUmanaAnnoOrderByRisorsaUmanaCognomeAscRisorsaUmanaNomeAsc(
			Long idEnte, Integer anno);

	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneId(Long idOrganizzazione);

	RisorsaUmanaOrganizzazione findTopByOrganizzazioneIdOrderByFineValiditaDesc(Long idOrganizzazione);

	RisorsaUmanaOrganizzazione findTopByRisorsaUmanaIdOrderByFineValiditaDesc(Long idRisorsaUmana);

	List<RisorsaUmanaOrganizzazione> findByOrganizzazioneIdAndRisorsaUmanaPoliticoIsFalseOrderByRisorsaUmanaCognomeAscRisorsaUmanaNomeAscInizioValiditaDesc(
			Long idOrganizzazione);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaIdOrderByInizioValidita(Long idRisorsa);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaCodiceFiscaleOrderByInizioValidita(String codiceFiscale);

	List<RisorsaUmanaOrganizzazione> findByRisorsaUmanaIdEnteAndRisorsaUmanaAnnoAndRisorsaUmanaPoliticoIsFalseOrderByRisorsaUmanaCodiceFiscaleAscInizioValiditaAscFineValiditaAsc(
			Long idEnte, int anno);

	long countByRisorsaUmanaAnno(Integer anno);

	RisorsaUmanaOrganizzazione findTopByRisorsaUmanaOrderByInizioValiditaDescFineValiditaDesc(RisorsaUmana r);



}
