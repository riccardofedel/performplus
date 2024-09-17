package com.finconsgroup.performplus.domain;

import java.math.BigDecimal;
import java.util.Objects;

import com.finconsgroup.performplus.domain.AbstractAuditingEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "VALORE_AMBITO")
@Data

public class ValoreAmbito extends AbstractAuditingEntity{
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "VALORE_AMBITO_STORE")
	@SequenceGenerator(name = "VALORE_AMBITO_STORE", sequenceName = "VALORE_AMBITO_SEQ"
			, allocationSize = 1)

	protected Long id;
	@ManyToOne(optional = false,fetch=FetchType.LAZY)
	@JoinColumn(name = "AMBITO_VALUTAZIONE_ID")
	private AmbitoValutazione ambitoValutazione;
	@Column(name="PESO")
	private BigDecimal peso;
	@Column(name="CODICE",nullable=false,length=20)
	private String codice;
	@Column(name = "INTESTAZIONE", nullable = false)
	private String intestazione;
	@Column(nullable = true, name = "DESCRIZIONE",length=10000)
	private String descrizione;
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValoreAmbito other = (ValoreAmbito) obj;
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