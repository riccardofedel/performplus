package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.ImageEntry;


/**
 * Spring Data  repository for the ImageEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageEntryRepository extends JpaRepository<ImageEntry, Long>, JpaSpecificationExecutor<ImageEntry> {

	ImageEntry findByIdEnteAndNome(Long idEnte, String nome);

	ImageEntry findByRisorsaId(Long id);

	int countByIdEnte(Long idEnte);

	List<ImageEntry> findByIdEnte(Long idEnte);

}
