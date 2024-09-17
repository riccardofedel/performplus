package com.finconsgroup.performplus.rest.api.vm;


import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.rest.api.vm.v2.OrganizzazioneVM;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class ProfiloModel extends EntityVM implements EnteInterface{
	private OrganizzazioneVM organizzazione;
    private Ruolo ruolo;
    private Integer anno;
	private Long idEnte=0l;

  
}
