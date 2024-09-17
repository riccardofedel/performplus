package com.finconsgroup.performplus.rest.api.vm.v2.struttura;

import java.time.LocalDate;
import java.util.List;

import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.enumeration.TipoStruttura;
import com.finconsgroup.performplus.enumeration.TipologiaStruttura;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEnumVM;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.rest.api.vm.v2.DipendenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.EnablingInterface;
import com.finconsgroup.performplus.rest.api.vm.v2.PersonaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Enabling;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class OrganizzazioneDetailVM extends EntityVM  implements EnablingInterface{

	
	private Long idPadre;
	private String codiceCompletoPadre;
	private int ordine;
	private String codice;
	private String codiceCompleto;
	private String codiceRidotto;
	private String intestazione;
	private Livello livello;
	private DecodificaEnumVM<TipoStruttura> tipoStruttura;
	private DecodificaEnumVM<TipologiaStruttura> tipologiaStruttura;
	private Boolean interim;
	private Integer anno;
	private Long idEnte=0l;
	private String codiceInterno;
	private PersonaVM responsabile;
	private LocalDate inizioValidita;
	private LocalDate fineValidita;
	private String descrizione;
//	private Integer totaleDisponibili;
//	private Integer totalePartTime;
//	private List<DipendenteVM> dipendenti;
//
//	private int totaleRisorse;
//
//	private float totaleRisorseEffettive;


 	private Enabling enabling;


	public OrganizzazioneDetailVM read(Enabling enabling) {
		this.enabling=enabling;
		return this;
	}

}
