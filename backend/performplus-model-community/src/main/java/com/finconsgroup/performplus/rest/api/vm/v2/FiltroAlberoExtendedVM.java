package com.finconsgroup.performplus.rest.api.vm.v2;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class FiltroAlberoExtendedVM extends FiltroAlberoVM{
	private String codiceCompletoPadre;
	private Integer periodo;
	private String codiceCompletoDirezione;
	public FiltroAlberoExtendedVM() {
		super();
	}
	public FiltroAlberoExtendedVM(FiltroAlberoVM filter) {
		super();
		this.idEnte=filter.getIdEnte();
		this.anno=filter.getAnno();
		this.testo=filter.getTesto();
		this.idResponsabile=filter.getIdResponsabile();
		this.idRisorsa=filter.getIdRisorsa();
		this.idDirezione=filter.getIdDirezione();
		this.tipiNodo=filter.getTipiNodo();
//		this.strategico=filter.getStrategico();
		this.codiceCompleto=filter.getCodiceCompleto();
		this.codiceInterno=filter.getCodiceInterno();
		this.codice=filter.getCodice();
		this.strutture=filter.getStrutture();
		if (StringUtils.isNotBlank(getCodiceCompleto())) {
			if(getCodiceCompleto().endsWith("*")){
				setCodiceCompleto(getCodiceCompleto().replace('*', '%'));
			}
		}
		if (StringUtils.isNotBlank(getCodiceInterno())) {
			if(getCodiceInterno().endsWith("*")){
				setCodiceInterno(getCodiceInterno().replace('*', '%'));
			}
		}
		if (StringUtils.isNotBlank(getCodice())) {
			if(getCodice().endsWith("*")){
				setCodice(getCodice().replace('*', '%'));
			}
		}

	}
	
}