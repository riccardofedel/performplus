package com.finconsgroup.performplus.rest.api.vm.v2;
import java.io.Serializable;

import com.finconsgroup.performplus.enumeration.Ruolo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@SuppressWarnings("serial")
@Data

public class AggiornaUtenteRequest implements Serializable{

	@NotBlank
	private String username;

	@NotBlank
	private String nome;
	@NotNull
	private Ruolo ruolo;
	@NotBlank
	private String codiceFiscale;
	private Long struttura;
	private Long risorsa;
	
	@NotNull
	private Integer anno;
	
	private Ruolo ruoloAggiunto;
	private Long strutturaAggiunta;


}