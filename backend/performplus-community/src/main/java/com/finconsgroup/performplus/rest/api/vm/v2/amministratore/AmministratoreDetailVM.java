package com.finconsgroup.performplus.rest.api.vm.v2.amministratore;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

import com.finconsgroup.performplus.enumeration.TipoFunzione;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEnumVM;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class AmministratoreDetailVM extends EntityVM{
	
	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;
	private DecodificaEnumVM<TipoFunzione> funzione;	
	
	private LocalDate dataNascita;
	
	private String codiceFiscale;
	private String codiceInterno;
	private String email;
	private String delega;
	
}
