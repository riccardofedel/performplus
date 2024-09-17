package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.domain.SchedaValutazioneIndicatore;
import com.finconsgroup.performplus.domain.SchedaValutazioneNodo;


@Repository
public interface SchedaValutazioneIndicatoreRepository extends JpaRepository<SchedaValutazioneIndicatore, Long>, JpaSpecificationExecutor<SchedaValutazioneIndicatore> {

	List<SchedaValutazioneIndicatore> findBySchedaValutazioneNodoOrderByIndicatorePianoSpecifica(SchedaValutazioneNodo schedaValutazioneNodo);
	SchedaValutazioneIndicatore findBySchedaValutazioneNodoAndIndicatorePiano(SchedaValutazioneNodo schedaValutazioneNodo, IndicatorePiano indicatore);
	void deleteBySchedaValutazioneNodoSchedaValutazioneRegistrazione(Registrazione registrazione);
	void deleteByIndicatorePianoAndSchedaValutazioneNodoSchedaValutazioneRegistrazione(IndicatorePiano indicatorePiano,
			Registrazione registrazione);
	
}
