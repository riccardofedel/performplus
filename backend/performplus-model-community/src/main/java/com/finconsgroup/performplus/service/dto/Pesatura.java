package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;

import com.finconsgroup.performplus.enumeration.Fascia;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipologiaObiettiviOperativi;

import lombok.Data;

@Data

public class Pesatura implements Serializable {
	
	private static final long serialVersionUID = 3973757546106418700L;
	
	private Long id;
	private Fascia LivelloStrategicita;
	private Fascia LivelloComplessita;
	private TipologiaObiettiviOperativi tipologia;
	private Double pesoStrategicita;
	private Double pesoComplessita;
	private Double peso;
	private TipoNodo tipoNodo;

	
	public boolean isObiettivoOperativo() {
		return TipoNodo.OBIETTIVO.equals(getTipoNodo());
	}
}