package com.finconsgroup.performplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.SchedaValutazioneNodo;


@Repository
public interface DettaglioRisultatoValutazioneRepository extends JpaRepository<SchedaValutazioneNodo, Long>, JpaSpecificationExecutor<SchedaValutazioneNodo> {


}
