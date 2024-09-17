package com.finconsgroup.performplus.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.enumeration.Periodo;
import com.finconsgroup.performplus.enumeration.StatoPiano;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.repository.advanced.NodoPianoRepositoryAdvanced;


/**
 * Spring Data  repository for the NodoPiano entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NodoPianoRepository extends JpaRepository<NodoPiano, Long>, JpaSpecificationExecutor<NodoPiano>, NodoPianoRepositoryAdvanced {

	List<NodoPiano> findByIdEnteAndAnnoAndDenominazioneAndDateDeleteIsNull(Long idEnte, Integer anno, String denominazione);

	List<NodoPiano> findByIdEnteAndAnnoAndTipoNodoAndDateDeleteIsNull(Long idEnte, Integer anno, TipoNodo tipoNodo);
	
	@Query("select f from NodoPiano f where f.idEnte=:idEnte and f.anno=:anno  and f.tipoNodo='PIANO' and f.statoPiano='ATTIVO' and f.dateDelete is null")
	NodoPiano getPiano(@Param("idEnte") Long idEnte, @Param("anno") Integer anno);

	List<NodoPiano> findByPadreAndTipoNodoAndDateDeleteIsNull(NodoPiano padre, TipoNodo tipoNodo);

	List<NodoPiano> findByIdEnteAndCodiceAndTipoNodoAndPadreIsNullAndDateDeleteIsNull(Long idEnte, String codice, TipoNodo tipoNodo);

	List<NodoPiano> findByIdEnteAndAnnoAndCodiceCompletoStartsWithAndDateDeleteIsNullOrderByCodiceCompleto(Long idEnte,Integer anno, String codiceCompleto);

	int countByIdEnteAndAnnoAndTipoNodoAndDateDeleteIsNull(Long idEnte, Integer anno, TipoNodo tipoNodo);

	List<NodoPiano> findByPadreAndDateDeleteIsNull(NodoPiano padre);

	List<NodoPiano> findByPadreIdAndTipoNodoAndDateDeleteIsNull(Long id, TipoNodo tipoNodo);

	List<NodoPiano> findByIdEnteAndAnnoAndDateDeleteIsNullOrderByCodiceCompleto(Long idEnte, Integer anno);

	@Query("select distinct f.anno from NodoPiano f where f.idEnte=:idEnte and f.tipoNodo='PIANO' and f.dateDelete is null order by f.anno")
	List<Integer> getAnni(@Param("idEnte") Long idEnte);
	@Query("select distinct f.anno from NodoPiano f where f.idEnte=:idEnte and dateDelete is null")
	List<Integer> getAnniAll(@Param("idEnte") Long idEnte);

	List<NodoPiano> findByOrganizzazioneAndTipoNodoAndPianoAndDateDeleteIsNull(Organizzazione o, TipoNodo tipoNodo, NodoPiano piano);

//	List<NodoPiano> findByOrganizzazioniAndTipoNodoAndPiano(Organizzazione o, TipoNodo tipoNodo,
//			NodoPiano piano);

	List<NodoPiano> findByPadreIdAndDateDeleteIsNull(Long idPadre);

//	List<NodoPiano> findByPadreIdAndOrganizzazioniIdAndTipoNodo(Long idNPadre, Long idOrganizzazione,
//			TipoNodo tipoNodo);
	List<NodoPiano> findByPadreIdAndOrganizzazioneIdAndTipoNodoAndDateDeleteIsNull(Long idNPadre, Long idOrganizzazione,
			TipoNodo tipoNodo);

	List<NodoPiano> findByPadreIdAndDateDeleteIsNullOrderByCodice(Long idPadre);

	NodoPiano findByIdEnteAndAnnoAndCodiceCompletoAndDateDeleteIsNull(Long idEnte, Integer anno, String codiceCompleto);

	NodoPiano findByIdEnteAndAnnoAndCodiceInternoAndDateDeleteIsNull(Long idEnte, Integer anno, String codiceInterno);

	@Query("select max(f.codice) from NodoPiano f where f.padre.id=:idPadre and f.dateDelete is null")
	String maxCodiceByPadreId(@Param("idPadre") Long idPadre);

	List<NodoPiano> findByPianoIdAndScadenzaEffettivaIsNullAndDateDeleteIsNull(Long idPiano);

	NodoPiano findTopByIdEnteAndAnnoAndTipoNodoAndDateDeleteIsNull(Long idEnte, Integer anno, TipoNodo tipoNodo);

	NodoPiano findByIdEnteAndAnnoAndTipoNodoAndStatoPianoAndDateDeleteIsNull(Long idEnte, Integer anno, TipoNodo tipoNodo, StatoPiano attivo);

	List<NodoPiano> findByPianoIdAndDateDeleteIsNullOrderByCodiceCompleto(Long idPiano);

	NodoPiano findTopByIdEnteAndCodiceAndTipoNodoAndDateDeleteIsNull(Long idEnte, String codice, TipoNodo tipoNodo);

	NodoPiano findTopByIdEnteAndAnnoAndTipoNodoAndStatoPianoAndDateDeleteIsNull(Long idEnte, Integer anno, TipoNodo tipoNodo, StatoPiano attivo);

	List<NodoPiano> findByIdEnteAndTipoNodo(Long idEnte, TipoNodo tipoNodo);

	int countByIdEnteAndTipoNodoAndDateDeleteIsNull(Long idEnte, TipoNodo tipoNodo);

	List<NodoPiano> findByIdEnteAndCodiceLessThanAndDateDeleteIsNull(Long idEnte, String codicePiano);

	List<NodoPiano> findByOrganizzazioneIdAndDateDeleteIsNullOrderByCodiceCompleto(Long idOrganizzazione);

//	List<NodoPiano> findByOrganizzazioniIdOrderByCodiceCompleto(Long id);

	List<NodoPiano> findByOrganizzazioneIdAndPianoIdAndDateDeleteIsNullOrderByCodiceCompleto(Long id, Long id2);

//	List<NodoPiano> findByOrganizzazioniIdAndPianoIdOrderByCodiceCompleto(Long id, Long id2);

	boolean existsByIdEnteAndAnnoAndCodiceCompletoAndDateDeleteIsNull(Long idEnte, Integer anno, String codiceCompleto);

//	List<NodoPiano> findByOrganizzazioniIdAndPadreIdAndTipoNodo(Long id, Long id2, TipoNodo tipoNodo);

	NodoPiano findFirstByIdEnteAndAnnoAndDenominazioneAndDateDeleteIsNull(Long idEnte, Integer anno, String denominazione);

	List<NodoPiano> findByIdEnteAndAnnoAndTipoNodoAndDateDeleteIsNullOrderByDenominazione(Long idEnte, Integer anno, TipoNodo tipoNodo);

	
	@Query(nativeQuery = true,value="select * from nodo_piano where id_ente=:idEnte and anno=:anno and upper(denominazione) like upper(:testo)||'%' and date_delete is null order by codice_completo"		)
	List<NodoPiano> findByIdEnteAndAnnoAndDenominazioneStartsWithIgnoreCaseAndDateDeleteIsNullOrderByCodiceCompleto(@Param("idEnte")Long idEnte,
			@Param("anno")Integer anno,  @Param("testo")String testo);

	int countByIdEnteAndAnnoAndDateDeleteIsNull(Long idEnte, Integer anno);

	@Query(nativeQuery = true,value = "select count(*) from nodo_piano np "
			+ " where np.tipo_nodo <> 'PIANO' and np.id_ente=:idEnte and np.date_delete is null and np.anno=:anno"
			+ " and not exists ("
			+ " select v.id from valutazione v, indicatore_piano ip"
			+ " where v.indicatore_piano_id=ip.id and ip.nodo_piano_id=np.id"
			+ " and v.tipo_valutazione='PREVENTIVO' and v.anno=:anno and v.periodo=4)")
	int countWithoutTarget(@Param("idEnte") Long idEnte, @Param("anno") Integer anno);
	
	@Query(nativeQuery = true,value = "select count(*) from nodo_piano np"
			+ " where np.tipo_nodo <> 'PIANO' and np.id_ente=:idEnte and np.date_delete is null and np.anno=:anno and not exists ("
			+ " select v.id from valutazione v, indicatore_piano ip"
			+ " where v.indicatore_piano_id=ip.id and ip.nodo_piano_id=np.id"
			+ " and v.tipo_valutazione='CONSUNTIVO'"
			+ " and v.anno=:anno and v.periodo=:periodo)"
		)
	int countWithoutConsuntivazione(@Param("idEnte") Long idEnte, @Param("anno") Integer anno
			, @Param("periodo") Integer perioodo);

	int countByPadreIdAndDateDeleteIsNull(Long id);

	boolean existsByPadreIdAndCodiceAndDateDeleteIsNull(Long id, String codice);

	boolean existsByPadreIdAndCodiceAndIdNotAndDateDeleteIsNull(Long idPadre, String codice, Long id);

	List<NodoPiano> findByIdEnteAndAnnoAndTipoNodoAndDateDeleteIsNullOrderByCodiceCompleto(Long idEnte, Integer anno,
			TipoNodo obiettivoStrategico);

	@Query(nativeQuery = true,value="select * from nodo_piano where id_ente=:idEnte and anno=:anno and tipo_nodo=:tipoNodo and upper(denominazione) like upper(:testo)||'%' and date_delete is null order by codice_completo"
			)
	List<NodoPiano> findByIdEnteAndAnnoAndTipoNodoAndDenominazioneStartsWithIgnoreCaseAndDateDeleteIsNullOrderByCodiceCompleto(@Param("idEnte")Long idEnte,
			@Param("anno")Integer anno, @Param("tipoNodo")String tipoNodo, @Param("testo")String testo);

	List<NodoPiano> findByIdEnteAndAnnoAndTipoNodoAndDateDeleteIsNullOrderByOrdineAscCodiceCompletoAsc(Long idEnte, Integer anno,
			TipoNodo programma);

	@Query(nativeQuery = true,value="select * from nodo_piano where id_ente=:idEnte and anno=:anno and tipo_nodo=:tipoNodo and upper(denominazione) like upper(:testo)||'%' and date_delete is null order by ordine, codice_completo"
			)
	List<NodoPiano> findByIdEnteAndAnnoAndTipoNodoAndDenominazioneStartsWithIgnoreCaseAndDateDeleteIsNullOrderByOrdineAscCodiceCompletoAsc(@Param("idEnte")Long idEnte,
			@Param("anno")Integer anno, @Param("tipoNodo")String tipoNodo, @Param("testo")String testo);

	List<NodoPiano> findByIdEnteAndAnnoAndDateDeleteIsNullOrderByOrdineAscCodiceCompletoAsc(Long idEnte, Integer anno);

	@Query(nativeQuery = true,value="select * from nodo_piano where id_ente=:idEnte and anno=:anno and upper(denominazione) like upper(:testo)||'%' and date_delete is null order by ordine, codice_completo"
			)
	List<NodoPiano> findByIdEnteAndAnnoAndDenominazioneStartsWithIgnoreCaseAndDateDeleteIsNullOrderByOrdineAscCodiceCompletoAsc(@Param("idEnte")Long idEnte,
			@Param("anno")Integer anno, @Param("testo")String testo);

	@Modifying
	@Query("update NodoPiano np set np.scadenzaEffettiva = :scadenzaEffettiva where np.id=:idNodoPiano")
	int updateDataScadenzaEffettiva(@Param("idNodoPiano") Long idNodoPiano,@Param("scadenzaEffettiva") LocalDate scadenzaEffettiva);


	@Modifying
	@Query("update NodoPiano np set np.scadenzaEffettiva = :scadenzaEffettiva, np.noteConsuntivo=:note where np.id=:idNodoPiano")
	int updateScadenzaEffettiva(@Param("idNodoPiano") Long idNodoPiano,@Param("scadenzaEffettiva") LocalDate scadenzaEffettiva, 
			@Param("note") String note);

	@Modifying
	@Query("update NodoPiano np set np.dateDelete = :dateDelete, np.userDelete=:userDelete"
			+" where np.codiceCompleto like :codiceCompleto and np.dateDelete is null and np.anno=:anno and np.idEnte=:idEnte")
	int logicalDeleteRamo(
			@Param("idEnte") Long idEnte,@Param("anno") Integer anno,
			@Param("codiceCompleto") String codiceCompleto, @Param("dateDelete") LocalDateTime dateDelete, 
			@Param("userDelete") String userDelete);
	@Modifying
	@Query("update NodoPiano np set np.dateDelete = :dateDelete, np.userDelete=:userDelete"
			+" where np.id=:idNodoPiano")
	int logicalDelete(
			@Param("idNodoPiano") Long idNodoPiano,
			@Param("dateDelete") LocalDateTime dateDelete, 
			@Param("userDelete") String userDelete);



	List<NodoPiano> findByDateModifyGreaterThan(LocalDateTime last);

	boolean existsByIdEnteAndAnnoAndCodiceInternoAndDateDeleteIsNull(Long idEnte, Integer anno, String codiceInterno);

	List<NodoPiano> findByIdEnteAndAnnoAndOrdineAndTipoNodoAndDateDeleteIsNull(Long idEnte, Integer anno,
			int ordine, TipoNodo tipoNodo);


	List<NodoPiano> findByIdEnteAndAnnoAndCodiceCompletoStartsWithAndDateDeleteIsNullOrderByOrdineAscCodiceCompletoAsc(
			Long idEnte, Integer anno, String codiceCompleto);

	List<NodoPiano> findByPadreIdAndDateDeleteIsNullOrderByInizioAscScadenzaEffettivaAscScadenzaAscCodiceAsc( Long idNodo);

	long countByAnno(Integer anno);

	
	
}
