package com.finconsgroup.performplus.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "INDICATORE_PIANO")
@Data
@EqualsAndHashCode
public class IndicatorePiano extends AbstractAuditingEntity {
	
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "INDICATORE_PIANO_STORE")
	@SequenceGenerator(name = "INDICATORE_PIANO_STORE", sequenceName = "INDICATORE_PIANO_SEQ"
			, allocationSize = 1)
    protected Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "NODO_PIANO_ID")
	private NodoPiano nodoPiano;
	@ManyToOne(optional = false)
	@JoinColumn(name = "INDICATORE_ID")
	private Indicatore indicatore;
	@Column(name = "SPECIFICA")
	private String specifica;
	@Column(name = "SPECIFICA_NUMERATORE")
	private String specificaNumeratore;
	@Column(name = "SPECIFICA_DENOMINATORE")
	private String specificaDenominatore;
	@Column(name = "NOTE", length = 5000)
	private String note;
	@Column(name = "NRO_VALUTAZIONI")
	private int nroValutazioni;
	@Column(name = "PESO")
	private Double peso;
	@Column(name = "SPECIFICA_PERCENTUALE")
	private Boolean specificaPercentuale;
	@Column(name = "DECRESCENTE")
	private Boolean decrescente;
	@Column(name = "STRATEGICO")
	private Boolean strategico;
	@Column(name = "SVILUPPO_SOSTENIBILE")
	private Boolean sviluppoSostenibile;
	@Column(name = "INIZIO")
	private LocalDate inizio;
	@Column(name = "SCADENZA")
	private LocalDate scadenza;
	@Column(name = "RAGGIUNGIMENTO_FORZATO")
	private Double raggiungimentoForzato;
	@Column(name = "NON_VALUTABILE")
	private Boolean nonValutabile;
	@Column(name = "RICHIESTA_FORZATURA")
	private Double richiestaForzatura;
	@Column(name = "NOTE_RICHIESTA_FORZATURA", length = 5000)
	private String noteRichiestaForzatura;
	@Column(name = "NOTE_RAGGIUNGIMENTO_FORZATO", length = 5000)
	private String noteRaggiungimentoForzato;
	
	
	@Column(name = "CODICE_INTERNO")
	private String codiceInterno;
	@Column(name = "UNITA_MISURA")
	private String unitaMisura;
	@Column(name = "DESCRIZIONE", length = 5000)
	private String descrizione;


	@Column(name = "SPECIFICA_DECIMALI")
	private Integer specificaDecimali;
	@Column(name = "SPECIFICA_DECIMALI_A")
	private Integer specificaDecimaliA;
	@Column(name = "SPECIFICA_DECIMALI_B")
	private Integer specificaDecimaliB;

	@ManyToOne
	@JoinColumn(name = "ORGANIZZAZIONE_ID")
	private Organizzazione organizzazione;

	@Column(name = "DIMENSIONE")
	private String dimensione;
	
	@Column(name = "FONTE")
	private String fonte;
	
	@Column(name = "TIPO_INDICATORE")
	private String tipoIndicatore;

	@Column(name = "PERC_RAGGIUNGIMENTO_FORZATA",length = 5, precision = 2)
	private Double percentualeRaggiungimentoForzata;
	
	@Column(name = "SCADENZA_INDICATORE")
	private LocalDate scadenzaIndicatore;

	@Column(name = "BASE_LINE")
	private String baseline;

	@Column(name = "PROSPETTIVA")
	private String prospettiva;
}
