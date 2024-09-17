package com.finconsgroup.performplus.repository.advanced;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizzazioneRepositoryAdvancedImpl implements OrganizzazioneRepositoryAdvanced {
	@Autowired
	private EntityManager entityManager;



	@Override
	public 	int modificaPathFigli(Long idEnte, Integer anno, String oldCodiceCompleto, String codiceCompleto) {
		int pos = oldCodiceCompleto.length() + 1;
		String hql = "update Organizzazione org set org.codiceCompleto = concat('"
				+ codiceCompleto
				+ "', substring(org.codiceCompleto,"
				+ pos
				+ ")) where org.idEnte="
				+ idEnte
				+" and org.anno="+anno
				+ " and org.codiceCompleto like '" + oldCodiceCompleto + ".%'";
		Query query = entityManager.createQuery(hql);
		return query.executeUpdate();
	}


}
