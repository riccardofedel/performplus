package com.finconsgroup.performplus.repository.advanced;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.finconsgroup.performplus.domain.Utente;
import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.repository.UtenteRepositoryAdvanced;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Component
public class UtenteRepositoryAdvancedImpl implements UtenteRepositoryAdvanced {
	@Autowired
	private EntityManager entityManager;

	@Override
	public int modificaPassword(Long idUtente, String password) {
		String hql = "update Utente u set u.passwd = :password where u.id=:idUtente";
		Query query = entityManager.createQuery(hql);
		query.setParameter("idUtente", idUtente);
		query.setParameter("password", password);
		return query.executeUpdate();
	}

	@Override
	public Page<Utente> search(Long idEnte, Integer anno, Ruolo ruolo, Long idDirezione, String nome,
			Pageable pageable) {
		TypedQuery<Long> queryCount = getQueryCount(idEnte, anno, ruolo, idDirezione, nome);

		long totalRows = queryCount.getSingleResult();

		TypedQuery<Utente> query = getQuery(idEnte, anno, ruolo, idDirezione, nome, pageable.getSort());

		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new PageImpl<>(query.getResultList(), pageable, totalRows);
	}

	private TypedQuery<Utente> getQuery(Long idEnte, Integer anno, Ruolo ruolo, Long idDirezione, String nome,
			Sort sort) {
		StringBuilder hql = select(idEnte, anno, ruolo, idDirezione, nome, false, sort);
		final TypedQuery<Utente> query = entityManager.createQuery(hql.toString(), Utente.class);
		parametri(idEnte, anno, ruolo, idDirezione, nome, query);
		return query;
	}

	private TypedQuery<Long> getQueryCount(Long idEnte, Integer anno, Ruolo ruolo, Long idDirezione, String nome) {
		StringBuilder hql = select(idEnte, anno, ruolo, idDirezione, nome, true, null);
		final TypedQuery<Long> query = entityManager.createQuery(hql.toString(), Long.class);
		parametri(idEnte, anno, ruolo, idDirezione, nome, query);
		return query;
	}

	private StringBuilder select(Long idEnte, Integer anno, Ruolo ruolo, Long idDirezione, String nome, boolean count,
			Sort sort) {
		StringBuilder hql = new StringBuilder();
		if (count)
			hql.append("SELECT count(*) FROM Utente e WHERE e.idEnte = :idEnte");
		else
			hql.append("SELECT e FROM Utente e WHERE e.idEnte = :idEnte");
		if (StringUtils.isNotBlank(nome)) {
			hql.append(" and  (upper(e.nome) like :nome or upper(e.userid) like :nome)");
		}
		if (ruolo != null && anno != null) {
			hql.append(
					" and exists( select p from Profilo p where p.utente.id=e.id and p.anno=:anno and p.ruolo=:ruolo)");
		}
		if (idDirezione != null && anno != null) {
			hql.append(" and exists( select p from Profilo p where p.utente.id=e.id and p.anno=:anno and "
					+ " p.organizzazione is not null and p.organizzazione.id=:idDirezione)");
		}
		if (!count) {

			if (sort != null && !sort.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				sb.append(" order by");
				for (Order order : sort) {
					sb.append(" e.").append(order.getProperty()).append(' ')
							.append(order.isDescending() ? "desc" : "asc").append(',');
				}
				String st = sb.toString();
				hql.append(st.substring(0, st.length() - 1));
			} else {
				hql.append(" order by e.nome asc, e.userid asc");
			}
		}
		return hql;
	}

	private void parametri(Long idEnte, Integer anno, Ruolo ruolo, Long idDirezione, String nome,
			final TypedQuery<?> query) {
		query.setParameter("idEnte", idEnte);
		if (anno != null && (ruolo != null || idDirezione != null))
			query.setParameter("anno", anno);
		if (StringUtils.isNotBlank(nome)) {
			query.setParameter("nome", "%" + nome.trim().toUpperCase() + "%");
		}
		if (idDirezione != null && anno != null) {
			query.setParameter("idDirezione", idDirezione);
		}
		if (ruolo != null && anno != null) {
			query.setParameter("ruolo", ruolo);
		}

	}
}
