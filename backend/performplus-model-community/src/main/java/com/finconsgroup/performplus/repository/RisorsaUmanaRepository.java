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

import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.TipoFunzione;
import com.finconsgroup.performplus.repository.advanced.RisorsaUmanaRepositoryAdvanced;


/**
 * Spring Data  repository for the RisorsaUmana entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RisorsaUmanaRepository extends JpaRepository<RisorsaUmana, Long>, JpaSpecificationExecutor<RisorsaUmana>, RisorsaUmanaRepositoryAdvanced {

	List<RisorsaUmana> findByIdEnteAndAnno(long idEnte, int anno);

	@Query(nativeQuery = true,value=
	"select r.* from risorsa_umana r "+
	" where r.id_ente=:idEnte and r.anno=:anno and upper(r.cognome) like '%'||upper(:testo)||'%'"
	+" order by r.cognome, r.nome")
	List<RisorsaUmana> findByIdEnteAndAnnoAndCognomeContainsIgnoreCase(
	@Param("idEnte")Long idEnte, @Param("anno")Integer anno, @Param("testo")String testo);

	RisorsaUmana findByIdEnteAndAnnoAndCodiceInterno(Long idEnte, int anno, String codiceInterno);

	@Query("select max(r.ordine) from RisorsaUmana r where r.idEnte=:idEnte and r.anno=:anno and r.disponibile=:disponibile")
	Integer maxOrdineByIdEnteAndAnnoAndDisponibile(@Param("idEnte") Long idEnte, @Param("anno") Integer anno, @Param("disponibile") Disponibile disponibile);

	int countByIdEnteAndAnno(Long idEnte, int anno);

	List<RisorsaUmana> findByIdEnteAndAnnoAndPolitico(Long idEnte, int anno, boolean politico);

	List<RisorsaUmana> findByIdEnteAndAnnoAndCodiceFiscale(long idEnte, int anno, String codiceFiscale);

	List<RisorsaUmana> findByIdEnteAndAnnoAndFunzione(Long idEnte, int anno, TipoFunzione presidente);

	List<RisorsaUmana> findByIdEnteAndAnnoAndCategoriaCodice(long idEnte, int anno, String codiceCategoria);

	List<RisorsaUmana> findByIdEnteAndAnnoAndEsternaAndPoliticoIsFalse(Long idEnte, int anno, boolean esterna);

	List<RisorsaUmana> findByIdEnteAndAnnoAndPoliticoIsTrue(Long idEnte, Integer anno);

	@Query(nativeQuery = true,value=
	"select r.* from risorsa_umana r "+
	" where r.id_ente=:idEnte and r.anno=:anno and r.esterna=:esterna and upper(r.cognome) like '%'||upper(:testo)||'%' and politico='0'"
	+" order by r.cognome, r.nome")
	List<RisorsaUmana> findByIdEnteAndAnnoAndEsternaAndPoliticoIsFalseAndCognomeContainsIgnoreCase(
	@Param("idEnte")Long idEnte, @Param("anno") Integer anno,@Param("esterna")Integer esterna, @Param("testo")String testo);

	@Query(nativeQuery = true,value=
	"select r.* from risorsa_umana r "+
	" where r.id_ente=:idEnte and r.anno=:anno and upper(r.cognome) like '%'||upper(:testo)||'%' and politico='1'"
	+" order by r.cognome, r.nome")
	List<RisorsaUmana> findByIdEnteAndAnnoAndPoliticoIsTrueAndCognomeContainsIgnoreCase(
			@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("testo")String testo);

	List<RisorsaUmana> findByIdEnteAndAnnoAndCognomeAndNome(Long idEnte, Integer anno, String cognome, String nome);

	@Query(value = "SELECT t FROM RisorsaUmana t where t.idEnte=:idEnte and t.anno=:anno and politico=false"
	+" and not exists (select p.id from RisorsaUmanaOrganizzazione p where p.organizzazione.id=:idOrganizzazione and p.risorsaUmana.id=t.id)"
	+ " order by t.cognome, t.nome")
	List<RisorsaUmana> cercaRisorseNonAssociateOrg(@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("idOrganizzazione")Long idOrganizzazione);

	@Query(value = "SELECT t FROM RisorsaUmana t where t.idEnte=:idEnte and t.anno=:anno and t.politico=false and t.cognome like :testo"
	+" and not exists (select p.id from RisorsaUmanaOrganizzazione p where p.organizzazione.id=:idOrganizzazione and p.risorsaUmana.id=t.id)"
	+ " order by t.cognome, t.nome")
	List<RisorsaUmana> cercaRisorseNonAssociateOrgTesto(@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("idOrganizzazione")Long idOrganizzazione,
			 @Param("testo")String testo);

	@Query(value = "SELECT t FROM RisorsaUmana t where t.idEnte=:idEnte and t.anno=:anno  and t.esterna=:esterna and t.politico=false"
	+" and not exists (select p.id from RisorsaUmanaOrganizzazione p where p.organizzazione.id=:idOrganizzazione and p.risorsaUmana.id=t.id)"
	+" order by t.cognome, t.nome")
	List<RisorsaUmana> cercaNonAssociateOrgEsterne(@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("idOrganizzazione")Long idOrganizzazione,
			@Param("esterna")Boolean esterna);

	@Query(value = "SELECT t FROM RisorsaUmana t where t.idEnte=:idEnte and t.anno=:anno  and t.esterna=:esterna and t.politico=false and t.cognome like :testo"
	+" and not exists (select p.id from RisorsaUmanaOrganizzazione p where p.organizzazione.id=:idOrganizzazione and p.risorsaUmana.id=t.id)"
	+" order by t.cognome, t.nome")
	List<RisorsaUmana> cercaNonAssociateOrgEsterneTesto(@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("idOrganizzazione")Long idOrganizzazione,
			@Param("esterna")Boolean esterna, @Param("testo")String testo);

	@Query(value = "SELECT t FROM RisorsaUmana t where t.idEnte=:idEnte and t.anno=:anno and politico=false"
	+" and not exists (select p.id from RisorsaUmanaNodoPiano p where p.nodoPiano.id=:idNodo and p.risorsaUmana.id=t.id and p.nodoPiano.dateDelete is null)"
	+" order by t.cognome, t.nome")
	List<RisorsaUmana> cercaRisorseNonAssociateNodo(@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("idNodo")Long idNodo);

	@Query(value = "SELECT t FROM RisorsaUmana t where t.idEnte=:idEnte and t.anno=:anno and politico=false"
	+" and not exists (select p.id from RisorsaUmanaNodoPiano p where p.nodoPiano.id=:idNodo and p.risorsaUmana.id=t.id and p.nodoPiano.dateDelete is null)"
	+" and exists (select ro.id from RisorsaUmanaOrganizzazione ro where ro.organizzazione.id in :orgs and ro.risorsaUmana.id=t.id )"
	+" order by t.cognome, t.nome")
	List<RisorsaUmana> cercaRisorseNonAssociateNodoInStrutture(@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("idNodo")Long idNodo
			,@Param("orgs")List<Long> orgs);

	@Query(value = "SELECT t FROM RisorsaUmana t where t.idEnte=:idEnte and t.anno=:anno and politico=false"
	+" and not exists (select p.id from RisorsaUmanaNodoPiano p where p.nodoPiano.id in :nodi and p.risorsaUmana.id=t.id and p.nodoPiano.dateDelete is null)"
	+" order by t.cognome, t.nome")
	List<RisorsaUmana> cercaRisorseNonAssociateNodi(@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("nodi") List<Long> nodi);

	@Query(value = "SELECT t FROM RisorsaUmana t where t.idEnte=:idEnte and t.anno=:anno and politico=false and t.cognome like :testo"
	+" and not exists (select p.id from RisorsaUmanaNodoPiano p where p.nodoPiano.id=:idNodo and p.risorsaUmana.id=t.id and p.nodoPiano.dateDelete is null)"
	+" order by t.cognome, t.nome")
	List<RisorsaUmana> cercaRisorseNonAssociateNodoTesto(@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("idNodo")Long idNodo,  @Param("testo")String testo);

	@Query(value = "SELECT t FROM RisorsaUmana t where t.idEnte=:idEnte and t.anno=:anno and politico=false and t.cognome like :testo"
	+" and not exists (select p.id from RisorsaUmanaNodoPiano p where p.nodoPiano.id=:idNodo and p.risorsaUmana.id=t.id and p.nodoPiano.dateDelete is null)"
	+" and exists (select ro.id from RisorsaUmanaOrganizzazione ro where ro.organizzazione.id in :orgs and ro.risorsaUmana.id=t.id )"
	+ " order by t.cognome, t.nome")
	List<RisorsaUmana> cercaRisorseNonAssociateNodoTestoInStrutture(@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("idNodo")Long idNodo,  @Param("testo")String testo
			,@Param("orgs")List<Long> orgs);

	@Query(nativeQuery = true,value=
	"select r.* from risorsa_umana r "+
	" where r.id_ente=:idEnte and r.anno=:anno and upper(r.cognome) like '%'||upper(:testo)||'%' and politico='0'"
	+" order by r.cognome, r.nome")
	List<RisorsaUmana> findByIdEnteAndAnnoAndCognomeContainsIgnoreCaseAndPoliticoIsFalse(
			@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("testo")String testo);

	List<RisorsaUmana> findByIdEnteAndAnnoAndPoliticoIsFalse(Long idEnte, Integer anno);

	List<RisorsaUmana> findByIdEnteAndAnnoOrderByCognomeAscNomeAsc(Long idEnte, int anno);

	RisorsaUmana findByIdEnteAndAnnoAndCodiceInterno(Long idEnte, Integer anno, String codice);

	List<RisorsaUmana> findByIdEnteAndAnnoAndPoliticoOrderByCognomeAscNomeAsc(Long idEnte, Integer anno, boolean politico);

	@Query(nativeQuery = true,value=
	"select r.* from risorsa_umana r "+
	" where r.id_ente=:idEnte and r.anno=:anno and r.politico=:politico and upper(r.cognome) like upper(:testo)||'%' order by r.cognome, r.nome"
	+" order by r.cognome, r.nome")
	List<RisorsaUmana> findByIdEnteAndAnnoAndPoliticoAndCognomeStartsWithIgnoreCaseOrderByCognomeAscNomeAsc(
			@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("politico") Integer politico, @Param("testo")String testo);

	@Query(nativeQuery = true,value=
	"select r.* from risorsa_umana r "+
	" where r.id_ente=:idEnte and r.anno=:anno and r.esterna=:esterna and upper(r.cognome) like upper(:testo)||'%' and r.politico='0'"
	+" order by r.cognome, r.nome")
	Page<RisorsaUmana> findByIdEnteAndAnnoAndEsternaAndCognomeStartsWithIgnoreCaseAndPoliticoIsFalse(
	@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("esterna") Integer esterna, @Param("testo")String testo, Pageable pageable);

	Page<RisorsaUmana> findByIdEnteAndAnnoAndEsternaAndPoliticoIsFalse(Long idEnte, Integer anno,boolean esterno, Pageable pageable);

	Page<RisorsaUmana> findByIdEnteAndAnnoAndPoliticoIsTrue(Long idEnte, Integer anno, Pageable pageable);

	@Query(nativeQuery = true,value=
	"select r.* from risorsa_umana r "+
	" where r.id_ente=:idEnte and r.anno=:anno and upper(r.cognome) like upper(:testo)||'%' and r.politico='1'"
	+" order by r.cognome, r.nome")
	Page<RisorsaUmana> findByIdEnteAndAnnoAndCognomeStartsWithIgnoreCaseAndPoliticoIsTrue(
			@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("testo")String testo, Pageable pageable);

	RisorsaUmana findByIdEnteAndAnnoAndCodiceInternoAndCognomeAndNomeAndPolitico(Long idEnte, int anno, String codiceInterno,
			String cognome, String nome, Boolean politico);

	boolean existsByIdEnteAndAnnoAndCodiceInternoAndCognomeAndNomeAndPolitico(Long idEnte, Integer anno, String codiceInterno,
			String cognome, String nome, Boolean politico);

	boolean existsByIdEnteAndAnnoAndCodiceInterno(Long idEnte, Integer anno, String codiceInterno);

	@Query(	nativeQuery = true,value=
	"select r.* from risorsa_umana r "+
	" where r.id_ente=:idEnte and r.anno=:anno and upper(r.cognome) like upper(:testo)||'%' and r.politico='1' order by r.cognome, r.nome")
	List<RisorsaUmana> findByIdEnteAndAnnoAndCognomeStartsWithIgnoreCaseAndPoliticoIsTrueOrderByCognomeAscNomeAsc(
	@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("testo")String testo);

	List<RisorsaUmana> findByIdEnteAndAnnoAndPoliticoIsTrueOrderByCognomeAscNomeAsc(Long idEnte, Integer anno);

	List<RisorsaUmana> findByIdEnteAndAnnoAndPoliticoIsFalseOrderByCognomeAscNomeAsc(Long idEnte, Integer anno);

	@Query(nativeQuery = true,value=
	"select r.* from risorsa_umana r "+
	" where r.id_ente=:idEnte and r.anno=:anno and upper(r.cognome) like upper(:testo)||'%' and r.politico='0' order by r.cognome, r.nome")
	List<RisorsaUmana> findByIdEnteAndAnnoAndCognomeStartsWithIgnoreCaseAndPoliticoIsFalseOrderByCognomeAscNomeAsc(
	@Param("idEnte")Long idEnte, @Param("anno") Integer anno, @Param("testo")String testo);

	List<RisorsaUmana> findByIdEnteAndAnnoAndCognomeAndNomeAndPolitico(Long idEnte, int i, String cognome, String nome,
			boolean politico);

	boolean existsByIdEnteAndAnnoAndCodiceFiscale(Long idEnte, Integer anno, String codiceFiscale);

	RisorsaUmana findByIdEnteAndAnnoAndCodiceFiscaleAndInizioValidita(Long idEnte, Integer anno, String codiceFiscale,
			LocalDate inizio);


	RisorsaUmana findTopByIdEnteAndAnnoAndCodiceFiscaleOrderByInizioValiditaDescFineValiditaDesc(Long idEnte, Integer anno,
			String codiceFiscale);

	long countByAnno(Integer anno);

	

}
