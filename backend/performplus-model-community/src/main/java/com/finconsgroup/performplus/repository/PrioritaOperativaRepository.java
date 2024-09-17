package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.PrioritaOperativa;


/**
 * Spring Data  repository for the PrioritaOperativa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrioritaOperativaRepository extends JpaRepository<PrioritaOperativa, Long>, JpaSpecificationExecutor<PrioritaOperativa> {

	PrioritaOperativa findByOrganizzazioneAndNodoPiano(Organizzazione organizzzione, NodoPiano nodoPiano);

	PrioritaOperativa findByOrganizzazioneIdAndNodoPianoId(Long organizzazioneId, Long nodoPianoId);

	List<PrioritaOperativa> findByOrganizzazioneIdAndNodoPianoDateDeleteIsNull(Long organizzazioneId);

	List<PrioritaOperativa> findByNodoPianoId(Long nodoPianoId);

	List<PrioritaOperativa> findByOrganizzazioneIdInAndNodoPianoDateDeleteIsNull(List<Long> items);

	List<PrioritaOperativa> findByOrganizzazioneIdEnteAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(Long idEnte, Integer anno);
	int countByOrganizzazioneIdEnteAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(Long idEnte, Integer anno);

	void deleteByNodoPianoId(Long id);

}
