package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class PubblicazioneValutazioneRequest implements Serializable{
	@NotNull
	private Long idRegistrazione;
	private String note;

}
