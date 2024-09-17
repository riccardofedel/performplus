package com.finconsgroup.performplus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.TemplateData;


/**
 * Spring Data  repository for the Contesto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TemplateDataRepository extends JpaRepository<TemplateData, Long>, JpaSpecificationExecutor<TemplateData> {

	TemplateData findByIdEnteAndContainerAndTypeIsNull(Long idEnte, String container);

	int countByIdEnte(Long idEnte);

	List<TemplateData> findByIdEnte(Long idEnte);

	TemplateData findByIdEnteAndContainerAndType(Long idEnte, String container, String type);

}
