package com.finconsgroup.performplus.rest.api.pi.vm;

import java.util.List;

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
public class SchedaDetailVM extends SchedaVM{

	   private Float pesoQuestionario;
	   private Float pesoObiettiviIndividuali;
	   private Float pesoObiettiviStruttura;
	   private Float valutazione;
	   private ValutazioneObiettiviVM valutazioneObiettivi;
	   private ValutazioneQuestionarioVM valutazioneQuestionario;

}
