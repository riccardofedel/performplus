package com.finconsgroup.performplus.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.Valutazione;
import com.finconsgroup.performplus.enumeration.TipoValutazione;


/**
 * Spring Data  repository for the Valutazione entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValutazioneRepository extends JpaRepository<Valutazione, Long>, JpaSpecificationExecutor<Valutazione> {

	Valutazione findTopByIndicatorePianoIdAndInizioAndScadenzaAndTipoValutazioneOrderByScadenzaDesc(Long idIndicatorePiano,
			LocalDate inizio, LocalDate scadenza, TipoValutazione tipoValutazione);

	List<Valutazione> findByIndicatorePianoOrderByAnnoAscPeriodoAsc(IndicatorePiano indicatorePiano);
	

	void deleteByIndicatorePianoId(Long id);

	List<Valutazione> findByIndicatorePianoNodoPianoIdOrderByIndicatorePianoIdAscAnnoAscPeriodoAsc(Long idNodoPiano);

	List<Valutazione> findByIndicatorePianoIdOrderByAnnoAscPeriodoAsc(Long idIndicatorePiano);

	List<Valutazione> findByIndicatorePianoNodoPianoPianoIdOrderByIndicatorePianoNodoPianoCodiceCompletoAscIndicatorePianoIdAscAnnoAscPeriodoAsc(Long idPiano);

	List<Valutazione> findByIndicatorePianoIdOrderByAnnoAscPeriodoAscDataRilevazioneAsc(Long idIndicatorePiano);

	Valutazione findByIndicatorePianoIdAndTipoValutazioneAndAnnoAndPeriodo(Long idIndicatorePiano,
			TipoValutazione tipoValutazione, Integer anno, Integer periodo);

	Valutazione findTopByIndicatorePianoIdAndTipoValutazioneOrderByAnnoDescPeriodoDesc(Long idIndicatorPiano,TipoValutazione tipoValutazione);


	List<Valutazione> findByIndicatorePianoIdAndAnnoAndTipoValutazioneOrderByPeriodo(Long idIndicatorePiano,
			Integer anno,
			TipoValutazione tipovalutazione);

	List<Valutazione> findByDateModifyGreaterThan(LocalDateTime last);

	List<Valutazione> findByIndicatorePianoNodoPianoIdEnteAndIndicatorePianoNodoPianoAnnoOrderByIndicatorePianoNodoPianoCodiceCompletoAscIndicatorePianoIdAscAnnoAscPeriodoAsc(
			Long idEnte, Integer anno);

	Valutazione findTopByIndicatorePianoIdAndTipoValutazioneAndAnnoGreaterThan(Long idIndicatorePiano, TipoValutazione tipoValutazione,
			Integer anno);

	List<Valutazione> findByIndicatorePianoAndTipoValutazioneOrderByAnnoAscPeriodoAsc(IndicatorePiano indicatorePiano,
			TipoValutazione tipoValutazione);

	Valutazione findTopByIndicatorePianoAndAnnoAndTipoValutazioneOrderByPeriodoDesc(IndicatorePiano indicatorePiano,
			Integer anno, TipoValutazione preventivo);

	long countByIndicatorePianoNodoPianoAnno(Integer anno);

}
