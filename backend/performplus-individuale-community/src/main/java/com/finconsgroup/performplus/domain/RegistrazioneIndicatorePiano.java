package com.finconsgroup.performplus.domain;

import java.math.BigDecimal;
import java.util.Objects;

import com.finconsgroup.performplus.domain.AbstractAuditingEntity;
import com.finconsgroup.performplus.domain.IndicatorePiano;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "REG_INDICATORE_PIANO")
@Data

public class RegistrazioneIndicatorePiano extends AbstractAuditingEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "REG_INDICATORE_PIANO_STORE")
	@SequenceGenerator(name = "REG_INDICATORE_PIANO_STORE", sequenceName = "REG_INDICATORE_PIANO_SEQ"
			, allocationSize = 1)

	protected Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "INDICATORE_PIANO_ID")
	private IndicatorePiano indicatorePiano;
	@ManyToOne(optional = false)
	@JoinColumn(name = "REGISTRAZIONE_NODO_PIANO_ID")
	private RegistrazioneNodoPiano registrazioneNodoPiano;
	@Column(nullable = true, name = "PESO")
	private BigDecimal peso;
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistrazioneIndicatorePiano other = (RegistrazioneIndicatorePiano) obj;
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
