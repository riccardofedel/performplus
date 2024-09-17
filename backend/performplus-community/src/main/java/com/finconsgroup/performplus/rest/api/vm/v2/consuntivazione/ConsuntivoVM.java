package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import java.util.List;

import com.finconsgroup.performplus.enumeration.Periodicita;
import com.finconsgroup.performplus.rest.api.vm.v2.IndicatorePianoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.ObiettivoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Enabling;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class ConsuntivoVM extends ObiettivoVM {

	private Float raggiungimento;
	private String statoRendicontazioneTesto;
	private List<String> fields;
	private List<ConsuntivoIndicatoreVM> consuntivoIndicatori;
	private IndicatorePianoVM indicatorePiano;
	private String statoAvanzamentoS1;
	private String statoAvanzamentoS2;
	
	private Integer annoInizioTarget;
	private Integer annoFineTarget;
	private Periodicita periodicitaTarget;
	private Periodicita periodicitaRend;
	
	private Integer periodo;
	
	
	private Double percentualeRaggiungimentoForzata;
	private Double percentualeRaggiungimentoForzataResp;


	private boolean enabledConsuntivo;
	
	public ConsuntivoVM enabling(Enabling enabling) {
		setEnabling(enabling);
		return this;
	}
	public ConsuntivoVM fields(List<String> fields) {
		setFields(fields); 
		return this;
	}
}
