package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class AggiornaPesiRegistrazioneRequest implements Serializable{
	@NotNull
	private Long idRegistrazione;
	private @Valid List<AggiornaPeso> nodi;
	private  @Valid List<AggiornaPeso> indicatori;

}
