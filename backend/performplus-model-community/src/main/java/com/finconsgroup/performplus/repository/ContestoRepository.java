package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Contesto;
import com.finconsgroup.performplus.domain.NodoPiano;


/**
 * Spring Data  repository for the Contesto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContestoRepository extends JpaRepository<Contesto, Long>, JpaSpecificationExecutor<Contesto> {

	Contesto findByPianoAndRelazione(NodoPiano piano, boolean isRelazione);

	Contesto findByPianoIdAndRelazione(Long idPiano, boolean isRelazione);

	List<Contesto> findByPianoIdEnteAndPianoAnnoAndPianoDateDeleteIsNull(Long idEnte, int anno);

	int countByPianoIdEnteAndPianoAnnoAndPianoDateDeleteIsNull(Long idEnte, int anno);
	
	List<Contesto> findByIdEnteAndAnnoAndPianoDateDeleteIsNull(Long idEnte, Integer anno);

	int countByIdEnteAndAnnoAndPianoDateDeleteIsNull(Long idEnte, Integer anno);

	Contesto findByIdEnteAndAnnoAndRelazioneIsFalseAndPianoDateDeleteIsNull(Long idEnte, Integer anno);

}
