package com.finconsgroup.performplus.domain;

import java.math.BigDecimal;
import java.util.Objects;

import com.finconsgroup.performplus.domain.AbstractAuditingEntity;
import com.finconsgroup.performplus.service.dto.EnteInterface;

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
@Table(name = "AMBITO_VALUTAZIONE")
@Data

public class AmbitoValutazione extends AbstractAuditingEntity implements EnteInterface {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "AMBITO_VALUTAZIONE_STORE")
	@SequenceGenerator(name = "AMBITO_VALUTAZIONE_STORE", sequenceName = "AMBITO_VALUTAZIONE_SEQ"
			, allocationSize = 1)

	private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "QUESTIONARIO_ID")
	private Questionario questionario;
	@Column(name = "Codice", nullable = false, length = 10)
	private String codice;
	@Column(name = "INTESTAZIONE", nullable = false)
	private String intestazione;
	@Column(nullable = true, name = "DESCRIZIONE")
	private String descrizione;
	@Column(nullable = false, name = "ID_ENTE")
	private Long idEnte = 0l;
	@Column(name = "FOGLIA", nullable = false)
	private boolean foglia = false;
	@ManyToOne(optional = true,fetch=FetchType.LAZY)
	@JoinColumn(name = "PADRE_ID")
	private AmbitoValutazione padre;
	@Column(name = "CODICE_COMPLETO", nullable = false)
	private String codiceCompleto;
	@Column(name = "PESO")
	private BigDecimal peso;

	@Column(name="PESO_MANCATA_ASSEGNAZIONE")
	private BigDecimal pesoMancataAssegnazione;
	
	@Column(name="PESO_MANCATO_COLLOQUIO")
	private BigDecimal pesoMancatoColloquio;

	@Column(name="FLAG_SOLO_ADMIN")
	private Boolean flagSoloAdmin=false;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AmbitoValutazione other = (AmbitoValutazione) obj;
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