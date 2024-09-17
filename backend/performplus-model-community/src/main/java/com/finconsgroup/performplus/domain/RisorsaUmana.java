package com.finconsgroup.performplus.domain;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.TipoFunzione;
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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "RISORSA_UMANA", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "ID_ENTE", "ANNO", "CODICE_FISCALE", "INIZIO_VALIDITA", "FINE_VALIDITA" }) })
@Data
@EqualsAndHashCode

public class RisorsaUmana extends AbstractAuditingEntity implements EnteInterface, AnnoInterface {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "RISORSA_UMANA_STORE")
	@SequenceGenerator(name = "RISORSA_UMANA_STORE", sequenceName = "RISORSA_UMANA_SEQ"
	, allocationSize = 1)

	protected Long id;

	@Column(name = "COGNOME")
	private String cognome;

	@Column(name = "DATA_NASCITA")
	private LocalDate dataNascita;
	@Column(name = "DISPONIBILE")
	@Enumerated(EnumType.STRING)
	private Disponibile disponibile;
	@Column(name = "ESTERNA")
	private Boolean esterna;
	@Column(name = "MESI")
	private Integer mesi;
	@Column(name = "NOME")
	private String nome;
	@Column(name = "ORDINE")
	private Integer ordine = 0;
	@Column(name = "ORE_PART_TIME")
	private Integer orePartTime;
	@Column(name = "PART_TIME")
	private Boolean partTime;
	@Column(nullable = true, name = "POLITICO")
	private Boolean politico=false;
	@Column(nullable = true, name = "DELEGA", length = 500)
	private String delega;
	@Column(nullable = true, name = "FUNZIONE")
	@Enumerated(EnumType.STRING)
	private TipoFunzione funzione;
	@ManyToOne
	@JoinColumn(name = "CATEGORIA_RISORSA_UMANA_ID")
	private CategoriaRisorsaUmana categoria;
	@ManyToOne
	@JoinColumn(name = "CONTRATTO_RISORSA_UMANA_ID")
	private ContrattoRisorsaUmana contratto;
	@ManyToOne
	@JoinColumn(name = "PROFILO_RISORSA_UMANA_ID")
	private ProfiloRisorsaUmana profilo;
	@Column(nullable = false, name = "ID_ENTE")
	private Long idEnte = 0l;
	@Column(name = "ANNO")
	private Integer anno;
	@Column(nullable = true, name = "CODICE_INTERNO")
	private String codiceInterno;

	@Column(nullable = true, name = "CODICE_FISCALE")
	private String codiceFiscale;

	@Column(nullable = true, name = "EMAIL")
	private String email;

	@Column(name = "INIZIO_VALIDITA", nullable=false)
	private LocalDate inizioValidita;
	@Column(name = "FINE_VALIDITA", nullable=false)
	private LocalDate fineValidita;

	@Column(name="PO")
	private Boolean po;
	
	@ManyToOne
	@JoinColumn(name = "INCARICO_ID")
	private Incarico incarico;
}