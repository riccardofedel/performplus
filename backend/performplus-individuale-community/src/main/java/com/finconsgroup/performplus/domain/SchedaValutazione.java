package com.finconsgroup.performplus.domain;

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
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name = "SCHEDA_VALUTAZIONE", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "REGISTRAZIONE_ID" }) })
@Data


public class SchedaValutazione extends AbstractAuditingEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SCHEDA_VALUTAZIONE_STORE")
	@SequenceGenerator(name = "SCHEDA_VALUTAZIONE_STORE", sequenceName = "SCHEDA_VALUTAZIONE_SEQ"
			, allocationSize = 1)

	protected Long id;

	@ManyToOne(optional = false,fetch=FetchType.LAZY)
	@JoinColumn(name = "SCHEDA_VALUTATO_ID")
	private SchedaValutato schedaValutato;

	@ManyToOne(optional = false,fetch=FetchType.LAZY)
	@JoinColumn(name = "REGISTRAZIONE_ID")
	private Registrazione registrazione;

	@Column(name = "PESO_QUEST")
	private Float pesoQuestionario;
	@Column(name = "PESO_OBIETTIVI_IND")
	private Float pesoObiettiviIndividuali;
	@Column(name = "PESO_OBIETTIVI_STRUT")
	private Float pesoObiettiviStruttura;
	@Column(name = "RAGG_QUEST")
	private Float raggiungimentoQuestionario;
	@Column(name = "RAGG_OBIETTIVI_IND")
	private Float raggiungimentoObiettiviIndividuali;
	@Column(name = "RAGG_OBIETTIVI_STRUT")
	private Float raggiungimentoObiettiviStruttura;
	@Column(name = "VALUTAZIONE")
	private Float valutazione;
	@Column(name = "FORZ")
	private Float forzata;
	@Column(name = "NOTE_FORZ")
	private String noteForzatura;
	
	@Column(name = "FORZ_QUEST")
	private Float forzataQuestionario;
	@Column(name = "NOTE_FORZ_QUEST")
	private String noteForzaturaQuestionario;

	@Column(name = "FORZ_OBIETTIVI_IND")
	private Float forzataObiettiviIndividuali;
	@Column(name = "NOTE_FORZ_OBIETTIVI_IND")
	private String noteForzaturaObiettiviIndividuali;

	@Column(name = "FORZ_OBIETTIVI_STRUT")
	private Float forzataObiettiviStruttura;
	@Column(name = "NOTE_FORZ_OBIETTIVI_STRUT")
	private String noteForzaturaObiettiviStruttura;

	@Column(name = "FORZ_OBIETTIVI_COLL")
	private Float forzataObiettiviPerformance;
	@Column(name = "NOTE_FORZ_OBIETTIVI_COLL")
	private String noteForzaturaObiettiviPerformance;

	@Column(name = "PESO_SCHEDA")
	private Float pesoScheda;

	@Column(name = "RAGG_OBIETTIVI_COLL")
	private Float raggiungimentoObiettiviPerformance;

	@Column(name = "PESO_OBIETTIVI_COLL")
	private Float pesoObiettiviPerformance;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedaValutazione other = (SchedaValutazione) obj;
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
