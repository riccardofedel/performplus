package com.finconsgroup.performplus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Configurazione;


/**
 * Spring Data  repository for the Sysconfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurazioneRepository extends JpaRepository<Configurazione, Long>, JpaSpecificationExecutor<Configurazione> {

	Optional<Configurazione> findByIdEnteAndAnno(Long idEnte, Integer anno);

	List<Configurazione> findByIdEnte(Long idEnte);
	
	int countByIdEnte(Long idEnte);


}
