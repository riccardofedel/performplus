package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Profilo;
import com.finconsgroup.performplus.domain.Utente;
import com.finconsgroup.performplus.enumeration.Ruolo;


/**
 * Spring Data  repository for the Profilo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfiloRepository extends JpaRepository<Profilo, Long>, JpaSpecificationExecutor<Profilo> {

	List<Profilo> findByUtenteIdAndAnno(Long idUtente, int anno);

	List<Profilo> findByUtenteId(Long idUtente);

	List<Profilo> findByIdEnteAndAnno(Long idEnte, int anno);

	@Query(nativeQuery = true,value="select p.* from profilo join utente u on u.id=p.utente_id where p.id_ente=:idEnte and p.anno=:anno and  upper(p.nome) like '%'||upper(:testo)||'%'"		)
	List<Profilo>  findByIdEnteAndAnnoAndUtenteNomeContainsIgnoreCase(@Param("idEnte")Long idEnte, @Param("anno")int anno, @Param("testo")String testo);

	List<Profilo> findByIdEnte(Long idEnte);

	Profilo findByUtenteIdAndOrganizzazioneIdAndRuolo(Long idUtente, Long idOrganizzazione, Ruolo ruolo);

	List<Profilo> findByIdEnteAndAnnoAndRisorsaUmanaId(Long idEnte, Integer anno, Long idRisorsaUmana);

	void deleteByUtenteId(Long id);

	List<Profilo> findByIdEnteAndAnnoAndRisorsaUmanaIsNull(long idEnte, int anno);

	List<Profilo> findByUtenteAndAnnoAndRuolo(Utente utente, Integer anno, Ruolo ruolo);

	long countByAnno(int anno);

}
