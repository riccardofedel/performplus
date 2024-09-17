package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.AltraProgrammazione;
import com.finconsgroup.performplus.enumeration.Dimensione;
import com.finconsgroup.performplus.enumeration.Periodicita;
import com.finconsgroup.performplus.enumeration.TipoIndicatore;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.rest.api.vm.IndicatoreVM;
import com.finconsgroup.performplus.rest.api.vm.v2.StrutturaVM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class IndicatorePianoDetailVM extends EntityVM {

	private Long idNodoPiano;
	private IndicatoreVM indicatore;
	private String specifica;
	private String specificaNumeratore;
	private String specificaDenominatore;
	private String note;
	private Double peso;
	private Boolean specificaPercentuale;
	private Boolean decrescente;
	private Boolean strategico;
	private Boolean sviluppoSostenibile;
	private Double pesoRelativo;

	
	private Periodicita periodicitaTarget;
	
	private String unitaMisura;
	private String descrizione;
	
	private Integer specificaDecimali;
	private Integer specificaDecimaliA;
	private Integer specificaDecimaliB;
	private StrutturaVM organizzazione;

	private String tipoIndicatore;
	private LocalDate scadenzaIndicatore;
	
	private String dimensione;
	private String fonte;
	private String baseline;

	private String prospettiva;





}
