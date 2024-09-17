package com.finconsgroup.performplus.rest.api.vm.v2.cruscotto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class AggiornaCruscottoRequest implements Serializable{
//	private LocalDate dataInizializzazioneAnno;
//	private Boolean flagInizializzazioneAnno;
	
	private LocalDate dataAttivazioneInserimentoInterventiDa;
	private LocalDate dataAttivazioneInserimentoInterventiA;
	private Boolean flagAttivazioneInserimentoInterventi;
	
	private LocalDate dataAttivazioneInserimentoIndicatoriDa;
	private LocalDate dataAttivazioneInserimentoIndicatoriA;
	private Boolean flagAttivazioneInserimentoIndicatori;

	private LocalDate dataAttivazioneInserimentoTargetDa;
	private LocalDate dataAttivazioneInserimentoTargetA;
	private Boolean flagAttivazioneInserimentoTarget;

	private LocalDate dataValidazionePiano;
	private Boolean flagValidazionePiano;

	private LocalDate dataChiusuraPiano;
	private Boolean flagChiusuraPiano;

	private LocalDate dataConsuntivazioneS1Da;
	private LocalDate dataConsuntivazioneS1A;
	private Boolean flagConsuntivazioneS1;

	private LocalDate dataConsuntivazioneS2Da;
	private LocalDate dataConsuntivazioneS2A;
	private Boolean flagConsuntivazioneS2;

	private LocalDate dataAttivazionePI;
	private Boolean flagAttivazionePI;

	private LocalDate dataCreazioneSchedaDa;
	private LocalDate dataCoreazioneSchedaA;
	private Boolean flagCreazioneScheda;

	private LocalDate dataConfermaSchedaDa;
	private LocalDate dataConfermaSchedaA;
	private Boolean flagConfermaScheda;

	private LocalDate dataPerformanceOrgDa;
	private LocalDate dataPerformanceOrgA;
	private Boolean  flagPerformanceOrg;

	private LocalDate dataCompletamentoSchedaDa;
	private LocalDate dataCompletamentoSchedaA;
	private Boolean flagCompletamentoScheda;

	private LocalDate dataValidazioneSchedaDa;
	private LocalDate dataValidazioneSchedaA;
	private Boolean flagValidazioneScheda;

	private LocalDate dataChiusuraPI;
	private Boolean flagChiusuraPI;



}
