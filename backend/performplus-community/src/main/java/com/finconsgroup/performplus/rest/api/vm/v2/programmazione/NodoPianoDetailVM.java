package com.finconsgroup.performplus.rest.api.vm.v2.programmazione;

import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.ObiettivoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Enabling;
import com.finconsgroup.performplus.service.dto.QuadraturaIndicatoriProg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class NodoPianoDetailVM extends ObiettivoVM {
	
	private List<QuadraturaIndicatoriProg> indicatori;
	
	private List<String> fields;
	
    private List<DecodificaVM> responsabili;

	private boolean abilitaObiettiviEIndicatori;
	private boolean abilitaAssociaRisorse;
	public NodoPianoDetailVM enabling(Enabling enabling) {
		setEnabling(enabling); 
		return this;
	}

	public NodoPianoDetailVM fields(List<String> fields) {
		setFields(fields); 
		return this;
	}
}
