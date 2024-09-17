package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.enumeration.TipologiaStruttura;
import com.finconsgroup.performplus.repository.advanced.OrganizzazioneRepositoryAdvanced;

/**
 * Spring Data  repository for the Organizzazione entity.
 */
@Repository
public interface OrganizzazioneRepository extends JpaRepository<Organizzazione, Long>, JpaSpecificationExecutor<Organizzazione> , OrganizzazioneRepositoryAdvanced{

 
	List<Organizzazione> findByIdEnteAndAnnoAndLivello(Long idEnte, Integer anno, Livello livello);

	List<Organizzazione> findByResponsabileAndIdEnteAndAnno(RisorsaUmana resp,Long idEnte, Integer anno);


	boolean existsByIdEnteAndAnnoAndCodiceCompleto(Long idEnte, Integer anno, String codiceCompleto);

	Organizzazione findByIdEnteAndAnnoAndCodiceCompleto(Long idEnte, Integer anno, String codiceCompleto);

	Integer countByIdEnteAndAnno(Long idEnte, Integer anno);

	List<Organizzazione> findByPadreIdOrderByCodice(Long idPadre);

	@Query("select max(o.codice) from Organizzazione o where o.padre.id=:idPadre")
	String maxCodice(@Param("idPadre") Long idPadre);


	Organizzazione findTopByIdEnteAndAnnoAndLivello(Long idEnte, Integer anno, Livello livello);

	Organizzazione findByIdEnteAndAnnoAndCodiceInterno(Long idEnte, Integer anno, String codiceInterno);

	Organizzazione findByIdEnteAndAnnoAndIntestazione(Long idEnte, Integer anno, String Integerestazione);

	List<Organizzazione> findByResponsabileIdAndAnno(Long idRisorsa, Integer anno);

	@Query(nativeQuery = true,value="select o.* from organizzazione o join risorsa_umana r on r.id=o.responsabile_id where o.id_ente=:idEnte and o.anno=:anno and upper(r.cognome) like '%'||upper(:cognome)||'%' "		)
	List<Organizzazione> findByIdEnteAndAnnoAndResponsabileCognomeContainsIgnoreCase(@Param("idEnte")Long idEnte, @Param("anno")Integer anno,
			@Param("cognome")String cognome);

	List<Organizzazione> findByIdEnteAndAnnoAndResponsabileIsNotNull(Long idEnte, Integer anno);


	List<Organizzazione> findByLivelloOrderByAnnoDesc(Livello livello);

	List<Organizzazione> findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(Long idEnte,Integer anno,String codiceCompleto);

	List<Organizzazione> findByIdEnte(Long idEnte);

	List<Organizzazione> findByIdEnteAndLivello(Long idEnte, Livello livello);

	@Query(nativeQuery = true,value="select * from organizzazione where id_ente=:idEnte and anno=:anno and upper(intestazione) like upper(:testo)||'%' order by codice_completo"		)
	List<Organizzazione> findByIdEnteAndAnnoAndIntestazioneStartsWithIgnoreCaseOrderByCodiceCompleto(@Param("idEnte")Long idEnte, @Param("anno")Integer anno, @Param("testo")String testo);

	List<Organizzazione> findByIdEnteAndAnnoOrderByCodiceCompleto(Long idEnte, Integer anno);

	Integer countByPadreId( Long idPadre);
	Integer countByPadre( Organizzazione padre);

	boolean existsByPadreIdAndCodice(Long idPadre, String codice);

	boolean existsByPadreIdAndCodiceAndIdNot(Long idPadre, String codice, Long id);

	List<Organizzazione> findByIdEnteAndAnnoOrderByResponsabileCognomeAscResponsabileNomeAsc(Long idEnte, Integer anno);

	List<Organizzazione> findByIdEnteAndAnnoAndLivelloOrderByCodiceCompleto(Long idEnte, Integer anno,
			Livello superiore);

	@Query(nativeQuery = true,value="select * from organizzazione where id_ente=:idEnte and anno=:anno and Livello=:Livello and upper(intestazione) like upper(:testo)||'%' order by codice_completo"		)
	List<Organizzazione> findByIdEnteAndAnnoAndLivelloAndIntestazioneStartsWithIgnoreCaseOrderByCodiceCompleto(
			@Param("idEnte")Long idEnte, @Param("anno")Integer anno, @Param("Livello")Integer Livello, @Param("testo")String testo);

	List<Organizzazione> findByIdEnteAndAnno(Long idEnte, int anno);

	long countByAnno(Integer anno);

	@Query(value="select o.id from Organizzazione o "+
	" join Organizzazione p on p.anno=o.anno and p.idEnte=o.idEnte"+
	" and o.codiceCompleto like p.codiceCompleto||'.%' "+
	" where p.id=:id order by o.codiceCompleto")
	List<Long> findRamoById(@Param("id")Long id);

	@Query(value="select o.codiceCompleto from Organizzazione o where o.id in :ids order by o.codiceCompleto")
	List<String> findCodiceCompletoByInId(@Param("ids")List<Long> ids);
	
	@Query(value="select o.id from Organizzazione o where o.idEnte=:idEnte and o.anno=:anno and o.codiceCompleto like :cc order by o.codiceCompleto")
	List<Long> findIdByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(@Param("idEnte")Long idEnte, @Param("anno")Integer anno, @Param("cc")String cc);

	@Query(value="select o.id from Organizzazione o where o.idEnte=:idEnte and o.anno=:anno and o.codiceCompleto = :cc")
	Long findIdByIdEnteAndAnnoAndCodiceCompleto(@Param("idEnte")Long idEnte, @Param("anno")Integer anno, @Param("cc")String cc);

	List<Organizzazione> findByIdEnteAndAnnoAndIntestazioneAndTipologiaStruttura(long idEnte, int anno, String descrizione,
			TipologiaStruttura tipologia);

}
