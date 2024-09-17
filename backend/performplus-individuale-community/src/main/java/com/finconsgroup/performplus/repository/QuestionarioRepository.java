package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Questionario;


@Repository
public interface QuestionarioRepository extends JpaRepository<Questionario, Long>, JpaSpecificationExecutor<Questionario> {

	int countByIdEnte(Long idEnte);

	List<Questionario> findByIdEnte(Long idEnte);

	
	@Query(nativeQuery = true, value="select count(*) from questionario where id_ente=:idEnte and upper(intestazione) like upper(:testo)")
	int existsByIdEnteAndIntestazioneIgnoreCase(@Param("idEnte")Long idEnte,@Param("testo")String intestazione);

	List<Questionario> findByIdEnteOrderByIntestazione(Long idEnte);

}
