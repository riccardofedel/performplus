package com.finconsgroup.performplus.domain;

import java.math.BigDecimal;

import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.StatoConservazione;
import com.finconsgroup.performplus.service.dto.AnnoInterface;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "RISORSA_STRUMENTALE")
@Data
@EqualsAndHashCode
public class RisorsaStrumentale extends AbstractAuditingEntity implements EnteInterface, AnnoInterface {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Column(name = "CODICE", nullable = false)
	private String codice;
	@Column(nullable = true, name = "DESCRIZIONE")
	private String descrizione;
	@Column(name = "DISPONIBILE", nullable = false)
	@Enumerated(EnumType.STRING)
	private Disponibile disponibile;
	private String note;
	@Column(name = "QUANTITA")
	private Integer quantita;
	@Column(name = "STATO_CONSERVAZIONE")
	@Enumerated(EnumType.STRING)
	private StatoConservazione statoConservazione;
	@Column(precision = 12, scale = 2, name = "VALORE")
	private BigDecimal valore;
	@ManyToOne
	@JoinColumn(name = "TIPOLOGIA_RISORSA_STR_ID")
	private TipologiaRisorsaStrumentale tipologia;
	@Column(nullable = false, name = "ID_ENTE")
	private Long idEnte = 0l;
	@Column(name = "ANNO")
	private Integer anno;
	@Column(name = "MODELLO")
	private String modello;
	@Column(name = "TARGA")
	private String targa;

}