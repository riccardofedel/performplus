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
public class AggiornaPeso  implements Serializable{
	@NotNull
	Long id;
	@NotNull
	Double peso;
}
