package com.finconsgroup.performplus.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.AmbitoValutazione;
import com.finconsgroup.performplus.domain.Questionario;
import com.finconsgroup.performplus.domain.ValoreAmbito;


@Repository
public interface ValoreAmbitoRepository extends JpaRepository<ValoreAmbito, Long>, JpaSpecificationExecutor<ValoreAmbito> {


	List<ValoreAmbito> findByAmbitoValutazioneIdOrderByCodice(Long idNodo);

	List<ValoreAmbito> findByAmbitoValutazioneQuestionarioIdOrderByAmbitoValutazioneCodiceCompletoAscCodiceAsc(Long idQuestionario);

	void deleteByAmbitoValutazioneId(Long id);

	boolean existsByAmbitoValutazioneIdAndCodice(Long id, String codice);

	@Query("select sum(v.peso) from ValoreAmbito v where v.ambitoValutazione.id=:id")
	BigDecimal sumPesoByAmbitoValutazioneId(@Param("id") Long id);

	@Modifying
	@Query("UPDATE ValoreAmbito set peso = :peso where id = :id")
	void updatePeso(@Param("id") Long id, @Param("peso") BigDecimal peso);

	void deleteByAmbitoValutazioneQuestionario(Questionario q);

	ValoreAmbito findTopByAmbitoValutazioneIdOrderByCodiceDesc(Long idPadre);

	List<ValoreAmbito> findByAmbitoValutazioneQuestionarioIdAndAmbitoValutazioneCodiceCompletoStartsWithOrderByAmbitoValutazioneCodiceCompletoAscCodiceAsc(
			Long id, String string);

	List<ValoreAmbito> findByAmbitoValutazioneQuestionarioIdInOrderByAmbitoValutazioneCodiceCompletoAscCodiceAsc(
			List<Long> ids);

	List<ValoreAmbito> findByAmbitoValutazioneOrderByCodice(AmbitoValutazione a);

	
}
