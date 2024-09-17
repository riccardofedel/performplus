package com.finconsgroup.performplus.rest.api.vm;

import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoSmartVM;
import com.finconsgroup.performplus.service.dto.NodoPianoEasyDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class NodoPianoStampaVM extends NodoPianoDetailVM {

	private PianoVM piano;
	private NodoPianoEasyDTO padreEasy;
	
	List<NodoPianoStampaVM> figli;
	private boolean crossed;
	private String gruppo;
	
	public String getNomeCompleto() {
		return getTipoNodo().label+" "+getCodiceRidotto()+" "+getDenominazione();
	}
	
	public NodoPianoStampaVM piano(PianoVM piano) {
		setPiano(piano);
		return this;
	}
	public NodoPianoStampaVM padreEasy(NodoPianoEasyDTO padreEasy) {
		this.padreEasy=padreEasy;
		return this;
	}
}
