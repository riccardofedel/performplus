package com.finconsgroup.performplus.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finconsgroup.performplus.domain.IndicatorePiano;
import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.domain.RegistrazioneIndicatorePiano;
import com.finconsgroup.performplus.domain.RegistrazioneNodoPiano;


public interface RegistrazioneIndicatorePianoRepository extends JpaRepository<RegistrazioneIndicatorePiano, Long>, JpaSpecificationExecutor< RegistrazioneIndicatorePiano>
 {

	RegistrazioneIndicatorePiano findByRegistrazioneNodoPianoIdAndIndicatorePianoId(Long idRegistrazioneNodoPiano, Long idIndicatorePiano);

	RegistrazioneIndicatorePiano findByRegistrazioneNodoPianoAndIndicatorePiano(RegistrazioneNodoPiano registrazioneNodoPiano, IndicatorePiano indicatorePiano);

	@Query("SELECT SUM(m.peso) FROM RegistrazioneIndicatorePiano m where m.registrazioneNodoPiano.id=:idRegistrazioneNodoPiano")
	BigDecimal sumByRegistrazioneNodoPianoId(@Param("idRegistrazioneNodoPiano") Long idRegistrazioneNodoPiano);

	@Modifying(clearAutomatically = true)
	@Query("update RegistrazioneIndicatorePiano m set m.peso=:peso where m.id=:id")
	void updatePeso(@Param("peso")BigDecimal peso, @Param("id")Long idRegistrazioneIndicatorePiano);

	List<RegistrazioneIndicatorePiano> findByRegistrazioneNodoPianoOrderByIndicatorePianoNodoPianoCodiceCompletoAscIndicatorePianoSpecificaAsc(
			RegistrazioneNodoPiano registrazioneNodoPiano);

	RegistrazioneIndicatorePiano findByRegistrazioneNodoPianoRegistrazioneIdAndIndicatorePianoId(Long idRegistrazione,
			Long idIndicatorePiano);

	List<RegistrazioneIndicatorePiano> findByRegistrazioneNodoPianoRegistrazioneOrderByIndicatorePianoNodoPianoCodiceCompletoAscIndicatorePianoSpecificaAsc(
			Registrazione registrazione);

	void deleteByRegistrazioneNodoPianoRegistrazione(Registrazione reg);

	void deleteByRegistrazioneNodoPiano(RegistrazioneNodoPiano rn);

	List<RegistrazioneIndicatorePiano> findByRegistrazioneNodoPianoRegistrazione(Registrazione registrazione);



}
