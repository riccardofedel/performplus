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


@Repository
public interface AmbitoValutazioneRepository extends JpaRepository<AmbitoValutazione, Long>, JpaSpecificationExecutor<AmbitoValutazione> {

	List<AmbitoValutazione> findByQuestionarioIdOrderByCodiceCompleto( Long idQuestionario);

	List<AmbitoValutazione> findByQuestionarioIdAndCodiceCompletoStartsWithOrderByCodiceCompleto(Long id,
			String codiceCompleto);

	boolean existsByPadreIdAndCodice(Long idPadre, String codice);

	boolean existsByQuestionarioIdAndPadreIsNullAndCodice(Long idQuestionario, String codice);

	@Query("select sum(v.peso) from AmbitoValutazione v where v.padre is not null and v.padre.id=:id and v.peso is not null")
	BigDecimal sumPesoByPadreIdAndPesoIsNotNull(@Param("id") Long id);

	@Query("select sum(v.peso) from AmbitoValutazione v where v.questionario.id=:id and v.padre is null and v.peso is not null")
	BigDecimal sumPesoByQuestionarioIdAndPadreIsNullAndPesoIsNotNull(@Param("id")Long id);

	@Modifying
	@Query("UPDATE AmbitoValutazione set peso = :peso where id = :id")
	void updatePeso(@Param("id") Long id, @Param("peso") BigDecimal peso);

	void deleteByQuestionario(Questionario q);

	AmbitoValutazione findTopByQuestionarioIdOrderByCodiceDesc(Long idPadre);

	List<AmbitoValutazione> findByQuestionarioIdInOrderByCodiceCompleto(List<Long> ids);

	List<AmbitoValutazione> findByQuestionarioOrderByCodice(Questionario q);

	AmbitoValutazione findTopByQuestionarioAndFogliaIsTrueAndPesoMancataAssegnazioneIsNotNull(
			Questionario questionario);

	AmbitoValutazione findTopByQuestionarioAndFogliaIsTrueAndPesoMancatoColloquioIsNotNull(Questionario questionario);

}
