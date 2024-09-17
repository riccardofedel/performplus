package com.finconsgroup.performplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Ente;


/**
 * Spring Data  repository for the Contesto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnteRepository extends JpaRepository<Ente, Long>, JpaSpecificationExecutor<Ente> {


}
