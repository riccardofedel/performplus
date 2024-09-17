package com.finconsgroup.performplus.rest.api.vm;

import com.finconsgroup.performplus.enumeration.Ruolo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ProfiloVM extends EntityVM {

	private static final long serialVersionUID = -7206436171753764096L;
	private OrganizzazioneSmartVM organizzazione;
	private Ruolo ruolo;
	private Integer anno;
	private Long idEnte = 0l;
	private Long idRisorsa;
	private Boolean aggiunto = false;

}