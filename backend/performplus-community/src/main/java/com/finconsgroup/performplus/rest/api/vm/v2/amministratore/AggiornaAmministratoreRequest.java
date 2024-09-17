package com.finconsgroup.performplus.rest.api.vm.v2.amministratore;

import java.io.Serializable;
import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.TipoFunzione;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class AggiornaAmministratoreRequest implements Serializable{
	
	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;

	private TipoFunzione funzione;	
	
	private LocalDate dataNascita;
	
	private String codiceFiscale;
	private String codiceInterno;
	private String email;
	
	private Integer anno;
	private Long idEnte=0l;
	
	private String delega;


}
