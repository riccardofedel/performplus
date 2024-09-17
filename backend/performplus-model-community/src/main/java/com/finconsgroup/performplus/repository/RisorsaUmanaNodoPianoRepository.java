package com.finconsgroup.performplus.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaNodoPiano;
import com.finconsgroup.performplus.enumeration.TipoNodo;


/**
 * Spring Data  repository for the RisorsaUmanaNodoPiano entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RisorsaUmanaNodoPianoRepository extends JpaRepository<RisorsaUmanaNodoPiano, Long>, JpaSpecificationExecutor<RisorsaUmanaNodoPiano> {

	
	@Query(nativeQuery = true,value=
	"select rn.* from risorsa_umana_nodo_piano rn join nodo_piano np on np.id=rn_nodo_piano_id join risorsa_umana r on r.id=rn.risorsa_umana_id"
			+" where r.esterna=:esterna and upper(r.cognome) like '%'||:testo||'%' and r.politico='0' and np.id=:idNodo"
	)
	List<RisorsaUmanaNodoPiano> findByNodoPianoAndRisorsaUmanaEsternaAndRisorsaUmanaCognomeContainsIgnoreCaseAndRisorsaUmanaPoliticoIsFalse(
			@Param("idNodo")Long idNodo, @Param("esterna")Integer esterna, @Param("testo")String testo);

	List<RisorsaUmanaNodoPiano> findByNodoPianoAndRisorsaUmanaEsternaAndRisorsaUmanaPoliticoIsFalse(NodoPiano nodo, boolean esterna);

	@Query("select sum(r.disponibilita) from RisorsaUmanaNodoPiano r where r.risorsaUmana.id=:idRisorsa and r.nodoPiano.anno=:anno and r.nodoPiano.dateDelete is null")
	BigDecimal sumDisponibilitaByRisorsaUmanaIdAndNodoPianoAnno(@Param("idRisorsa") Long idRisorsa, @Param("anno") int anno);

	List<RisorsaUmanaNodoPiano> findByRisorsaUmanaIdEnteAndRisorsaUmanaAnnoAndNodoPianoDateDeleteIsNull(
			Long idEnte, Integer anno);

	List<RisorsaUmanaNodoPiano> findByNodoPianoIdInAndRisorsaUmanaAnnoAndRisorsaUmanaPolitico(List<Long> items, Integer anno,boolean politico);

	List<RisorsaUmanaNodoPiano> findByNodoPianoIdInAndRisorsaUmanaAnnoAndRisorsaUmanaCognomeContainsIgnoreCase(
			List<Long> items, Integer anno, String testo);

	List<RisorsaUmanaNodoPiano> findByNodoPianoIdAndRisorsaUmanaAnno(Long idNodoPiano, Integer anno);

	List<RisorsaUmanaNodoPiano> findByRisorsaUmanaIdInAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(List<Long> ris, Integer anno);

	List<RisorsaUmanaNodoPiano> findByNodoPianoId(Long idNodoPiano);

	List<RisorsaUmanaNodoPiano> findByNodoPianoAndRisorsaUmanaPoliticoIsTrue(NodoPiano nodoPiano);

	List<RisorsaUmanaNodoPiano> findByNodoPianoIdAndRisorsaUmanaEsternaAndRisorsaUmanaPoliticoIsFalse(Long idNodo, boolean b);

	RisorsaUmanaNodoPiano findByRisorsaUmanaIdAndNodoPianoId(Long idRisorsa, Long idNodo);

	@Query("select sum(r.disponibilita) from RisorsaUmanaNodoPiano r where r.risorsaUmana.id=:idRisorsa and r.nodoPiano.tipoNodo=:tipoNodo and r.nodoPiano.dateDelete is null")
	BigDecimal sumDisponibilitaByRisorsaUmanaIdAndNodoPianoTipoNodo(@Param("idRisorsa") Long idRisorsa, @Param("tipoNodo") TipoNodo tipoNodo);

	@Query(nativeQuery = true,value=
	"select rn.* from risorsa_umana_nodo_piano rn join nodo_piano np on np.id=rn_nodo_piano_id join risorsa_umana r on r.id=rn.risorsa_umana_id"
			+" where r.esterna=:esterna and upper(r.cognome) like '%'||:testo||'%' and r.politico='0' and np.id=:idNodo"
	)
	List<RisorsaUmanaNodoPiano> findByNodoPianoIdAndRisorsaUmanaEsternaAndRisorsaUmanaCognomeContainsIgnoreCaseAndRisorsaUmanaPoliticoIsFalse(
			@Param("idNodo")Long idNodo, @Param("esterna")Integer esterna, @Param("testo")String testo);

	@Query("select sum(r.disponibilita) from RisorsaUmanaNodoPiano r where r.risorsaUmana.id=:idRisorsa and r.nodoPiano.idEnte=:idEnte"
			+" and r.nodoPiano.anno=:anno and r.nodoPiano.piano.statoPiano='ATTIVO' and r.nodoPiano.dateDelete is null")
	BigDecimal utilizzo(@Param("idRisorsa") Long idRisorsa, @Param("idEnte") long idEnte, @Param("anno") int anno);

	void deleteByNodoPianoId(Long idNodo);

	List<RisorsaUmanaNodoPiano> findByNodoPianoIdIn(List<Long> items);

	List<RisorsaUmanaNodoPiano> findByNodoPianoIdAndRisorsaUmanaPoliticoIsFalseOrderByRisorsaUmanaCognomeAscRisorsaUmanaNomeAsc(
			Long idNodo);

	List<RisorsaUmanaNodoPiano> findByNodoPianoCodiceCompletoStartsWithAndNodoPianoDateDeleteIsNull(String codiceCompleto);

	boolean existsByRisorsaUmanaAndNodoPiano(RisorsaUmana valutato, NodoPiano nodo);

	long countByNodoPianoAnno(Integer anno);

	List<RisorsaUmanaNodoPiano> findByRisorsaUmanaIdAndNodoPianoDateDeleteIsNull(Long id);




}
