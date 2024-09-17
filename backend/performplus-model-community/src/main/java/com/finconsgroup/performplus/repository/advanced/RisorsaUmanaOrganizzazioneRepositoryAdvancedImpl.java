package com.finconsgroup.performplus.repository.advanced;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaOrganizzazione;
import com.finconsgroup.performplus.service.business.utils.DateHelper;
import com.finconsgroup.performplus.service.dto.RicercaRisorseUmane;
import com.finconsgroup.performplus.service.dto.RicercaRisorseUmaneOrg;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Component
public class RisorsaUmanaOrganizzazioneRepositoryAdvancedImpl implements RisorsaUmanaOrganizzazioneRepositoryAdvanced {
	@Autowired
	private EntityManager entityManager;

	@Override
	public int modificaDisponibilita(Integer disponibilita, Long id) {
		String hql = "update " + " RisorsaUmanaOrganizzazione ro" + " set ro.disponibilita=:disponibilita"
				+ " WHERE ro.id=:id";
		Query query = entityManager.createQuery(hql);
		query.setParameter("disponibilita", disponibilita);
		query.setParameter("id", id);
		return query.executeUpdate();

	}

	@Override
	public List<RisorsaUmanaOrganizzazione> list(RicercaRisorseUmaneOrg parametri) {
		TypedQuery<RisorsaUmanaOrganizzazione> query = getQuery(parametri);
		return query.getResultList();
	}

	@Override
	public Page<RisorsaUmanaOrganizzazione> search(final RicercaRisorseUmaneOrg parametri, Pageable page) {
		TypedQuery<Long> queryCount = getQueryCount(parametri);
		long totalRows = queryCount.getSingleResult();
		TypedQuery<RisorsaUmanaOrganizzazione> query = getQuery(parametri);
		query.setFirstResult(page.getPageNumber() * page.getPageSize());
		query.setMaxResults(page.getPageSize());
		final List<RisorsaUmanaOrganizzazione> list = query.getResultList();
		return new PageImpl<>(list, page, totalRows);
	}

	private TypedQuery<RisorsaUmanaOrganizzazione> getQuery(final RicercaRisorseUmaneOrg parametri) {
		StringBuilder hql = select(parametri, false);

		final TypedQuery<RisorsaUmanaOrganizzazione> query = entityManager.createQuery(hql.toString(),
				RisorsaUmanaOrganizzazione.class);
		parametri(parametri, query);
		return query;
	}

	private TypedQuery<Long> getQueryCount(final RicercaRisorseUmaneOrg parametri) {
		
		StringBuilder hql = select(parametri, true);

		final TypedQuery<Long> query = entityManager.createQuery(hql.toString(), Long.class);
		parametri(parametri, query);
		return query;
	}

	private StringBuilder select(final RicercaRisorseUmaneOrg parametri, final boolean count) {

		StringBuilder hql = new StringBuilder();
		if (count)
			hql.append("SELECT count(*)");
		else
			hql.append("SELECT e");
		
		hql.append(" FROM RisorsaUmanaOrganizzazione e WHERE e.risorsaUmana.idEnte = :idEnte")
				.append(" and e.inizioValidita<=:max and e.fineValidita>=:min")
				.append(" and e.risorsaUmana.inizioValidita<=:fineAnno and e.risorsaUmana.fineValidita>=:inizioAnno")
				.append(" and e.risorsaUmana.anno=:anno").append(" and e.organizzazione.anno=:anno");

		if (StringUtils.isNotBlank(parametri.getTestoRicerca())) {
			hql.append(" and upper(e.risorsaUmana.cognome) like :testo");
		}
		if (parametri.getEsterna() != null) {
			hql.append(" and e.risorsaUmana.esterna=:esterna");
		}
		if (parametri.getPolitico() != null) {
			hql.append(" and e.risorsaUmana.politico=:politico");
		}
		if (parametri.getDisponibile() != null) {
			hql.append(" and e.risorsaUmana.disponibile=:disponibile");
		}
		if (parametri.getPartTime() != null) {
			hql.append(" and e.risorsaUmana.partTime=:partTime");
		}
		if (StringUtils.isNotBlank(parametri.getCodiceInterno())) {
			hql.append(" and e.risorsaUmana.codiceInterno=:codiceInterno");
		}
		if (parametri.getIdCategoria() != null) {
			hql.append(" and e.risorsaUmana.categoria.id=:categoria");
		}
		if (parametri.getIdContratto() != null) {
			hql.append(" and e.risorsaUmana.contratto.id=:contratto");
		}
		if (parametri.getIdProfilo() != null) {
			hql.append(" and e.risorsaUmana.profilo.id=:profilo");
		}
		if (parametri.getOrganizzazioni() != null && !parametri.getOrganizzazioni().isEmpty()) {
			hql.append(" and e.organizzazione.id in :organizzazioni");
		}
		if(!count)
		hql.append(" order by e.risorsaUmana.cognome asc, e.risorsaUmana.nome asc, e.inizioValidita desc");

		
		return hql;
	}
	private void parametri(final RicercaRisorseUmaneOrg parametri,final TypedQuery<?> query) {
		final int anno = parametri.getAnno();
		final LocalDate fineAnno = DateHelper.fineAnno(anno);
		final LocalDate inizioAnno = DateHelper.inizioAnno(anno);
		LocalDate max = fineAnno;
		LocalDate min = inizioAnno;
		if (Boolean.TRUE.equals(parametri.getSoloAttiveAnno())) {
			LocalDate now = LocalDate.now();
			min = now;
			max = now;
		}
		query.setParameter("idEnte", parametri.getIdEnte());
		query.setParameter("anno", parametri.getAnno());
		query.setParameter("max", max);
		query.setParameter("min", min);
		query.setParameter("inizioAnno", inizioAnno);
		query.setParameter("fineAnno", fineAnno);

		if (StringUtils.isNotBlank(parametri.getTestoRicerca())) {
			query.setParameter("testo", parametri.getTestoRicerca().trim().toUpperCase() + "%");
		}
		if (parametri.getEsterna() != null) {
			query.setParameter("esterna", parametri.getEsterna());
		}
		if (parametri.getPolitico() != null) {
			query.setParameter("politico", parametri.getPolitico());
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
		if (parametri.getOrganizzazioni() != null && !parametri.getOrganizzazioni().isEmpty()) {
			query.setParameter("organizzazioni", parametri.getOrganizzazioni());
		}

	}

}
