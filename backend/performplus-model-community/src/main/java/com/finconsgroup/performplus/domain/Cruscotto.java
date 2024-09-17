package com.finconsgroup.performplus.domain;

import java.time.LocalDate;

import com.finconsgroup.performplus.service.dto.AnnoInterface;
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
@Table(name="CRUSCOTTO")
@Data
@EqualsAndHashCode
public class Cruscotto extends AbstractAuditingEntity implements EnteInterface, AnnoInterface{
	
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

	@Column(name = "DATA_INIZ_ANNO")
	private LocalDate dataInizializzazioneAnno;
	@Column(name = "FLAG_INIZ_ANNO")
	private Boolean flagInizializzazioneAnno;
	
	@Column(name = "DATA_ATT_INS_INTERVENTI_DA")
	private LocalDate dataAttivazioneInserimentoInterventiDa;
	@Column(name = "DATA_ATT_INS_INTERVENTI_A")
	private LocalDate dataAttivazioneInserimentoInterventiA;
	@Column(name = "FLAG_ATT_INS_INTERVENTI")
	private Boolean flagAttivazioneInserimentoInterventi;
	
	@Column(name = "DATA_ATT_INS_INDICATORI_DA")
	private LocalDate dataAttivazioneInserimentoIndicatoriDa;
	@Column(name = "DATA_ATT_INS_INDICATORI_A")
	private LocalDate dataAttivazioneInserimentoIndicatoriA;
	@Column(name = "FLAG_ATT_INS_INDICATORI")
	private Boolean flagAttivazioneInserimentoIndicatori;

	@Column(name = "DATA_ATT_INS_TARGET_DA")
	private LocalDate dataAttivazioneInserimentoTargetDa;
	@Column(name = "DATA_ATT_INS_TARGET_A")
	private LocalDate dataAttivazioneInserimentoTargetA;
	@Column(name = "FLAG_ATT_INS_TARGET")
	private Boolean flagAttivazioneInserimentoTarget;

	@Column(name = "DATA_VALID_PIANO")
	private LocalDate dataValidazionePiano;
	@Column(name = "FLAG_VALID_PIANO")
	private Boolean flagValidazionePiano;

	@Column(name = "DATA_CHIUSURA_PIANO")
	private LocalDate dataChiusuraPiano;
	@Column(name = "FLAG_CHIUSURA_PIANO")
	private Boolean flagChiusuraPiano;

	@Column(name = "DATA_CONSUNT_S1_DA")
	private LocalDate dataConsuntivazioneS1Da;
	@Column(name = "DATACONSUNT_S1_A")
	private LocalDate dataConsuntivazioneS1A;
	@Column(name = "FLAG_CONSUNT_S1")
	private Boolean flagConsuntivazioneS1;

	@Column(name = "DATA_CONSUNT_S2_DA")
	private LocalDate dataConsuntivazioneS2Da;
	@Column(name = "DATACONSUNT_S2_A")
	private LocalDate dataConsuntivazioneS2A;
	@Column(name = "FLAG_CONSUNT_S2")
	private Boolean flagConsuntivazioneS2;

	@Column(name = "DATA_ATT_PI")
	private LocalDate dataAttivazionePI;
	@Column(name = "FLAG_ATT_PI")
	private Boolean flagAttivazionePI;

	@Column(name = "DATA_CREAZIONE_SCHEDA_DA")
	private LocalDate dataCreazioneSchedaDa;
	@Column(name = "DATA_CREAZIONE_SCHEDA_A")
	private LocalDate dataCreazioneSchedaA;
	@Column(name = "FLAG_CREAZIONE_SCHEDA")
	private Boolean flagCreazioneScheda;

	@Column(name = "DATA_PERFORMANCE_ORG_DA")
	private LocalDate dataPerformanceOrgDa;
	@Column(name = "DATA_PERFORMANCE_ORG_A")
	private LocalDate dataPerformanceOrgA;
	@Column(name = "FLAG_PERFORMANCE_ORG")
	private Boolean  flagPerformanceOrg;

	@Column(name = "DATA_CONFERMA_SCHEDA_DA")
	private LocalDate dataConfermaSchedaDa;
	@Column(name = "DATA_CONFERMA_SCHEDA_A")
	private LocalDate dataConfermaSchedaA;
	@Column(name = "FLAG_CONFERMA_SCHEDA")
	private Boolean flagConfermaScheda;

	@Column(name = "DATA_COMPL_SCHEDA_DA")
	private LocalDate dataCompletamentoSchedaDa;
	@Column(name = "DATA_COMPL_SCHEDA_A")
	private LocalDate dataCompletamentoSchedaA;
	@Column(name = "FLAG_COMPL_SCHEDA")
	private Boolean flagCompletamentoScheda;

	@Column(name = "DATA_VALID_SCHEDA_DA")
	private LocalDate dataValidazioneSchedaDa;
	@Column(name = "DATA_VALID_SCHEDA_A")
	private LocalDate dataValidazioneSchedaA;
	@Column(name = "FLAG_VALID_SCHEDA")
	private Boolean flagValidazioneScheda;

	@Column(name = "DATA_CHIUSURA_PI")
	private LocalDate dataChiusuraPI;
	@Column(name = "FLAG_CHIUSURA_PI")
	private Boolean flagChiusuraPI;

	@Column(name = "ID_ENTE")
	private Long idEnte = 0l;
	@Column(name = "ANNO")
	private Integer anno;


}