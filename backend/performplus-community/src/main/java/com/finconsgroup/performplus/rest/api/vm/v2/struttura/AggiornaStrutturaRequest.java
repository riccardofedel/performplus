package com.finconsgroup.performplus.rest.api.vm.v2.struttura;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.enumeration.TipoStruttura;
import com.finconsgroup.performplus.enumeration.TipologiaStruttura;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class AggiornaStrutturaRequest extends EntityVM {
	@NotBlank
	private String codice;
	@NotBlank
	private String intestazione;
	private String descrizione;
	private Long idResponsabile;
	private String codiceInterno;
	
	private LocalDate inizioValidita;
	private LocalDate fineValidita;
	
	Boolean interim;

	private TipoStruttura tipoStruttura;
	private TipologiaStruttura tipologiaStruttura;
	
	private Livello livello;

}
