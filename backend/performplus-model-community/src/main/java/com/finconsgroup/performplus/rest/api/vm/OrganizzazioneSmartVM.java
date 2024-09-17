package com.finconsgroup.performplus.rest.api.vm;
import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.enumeration.TipoStruttura;
import com.finconsgroup.performplus.enumeration.TipologiaStruttura;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.service.business.utils.OrganizzazioneHelper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class OrganizzazioneSmartVM extends EntityVM {

	private String codice;
	private String codiceInterno;
	private String codiceCompleto;
	private String intestazione;
	private Livello livello;
	private LocalDate inizioValidita;
	private LocalDate fineValidita;
	private Boolean interim;
	private Integer anno;
	private Long idEnte=0l;
	private Long idPadre;
	private Long idResponsabile;
	private String nomeResponsabile;
	private TipoStruttura tipoStruttura;
	private TipologiaStruttura tipologiaStruttura;

		
	public OrganizzazioneSmartVM idPadre(Long idPadre) {
		setIdPadre(idPadre);
		return this;
	}
	public OrganizzazioneSmartVM idResponsabile(Long idResponsabile) {
		setIdResponsabile(idResponsabile);
		return this;
	}
	public OrganizzazioneSmartVM nomeResponsabile(String nomeResponsabile) {
		setNomeResponsabile(nomeResponsabile);
		return this;
	}

	public String getDenominazione() {
		return StringUtils.isNotBlank(getCodiceRidotto())?getCodiceRidotto()+" "+intestazione:intestazione;
	}

	public String getCodiceRidotto() {
		return OrganizzazioneHelper.ridotto(codiceCompleto);
	}


}