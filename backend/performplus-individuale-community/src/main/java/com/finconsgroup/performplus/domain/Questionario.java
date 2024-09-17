package com.finconsgroup.performplus.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.finconsgroup.performplus.domain.AbstractAuditingEntity;
import com.finconsgroup.performplus.domain.CategoriaRisorsaUmana;
import com.finconsgroup.performplus.domain.Incarico;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name = "QUESTIONARIO")
@Data

public class Questionario extends AbstractAuditingEntity implements EnteInterface {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "QUESTIONARIO_STORE")
	@SequenceGenerator(name = "QUESTIONARIO_STORE", sequenceName = "QUESTIONARIO_SEQ"
			, allocationSize = 1)

	private Long id;
	@Column(name = "INTESTAZIONE", nullable = false)
	private String intestazione;
	@Column(nullable = true, name = "DESCRIZIONE")
	private String descrizione;
	@Column(nullable = false, name = "ID_ENTE")
	private Long idEnte = 0l;
	@Column(name = "PESO")
	private BigDecimal peso;
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "QUESTIONARIO_CATEGORIE")
	private List<CategoriaRisorsaUmana> categorie;
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "QUESTIONARIO_INCARICHI")
	private List<Incarico> incarichi;
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Questionario other = (Questionario) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id);
		return result;
	}

	
}