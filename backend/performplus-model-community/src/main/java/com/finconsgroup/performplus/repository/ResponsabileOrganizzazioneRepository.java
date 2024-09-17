package com.finconsgroup.performplus.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.ResponsabileOrganizzazione;


/**
 * Spring Data  repository for the ResponsabileOrganizzazione entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponsabileOrganizzazioneRepository extends JpaRepository<ResponsabileOrganizzazione, Long>, JpaSpecificationExecutor<ResponsabileOrganizzazione> {


	List<ResponsabileOrganizzazione> findByOrganizzazioneIdOrderByInizioValidita(Long idOrganizzazione);

	List<ResponsabileOrganizzazione> findByResponsabileIdOrderByInizioValidita(Long idResponsabile);

	int countByOrganizzazioneIdEnteAndOrganizzazioneAnno(Long idEnte,Integer anno);

	List<ResponsabileOrganizzazione> findByOrganizzazioneIdEnteAndOrganizzazioneAnno(Long idEnte, Integer anno);
	
	ResponsabileOrganizzazione findByResponsabileIdAndOrganizzazioneIdAndInizioValidita(long idResponsabile,Long idOrganizzaione, LocalDate inizioValidita);


	List<ResponsabileOrganizzazione> findByResponsabileIdAndOrganizzazioneIdOrderByInizioValidita(long idResponsabile,Long idOrganizzaione);

	List<ResponsabileOrganizzazione> findByResponsabileId(Long idResponsabile);

	ResponsabileOrganizzazione findTopByOrganizzazioneIdOrderByInizioValiditaDescFineValiditaDesc(Long idOrganizzazione);

	ResponsabileOrganizzazione findTopByResponsabileIdOrderByInizioValiditaDescFineValiditaDesc(Long idResponsabile);

	ResponsabileOrganizzazione findTopByResponsabileIdAndOrganizzazioneIdOrderByInizioValiditaDesc(Long idResp, Long idOrg);

	List<ResponsabileOrganizzazione> findByResponsabileIdAndOrganizzazioneIdOrderByInizioValiditaDesc(Long idResp,
			Long idOrg);


}
