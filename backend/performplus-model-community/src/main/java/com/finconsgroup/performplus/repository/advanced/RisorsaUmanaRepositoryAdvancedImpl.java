package com.finconsgroup.performplus.repository.advanced;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.service.business.utils.DateHelper;
import com.finconsgroup.performplus.service.dto.RicercaRisorseUmane;

@Component
public class RisorsaUmanaRepositoryAdvancedImpl implements RisorsaUmanaRepositoryAdvanced {
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<RisorsaUmana> cerca(RicercaRisorseUmane parametri) {
		TypedQuery<RisorsaUmana> query = getQuery(parametri, null);
		return query.getResultList();
	}

	private TypedQuery<RisorsaUmana> getQuery(RicercaRisorseUmane parametri, Sort sort) {
		StringBuilder hql = select(parametri, sort, false);
		final TypedQuery<RisorsaUmana> query = entityManager.createQuery(hql.toString(), RisorsaUmana.class);
		parametri(parametri, query);
		return query;
	}

	private TypedQuery<Long> getQueryCount(RicercaRisorseUmane parametri) {
		StringBuilder hql = select(parametri, null, true);
		final TypedQuery<Long> query = entityManager.createQuery(hql.toString(), Long.class);
		parametri(parametri, query);
		return query;
	}

	private StringBuilder select(RicercaRisorseUmane parametri, Sort sort, boolean count) {
		StringBuilder hql = new StringBuilder();
		if (count) {
			hql.append("SELECT count(*)");
		} else {
			hql.append("SELECT e");
		}
		hql.append(" FROM RisorsaUmana e WHERE e.idEnte = :idEnte and e.anno=:anno");

		if (!Boolean.FALSE.equals(parametri.getSoloAttiveAnno())) {
			hql.append(" and e.inizioValidita<=:max and e.fineValidita>=:min");
		}

		if (StringUtils.isNotBlank(parametri.getTestoRicerca())) {
			hql.append(" and upper(e.cognome) like :testo");
		}
		if (parametri.getEsterna() != null) {
			hql.append(" and e.esterna=:esterna");
		}
		if (parametri.getDisponibile() != null) {
			hql.append(" and e.disponibile=:disponibile");
		}
		if (parametri.getPartTime() != null) {
			hql.append(" and e.partTime=:partTime");
		}
		if (StringUtils.isNotBlank(parametri.getCodiceInterno())) {
			hql.append(" and e.codiceInterno=:codiceInterno");
		}
		if (parametri.getIdCategoria() != null) {
			hql.append(" and e.categoria.id=:categoria");
		}
		if (parametri.getIdContratto() != null) {
			hql.append(" and e.contratto.id=:contratto");
		}
		if (parametri.getIdProfilo() != null) {
			hql.append(" and e.profilo.id=:profilo");
		}
		if (!count) {
			if (sort == null || sort.isEmpty()) {
				hql.append(" order by e.cognome,e.nome,e.codiceInterno");
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append(" order by");
				for (Order order : sort) {
					sb.append(" e.").append(order.getProperty()).append(' ')
							.append(order.isDescending() ? "desc" : "asc").append(',');
				}
				String st = sb.toString();
				hql.append(st.substring(0, st.length() - 1));
			}
		}
		return hql;
	}

	private void parametri(final RicercaRisorseUmane parametri, final TypedQuery<?> query) {
		int anno = parametri.getAnno();
		final LocalDate inizioAnno = DateHelper.inizioAnno(anno);
		final LocalDate fineAnno = DateHelper.fineAnno(anno);
		LocalDate min = inizioAnno;
		LocalDate max = fineAnno;

		query.setParameter("idEnte", parametri.getIdEnte());
		query.setParameter("anno", parametri.getAnno());

		if (!Boolean.FALSE.equals(parametri.getSoloAttiveAnno())) {
			query.setParameter("max", max);
			query.setParameter("min", min);
		}

		if (StringUtils.isNotBlank(parametri.getTestoRicerca())) {
			query.setParameter("testo", parametri.getTestoRicerca().trim().toUpperCase() + "%");
		}
		if (parametri.getEsterna() != null) {
			query.setParameter("esterna", parametri.getEsterna());
		}
		if (parametri.getDisponibile() != null) {
			query.setParameter("disponibile", parametri.getDisponibile().ordinal());
		}
		if (parametri.getPartTime() != null) {
			query.setParameter("partTime", parametri.getPartTime());
		}
		if (StringUtils.isNotBlank(parametri.getCodiceInterno())) {
			query.setParameter("codiceInterno", parametri.getCodiceInterno());
		}
		if (parametri.getIdCategoria() != null) {
			query.setParameter("categoria", parametri.getIdCategoria());
		}
		if (parametri.getIdContratto() != null) {
			query.setParameter("contratto", parametri.getIdContratto());
		}
		if (parametri.getIdProfilo() != null) {
			query.setParameter("profilo", parametri.getIdProfilo());
		}

	}

	@Override
	public Page<RisorsaUmana> search(RicercaRisorseUmane parametri, Pageable page) {
		TypedQuery<Long> queryCount = getQueryCount(parametri);

		long totalRows = queryCount.getSingleResult();

		TypedQuery<RisorsaUmana> query = getQuery(parametri, page.getSort());

		query.setFirstResult(page.getPageNumber() * page.getPageSize());
		query.setMaxResults(page.getPageSize());

		return new PageImpl<>(query.getResultList(), page, totalRows);

	}
}
