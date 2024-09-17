package com.finconsgroup.performplus.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.ResponsabileNodo;


/**
 * Spring Data  repository for the ResponsabileNodo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponsabileNodoRepository extends JpaRepository<ResponsabileNodo, Long>, JpaSpecificationExecutor<ResponsabileNodo> {


	List<ResponsabileNodo> findByNodoPianoIdOrderByInizioValidita(Long idNodoPiano);

	List<ResponsabileNodo> findByResponsabileIdAndNodoPianoDateDeleteIsNullOrderByInizioValidita(Long idResponsabile);

	int countByNodoPianoIdEnteAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(Long idEnte,Integer anno);

	List<ResponsabileNodo> findByNodoPianoIdEnteAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(Long idEnte, Integer anno);

	ResponsabileNodo findByResponsabileIdAndNodoPianoIdAndInizioValidita(long idResponsabile,Long idNodoPiano, LocalDate inizioValidita);

	List<ResponsabileNodo> findByResponsabileIdAndNodoPianoIdOrderByInizioValidita(long idResponsabile,Long idNodoPiano);

	List<ResponsabileNodo> findByNodoPianoId(Long id);

	ResponsabileNodo findTopByResponsabileIdAndNodoPianoIdOrderByInizioValiditaDesc(Long idResponsabile, Long idNodoPiano);


}
