package com.finconsgroup.performplus.rest.api.vm.v2;

import org.apache.commons.lang3.StringUtils;

import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.service.business.utils.OrganizzazioneHelper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class StrutturaVM extends EntityVM{
	private String codiceCompleto;
	private String codiceInterno;
	private String intestazione;
	private Livello livello;
	private String codice;
	
	public String getDenominazione() {
		
		return StringUtils.isNotBlank(getCodiceRidotto())?getCodiceRidotto()+" "+intestazione:intestazione;
	}

	public String getCodiceRidotto() {
		return OrganizzazioneHelper.ridotto(codiceCompleto);
	}
	public StrutturaVM codiceIterno(String codiceInterno) {
		this.codiceInterno=codiceInterno;
		return this;
	}
}
