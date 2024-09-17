package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.ContenutoContesto;
import com.finconsgroup.performplus.domain.Contesto;
import com.finconsgroup.performplus.domain.NodoPiano;


/**
 * Spring Data  repository for the Contesto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContenutoContestoRepository extends JpaRepository<ContenutoContesto, Long>, JpaSpecificationExecutor<Contesto> {

	List<ContenutoContesto> findByPiano(NodoPiano piano);

	List<ContenutoContesto> findByPianoId(Long idPiano);

	List<ContenutoContesto> findByPianoIdEnteAndPianoAnno(Long idEnte, Integer anno);

	int countByPianoIdEnteAndPianoAnno(Long idEnte, Integer anno);
	
	ContenutoContesto findByPianoIdAndNome(Long id, String nome);

	ContenutoContesto findByPianoAndGruppoAndNome(NodoPiano piano, String gruppo, String nome);

}
