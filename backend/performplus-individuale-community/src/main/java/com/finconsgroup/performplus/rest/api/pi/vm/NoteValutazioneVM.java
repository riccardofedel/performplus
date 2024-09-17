package com.finconsgroup.performplus.rest.api.pi.vm;

import java.time.LocalDate;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class NoteValutazioneVM extends EntityVM{
	   private LocalDate dataPubblicazioneScheda;
	   private String notePubblicazioneScheda;
	   private LocalDate dataApprovazionSchedaOiv;
	   private String noteApprovazioneSchedaOiv;
	   private LocalDate dataAccettazioneScheda;
	   private String noteAccettazioneScheda;
	   private Boolean flagAccettazioneScheda;
	   
	   private LocalDate dataPubblicazioneValutazione;
	   private String notePubblicazioneValutazione;
	   private LocalDate dataApprovazioneValutazioneOiv;
	   private String noteApprovazioneValutazioneOiv;
	   private LocalDate dataAccettazioneValutazione;
	   private String noteAccettazioneValutazione;
	   private Boolean flagAccettazioneValutazione;
}
