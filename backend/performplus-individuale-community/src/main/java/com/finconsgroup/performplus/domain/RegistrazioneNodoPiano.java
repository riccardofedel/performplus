package com.finconsgroup.performplus.domain;

import java.math.BigDecimal;
import java.util.Objects;

import com.finconsgroup.performplus.domain.AbstractAuditingEntity;
import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.enumeration.TipoRegolamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name = "REGISTRAZIONE_NODO_PIANO")
@Data

public class RegistrazioneNodoPiano extends AbstractAuditingEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "REG_NODO_PIANO_STORE")
	@SequenceGenerator(name = "REG_NODO_PIANO_STORE", sequenceName = "REG_NODO_PIANO_SEQ"
			, allocationSize = 1)

	protected Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "NODO_PIANO_ID")
	private NodoPiano nodoPiano;
	@ManyToOne(optional = false)
	@JoinColumn(name = "REGISTRAZIONE_ID")
	private Registrazione registrazione;
	@Column(nullable = true, name = "PESO")
	private BigDecimal peso;
	
	@Column(name="TIPO",nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoRegolamento tipo;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistrazioneNodoPiano other = (RegistrazioneNodoPiano) obj;
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
