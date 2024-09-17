package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.domain.Regolamento;
import com.finconsgroup.performplus.domain.RisorsaUmana;


@Repository
public interface RegistrazioneRepository extends JpaRepository<Registrazione, Long>, JpaSpecificationExecutor<Registrazione> 
, RegistrazioneRepositoryAdvanced{

	List<Registrazione> findByQuestionarioIdAndAnnoOrderByValutatoCognomeAsc(Long idQuestionario, Integer anno);

	List<Registrazione> findByValutatoreIdOrderByValutatoCognomeAsc(Long idValutatore);

	List<Registrazione> findByValutatoIdOrderByValutatoCognomeAsc(Long idValutato);

	List<Registrazione> findByValutatoreIdAndValutatoIdOrderByValutatoCognomeAsc(Long idValutatore, Long idValutato);

	long countByRegolamento(Regolamento regolamento);

	long countByValutatoreId(Long idValutatore);

	long countByValutatoId(Long idValutato);

	List<Registrazione> findByValutatoreAndValutatoAndRegolamentoAndOrganizzazione(RisorsaUmana valutatore,
			RisorsaUmana valutato, Regolamento regolamento, Organizzazione organizzazione);

	List<Registrazione> findByidEnteAndAnnoOrderByValutatoIdAscInizioValiditaAscFineValiditaAsc(Long idEnte,
			Integer anno);

	List<Registrazione> findByIdEnteAndAnnoAndValutatoCodiceFiscaleOrderByInizioValidita(Long idEnte, Integer anno,
			String codiceFiscale);

	List<Registrazione> findByIdEnteAndAnnoAndValutatoCognomeAndValutatoNomeAndValutatoPoliticoIsFalseOrderByInizioValidita(
			Long idEnte, Integer anno, String cognome, String nome);

	List<Registrazione> findByValutatoIdOrderByInizioValiditaDesc(Long idValutato);

	List<Registrazione> findByidEnteAndAnnoOrderByValutatoIdAscInizioValiditaDescFineValiditaDesc(long l, Integer anno);

	Registrazione findTopByValutatoIdOrderByInizioValiditaDesc(Long idValutato);

	long countByValutatoreAndInattivaIsFalseAndInterimIsFalse(RisorsaUmana risorsa);

	List<Registrazione> findByValutatoAndRegolamentoAndOrganizzazioneAndPo(RisorsaUmana valutato,
			Regolamento regolamento, Organizzazione organizzazione, boolean po);


}
