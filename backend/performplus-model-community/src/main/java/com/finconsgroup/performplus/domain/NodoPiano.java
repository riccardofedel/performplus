package com.finconsgroup.performplus.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.finconsgroup.performplus.enumeration.Fascia;
import com.finconsgroup.performplus.enumeration.ModalitaAttuative;
import com.finconsgroup.performplus.enumeration.StatoPiano;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoObiettivo;
import com.finconsgroup.performplus.enumeration.TipoPiano;
import com.finconsgroup.performplus.enumeration.TipologiaObiettiviOperativi;
import com.finconsgroup.performplus.service.dto.AnnoInterface;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "NODO_PIANO",uniqueConstraints = { @UniqueConstraint(columnNames = {
		"ID_ENTE","ANNO", "CODICE_COMPLETO", "DATE_DELETE" }),
		@UniqueConstraint(columnNames = {
				"ID_ENTE","ANNO", "CODICE_INTERNO", "DATE_DELETE" })})
@Data
@EqualsAndHashCode

public class NodoPiano extends AbstractLogicalDeleteEntity implements EnteInterface, AnnoInterface {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "NODO_PIANO_STORE")
	@SequenceGenerator(name = "NODO_PIANO_STORE", sequenceName = "NODO_PIANO_SEQ"
	, allocationSize = 1)

	protected Long id;

	@Column(nullable = false, name = "TIPO_NODO")
	@Enumerated(EnumType.STRING)
	private TipoNodo tipoNodo;

	@Column(name = "CODICE_COMPLETO", nullable = false)
	private String codiceCompleto;
	@Column(nullable = false, name = "ID_ENTE")
	private Long idEnte = 0l;
	@Column(name = "ANNO")
	private Integer anno;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PIANO_ID")
	private NodoPiano piano;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PADRE_ID")
	private NodoPiano padre;

	@Column(name = "ORDINE")
	private int ordine = 0;

	@Column(nullable = false, name = "CODICE")
	private String codice;

	@Column(name = "DESCRIZIONE", length = 5000)
	private String descrizione;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "ORGANIZZAZIONE_NODO_PIANO", inverseJoinColumns = @JoinColumn(table = "organizzazione", name = "organizzazioni_id"), joinColumns = @JoinColumn(table = "nodo_piano", name = "nodopiano_id"))
	private List<Organizzazione> organizzazioni = new ArrayList<>();

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ORGANIZZAZIONE_ID")
	private Organizzazione organizzazione;
	@Column(name = "DENOMINAZIONE", nullable = false, length = 5000)
	private String denominazione;
	@Column(name = "NOTE", length = 5000)
	private String note;
	@Column(precision = 7, scale = 4, name = "PESO")
	private BigDecimal peso;
//	@Column(name = "PERCENTUALE_FIGLI")
//	private Integer percentualeFigli;

	
	@Column(name = "BLOCCATO")
	private Boolean bloccato=false;

	@Column(name = "Livello_STRATEGICITA")
	@Enumerated(EnumType.STRING)
	private Fascia LivelloStrategicita;
	@Column(name = "Livello_COMPLESSITA")
	@Enumerated(EnumType.STRING)
	private Fascia LivelloComplessita;

	@Column(name = "INIZIO")
	private LocalDate inizio;
	@Column(name = "SCADENZA")
	private LocalDate scadenza;
		
	
	@Column(name = "CODICE_INTERNO", nullable = true)
	private String codiceInterno;

	
	//obiettivo strategico
	@Column(name = "STRATEGICO")
	private Boolean strategico=false;



	// CONSUNTIVAZIONE
	
	@Column(name = "INIZIO_EFFETTIVO")
	private LocalDate inizioEffettivo;
	
	@Column(name = "SCADENZA_EFFETTIVA")
	private LocalDate scadenzaEffettiva;
	@Column(name = "NOTE_CONSUNTIVO", length = 5000)
	private String noteConsuntivo;
	@Column(name = "DATA_MODIFICA_NOTA")
	private LocalDate dataModificaNota;

	
	// PIANO
	@Column(nullable = true, name = "ANNO_INIZIO")
	private Integer annoInizio;
	@Column(nullable = true, name = "ANNO_FINE")
	private Integer annoFine;
	@Column(name = "APPROVAZIONE")
	private LocalDate approvazione;
	@Column(nullable = true, name = "TIPO_PIANO")
	@Enumerated(EnumType.STRING)
	private TipoPiano tipoPiano;
	@Column(nullable = true, name = "STATO_PIANO")
	@Enumerated(EnumType.STRING)
	private StatoPiano statoPiano;
	
	@ManyToOne(optional = true,fetch=FetchType.LAZY)
	@JoinColumn(name = "PIANO_ORIGINE_ID")
	private NodoPiano pianoOrigine;
	
	
	@Column(nullable = true, name = "STATO_AVANZAMENTO_S1",length = 10000)
	private String statoAvanzamentoS1;
	@Column(nullable = true, name = "STATO_AVANZAMENTO_S2",length = 10000)
	private String statoAvanzamentoS2;

	//MISSIONE
	@Column(name = "SINTESI",length = 10000)
	private String sintesi;
	@Column(name = "CRITICITA",length = 10000)
	private String criticita;

	//PEG
	
	@Column(name = "MODALITA_ATTUATIVE")
	@Enumerated(EnumType.STRING)
	private ModalitaAttuative modalitaAttuative;
	
	@Column(name = "TIPOLOGIE", length = 10)
	@Enumerated(EnumType.STRING)
	private TipologiaObiettiviOperativi tipologie; 
	
	@Column(name = "TIPO_OBIETTIVO")
	@Enumerated(EnumType.STRING)
	private TipoObiettivo tipo;
	
	
	//PDO
	
	@Column(name = "PENALIZZAZIONE",length = 5, precision = 2)
	private Double penalizzazione;
	
	@Column(name = "CAPITOLO",length = 500)
	private String capitolo;

	@Column(name = "POLITICA",length = 500)
	private String politica;
	
	@Column(name = "DIMENSIONE")
	private String dimensione;
	@Column(name = "CONTRIBUTORS",length = 500)
	private String contributors;
	@Column(name = "STAKEHOLDERS",length = 500)
	private String stakeholders;
	
	@Column(name = "FOCUS_SEMPLIFICAZIONE")
	private Boolean focusSemplificazione;
	@Column(name = "FOCUS_DIGITALIZZAZIONE")
	private Boolean focusDigitalizzazione;
	@Column(name = "FOCUS_ACCESSIBILITA")
	private Boolean focusAccessibilita;
	@Column(name = "FOCUS_PARI_OPPORTUNITA")
	private Boolean focusPariOpportunita;
	@Column(name = "FOCUS_RISPARMIO_ENERGETICO")
	private Boolean focusRisparmioEnergetico;
	
	@Column(name = "PROSPETTIVA")
	private String prospettiva;
	@Column(name = "INNOVAZIONE")
	private String innovazione;
	@Column(name = "ANNUALITA")
	private String annualita;

	@Column(name = "FLAG_PNRR")
	private Boolean flagPnrr;
	
	@Column(name = "PERC_RAGG_FORZATA_RESP",length = 5, precision = 2)
	private Double percentualeRaggiungimentoForzata;
	@Column(name = "PERC_RAGGIUNGIMENTO_FORZATA",length = 5, precision = 2)
	private Double percentualeRaggiungimentoForzataResp;

	
	@Column(name = "FLAG_OIV")
	private Boolean flagOIV;
	
	@Column(name = "NOTE_OIV", length = 5000)
	private String noteOIV;
	
	public NodoPiano() {
		super();
	}

	public NodoPiano(Long idEnte, Integer anno, TipoNodo tipoNodo) {
		super();
		this.idEnte = idEnte;
		this.anno = anno;
		this.tipoNodo = tipoNodo;
	}

	public NodoPiano(Long idEnte, Integer anno) {
		this(idEnte, anno, null);
	}

	@Transient
	public String getPosizione() {
		if (getPadre() == null)
			return "0";
		return getPadre().getPosizione() + "." + ordine;
	}

	public NodoPiano tipoNodo(TipoNodo tipoNodo) {
		setTipoNodo(tipoNodo);
		return this;
	}
	
}
