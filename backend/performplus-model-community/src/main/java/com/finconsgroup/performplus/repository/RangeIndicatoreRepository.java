package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.RangeIndicatore;



@SuppressWarnings("unused")
@Repository
public interface RangeIndicatoreRepository extends JpaRepository<RangeIndicatore, Long>, JpaSpecificationExecutor<RangeIndicatore> {

	List<RangeIndicatore> findByIndicatorePianoId(Long idIndicatorePiano);

	int countByIndicatorePianoNodoPianoIdEnteAndIndicatorePianoNodoPianoAnnoAndIndicatorePianoNodoPianoDateDeleteIsNull(Long idEnte, Integer anno);

	List<RangeIndicatore> findByIndicatorePianoNodoPianoIdEnteAndIndicatorePianoNodoPianoAnnoAndIndicatorePianoNodoPianoDateDeleteIsNull(Long idEnte, Integer anno);

	void deleteByIndicatorePianoId(Long id);

	List<RangeIndicatore> findByIndicatorePianoIdOrderByMinimoAsc(Long id);

}
