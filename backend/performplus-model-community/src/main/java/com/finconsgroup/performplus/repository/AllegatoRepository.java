package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Allegato;


@Repository
public interface AllegatoRepository extends JpaRepository<Allegato, Long>, JpaSpecificationExecutor<Allegato> {

	Allegato findByIdEnteAndNome(Long idEnte, String nome);

	int countByIdEnte(Long idEnte);

	List<Allegato> findByIdEnte(Long idEnte);

}
