package com.finconsgroup.performplus.repository.advanced;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.repository.RegistrazioneRepositoryAdvanced;
import com.finconsgroup.performplus.rest.api.pi.vm.ParametriRicercaRegistrazione;

@Component
public class RegistrazioneRepositoryAdvancedImpl implements RegistrazioneRepositoryAdvanced {
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Registrazione> cerca(ParametriRicercaRegistrazione parametri) {
		TypedQuery<Registrazione> query = getQuery(parametri);
		return query.getResultList();
	}


	@Override
	public Page<Registrazione> search(ParametriRicercaRegistrazione parametri, Pageable page) {
		TypedQuery<Long> queryCount = getQueryCount(parametri);

		long totalRows = queryCount.getSingleResult();

		TypedQuery<Registrazione> query = getQuery(parametri);
		query.setFirstResult(page.getPageNumber() * page.getPageSize());
		query.setMaxResults(page.getPageSize());
		return new PageImpl<>(query.getResultList(), page, totalRows);

	}
	
	private TypedQuery<Registrazione> getQuery(ParametriRicercaRegistrazione parametri) {
		String hql = select(parametri, false);
		final TypedQuery<Registrazione> query = entityManager.createQuery(hql, Registrazione.class);
		parametri(parametri, query);
		return query;
	}

	private TypedQuery<Long> getQueryCount(ParametriRicercaRegistrazione parametri) {
		String hql = select(parametri, true);
		final TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
		parametri(parametri, query);
		return query;
	}
	private String select(ParametriRicercaRegistrazione parametri, boolean count) {
		StringBuilder sb = new StringBuilder();
		if (count) {
			sb.append("SELECT count(*)");
		} else {
			sb.append("SELECT e");
		}
		sb.append(" FROM Registrazione e WHERE e.idEnte = :idEnte and e.anno=:anno");
		if (StringUtils.isNotBlank(parametri.getCognome())) {
			sb.append(" and (upper(e.valutato.cognome) like :testo1 or upper(e.valutatore.cognome) like :testo1)");
		}
		if (StringUtils.isNotBlank(parametri.getCognomeValutato())) {
			sb.append(" and upper(e.valutato.cognome) like :testo2");
		}
		if (StringUtils.isNotBlank(parametri.getCognomeValutatore())) {
			sb.append(" and upper(e.valutatore.cognome) like :testo3");
		}
		if (parametri.getIdQuestionario() != null) {
			sb.append(" and e.questionario.id=:idQuestionario");
		}
		if (parametri.getIdRegolamento() != null) {
			sb.append(" and e.regolamento.id=:idRegolamento");
		}
		if (parametri.getIdStruttura() != null) {
			sb.append(" and e.organizzazione.id=:idStruttura");
		}

		if (parametri.getIdValutato() != null) {
			sb.append(" and e.valutato.id=:idValutato");
		}

		if (parametri.getIdValutatore() != null) {
			sb.append(" and e.valutatore.id=:idValutatore");
		}
		if (parametri.getInattiva() != null) {
			sb.append(" and e.inattiva=:inattiva");
		}
		if (parametri.getInterim() != null) {
			sb.append(" and e.interim=:interim");
		}
		if (parametri.getResponsabile() != null) {
			sb.append(" and e.responsabile=:responsabile");
		}
		if (parametri.getPo() != null) {
			sb.append(" and e.po=:po");
		}
		if (!count)
			sb.append(" order by e.valutato.cognome, e.valutato.nome, e.valutatore.cognome");
		return sb.toString();
	}

	private void parametri(ParametriRicercaRegistrazione parametri, TypedQuery<?> query) {
		query.setParameter("idEnte", parametri.getIdEnte());
		query.setParameter("anno", parametri.getAnno());
		if (StringUtils.isNotBlank(parametri.getCognome())) {
			query.setParameter("testo1", parametri.getCognome().trim().toUpperCase() + "%");
		}
		if (StringUtils.isNotBlank(parametri.getCognomeValutato())) {
			query.setParameter("testo2", parametri.getCognome().trim().toUpperCase() + "%");
		}
		if (StringUtils.isNotBlank(parametri.getCognomeValutatore())) {
			query.setParameter("testo3", parametri.getCognome().trim().toUpperCase() + "%");
		}

		if (parametri.getIdQuestionario() != null) {
			query.setParameter("idQuestionario", parametri.getIdQuestionario());
		}
		if (parametri.getIdRegolamento() != null) {
			query.setParameter("idRegolamento", parametri.getIdRegolamento());
		}
		if (parametri.getIdStruttura() != null) {
			query.setParameter("idStruttura", parametri.getIdStruttura());
		}
		if (parametri.getIdValutato() != null) {
			query.setParameter("idValutato", parametri.getIdValutato());
		}
		if (parametri.getIdValutatore() != null) {
			query.setParameter("idValutatore", parametri.getIdValutatore());
		}
		if (parametri.getInattiva() != null) {
			query.setParameter("inattiva", parametri.getInattiva());
		}
		if (parametri.getInterim() != null) {
			query.setParameter("interim", parametri.getInterim());
		}
		if (parametri.getResponsabile() != null) {
			query.setParameter("responsabile", parametri.getResponsabile());
		}
		if (parametri.getPo() != null) {
			query.setParameter("po", parametri.getPo());
		}
	}

}
