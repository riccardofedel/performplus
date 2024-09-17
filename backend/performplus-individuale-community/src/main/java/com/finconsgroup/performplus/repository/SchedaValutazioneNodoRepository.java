package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.domain.SchedaValutazione;
import com.finconsgroup.performplus.domain.SchedaValutazioneNodo;


@Repository
public interface SchedaValutazioneNodoRepository extends JpaRepository<SchedaValutazioneNodo, Long>, JpaSpecificationExecutor<SchedaValutazioneNodo> {

	List<SchedaValutazioneNodo> findBySchedaValutazioneOrderByNodoPianoCodiceCompleto(SchedaValutazione schedaValutazione);
	SchedaValutazioneNodo findBySchedaValutazioneAndNodoPiano(SchedaValutazione schedaValutazione, NodoPiano nodo);
	void deleteBySchedaValutazioneRegistrazione(Registrazione r);
	void deleteByNodoPianoAndSchedaValutazioneRegistrazione(NodoPiano nodoPiano, Registrazione registrazione);
	

}
