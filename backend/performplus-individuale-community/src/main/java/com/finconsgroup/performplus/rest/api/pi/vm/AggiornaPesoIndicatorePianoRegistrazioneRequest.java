package com.finconsgroup.performplus.rest.api.pi.vm;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class AggiornaPesoIndicatorePianoRegistrazioneRequest  extends EntityVM{
	@NotNull
	Long idRegistrazione;
	@NotNull
	Long idIndicatorePiano;
	@NotNull
	Double peso;
}
