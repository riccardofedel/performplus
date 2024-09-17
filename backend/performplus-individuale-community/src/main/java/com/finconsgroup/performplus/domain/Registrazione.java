package com.finconsgroup.performplus.domain;

import java.time.LocalDate;
import java.util.Objects;

import com.finconsgroup.performplus.domain.AbstractAuditingEntity;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.service.dto.AnnoInterface;
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
import lombok.ToString;

@Entity
@Table(name = "REGISTRAZIONE")
@Data
@ToString

public class Registrazione extends AbstractAuditingEntity implements EnteInterface, AnnoInterface {

	private static final long serialVersionUID = -2370684681575480509L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "REGISTRAZIONE_STORE")
	@SequenceGenerator(name = "REGISTRAZIONE_STORE", sequenceName = "REGISTRAZIONE_SEQ"
			, allocationSize = 1)

	protected Long id;
	
	@ManyToOne(optional = false,fetch=FetchType.LAZY)
	@JoinColumn(name = "VALUTATORE_ID")
	private RisorsaUmana valutatore;
	@ManyToOne(optional = false,fetch=FetchType.LAZY)
	@JoinColumn(name = "VALUTATO_ID")
	private RisorsaUmana valutato;
	@ManyToOne(optional = true,fetch=FetchType.LAZY)
	@JoinColumn(name = "QUESTIONARIO_ID")
	private Questionario questionario;
	@ManyToOne(optional = true,fetch=FetchType.LAZY)
	@JoinColumn(name = "REGOLAMENTO_ID")
	private Regolamento regolamento;
	@ManyToOne(optional = true,fetch=FetchType.LAZY)
	@JoinColumn(name = "ORGANIZZAZIONE_ID")
	private Organizzazione organizzazione;
	@Column(name = "ID_ENTE", nullable = false)
	private Long idEnte=0l;
	@Column(name = "ANNO", nullable = false)
	private Integer anno;
	@Column(name = "NOTE", nullable = true, length = 5000)
	private String note;
	
	@Column(name = "FLAG_ACC_SCHEDA")
	private Boolean flagAccettazioneScheda;
	@Column(name = "DATA_ACC_SCHEDA", nullable = true)
	private LocalDate dataAccettazioneScheda;
	@Column(name = "NOTE_ACC_SCHEDA", length = 5000)
	private String noteAccettazioneScheda;

	@Column(name = "FLAG_ACC_VALUTAZIONE")
	private Boolean flagAccettazioneValutazione;
	@Column(name = "DATA_ACC_VALUTAZIONE", nullable = true)
	private LocalDate dataAccettazioneValutazione;
	@Column(name = "NOTE_ACC_VALUTAZIONE", length = 5000)
	private String noteAccettazioneValutazione;

	@Column(name = "NOTE_APP_SCHEDA_OIV", length = 5000)
	private String noteApprovazioneSchedaOiv;
	@Column(name = "DATA_APP_SCHEDA_OIV", nullable = true)
	private LocalDate dataApprovazioneSchedaOiv;
	
	@Column(name = "NOTE_APP_VALUTAZIONE_OIV", length = 5000)
	private String noteApprovazioneValutazioneOiv;
	@Column(name = "DATA_APP_VALUTAZIONE_OIV", nullable = true)
	private LocalDate dataApprovazioneValutazioneOiv;

	@Column(name = "DATA_PUBBL_SCHEDA", nullable = true)
	private LocalDate dataPubblicazioneScheda;
	@Column(name = "NOTE_PUBBL_SCHEDA", length = 5000)
	private String notePubblicazioneScheda;
	
	@Column(name = "DATA_PUBBL_VALUTAZIONE", nullable = true)
	private LocalDate dataPubblicazioneValutazione;
	@Column(name = "NOTE_PUBBL_VALUTAZIONE", length = 5000)
	private String notePubblicazioneValutazione;

	@Column(name = "INIZIO_VALIDITA")
	private LocalDate inizioValidita;

	@Column(name = "FINE_VALIDITA")
	private LocalDate fineValidita;

	@Column(name = "PO")
	private Boolean po=false;
	@Column(name = "RESPONSABILE")
	private Boolean responsabile=false;
	@Column(name = "INTERIM")
	private Boolean interim=false;
		
	@Column(name = "FORZA_SCHEDA_SEPARATA")
	private Boolean forzaSchedaSeparata=false;
	@Column(name="INATTIVA")
	private Boolean inattiva=false;
	@Column(name="VALORI_FORZATI")
	private Boolean valoriForzati=false;

	@Column(name = "FORZA_VALUTATORE")
	private Boolean forzaValutatore=false;


	@Column(name = "MANCATA_ASSEGNAZIONE")
	private Boolean mancataAssegnazione=false;
	
	@Column(name = "MANCATO_COLLOQUIO")
	private Boolean mancatoColloquio=false;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Registrazione other = (Registrazione) obj;
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