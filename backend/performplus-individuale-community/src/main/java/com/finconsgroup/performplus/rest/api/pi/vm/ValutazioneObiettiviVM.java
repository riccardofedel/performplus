package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
@NoArgsConstructor
public class ValutazioneObiettiviVM implements Serializable{
	   private Float raggiungimentoObiettiviIndividuali;
	   private Float raggiungimentoObiettiviStruttura;
	   private Float raggiungimentoObiettivi;
	   List<ValutazioneObiettivoVM> obiettivi;
}
