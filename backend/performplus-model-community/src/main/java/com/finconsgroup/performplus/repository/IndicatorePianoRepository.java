package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.IndicatorePiano;

/**
 * Spring Data repository for the IndicatorePiano entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndicatorePianoRepository
		extends JpaRepository<IndicatorePiano, Long>, JpaSpecificationExecutor<IndicatorePiano> {

	List<IndicatorePiano> findByNodoPianoId(Long idNodoPiano);

	List<IndicatorePiano> findByNodoPianoIdIn(List<Long> nodi);
	
	IndicatorePiano findTopByNodoPianoId(Long idNodoPiano);

	@Modifying
	@Query("update IndicatorePiano ip set ip.note = :note where ip.id = :id")
	int updateNote(@Param("note") String note, @Param("id") Long id);

	@Modifying
	@Query("update IndicatorePiano ip set ip.raggiungimentoForzato = :raggiungimentoForzato where ip.id = :id")
	int updateRaggiungimentoForzato(@Param("raggiungimentoForzato") Double raggiungimentoForzato, @Param("id") Long id);

	@Modifying
	@Query("update IndicatorePiano ip set ip.nonValutabile = :nonValutabile where ip.id = :id")
	int updateNonValutabile(@Param("nonValutabile") Boolean nonValutabile, @Param("id") Long id);

	List<IndicatorePiano> findByNodoPianoIdEnteAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(Long idEnte, Integer anno);

	int countByNodoPianoIdEnteAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(Long idEnte, Integer anno);

	@Modifying
	@Query("update IndicatorePiano ip set ip.raggiungimentoForzato = :raggiungimentoForzato"
			+ ", ip.nonValutabile = :nonValutabile, ip.noteRaggiungimentoForzato = :noteRaggiungimentoForzato where ip.id = :id")
	int modificaForzatura(@Param("id") Long id, @Param("raggiungimentoForzato") Double raggiungimentoForzato,
			@Param("nonValutabile") Boolean nonValutabile,
			@Param("noteRaggiungimentoForzato") String noteRaggiungimentoForzato);

	@Modifying
	@Query("update IndicatorePiano ip set ip.richiestaForzatura = :richiestaForzatura, ip.noteRichiestaForzatura=:noteRichiestaForzatura where ip.id = :id")
	int modificaRichiestaForzatura(@Param("id") Long id, @Param("richiestaForzatura") Double richiestaForzatura,
			@Param("noteRichiestaForzatura") String noteRichiestaForzatura);

	@Modifying
	@Query("update IndicatorePiano ip set ip.peso = :peso where ip.id = :id")
	void aggiornaPeso(@Param("id") Long id, @Param("peso") Double peso);

	boolean existsByIndicatoreIdAndNodoPianoDateDeleteIsNull(Long indicatoreId);

	IndicatorePiano findByNodoPianoIdEnteAndNodoPianoAnnoAndCodiceInterno(Long idEnte, Integer aNNO, String codiceInterno);

	List<IndicatorePiano> findByNodoPianoIdOrderBySpecificaAscIndicatoreDenominazioneAsc(Long id);

	long countByNodoPianoAnno(Integer anno);

}
