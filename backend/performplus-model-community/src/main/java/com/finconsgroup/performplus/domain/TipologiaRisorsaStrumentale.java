package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "TIPOLOGIA_RISORSA_STR")
@Data
@EqualsAndHashCode

public class TipologiaRisorsaStrumentale extends AbstractAuditingEntity implements EnteInterface {
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

	@Column(name = "CODICE", nullable = false, length = 10)
	private String codice;
	@Column(nullable = false, name = "DESCRIZIONE", length = 100)
	private String descrizione;
	@Column(nullable = false, name = "ID_ENTE")
	private Long idEnte = 0l;


}