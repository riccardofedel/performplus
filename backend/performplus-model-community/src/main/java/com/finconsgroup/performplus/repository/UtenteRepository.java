package com.finconsgroup.performplus.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Utente;



@SuppressWarnings("unused")
@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long>, JpaSpecificationExecutor<Utente> , UtenteRepositoryAdvanced {


	@Query(nativeQuery = true, value="select * from utente where upper(nome) like '%'||upper(:testo)||'%'")
	List<Utente> findByNomeContainsIgnoreCase(@Param("testo")String testo);

	Utente findByUserid(String userid);

	Utente findByUseridAndPasswd(String userid, String encrypted);

	List<Utente> findByIdEnteOrderByNome(Long idEnte);
	
	Integer countByIdEnte(Long idEnte);

	Page<Utente> findByIdEnte(Long idEnte, Pageable pageable);

	@Query(nativeQuery = true, value="select * from utente where id_ente=:idEnte and upper(nome) like upper(:testo)||'%'")
	Page<Utente> findByIdEnteAndNomeStartsWithIgnoreCase(@Param("idEnte")Long idEnte, @Param("testo")String name, Pageable pageable);
	

	@Query(value = "select u from Utente u where idEnte=:idEnte and (upper(u.nome) like %:nome% or upper(u.userid) like %:nome%)")   
	Page<Utente> findByIdEnteAndUpperNomeLike(@Param("idEnte") Long idEnte, @Param("nome") String nome, Pageable pageable);

	Utente findByIdEnteAndCodiceInterno(Long idEnte, String codiceInterno);

	@Query(nativeQuery = true, value="select * from utente where id_ente=:idEnte and upper(codice_interno) = upper(:codiceInterno)")
	Utente findByIdEnteAndCodiceInternoIgnoreCase(@Param("idEnte") Long idEnte, @Param("codiceInterno")String codiceInterno);

	Utente findTopByIdEnteAndCodiceInterno(Long idEnte, String codiceInterno);


}
