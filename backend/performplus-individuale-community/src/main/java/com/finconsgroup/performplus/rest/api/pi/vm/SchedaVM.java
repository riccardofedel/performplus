package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
@NoArgsConstructor
public class SchedaVM implements Serializable{
   private String codiceUnivoco;
   private Long idRegistrazione;
   private Long idSchedaValutazione;
   private Long idValutato;
   private Long idValutatore;
   private Long idQuestionario;
   private Long idRegolamento;
   private Long idStruttura;
   private String valutato;
   private String valutatore;
   private String struttura;
   private LocalDate inizio;
   private LocalDate fine;
   private String questionario;
   private String regolamento;
//   private Boolean inattiva=false;
   private Float valutazione;
   
   private boolean responsabile;
   private boolean po;
   private boolean interim;
   private boolean abilitaPerformance;
   private boolean abilitaInvioScheda;
   private boolean abilitaInvioValutazione;

//		
//
//	private Boolean forzaSchedaSeparata;
//
//	private Boolean inattiva;
//
//	private Boolean valoriForzati;
//
//
//	private Boolean forzaValutatore;

	private boolean mancataAssegnazione;
	
	private boolean mancatoColloquio;
	
	private boolean performanceOrganizzativa;
	private Float pesoPerformanceOrganizzativa;

}
