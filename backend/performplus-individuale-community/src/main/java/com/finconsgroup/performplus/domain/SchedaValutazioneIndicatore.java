package com.finconsgroup.performplus.domain;

import java.util.Objects;

import com.finconsgroup.performplus.domain.AbstractAuditingEntity;
import com.finconsgroup.performplus.domain.IndicatorePiano;

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
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name = "SCHEDA_VALUTAZIONE_IND", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "SCHEDA_VALUTAZIONE_NODO_ID" , "INDICATORE_PIANO_ID"}) })
@Data


public class SchedaValutazioneIndicatore extends AbstractAuditingEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SCHEDA_VALUTAZIONE_IND_STORE")
	@SequenceGenerator(name = "SCHEDA_VALUTAZIONE_IND_STORE", sequenceName = "SCHEDA_VALUTAZIONE_IND_SEQ"
			, allocationSize = 1)

	protected Long id;

	@ManyToOne(optional = true,fetch=FetchType.LAZY)
	@JoinColumn(name = "SCHEDA_VALUTAZIONE_NODO_ID",nullable = false)
	private SchedaValutazioneNodo schedaValutazioneNodo;
	@ManyToOne(optional = true,fetch=FetchType.LAZY)
	@JoinColumn(name = "INDICATORE_PIANO_ID",nullable = false)
	private IndicatorePiano indicatorePiano;
	
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
	
	@Column(name = "TIPO")
	String tipo;
	@Column(name = "FORZATURA_RAGGIUNGIMENTO")
	Float forzaturaRaggiungimento;
	@Column(name = "NOTE_FORZATURA_RAGGIUNGIMENTO")
	String noteForzaturaRaggiungimento;

	@Column(name = "VALUTAZIONE_RELATIVA")
	private Float valutazioneRelativa;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedaValutazioneIndicatore other = (SchedaValutazioneIndicatore) obj;
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
