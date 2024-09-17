package com.finconsgroup.performplus.repository.advanced;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.enumeration.Fascia;
import com.finconsgroup.performplus.enumeration.TipologiaObiettiviOperativi;
import com.finconsgroup.performplus.rest.api.vm.v2.FiltroAlberoExtendedVM;
import com.finconsgroup.performplus.service.dto.Pesatura;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Component
public class NodoPianoRepositoryAdvancedImpl implements NodoPianoRepositoryAdvanced {
	@Autowired
	private EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(NodoPianoRepositoryAdvancedImpl.class);

	@Override
	public List<String> fontiFinanziamentoSql(Long idEnte, Integer anno, String codiceCompleto) {
		String sql = "SELECT distinct st.DESCRIZIONE"
				+ " FROM NODO_PIANO nodo inner join FONTE_FINANZIAMENTO_NODO_PIANO stn ON stn.NODOPIANO_ID=nodo.ID"
				+ " join FONTE_FINANZIAMENTO st ON stn.fonteFinanziamento_id=st.ID" + " WHERE nodo.ID_ENTE=:idEnte"
				+ " and nodo.CODICE_COMPLETO LIKE '" + codiceCompleto + ".%'"
				+ " AND nodo.id_ente=:idEnte and nodo.anno=:anno and nodo.dateDelete is null"
				+ " order by st.DESCRIZIONE";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("idEnte", idEnte);
		query.setParameter("anno", anno);
		List<String> items = query.getResultList();
		return items;
	}

	@Override
	public int modificaPathFigli(Long idEnte, Integer anno, String vecchio, String nuovo) {
		int pos = vecchio.length() + 1;
		String hql = "update NodoPiano nodo set nodo.codiceCompleto = concat('" + nuovo
				+ "', substring(nodo.codiceCompleto," + pos + ")) where nodo.idEnte=" + idEnte + " and nodo.anno="
				+ anno + " and nodo.codiceCompleto like '" + vecchio + ".%' and nodo.dateDelete is null";
		Query query = entityManager.createQuery(hql);
		return query.executeUpdate();
	}

	@Override
	public int modificaPesoRamo(BigDecimal peso, Long idEnte, Integer anno, String codiceCompleto) {
		String hql = "update " + " NodoPiano nodo" + " set nodo.peso=:peso"
				+ " WHERE nodo.idEnte=:idEnte and nodo.anno=:anno and nodo.codiceCompleto like " + codiceCompleto
				+ ".%' and nodo.dateDelete is null";
		Query query = entityManager.createQuery(hql);
		query.setParameter("peso", peso);
		return query.executeUpdate();

	}

	@Override
	public int modificaPeso(Pesatura pesatura) {
		boolean obiettivoOperativo = pesatura.isObiettivoOperativo();
		String hql = "update " + (obiettivoOperativo ? "ObiettivoOperativo" : "NodoPiano") + " nodo"
				+ " set nodo.peso=:peso" + ", userModify=:user , dateModify=:date"
				+ " , LivelloStrategicita=:ls, LivelloComplessita=:lc";
		if (obiettivoOperativo) {
			hql += " , tipologia=:tipologia";
		}
		hql += " WHERE nodo.id=:id";
		Query query = entityManager.createQuery(hql);
		query.setParameter("peso", BigDecimal.valueOf(pesatura.getPeso()));
		query.setParameter("ls", pesatura.getLivelloStrategicita());
		query.setParameter("lc", pesatura.getLivelloComplessita());
		if (obiettivoOperativo) {
			query.setParameter("tipologia", pesatura.getTipologia());
		}
		query.setParameter("id", pesatura.getId());
		return query.executeUpdate();
	}

	@Override
	public int modificaPeso(Long id, Fascia LivelloStrategicita, Fascia LivelloComplessita,
			TipologiaObiettiviOperativi tipologia, Double peso) {
		String hql = "update NodoPiano  nodo" + " set nodo.peso=:peso"
				+ ", LivelloStrategicita=:ls, LivelloComplessita=:lc" + " , tipologia=:tipologia"
				+ " WHERE nodo.id=:id";
		Query query = entityManager.createQuery(hql);
		query.setParameter("peso", BigDecimal.valueOf(peso));
		query.setParameter("ls", LivelloStrategicita);
		query.setParameter("lc", LivelloComplessita);
		query.setParameter("tipologia", tipologia);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	@Override
	public List<NodoPiano> searchByFilter(FiltroAlberoExtendedVM parametri) {
		final Query query = getQueryNative(parametri);
		return query.getResultList();
	}

	@Override
	public Page<NodoPiano> searchByFilter(FiltroAlberoExtendedVM parametri, Pageable pageable) {
		Query queryCount = getQueryNativeCount(parametri);

		long totalRows = (Long) queryCount.getSingleResult();

		Query query = getQueryNative(parametri);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new PageImpl<>(query.getResultList(), pageable, totalRows);

	}

	private Query getQueryNative(FiltroAlberoExtendedVM parametri) {
		StringBuilder hql = select(parametri, false);
		logger.info(">>>sql:" + hql.toString());
		Query query = entityManager.createNativeQuery(hql.toString(), NodoPiano.class);
		parametri(parametri, query);
		return query;
	}

	private Query getQueryNativeCount(FiltroAlberoExtendedVM parametri) {
		StringBuilder hql = select(parametri, true);
		logger.info(">>>sql:" + hql.toString());
		Query query = entityManager.createNativeQuery(hql.toString(), Long.class);
		parametri(parametri, query);
		return query;
	}

	private void parametri(final FiltroAlberoExtendedVM parametri, final Query query) {
		query.setParameter("idEnte", parametri.getIdEnte());
		query.setParameter("anno", parametri.getAnno());
		if (StringUtils.isNotBlank(parametri.getTesto())) {
			query.setParameter("testo", '%' + parametri.getTesto().trim().toUpperCase() + "%");
		}
		if (StringUtils.isNotBlank(parametri.getCodiceCompletoPadre())) {
			query.setParameter("codiceCompleto", parametri.getCodiceCompletoPadre().trim() + "%");
		}
		if (parametri.getIdResponsabile() != null) {
			query.setParameter("idResponsabile", parametri.getIdResponsabile());
		}
		if (parametri.getIdRisorsa() != null) {
			query.setParameter("idRisorsa", parametri.getIdRisorsa());
		}
		if (parametri.getIdDirezione() != null) {
			query.setParameter("idDirezione", parametri.getIdDirezione());
			query.setParameter("ccDir", parametri.getCodiceCompletoDirezione().trim() + ".%");
		}
		if(parametri.getStrutture()!=null && !parametri.getStrutture().isEmpty()) {
			query.setParameter("strutture", parametri.getStrutture());
		}
		if (parametri.getTipiNodo() != null && !parametri.getTipiNodo().isEmpty()) {
			query.setParameter("tipiNodo", parametri.getTipiNodo());
		}
		if (StringUtils.isNotBlank(parametri.getCodiceCompleto())) {
			String cc = codiceCompleto(parametri.getCodiceCompleto(), parametri.getAnno());
			query.setParameter("codiceCompleto", cc);
		} else if (StringUtils.isNotBlank(parametri.getCodiceInterno())) {
			String cc = codiceInterno(parametri.getCodiceInterno());
			query.setParameter("codiceInterno", cc);
		} else if (StringUtils.isNotBlank(parametri.getCodice())) {
			query.setParameter("codiceCompleto", codiceCompleto(parametri.getCodice(), parametri.getAnno()));
			query.setParameter("codiceInterno", codiceInterno(parametri.getCodice()));
		}

	}

	private StringBuilder select(FiltroAlberoExtendedVM parametri, boolean count) {

		StringBuilder hql = new StringBuilder();
		if (count) {
			hql.append("SELECT count(*) FROM nodo_piano e");
		} else {
			hql.append("SELECT e.* FROM nodo_piano e");
		}
		if (parametri.getIdResponsabile() != null || StringUtils.isNotBlank(parametri.getCodiceCompletoDirezione())) {
			hql.append(" JOIN organizzazione o ON e.organizzazione_id=o.id");
		}
		

		hql.append(" WHERE e.id_ente = :idEnte and e.anno=:anno");
		if (StringUtils.isNotBlank(parametri.getTesto())) {
			hql.append(" and upper(e.denominazione) like :testo");
		}
		if (StringUtils.isNotBlank(parametri.getCodiceCompletoPadre())) {
			hql.append(" and e.codice_completo like :codiceCompleto");
		}
		if (parametri.getIdResponsabile() != null) {
			hql.append(" and o.responsabile_id=:idResponsabile");
		}
		if (parametri.getIdRisorsa() != null) {
			hql.append(
					" and exists(select rn.id from risorsa_umana_nodo_piano rn where e.id=rn.nodo_piano_id and rn.risorsa_umana_id=:idRisorsa)");
		}
		if (parametri.getIdDirezione() != null) {
			hql.append(" and ( e.organizzazione_id = :idDirezione or o.codice_completo like :ccDir)");
		}
		if(parametri.getStrutture()!=null && !parametri.getStrutture().isEmpty()) {
			hql.append(" and e.organizzazione_id in :strutture");	
		}

		if (parametri.getTipiNodo() != null && !parametri.getTipiNodo().isEmpty()) {
			hql.append(" and e.tipo_nodo in :tipiNodo");
		}
		if (StringUtils.isNotBlank(parametri.getCodiceCompleto())) {
			if (parametri.getCodiceCompleto().endsWith("%")) {
				hql.append(" and e.codice_completo like :codiceCompleto");
			} else {
				hql.append(" and e.codice_completo=:codiceCompleto");
			}
		} else if (StringUtils.isNotBlank(parametri.getCodiceInterno())) {
			if (parametri.getCodiceInterno().endsWith("%")) {
				hql.append(" and e.codice_interno like :codiceInterno");
			} else {
				hql.append(" and e.codice_interno=:codiceInterno");
			}
		} else if (StringUtils.isNotBlank(parametri.getCodice())) {
			if (parametri.getCodice().endsWith("%")) {
				hql.append(" and (e.codice_completo like :codiceCompleto or e.codice_interno like :codiceInterno)");
			} else {
				hql.append(" and (e.codice_completo = :codiceCompleto or e.codice_interno = :codiceInterno)");
			}
		}

		hql.append(" and e.date_delete is null");
		if (!count)
			hql.append(" order by e.codice_completo asc");
		return hql;
	}

	private void parametri(FiltroAlberoExtendedVM parametri, TypedQuery<?> query) {
		query.setParameter("idEnte", parametri.getIdEnte());
		query.setParameter("anno", parametri.getAnno());
		if (StringUtils.isNotBlank(parametri.getTesto())) {
			query.setParameter("testo", '%' + parametri.getTesto().trim().toUpperCase() + "%");
		}
		if (StringUtils.isNotBlank(parametri.getCodiceCompletoPadre())) {
			query.setParameter("codiceCompleto", parametri.getCodiceCompletoPadre().trim() + "%");
		}
		if (parametri.getIdResponsabile() != null) {
			query.setParameter("idResponsabile", parametri.getIdResponsabile());
		}
		if (parametri.getIdRisorsa() != null) {
			query.setParameter("idRisorsa", parametri.getIdRisorsa());
		}
		if (parametri.getIdDirezione() != null) {
			query.setParameter("idDirezione", parametri.getIdDirezione());
			query.setParameter("ccDir", parametri.getCodiceCompletoDirezione().trim() + ".%");
		}
		if (parametri.getTipiNodo() != null && !parametri.getTipiNodo().isEmpty()) {
			query.setParameter("tipiNodo", parametri.getTipiNodo());
		}
		if (StringUtils.isNotBlank(parametri.getCodiceCompleto())) {
			String cc = codiceCompleto(parametri.getCodiceCompleto(), parametri.getAnno());
			query.setParameter("codiceCompleto", cc);
		} else if (StringUtils.isNotBlank(parametri.getCodiceInterno())) {
			String cc = codiceInterno(parametri.getCodiceInterno());
			query.setParameter("codiceInterno", cc);
		} else if (StringUtils.isNotBlank(parametri.getCodice())) {
			query.setParameter("codiceCompleto", codiceCompleto(parametri.getCodice(), parametri.getAnno()));
			query.setParameter("codiceInterno", codiceInterno(parametri.getCodice()));
		}

	}

	private TypedQuery<NodoPiano> getQuery(FiltroAlberoExtendedVM parametri) {
		StringBuilder hql = select1(parametri,false);
		final TypedQuery<NodoPiano> query = entityManager.createQuery(hql.toString(), NodoPiano.class);
		parametri(parametri, query);
		return query;
	}

	private StringBuilder select1(FiltroAlberoExtendedVM parametri, boolean count) {
		StringBuilder hql=new StringBuilder();
		hql.append("SELECT e FROM NodoPiano e WHERE e.idEnte = :idEnte and e.anno=:anno");
		if (StringUtils.isNotBlank(parametri.getTesto())) {
			hql.append(" and upper(e.denominazione) like :testo");
		}
		if (StringUtils.isNotBlank(parametri.getCodiceCompletoPadre())) {
			hql.append(" and e.codiceCompleto like :codiceCompleto");
		}
		if (parametri.getIdResponsabile() != null) {
			hql.append(" and e.organizzazione is not null").append(" and e.organizzazione.responsabile is not null")
					.append(" and e.organizzazione.responsabile.id=:idResponsabile");
		}
		if (parametri.getIdRisorsa() != null) {
			hql.append(
					" and exists(select rn.id from RisorsaUmanaNodoPiano rn where e=rn.nodoPiano and rn.risorsaUmana.id=:idRisorsa)");
		}
		if (parametri.getIdDirezione() != null) {
			hql.append(" and e.organizzazione is not null")
					.append(" and (e.organizzazione.id=:idDirezione or e.organizzazione.codiceCompleto like :ccDir)");
		}
		if (parametri.getTipiNodo() != null && !parametri.getTipiNodo().isEmpty()) {
			hql.append(" and e.tipoNodo in :tipiNodo");
		}
		if (StringUtils.isNotBlank(parametri.getCodiceCompleto())) {
			if (parametri.getCodiceCompleto().endsWith("%")) {
				hql.append(" and e.codiceCompleto like :codiceCompleto");
			} else {
				hql.append(" and e.codiceCompleto=:codiceCompleto");
			}
		} else if (StringUtils.isNotBlank(parametri.getCodiceInterno())) {
			if (parametri.getCodiceInterno().endsWith("%")) {
				hql.append(" and e.codiceInterno like :codiceInterno");
			} else {
				hql.append(" and e.codiceInterno=:codiceInterno");
			}
		} else if (StringUtils.isNotBlank(parametri.getCodice())) {
			if (parametri.getCodice().endsWith("%")) {
				hql.append(" and (e.codiceCompleto like :codiceCompleto or e.codiceInterno like :codiceInterno)");
			} else {
				hql.append(" and (e.codiceCompleto = :codiceCompleto or e.codiceInterno = :codiceInterno)");
			}
		}

		hql.append(" and e.dateDelete is null");
		if(!count)
		hql.append(" order by e.codiceCompleto asc");

		return hql;
	}

	private String codiceInterno(String codice) {
		String cc = codice.trim();
		if (cc.startsWith("."))
			cc = cc.substring(1);
		return cc;
	}

	private String codiceCompleto(String codice, Integer anno) {
		String prefix = "piano_" + anno + ".";
		String cc = codice.trim();
		if (cc.startsWith("."))
			cc = cc.substring(1);
		if (!cc.startsWith(prefix))
			cc = prefix + cc;
		return cc;
	}

	@Override
	public int modificaNoteInterne(Long id, String noteAssessori) {
		String hql = "update " + " NodoPiano nodo" + " set nodo.noteAssessori=:noteAssessori" + " WHERE nodo.id=:id";
		Query query = entityManager.createQuery(hql);
		query.setParameter("noteAssessori", noteAssessori);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	@Override
	public BigDecimal pesoTotaleFigli(Long idNodoPiano) {
		String sql = "SELECT SUM(np.PESO)" + " FROM NODO_PIANO np" + " WHERE np.PADRE_ID=" + idNodoPiano
				+ " and np.date_delete is null";
		Query query = entityManager.createNativeQuery(sql);
		Object r = query.getSingleResult();
		if (r == null) {
			return BigDecimal.ZERO;
		} else if (r instanceof BigDecimal ele) {
			return ele;
		} else if (r instanceof Number ele) {
			return BigDecimal.valueOf(ele.doubleValue());
		} else {
			return BigDecimal.ZERO;
		}

	}

	@Override
	public int updateStatoAvanzamento(String statoAvanzamento, Integer period, Long idNodo) {
		final String suff = period <= 2 ? "S1" : "S2";
		String hql = "update " + " NodoPiano nodo" + " set nodo.statoAvanzamento" + suff + "=:statoAvanzamento"
				+ " WHERE nodo.id=:idNodo";
		Query query = entityManager.createQuery(hql);
		query.setParameter("statoAvanzamento", statoAvanzamento);
		query.setParameter("idNodo", idNodo);
		return query.executeUpdate();
	}

	@Override
	public int updatePercentualiForzatura(Long idNodo, Double percentualeForzatura, Double percentualeForzaturaResp) {
		String hql = "update " + " NodoPiano nodo" + " set nodo.percentualeRaggiungimentoForzata=:percentualeForzatura"
				+ ", nodo.percentualeRaggiungimentoForzataResp=:percentualeForzaturaResp" + " WHERE nodo.id=:idNodo";
		Query query = entityManager.createQuery(hql);
		query.setParameter("percentualeForzatura", percentualeForzatura);
		query.setParameter("percentualeForzaturaResp", percentualeForzaturaResp);
		query.setParameter("idNodo", idNodo);
		return query.executeUpdate();
	}
}
