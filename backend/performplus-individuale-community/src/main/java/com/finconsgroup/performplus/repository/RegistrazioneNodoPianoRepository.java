package com.finconsgroup.performplus.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.domain.RegistrazioneNodoPiano;

@Repository
public interface RegistrazioneNodoPianoRepository
		extends JpaRepository<RegistrazioneNodoPiano, Long>, JpaSpecificationExecutor<RegistrazioneNodoPiano> {

	List<RegistrazioneNodoPiano> findByRegistrazioneId(Long idRegistrazione);

	RegistrazioneNodoPiano findByRegistrazioneIdAndNodoPianoId(Long idRegistrazione, Long idNodo);

	RegistrazioneNodoPiano findByRegistrazioneAndNodoPiano(Registrazione Registrazione, NodoPiano nodo);

	@Query("SELECT SUM(m.peso) FROM RegistrazioneNodoPiano m where m.registrazione.id=:idRegistrazione")
	BigDecimal sumByRegistrazioneId(@Param("idRegistrazione") Long idRegistrazione);

	@Modifying(clearAutomatically = true)
	@Query("update RegistrazioneNodoPiano m set m.peso=:peso where m.id=:id")
	void updatePeso(@Param("peso") BigDecimal peso, @Param("id") Long id);

	List<RegistrazioneNodoPiano> findByRegistrazioneOrderByNodoPianoCodiceCompleto(Registrazione Registrazione);

	void deleteByRegistrazione(Registrazione reg);

	List<RegistrazioneNodoPiano> findByRegistrazione(Registrazione registrazione);


}
