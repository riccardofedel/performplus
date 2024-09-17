package com.finconsgroup.performplus.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.finconsgroup.performplus.domain.AbstractAuditingEntity;
import com.finconsgroup.performplus.service.dto.AnnoInterface;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name = "SCHEDA_VALUTATO"
, uniqueConstraints = {
		@UniqueConstraint(columnNames = { "ID_ENTE", "ANNO", "CODICE_UNIVOCO" }) }
, indexes = {
		@Index(columnList = "DATA_ORA_CALCOLO, CODICE_UNIVOCO",unique = false)}

)
@Data

public class SchedaValutato extends AbstractAuditingEntity implements EnteInterface, AnnoInterface{

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SCHEDA_VALUTATO_STORE")
	@SequenceGenerator(name = "SCHEDA_VALUTATO_STORE", sequenceName = "SCHEDA_VALUTATO_SEQ"
			, allocationSize = 1)

	protected Long id;
	@Column(nullable = false, name = "ID_ENTE")
	private Long idEnte = 0l;
	@Column(nullable = false, name = "ANNO")
	private Integer anno;

	@Column(name = "CODICE_UNIVOCO")
	private String codiceUnivoco;

	@Column(name = "COGNOME")
	private String cognome;
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "VALUTAZIONE")
	private Float valutazione;
	@Column(name = "FORZATURA")
	private Float forzata;
	@Column(name = "NOTE_FORZATURA")
	private String noteForzatura;

	@Column(name="DATA_ORA_CALCOLO",nullable = true)
	private LocalDateTime dataOraCalcolo;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedaValutato other = (SchedaValutato) obj;
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
