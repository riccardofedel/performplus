package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.AltraProgrammazione;
import com.finconsgroup.performplus.enumeration.Dimensione;
import com.finconsgroup.performplus.enumeration.Periodicita;
import com.finconsgroup.performplus.enumeration.TipoIndicatore;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class IndicatorePianoListVM extends EntityVM {
	private String denominazione;
	private Boolean strategico;
	private Boolean sviluppoSostenibile;
	private Integer anno;
	private String valoreStorico;

	private String target;

	private String target1;
	private String target2;
	private String target3;
	private String target4;
	private String target5;
	private String target6;
	private Float peso;
	private Float pesoRelativo;

	private String dimensione;
	private Double percentualeRaggiungimentoForzata;
	private LocalDate scadenzaIndicatore;
	private String baseline;
	private String prospettiva;
	private String fonte;
	private String tipoIndicatore;

	private Periodicita periodicitaTarget;
	
	private String unitaMisura;
	
	private String scadenzaIndicatoreSt;

	public IndicatorePianoListVM periodicitaTarget(Periodicita periodicitaTarget) {
		this.periodicitaTarget = periodicitaTarget;
		return this;
	}
}
