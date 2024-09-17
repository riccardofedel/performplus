package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class ValutazioneQuestionarioRequest implements Serializable{
	@NotNull
	Long idRegistrazione;
	@NotEmpty
	List<Long> risposte;
}
