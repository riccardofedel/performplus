package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.domain.ValutazioneRegistrazione;


@Repository
public interface ValutazioneRegistrazioneRepository extends JpaRepository<ValutazioneRegistrazione, Long>, JpaSpecificationExecutor<ValutazioneRegistrazione> {

	List<ValutazioneRegistrazione> findByRegistrazione(Registrazione registrazione);

	void deleteByRegistrazioneId(long idRegistrazione);

	List<ValutazioneRegistrazione> findByRegistrazioneOrderByValoreAmbitoAmbitoValutazioneCodiceCompletoAsc(
			Registrazione r);
}
