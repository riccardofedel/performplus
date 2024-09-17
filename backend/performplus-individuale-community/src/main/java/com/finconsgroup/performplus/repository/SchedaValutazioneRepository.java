package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.SchedaValutato;
import com.finconsgroup.performplus.domain.SchedaValutazione;


@Repository
public interface SchedaValutazioneRepository extends JpaRepository<SchedaValutazione, Long>, JpaSpecificationExecutor<SchedaValutazione> {

	List<SchedaValutazione> findBySchedaValutato(SchedaValutato schedaValutato);
	SchedaValutazione findByRegistrazione(Registrazione registrazione);
	List<SchedaValutazione> findByRegistrazioneValutatoAndRegistrazioneOrganizzazioneAndRegistrazioneResponsabile(
			RisorsaUmana resp, Organizzazione org, boolean responsabile);
	void deleteByRegistrazione(Registrazione r);
	List<SchedaValutazione> findByRegistrazioneValutatoAndRegistrazioneResponsabileAndRegistrazioneInattivaAndRegistrazioneInterim(
			RisorsaUmana valutato, boolean responsabile, boolean inattiva, boolean interim);
	

}
