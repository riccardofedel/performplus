package com.finconsgroup.performplus.rest.api.vm.v2.struttura;

import java.io.Serializable;
import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.enumeration.TipoStruttura;
import com.finconsgroup.performplus.enumeration.TipologiaStruttura;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class CreaStrutturaRequest implements Serializable {
	@NotNull
	private Long idPadre;
	@NotBlank
	private String codice;
	@NotBlank
	private String intestazione;
	private String descrizione;
	private Long idResponsabile;
	private String codiceInterno;
	private LocalDate inizioValidita;
	private LocalDate fineValidita;
	private Boolean interim;
	private TipoStruttura tipoStruttura;
	private TipologiaStruttura tipologiaStruttura;
    private Livello livello;

}
