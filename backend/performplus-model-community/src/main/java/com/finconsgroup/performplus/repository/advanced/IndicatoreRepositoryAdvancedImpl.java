package com.finconsgroup.performplus.repository.advanced;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.finconsgroup.performplus.domain.Indicatore;
import com.finconsgroup.performplus.service.dto.RicercaIndicatori;

@Component
public class IndicatoreRepositoryAdvancedImpl implements IndicatoreRepositoryAdvanced {
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Indicatore> cerca(RicercaIndicatori parametri) {
		String hql = "SELECT e FROM Indicatore e WHERE e.idEnte = :idEnte";
		if (StringUtils.isNotBlank(parametri.getTestoRicerca())) {
			hql += " and e.denominazione=:testo";
		}
		if (parametri.getRaggruppamento() != null) {
			hql += " and e.raggruppamento=:ragg";
		}
		TypedQuery<Indicatore> query = entityManager.createQuery(hql, Indicatore.class);
		query.setParameter("idEnte", parametri.getIdEnte());
		if (StringUtils.isNotBlank(parametri.getTestoRicerca())) {
			query.setParameter("testo", parametri.getTestoRicerca());
		}
		if (parametri.getRaggruppamento() != null) {
			query.setParameter("ragg", parametri.getRaggruppamento().ordinal());
		}
		return query.getResultList();
	}

	@Override
	public Page<Indicatore> search(RicercaIndicatori parametri, Pageable page) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Indicatore> cq = cb.createQuery(Indicatore.class);
	    Root<Indicatore> iRoot = cq.from(Indicatore.class);
	    List<Predicate> predicates = new ArrayList<>();
	    predicates.add(cb.equal(iRoot.<Long>get("idEnte"), parametri.getIdEnte()));
	    if (StringUtils.isNotBlank(parametri.getTestoRicerca())) {
	        predicates.add(cb.like(cb.lower(iRoot.<String>get("denominazione")), "%"+parametri.getTestoRicerca().toLowerCase().trim() + "%"));
	    }
	    if (parametri.getRaggruppamento()!=null) {
	        predicates.add(cb.equal(iRoot.<Integer>get("raggruppamento"),  parametri.getRaggruppamento().ordinal()));
	    }
	    if (parametri.getFormula()!=null) {
	        predicates.add(cb.equal(iRoot.<Integer>get("tipoFormula"),  parametri.getFormula().ordinal()));
	    }

	    Predicate[] predArray = new Predicate[predicates.size()];
	    predicates.toArray(predArray);

	    cq.where(predArray);

	    TypedQuery<Indicatore> query = entityManager.createQuery(cq);

	    int totalRows = query.getResultList().size();

	    query.setFirstResult(page.getPageNumber() * page.getPageSize());
	    query.setMaxResults(page.getPageSize());

	    return  new PageImpl<>(query.getResultList(), page, totalRows);

	}
}
