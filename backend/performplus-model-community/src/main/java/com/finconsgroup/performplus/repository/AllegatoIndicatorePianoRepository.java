package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.AllegatoIndicatorePiano;
import com.finconsgroup.performplus.domain.IndicatorePiano;


@Repository
public interface AllegatoIndicatorePianoRepository extends JpaRepository<AllegatoIndicatorePiano, Long>, JpaSpecificationExecutor<AllegatoIndicatorePiano> {

 	List<AllegatoIndicatorePiano> findByIndicatorePiano(IndicatorePiano indicatorePiano);

	int countByIndicatorePianoNodoPianoIdEnteAndIndicatorePianoNodoPianoDateDeleteIsNull(Long idEnte);
	
	List<AllegatoIndicatorePiano> findByIndicatorePianoNodoPianoIdEnteAndIndicatorePianoNodoPianoDateDeleteIsNull(Long idEnte);

	List<AllegatoIndicatorePiano> findByIndicatorePianoId(Long idIndicatorePiano);

	AllegatoIndicatorePiano findByIndicatorePianoIdAndNome(Long idIndicatorePiano, String nome);



}
