package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;

import com.finconsgroup.performplus.enumeration.Ruolo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class AggiungiRuoloUtenteRequest implements Serializable {
	@NotBlank
	private String userid;
	@NotNull
	private  Long idOrganizzazione;
	@NotNull
	private Ruolo ruolo;
}
