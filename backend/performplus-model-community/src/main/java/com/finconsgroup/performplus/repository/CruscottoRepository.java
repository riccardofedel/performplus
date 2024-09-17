package com.finconsgroup.performplus.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finconsgroup.performplus.domain.Cruscotto;


/**
 * Spring Data  repository for the Contesto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CruscottoRepository extends JpaRepository<Cruscotto, Long>, JpaSpecificationExecutor<Cruscotto> {

	Optional<Cruscotto> findByIdEnteAndAnno(Long idEnte, Integer anno);

	int countByIdEnte(Long idEnte);

	List<Cruscotto> findByIdEnte(Long idEnte);
	
	@Query("select c from Cruscotto c where c.dataAttivazionePI<=:oggi and (c.dataChiusuraPI is null or c.dataChiusuraPI>:oggi)")
	List<Cruscotto> findByDataAttivazionePILessThanEqualAndFlagAttivazionePIIsTrueAndDataChiusuraPIGreaterThan(
			@Param("oggi") LocalDate oggi);

	@Query(nativeQuery = true,
			value = "select \n"
					+ "  np.id_ente as id_ente\n"
					+ ", piano.codice_interno as piano_codice\n"
					+ ", np.tipo_nodo"
					+ ", np.codice_completo as np_codice\n"
					+ ", np.denominazione as np_denominazione\n"
					+ ", np.inizio\n"
					+ ", np.scadenza\n"
					+ ", np.capitolo_bilancio \n"
					+ ", np.stato_nodo_piano"
					+ ", np.stato_proposta"
					+ ", np.strategico \n"
					+ ", np.note\n"
					+ ", target.anno as target_anno\n"
					+ ", target.periodo as target_periodo\n"
					+ ", target.valore_numerico as target_valore\n"
					+ ", rend.anno as rend_anno\n"
					+ ", rend.periodo as rend_periodo\n"
					+ ", rend.valore_numerico as rend_valore\n"
					+ ", piano.anno\n"
					+ ", piano.anno_inizio\n"
					+ ", piano.anno_fine\n"
					+ ", case when np.tipo_nodo = 'PIANO' then ''\n"
					+ "       else padre.codice_completo\n"
					+ "  end as padre_codice\n"
					+ ", case when np.tipo_nodo = 'AZIONE' then ru.cognome \n"
					+ "       when np.tipo_nodo = 'RISULTATO_ATTESO' then coalesce(ru.cognome,ro.cognome)\n"
					+ "       else '' \n"
					+ "  end as cognome\n"
					+ ", case when np.tipo_nodo = 'AZIONE' then ru.nome \n"
					+ "       when np.tipo_nodo = 'RISULTATO_ATTESO' then coalesce(ru.nome,ro.nome)\n"
					+ "       else '' \n"
					+ "  end as nome\n"
					+ ", case when np.tipo_nodo = 'AZIONE' then ru.codice_fiscale \n"
					+ "       when np.tipo_nodo = 'RISULTATO_ATTESO' then coalesce(ru.codice_fiscale,ro.codice_fiscale)\n"
					+ "       else '' \n"
					+ "  end as codice_fiscale\n"
					+ ", o.codice_interno as direzione_codice"
					+ ", o.intestazione as direzione_desc\n"
					+ ", array_to_string(array(select tag.codice_completo from tag_nodo_piano tagnp join tag on tag.id=tagnp.tag_id\n"
					+ "       where tagnp.nodopiano_id =np.id order by tag.codice_completo),' | ') as tags\n"
					+ ", array_to_string(array(select terr.codice_completo from territorio_nodo_piano tnp join territorio terr on terr.id=tnp.territorio_id \n"
					+ "       where tnp.nodopiano_id =np.id order by terr.codice_completo),' | ' ) as territori\n"
					+ ", np.altro_territorio as terr_altro\n"
					+ "from nodo_piano np\n"
					+ "left join indicatore_piano ip on ip.nodo_piano_id =np.id \n"
					+ "left join valutazione target on target.indicatore_piano_id =ip.id and target.tipo_valutazione ='PREVENTIVO'\n"
					+ "left join valutazione rend on rend.indicatore_piano_id =ip.id and rend.tipo_valutazione ='CONSUNTIVO'\n"
					+ "left join organizzazione o on o.id=np.organizzazione_id \n"
					+ "left join risorsa_umana ro on ro.id=o.responsabile_id \n"
					+ "left join risorsa_umana ru on ru.id=np.responsabile_id \n"
					+ "left join nodo_piano padre on padre.id=np.padre_id \n"
					+ "left join fonteFinanziamento_nodo_piano snp on snp.nodopiano_id =np.id\n"
					+ "left join territorio_nodo_piano tnp on tnp.nodopiano_id =np.id\n"
					+ "left join territorio terr on terr.id=tnp.territorio_id \n"
					+ "join nodo_piano piano on piano.id=np.piano_id \n"
					+ "where np.date_delete  is null \n"
					+ "and np.tipo_nodo <> 'PIANO'\n"
					+ "and np.id_ente =:idEnte\n"
					+ "and piano.anno=:anno\n"
					+ "order by np_codice, target_anno, target_periodo,rend_anno, rend_periodo;")
    List<Object[]> cubo(@Param("idEnte") Long idEnte, @Param("anno") Integer anno);


}
