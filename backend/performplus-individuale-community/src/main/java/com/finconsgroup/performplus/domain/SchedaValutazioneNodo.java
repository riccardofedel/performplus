package com.finconsgroup.performplus.domain;

import java.util.Objects;

import com.finconsgroup.performplus.domain.AbstractAuditingEntity;
import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.enumeration.TipoRegolamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name = "SCHEDA_VALUTAZIONE_NODO", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "SCHEDA_VALUTAZIONE_ID" , "NODO_PIANO_ID"}) })
@Data

public class SchedaValutazioneNodo extends AbstractAuditingEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SCHEDA_VAL_NODO_STORE")
	@SequenceGenerator(name = "SCHEDA_VAL_NODO_STORE", sequenceName = "SCHEDA_VAL_NODO_SEQ"
			, allocationSize = 1)

	protected Long id;

	@ManyToOne(optional = true,fetch=FetchType.LAZY)
	@JoinColumn(name = "SCHEDA_VALUTAZIONE_ID",nullable = false)
	private SchedaValutazione schedaValutazione;
	@ManyToOne(optional = true,fetch=FetchType.LAZY)
	@JoinColumn(name = "NODO_PIANO_ID",nullable = false)
	private NodoPiano nodoPiano;
	@Column(name = "TIPO")
	@Enumerated(EnumType.STRING)
	private TipoRegolamento tipoRegolamento;

	@Column(name = "RAGGIUNGIMENTO")
	private Float raggiungimento;
	@Column(name = "VALUTAZIONE")
	private Float valutazione;
	@Column(name = "FORZATURA")
	private Float forzatura;
	@Column(name = "NOTE_FORZATURA")
	private String noteForzatura;
	
	@Column(name = "PESO")
	private Float peso;

	@Column(name = "PESO_FORZATO")
	Float pesoForzato;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedaValutazioneNodo other = (SchedaValutazioneNodo) obj;
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
