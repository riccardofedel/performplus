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
public class ValutazioneQuestionarioVM implements Serializable{
	private Float raggiungimentoQuestionario;
	private Long idRegistrazione;
	private Long idQuestionario;
	private String questionario;
	List<ElementoQuestionario> items;
}
